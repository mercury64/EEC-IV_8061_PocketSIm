package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

/**
 * @author h
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
        }

        operands = new byte[numberOfBytes];
        for (int i = 0; i < numberOfBytes; i++) {
			operands[i] = (byte)memory[pc + i];
		}
        
        
        // little endian
        byte register = operands[2] ;
        byte value = operands[1] ;
        
        if (this.getAddressModeType() == AddressMode.INDIRECT)
        {
        	// register [R14++]
        	short register_value = state_.getByteRegister((byte) (value));// [R32]
			
			value = (byte) register_value;	
        }
		if (this.getAddressModeType() == AddressMode.INDIRECT_AUTO_INC)
		{
		
			// register [R14++]
			short register_value = state_.getWordRegister((byte) (value - 1));// [R32]
			state_.setWordRegister((byte) (value - 1), (short) (register_value + 1));	
		 
			// register [value]
			int mem_index = register_value & 0xffff;
			short tmp_register_value =  (short) memory[(int)mem_index];
			value = (byte) tmp_register_value;	
		
		}
		
        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);
        state_.setByteRegister(register, value);

    }
}
