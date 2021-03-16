package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

/**
 * LDW - Load Word
 * 
 * Description:
 * 	LDW transfers a 16-bit "A" operand to a 16-bit "B" operand location.
 * 
 * 
 * @author wwhite
 *
 */
public class OpCodeLDW extends OpCode<OpCodeLDW> implements IOpCode {


    public OpCodeLDW(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	@Override
    public final void exec(State state_) {
        int[] memory = state_.getMemory();
        final int pc = state_.getPc();

        byte[] operands;

        int numberOfBytes = 0;
        int stateTime = 0;

        switch (this.getAddressModeInt())
        {
	        case AddressMode.DIRECT:
	        	numberOfBytes = 3;
	        	stateTime = 4;
	        	break;
	        case AddressMode.IMMEDIATE:
	        	numberOfBytes = 4;
	        	stateTime = 5; 
	        	break;
	        case AddressMode.INDIRECT:
	        	numberOfBytes = 3;
	        	stateTime = 6;
	        	break;
	        case AddressMode.INDIRECT_AUTO_INC:
	        	numberOfBytes = 3;
	        	stateTime = 7;
	        	break;
	        case AddressMode.SHORT_INDEXED:
	        	numberOfBytes = 4;
	        	stateTime = 6;
	        	break;	
	        case AddressMode.LONG_INDEXED:
	        	numberOfBytes = 5;
	        	stateTime = 7;
	        	break;
        }

        operands = new byte[numberOfBytes];
        for (int i = 0; i < numberOfBytes; i++) {
			operands[i] = (byte)memory[pc + i];
		}
       
        //a9d7: a3,01,00,0d,14      ldw   R14,[R0+d00]     R14 = [d00];
        // little endian
        short dest_dwreg = operands[numberOfBytes-1];
        short value  = (short) ((operands[numberOfBytes-2] << 8) | operands[numberOfBytes-3]  & 0xff);;
       
        byte lo;
        byte hi;
        
        if (this.getAddressModeType() == AddressMode.DIRECT)
        {
     	   System.err.println("Not Implemented");
     	   System.exit(1);
        }
       if (this.getAddressModeType() == AddressMode.INDIRECT)
       {
    	   
    	   // aa98: 9a,aa word aa91
    	   
    	   value = (short) operands[numberOfBytes-2]; // operand
    	   value = state_.getWordRegister((byte) value); // value of [Register]
    	   
    	   value = this.getWordValue(memory, value);
    	   
    	   state_.setWordRegister(dest_dwreg, value);
    	   
       }
       
       if (this.getAddressModeType() == AddressMode.INDIRECT_AUTO_INC)
       {
    	   // LDW (indirreg)+, breg
    	   // (RB) <- ([RA]); (RA) <- (RA) + 2
    	   
    	   short indirectRegRA = (short) (operands[numberOfBytes-2] & 0xfe);
    	   short destRegRB = operands[numberOfBytes-1];
    	   
    	   // [RA]
    	   short RA = state_.getWordRegister(indirectRegRA);
    	   
    	   value = this.getWordValue(memory, RA);
    	   
    	   // (RB) <- ([RA])
    	   state_.setWordRegister(destRegRB, (short)value);
    	   
    	   // (RA) <- (RA) + 2
    	   state_.setWordRegister(indirectRegRA, (short)(RA + 2));
       }
       if (this.getAddressModeType() == AddressMode.IMMEDIATE)
       {
    	   // Assembler Format: LDW =data, breg
    	   // Instruction Operation: (RB) <- Data
    	   // Execution States: 5
    	   // Machine Format: [ ^A1 ], [ Data Lo Byte ], [ Data Hi Byte ], [ Dest RB]
    	   
           byte destRB = operands[numberOfBytes-1];
           value  = (short) ((operands[numberOfBytes-2] << 8) | operands[numberOfBytes-3]  & 0xff);

           state_.setWordRegister((short) (destRB & 0xff), value);
       }
       if (this.getAddressModeType() == AddressMode.SHORT_INDEXED)
       {
			// Assembler Format: LDW offset (basereg), breg
			// Instruction Operation: (RB) <- ([RA] + Offset)
			// Execution States: 6/11
			// Machine Format:[ ^A3 ], [ Base RA | 0 MB ], [ +- | Offset ], [ Dest RB ]
			byte baseRA = (byte) ((operands[1] & 0xff) & 0xfe);
			byte offset = (byte) (operands[2] & 0xff);
			byte destRB = (byte) (operands[3] & 0xff); 
			
			short basereg = state_.getWordRegister((short) (baseRA & 0xff));
			short valueRA =  this.getWordValue(memory, (short)(basereg + offset));
			
			state_.setWordRegister(destRB, valueRA );
       }
       if (this.getAddressModeType() == AddressMode.LONG_INDEXED)
       {
    	   
    	   // LDW offset(indexReg), breg
    	   // (RB)<-([RA] + Offset)
    	   //[ ^A3 ], [ Index RA | 0x1 MB ], [ Offset Lo Byte], [ +- | Offset Hi Byte ], [ Dest RB ]
    	   
    	   byte indexRA  = (byte) (operands[numberOfBytes-4] & 0xfe);
           
    	   byte offset_lo = operands[numberOfBytes-3];
           byte offset_hi = operands[numberOfBytes-2];
           	
           byte destRB = (byte) (operands[numberOfBytes-1]);
           
           short offset = (short) ((offset_hi << 8) | (offset_lo & 0xff));
        	
           short indexRAValue = state_.getWordRegister(indexRA);

    	   short RA = (short) (indexRAValue + offset);
    	   short RAvalue = this.getWordValue(memory, RA);
       	
       	System.out.println(String.format(" [0x%04X + 0x%04X]", RAvalue, offset));
       	System.out.println(String.format(" Offset hi & lo : [0x%04X + 0x%04X]", (short) (offset_hi << 8), offset_lo));
       	

       	state_.setWordRegister(destRB, RAvalue);
       	
       	
       }
       
        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);
       

    }

	@Override
	public void setAddressMode(AddressMode direct) {
		// TODO Auto-generated method stub
		super.setAddressMode(direct);
	}
	
	
}
