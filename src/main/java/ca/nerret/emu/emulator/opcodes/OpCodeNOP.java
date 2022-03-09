package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.mem.Memory;
import ca.nerret.emu.processor.Registers;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;
import ca.nerret.emu.emulator.cpu.RALU;

/**
 * This is a NOP.
 * @author http://github.com/alexdforeman
 */
public class OpCodeNOP extends OpCode implements IOpCode {

    public OpCodeNOP(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
     * @see ca.nerret.emu.emulator.IOpcode#exec(ca.nerret.emu.emulator.State)
     */
    @Override
    public final void exec(State state_) {
        // NOP Just advance.
      //  state_.setPc(state_.getPc() + 1);
    }
    
	public  int execDirect()
    {
    	// AssemblerFormat: NOP
    	// InstructionOperation: None
    	// ExecutionStates: 4
    	// MachineFormat: [ ^FF ]

		setExecutionStates(4);
		setNumberOfBytes(1);

		return getExecutionStates();
    }
	
	public void setAddressMode(AddressMode mode) {
		// TODO Auto-generated method stub
	    mode.setType(AddressMode.DIRECT);
	    
	    this.addressMode = mode;
	}

	//@Override
	public byte exec(RALU alu, Registers registers, Memory memory, byte value) {
		// TODO Auto-generated method stub
		return 0;
	}
}
