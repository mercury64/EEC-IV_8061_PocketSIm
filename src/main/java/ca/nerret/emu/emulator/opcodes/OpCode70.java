package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

/**
 * OpCode 0x70 MOV M,B.
 * Move B to M or the HL pair.
 * @author http://github.com/alexdforeman
 */
public class OpCode70 extends OpCode implements IOpCode {

    public OpCode70(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
     * @see ca.nerret.emu.emulator.opcodes.IOpcode#exec(ca.nerret.emu.emulator.State)
     */
    @Override
    public final void exec(State state_) {
        int memLocation = (state_.getH() << _SHIFT) | state_.getL();
        state_.getMemory()[memLocation] = state_.getB();
        state_.setPc(state_.getPc() + 1);
    }
}
