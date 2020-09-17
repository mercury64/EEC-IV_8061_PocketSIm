package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

/**
 * OpCode 0x31 is LXI SP, D16.
 * Loads the sp register with the next 16 bits.
 * @author http://github.com/alexdforeman
 */
public class OpCode31 extends OpCode implements IOpCode {

    public OpCode31(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	private static final int _SHIFT = 8;
    private static final int _PC_INCR = 3;

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
        state_.setSp(result);
        // Now increment the pc by 3
        state_.setPc(pc + _PC_INCR);
    }
}
