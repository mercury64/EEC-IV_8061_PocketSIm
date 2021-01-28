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
    	   
    	   short indirectRegRA = (short) (operands[numberOfBytes-2] - 1);
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
           dest_dwreg = operands[numberOfBytes-1];
           value  = (short) ((operands[numberOfBytes-2] << 8) | operands[numberOfBytes-3]  & 0xff);;

           state_.setWordRegister(dest_dwreg, value);
       }
       
       if (this.getAddressModeType() == AddressMode.LONG_INDEXED)
       {
    	   // a3,
    	   short tmp_reg = (short) (operands[numberOfBytes-4] - 1);
    	   int index = (int)value & 0xffff;
    	   short tmp_value = (short) memory[(int)tmp_reg];
    	   
    	   value = (short) (tmp_value + index);
    	   state_.setWordRegister(dest_dwreg, value);
       }
       
        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);
       

    }

	private short getWordValue(int[] memory, short location) {
 	   int index = (int)location & 0xffff; // byte index, LSB
 	   int index2 = (int)location+1 & 0xffff;// byte index2, MSB
 	   
 	   short value = (short) memory[(int)index]; // LSB
 	   short value2 = (short) memory[(int)index2]; // MSB
  	  
 	   short RA = (short) (value2 << 8 |  value & 0xff); // put MSB | LSB
 	   
 	   value = RA;
		return value;
	}

	@Override
	public void setAddressMode(AddressMode direct) {
		// TODO Auto-generated method stub
		super.setAddressMode(direct);
	}
	
	
}
