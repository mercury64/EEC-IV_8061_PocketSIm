package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

/**
 * OpCode 0x73 MOV M,E.
 * Move E to M or the HL pair.
 * @author http://github.com/alexdforeman
 */
public class OpCode73 extends OpCode implements IOpCode {

    public OpCode73(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
     * @see ca.nerret.emu.emulator.opcodes.IOpcode#exec(ca.nerret.emu.emulator.State)
     */
    @Override
    public final void exec(State state_) {
        int memLocation = (state_.getH() << _SHIFT) | state_.getL();
        state_.getMemory()[memLocation] = state_.getE();
        state_.setPc(state_.getPc() + 1);
    }
}
