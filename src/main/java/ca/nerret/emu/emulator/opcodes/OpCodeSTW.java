package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

/**
 * @author h
 */
public class OpCodeSTW extends OpCode implements IOpCode {

	public OpCodeSTW(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	// RA <-- RB
    @Override
    public final void exec(State state_) {
        int[] memory = state_.getMemory();
        final int pc = state_.getPc();

        byte[] operands;

        short RA = (short) memory[pc + 1];
        short RB;

        int numberOfBytes = 0;
        int stateTime = 0;

        // Note 13:
        // The Immediate address mode is not valid for this instruction
        switch (this.getAddressModeInt())
        {
	        case AddressMode.DIRECT:
	        	numberOfBytes = 3;
	        	stateTime = 4;
	        	break;
	       // Does not exist for this opcode
	       /* case AddressMode.IMMEDIATE:
	        	numberOfBytes = 4;
	        	stateTime = 5; 
	        	break;
	        */
	        case AddressMode.INDIRECT:
	        	numberOfBytes = 3;
	        	stateTime = 7;
	        	
	        	break;
	        case AddressMode.SHORT_INDEXED:
	        	numberOfBytes = 4;
	        	stateTime = 7;
	        	break;	
        }

        operands = new byte[numberOfBytes];
        for (int i = 0; i < numberOfBytes; i++) {
			operands[i] = (byte)memory[pc + i];
		}
        
        // little endian
        short register = (short) (operands[1] & 0xff);
        short value = (short) (operands[2] & 0xff);
            
        
        if (this.getAddressModeType() == AddressMode.INDIRECT)
        {
        	if (  register%2 > 0 )
        	{
        		register = (short) (register-1);
        		this.getAddressMode().setType(AddressMode.INDIRECT_AUTO_INC);
        		short next_reg = (short) state_.getWordRegister((byte) (register));
        		
        		state_.setWordRegister((short) (register), (short) (next_reg + 2));
        		register = next_reg;
        		
        		
        	}
        	
        
        }

        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);
        state_.setWordRegister(register, value);

    }
}
