package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

public class OpCodeFF extends OpCode implements IOpCode {

	public OpCodeFF(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void exec(State state_) {

		// Nop
        state_.setPc(state_.getPc() + 1);

	}

}
