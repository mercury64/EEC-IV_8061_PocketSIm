package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * 
 */
public class OpCodeCLRW extends OpCode implements IOpCode {

    public OpCodeCLRW(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}
    
    public OpCodeCLRW(int opcode, String mnemonic, int numBytes) {
    	this(opcode, mnemonic);
    	this.setNumberOfBytes(numBytes);
		
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
     * @see ca.nerret.emu.emulator.opcodes.IOpcode#exec(ca.nerret.emu.emulator.State)
     */
    @Override
    public final void exec(State state_) {
    	
    	super.exec(state_);
        
        state_.setWordRegister(this.getOperandLocation(),this.getResult());
        
        // set program status word (PSW)
        state_.setPswBit(ProgramStatusWord.ZERO, true);
        state_.setPswBit(ProgramStatusWord.NEGATIVE, false);
        state_.setPswBit(ProgramStatusWord.OVERFLOW, false);
        state_.setPswBit(ProgramStatusWord.CARRY, false);   
        
        return;
        
        /**
        int[] memory = state_.getMemory();
        int pc = state_.getPc();
        
        byte register = (byte)memory[pc + 1] ;
        
        // Set words
        short value = 0;
        
        state_.setWordRegister((short)register,value );
        
        // increment program counter
        state_.setPc(pc + _PC_INCR_2);
        
        // update statetime
        state_.updateStateTime(4);
        
        // set program status word (PSW)
        state_.setPswBit(ProgramStatusWord.ZERO, true);
        state_.setPswBit(ProgramStatusWord.NEGATIVE, false);
        state_.setPswBit(ProgramStatusWord.OVERFLOW, false);
        state_.setPswBit(ProgramStatusWord.CARRY, false);        
        
        */
    }
    
	public void setAddressMode(AddressMode mode) {
		// TODO Auto-generated method stub
	    mode.setType(AddressMode.DIRECT);
	    
	    this.addressMode = mode;
	}
	
	public int execDirect()
	{
    	// AssemblerFormat: CLRW
    	// InstructionOperation: (RB)<-^0000
    	// ExecutionStates: 4
    	// MachineFormat: [ ^01 ][ Dest RB ]

		setExecutionStates(4);
		setNumberOfBytes(2);

		byte[] operands = this.getOperands(getNumberOfBytes(), getExecutionStates());
    	
    	byte value = 0;
    	this.setResult(value);
    	
    	this.setOperandLocation(operands[1]);

		return getExecutionStates();
	}
}
