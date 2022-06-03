package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

public class OpCodeDI extends OpCode implements IOpCode {

	public int numberOfBytes = 1;
	
	public OpCodeDI(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void exec(State state_) {
		
		// Disable Interupts
        //state_.setPc(state_.getPc() + 1);
        
        System.out.print("Disable Interupt...");
        System.err.println("not implemented.");

	}
	
	public int execDirect()
	{
    	// AssemblerFormat: DI
    	// InstructionOperation: PSW(b15) <- 0
    	// ExecutionStates: 4
    	// MachineFormat: [ ^FA ]

		setExecutionStates(4);
		setNumberOfBytes(this.numberOfBytes);

		return getExecutionStates();
	}
	
	public void setAddressMode(AddressMode mode) {
		// TODO Auto-generated method stub
	    mode.setType(AddressMode.DIRECT);
	    
	    this.addressMode = mode;
	}

}
