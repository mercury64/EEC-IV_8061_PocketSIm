package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

public class OpCodeEI extends OpCode implements IOpCode {

	public OpCodeEI(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void exec(State state_) {
		
		// Disable Interupts
        state_.setPc(state_.getPc() + 1);
        
        // execution state 1
        
        System.out.println("Enable Interupt...not implemented.");

	}

}
