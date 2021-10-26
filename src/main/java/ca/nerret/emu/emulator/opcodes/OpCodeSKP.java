package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

public class OpCodeSKP extends OpCode implements IOpCode {

	public OpCodeSKP(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void exec(State state_) {

		// Nop
        state_.setPc(state_.getPc() + 1);

	}
	
	public int execDirect()
	{
    	// AssemblerFormat: SKP
    	// InstructionOperation: (PC)<-(PC)+1; fetch but do not execute next instruction byte from memory
    	// ExecutionStates: 4
    	// MachineFormat: [ ^00 ][ Byte Skipped ]

		setExecutionStates(4);
		
		// NOTE: SKP is actually 1 instruction byte long although 2 bytes are fetched from memory
		setNumberOfBytes(2);

		return getExecutionStates();
	}

}
