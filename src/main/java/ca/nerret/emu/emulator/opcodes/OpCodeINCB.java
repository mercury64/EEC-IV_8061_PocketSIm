package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * 
 */
public class OpCodeINCB extends OpCode implements IOpCode {

    public OpCodeINCB(int opcode, String mnemonic) {
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
        
        byte register = (byte)memory[pc + 1] ;
        byte value = 0 ;
        
        value = (byte) (1 +  state_.getByteRegister(register));
       
        state_.setByteRegister( register,value );
        
        // Now increment the pc by 3
        state_.setPc(pc + _PC_INCR_2);
        
        // set program status word (PSW)
        state_.setPswBit(ProgramStatusWord.ZERO, true);
        state_.setPswBit(ProgramStatusWord.NEGATIVE, false);
        state_.setPswBit(ProgramStatusWord.OVERFLOW, false);
        state_.setPswBit(ProgramStatusWord.OVERFLOW_TRAP, false);
        state_.setPswBit(ProgramStatusWord.CARRY, false);    
        state_.setPswBit(ProgramStatusWord.STICKY_BIT, false);
    }
    
	public void setAddressMode(AddressMode addressMode) {
		// TODO Auto-generated method stub
	    
	   super.setAddressMode(new AddressMode((byte)AddressMode.DIRECT));
		
	}
}
