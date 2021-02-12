package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

/**
 * 
 */
public class OpCodeRET extends OpCode implements IOpCode {

	public OpCodeRET(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
     * @see ca.nerret.emu.emulator.opcodes.IOpcode#exec(ca.nerret.emu.emulator.State)
     */
    @Override
    public final void exec(State state_) {
        int[] memory = state_.getMemory();

        final int pc = state_.getPc();

        int stateTime = 12;
        
        short stack = state_.getWordRegister((short)0x10);
        short stackValue = state_.getWordRegister(stack);
        final int programCounter = stackValue;
        
        // SP <- SP+2; 
        state_.incrementSP();

        state_.setPc(programCounter);
        
        state_.updateStateTime(stateTime);
        
        System.out.println(" Return to: " + String.format("0x%02X",(short)programCounter));

    }

}
