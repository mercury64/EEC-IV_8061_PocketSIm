package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

/**
 * OpCode 0x1A is LDAX D.
 * Copies D&E pair into Accumulator.
 * A <- (DE)
 * @author http://github.com/alexdforeman
 */
public class OpCode1A extends OpCode implements IOpCode {

    public OpCode1A(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
     * @see ca.nerret.emu.emulator.opcodes.IOpcode#exec(ca.nerret.emu.emulator.State)
     */
    @Override
    public final void exec(State state_) {
        state_.setA((state_.getD() << _SHIFT) | state_.getE());
        state_.setPc(state_.getPc() + 1);
    }
}
