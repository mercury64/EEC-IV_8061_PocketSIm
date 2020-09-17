package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

/**
 * OpCode 0x06 is MVI B, D8
 * Loads the B register with the next byte.
 * @author http://github.com/alexdforeman
 */
public class OpCode06 extends OpCode implements IOpCode {

    public OpCode06(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
     * @see ca.nerret.emu.emulator.opcodes.IOpcode#exec(ca.nerret.emu.emulator.State)
     */
    @Override
    public final void exec(State state_) {
        int[] memory = state_.getMemory();
        int pc = state_.getPc();
        int result = memory[pc + 1];
        state_.setB(result);
        state_.setPc(pc + _PC_INCR_2);
    }
}
