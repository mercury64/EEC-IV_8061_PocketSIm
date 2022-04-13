package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * 
 */
public class OpCodeCLRB extends OpCode implements IOpCode {

    public OpCodeCLRB(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
     * @see ca.nerret.emu.emulator.opcodes.IOpcode#exec(ca.nerret.emu.emulator.State)
     */
    @Override
    public final void exec(State state_) {
    	
		
    	super.exec(state_);
        
        state_.setWordRegister(this.getOperandLocation(),this.getResult());
        
        return;
        
        /**
        int[] memory = state_.getMemory();
        int pc = state_.getPc();
        
        byte register = (byte)memory[pc + 1] ;
        byte value = 0 ;
        state_.setByteRegister(register,value );
        
        // increment program counter
       state_.setPc(pc + 1);
        
        // update statetime
        state_.updateStateTime(4);
        
        // set program status word (PSW)
        state_.setPswBit(ProgramStatusWord.ZERO, true);
        state_.setPswBit(ProgramStatusWord.NEGATIVE, false);
        state_.setPswBit(ProgramStatusWord.OVERFLOW, false);
        state_.setPswBit(ProgramStatusWord.CARRY, false);        
        */
    }
    
	public int execImmediate()
	{
    	this.setNumberOfBytes(2);
    	setExecutionStates(4);
    	
    	byte[] operands = this.getOperands(getNumberOfBytes(), getExecutionStates());
    	
    	byte value = 0;
    	this.setResult(value);
    	
    	this.setOperandLocation(operands[1]);
		return getExecutionStates();
	}

}
