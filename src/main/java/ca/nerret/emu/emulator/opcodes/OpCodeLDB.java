package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

/**
 * LDB - Load Byte
 * 
 * Description:
 *   LDB transfers an 8-bit "A" operand to an 8-bit "B" operand location.
 *   
 *   
 * @author wwhite
 *
 */
public class OpCodeLDB extends OpCode implements IOpCode {

    public OpCodeLDB(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	@Override
    public final void exec(State state_) {
		
    	super.exec(state_);
        
        state_.setWordRegister(this.getOperandLocation(),this.getResult());
        
        return;
        
        /**
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
	        	numberOfBytes = 3;
	        	stateTime = 4; 
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
	        	stateTime = 6;
	        	break;	
        }

        operands = new byte[numberOfBytes];
        for (int i = 0; i < numberOfBytes; i++) {
			operands[i] = (byte)memory[pc + i];
		}
        
        byte areg = 0;
        byte breg = operands[numberOfBytes-1];
        byte data;
        byte indirreg = 0;
        byte indirreg_inc;
        
        byte basereg;
       
        
        long cmpResult;
        
        switch (this.getAddressModeInt())
        {
	        case AddressMode.DIRECT:
	        	// LDB areg,breg
	        	// InstructionOperation: (Rb)<-(Ra)
	        	// Machine Format: [ ^B0 ], [ Source Ra ] [ Dest Rb ]
	        	areg = operands[numberOfBytes-2];
	        	breg = operands[numberOfBytes-1];
	        	areg = state_.getByteRegister( areg );
	        	break;
	        case AddressMode.IMMEDIATE:
	        	data = (byte)operands[numberOfBytes-2];
	        	areg = data;
	        
	        	break;
	        case AddressMode.INDIRECT:
	        	
	        	// LDB @indlrreg, breg
	        	// (Rb) <- ([Ra])
	        	// [ ^B2 ], [ Indirect Ra | 0 MB ], [ Dest Rb ]
	        	// 2070: b2,1a,1c            ldb   R1c,[R1a]        R1c = [R1a];
	        	
	        	// (Ra)
	        	indirreg = operands[numberOfBytes-2];
	        	indirreg = (byte) (indirreg &  0xfe);

	        	indirreg = state_.getByteRegister( indirreg );// [R32]
				
	        	// ([Ra])
	        	byte indirRegValue = getByteValue(memory, (short) (indirreg & 0xff));
	        	
				areg = indirRegValue;//getByteValue(memory, indirreg);
				
	        	break;
	        case AddressMode.INDIRECT_AUTO_INC:
				
		    	   
		    	   byte indirectRegRA = operands[numberOfBytes-2];
		    	   indirectRegRA = (byte) (indirectRegRA &  0xfe);
		    	   byte destRegRB = operands[numberOfBytes-1];
		    	   
		    	   // [RA]
		    	   short RA = state_.getWordRegister(indirectRegRA);
		    	   
		    	   // (RB) <- ([RA])
		    	  // state_.setByteRegister(destRegRB, RA);
		    	   areg = getByteValue(memory, RA);
		    	   breg = destRegRB;
		    	   
		    	   // (RA) <- (RA) + 1
		    	   state_.setWordRegister(indirectRegRA, (short) (RA + 1));


	        	break;
	        case AddressMode.SHORT_INDEXED:
	        	// Assembler Format: LDB offset(basereg),breg
	        	// Instruction Operation: (Rb) <- ([Ra] + Offset)
	        	// Execution States: 6/11
	        	// Machine Format: [ ^B3 ], [ Base Ra | 0 MB ], [ +-| Offset Byte ], [ Dest Rb ]
	        	byte baseRa  = (byte) (operands[numberOfBytes-3] & 0xfe); // mode bit mask
	        	byte offset   = operands[numberOfBytes-2];
	        	byte destRb   = operands[numberOfBytes-1];
	       
	        	short valueBaseRa = state_.getWordRegister((short) (baseRa & 0xff)); 
	        	byte value = this.getByteValue(memory, (short) (valueBaseRa + offset));
	        	
	        	breg = destRb;
	        	areg = value;
	        	break;	
	        case AddressMode.LONG_INDEXED:
	        	// Assembler Format: LDB offset(indexreg),breg 
	        	// Instruction Operation:(Rb) <- ([Ra]+Offset)
	        	// Execution States: 7/12
	        	// MachineFormat: [ ^B3 ] [ Index Ra | 1 MB ], [ Offset Lo Byte ], [+-| Offset Hi Byte ], [ Dest Rb ]
	        			
	        	byte indexRa  = (byte) (operands[numberOfBytes-4] & 0xfe); // mode bit mask
	        	byte offset_lo = operands[numberOfBytes-3];
	        	byte offset_hi = operands[numberOfBytes-2];
	        	byte dest_reg = operands[numberOfBytes-1];
	        	
	        	short valueRa = state_.getWordRegister((short) (indexRa & 0xff)); 
	        	
	        	short sOffset = (short) ((offset_hi << 8) | (offset_lo & 0xff));

	        	short offsetWord = (short) (valueRa + sOffset);
	        	System.out.println(String.format(" [0x%04X + 0x%04X]", valueRa, sOffset) + String.format("= 0x%04X ", offsetWord));
	        	
	        	//src_reg = (short) (operands[4] & 0xff);
	        	
	        	areg = this.getByteValue(memory, offsetWord);
	        	breg = dest_reg;
	        	break;
        }

        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);
        state_.setByteRegister(breg, areg);
        
        */

    }
	
	public int execImmediate()
	{
    	this.setNumberOfBytes(3);
    	setExecutionStates(4);
    	
    	byte[] operands = this.getOperands(getNumberOfBytes(), getExecutionStates());
    	
    	byte data = (byte)operands[1];
    	byte dest_dwreg = (byte)operands[2];

        this.setResult(data);
     	this.setOperandLocation(dest_dwreg);
  	
		return getExecutionStates();
	}

}
