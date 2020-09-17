package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

/**
 * OpCode 0xC3 is JMP adr.
 * Takes next two bytes as the Address to jump to.
 * pc <= adr.
 * @author http://github.com/alexdforeman
 */
public class OpCodeC3 extends OpCode implements IOpCode {

    public OpCodeC3(int opcode, String mnemonic) {
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
        int byte1 = memory[pc + 1];
        int byte2 = memory[pc + 2];
        int result = (byte2 << _SHIFT) | byte1;
        state_.setPc(result);
    }
}
