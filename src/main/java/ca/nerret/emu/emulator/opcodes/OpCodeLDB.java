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
        short breg = operands[numberOfBytes-1];
        byte data;
        byte indirreg = 0;
        byte indirreg_inc;
        
        byte basereg;
       
        
        long cmpResult;
        
        switch (this.getAddressModeInt())
        {
	        case AddressMode.DIRECT:
	        	areg = operands[numberOfBytes-2];
	        	break;
	        case AddressMode.IMMEDIATE:
	        	data = (byte)operands[numberOfBytes-2];
	        	areg = data;
	        
	        	break;
	        case AddressMode.INDIRECT:
	        	
	        	indirreg = operands[numberOfBytes-2];
	        	indirreg = (byte) (indirreg &  0xfe);

	        	indirreg = state_.getByteRegister( indirreg );// [R32]
				
				areg = indirreg;	
				
	        	break;
	        case AddressMode.INDIRECT_AUTO_INC:
				
		    	   
		    	   byte indirectRegRA = operands[numberOfBytes-2];
		    	   indirectRegRA = (byte) (indirectRegRA &  0xfe);
		    	   short destRegRB = operands[numberOfBytes-1];
		    	   
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
	        	byte offset  = operands[numberOfBytes-4];
	        	
	        	break;	
	        case AddressMode.LONG_INDEXED:
	        	
	        	byte indexreg  = operands[numberOfBytes-4];
	        	byte offset_lo = operands[numberOfBytes-3];
	        	byte offset_hi = operands[numberOfBytes-2];
	        	
	        	short value = state_.getByteRegister((byte) indexreg);
	        	
	        	short dest_reg = (short) ((offset_lo << 8) | offset_hi);
	        	System.out.println(String.format(" [0x%04X + 0x%04X]", value, dest_reg));
	        	
	        	dest_reg = (short) (value + dest_reg);
	        	//src_reg = (short) (operands[4] & 0xff);
	        	
	        	//value = state_.getWordRegister((byte) src_reg);
	        	
	        	break;
        }

        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);
        state_.setByteRegister(breg, areg);

    }
	
	private byte getByteValue(int[] memory, short location) {
	 	   int index = (int)location & 0xffff; // byte index, LSB
	 	   //int index2 = (int)location+1 & 0xffff;// byte index2, MSB
	 	   
	 	   byte value = (byte) memory[(int)index]; // LSB
	 	  // short value2 = (short) memory[(int)index2]; // MSB
	  	  
	 	   //short RA = (short) (value2 << 8 |  value & 0xff); // put MSB | LSB
	 	   
	 	   //value = RA;
			return value;
		}
}
