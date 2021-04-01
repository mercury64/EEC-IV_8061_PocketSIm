package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

/**
 * LDZBW - Load Byte and Zero-extend to Word
 * 
 * Description:
 * 	LDZBW zero-extends an 8-bit "A" operand in to a 16-bit word by zero filling the high byte, 
 * 	 and then transfers the result to a 16-bit "B" operand location.
 * @author wwhite
 *
 */
public class OpCodeLDZBW extends OpCode<OpCodeLDZBW> implements IOpCode {


    public OpCodeLDZBW(int opcode, String mnemonic) {
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
	        	stateTime = 7;
	        	break;
        }

        operands = new byte[numberOfBytes];
        for (int i = 0; i < numberOfBytes; i++) {
			operands[i] = (byte)memory[pc + i];
		}
       
        short areg = 0;
        short  breg = 0;
        byte data;
        byte indirreg;
        byte indirreg_inc;
        byte offset;
        byte basereg;
        byte indexreg;
        
        long cmpResult;
        
        switch (this.getAddressModeInt())
        {
	        case AddressMode.DIRECT:
	        	byte destRb = operands[numberOfBytes-1];
	        	byte sourceRa = operands[numberOfBytes-2];
	        	byte valueRa = state_.getByteRegister(sourceRa);
	        	
	        	short zeroExtend = (short) (0x00ff & valueRa);
	        	breg = destRb;
	        	areg = zeroExtend;
	        	break;
	        case AddressMode.IMMEDIATE:
	        	data = (byte)operands[numberOfBytes-2];
	        	areg = (short) (0x00ff & data);
	        	breg = operands[numberOfBytes-1];
	        	break;
	        case AddressMode.INDIRECT:

	        	break;
	        case AddressMode.INDIRECT_AUTO_INC:

	        	break;
	        case AddressMode.SHORT_INDEXED:

	        	break;	
	        case AddressMode.LONG_INDEXED:
	        	
	        	break;
        }
       
        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);
       
        state_.setWordRegister(breg, areg);
    }

	@Override
	public void setAddressMode(AddressMode direct) {
		// TODO Auto-generated method stub
		super.setAddressMode(direct);
	}
	
	
}
