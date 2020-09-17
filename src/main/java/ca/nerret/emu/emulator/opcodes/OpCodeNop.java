package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

/**
 * This is a NOP.
 * @author http://github.com/alexdforeman
 */
public class OpCodeNop extends OpCode implements IOpCode {

    public OpCodeNop(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
     * @see ca.nerret.emu.emulator.IOpcode#exec(ca.nerret.emu.emulator.State)
     */
    @Override
    public final void exec(State state_) {
        // NOP Just advance.
        state_.setPc(state_.getPc() + 1);
    }
}
