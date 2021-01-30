package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * INCW - Increment Word
 * 
 * Description: 
 * 		INCW increments by one a 16-blt "B" operand and returns the result to the operand location.
 * 
 * PSW Flags Affected:
 *  Z  N  V  VT C  ST 
 *  √  √  √  √  √  -
 * 
 * FLAG	SET CONDITION
 *   Z,C	One if result is zero(i.e.,original operand was'^FFFF); else zero.
 *    N		One if result is in the range of **8000 to'^FFFF;else zero.
 *    V		One if algebraically correct result is too large to be contained in fifteen bits and sign bit 
 *    		(i.e.,original operand was'*7FFF); else zero.
 *   VT		One if V-flag is set any time during the instruction execution; 
 *   		else state of previous VT-flag.
 *  
 * 
 * @author wwhite
 *
 */
public class OpCodeINCW extends OpCode implements IOpCode {

    public OpCodeINCW(int opcode, String mnemonic) {
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
        short originalRB = 0 ;
        short resultRB = 0;
        
        originalRB = state_.getWordRegister(register);
        resultRB = (short) (originalRB + 1);
        
        // Z,C	One if result is zero(i.e.,original operand was'^FFFF); else zero.
        if (resultRB == 0)
        {
        	 state_.setPswBit(ProgramStatusWord.ZERO, true);
        	 state_.setPswBit(ProgramStatusWord.CARRY, true);    
        	 
        }
        else 
        {
        	state_.setPswBit(ProgramStatusWord.ZERO, false);
       	 	state_.setPswBit(ProgramStatusWord.CARRY, false); 
        }
        
        // N  One if result is in the range of **8000 to'^FFFF;else zero.
        if( resultRB >= 0x8000 && resultRB <= 0xFFFF)
        {
        	 state_.setPswBit(ProgramStatusWord.NEGATIVE, true);
        }
        else
        {
        	 state_.setPswBit(ProgramStatusWord.NEGATIVE, false);
        }
        
        // V One if algebraically correct result is too large to be contained in fifteen bits and sign bit 
        if ( originalRB == 0x7fff )
        {
        	 state_.setPswBit(ProgramStatusWord.OVERFLOW, true);
        	 state_.setPswBit(ProgramStatusWord.OVERFLOW_TRAP, true);
        }
        else
        {
        	 state_.setPswBit(ProgramStatusWord.OVERFLOW, false);
        }

        state_.setWordRegister( register,resultRB );
        
        
        // Now increment the pc by 2
        state_.setPc(pc + _PC_INCR_2);
    
    }
    
	public void setAddressMode(AddressMode addressMode) {
		// TODO Auto-generated method stub
	    
	   super.setAddressMode(new AddressMode((byte)AddressMode.DIRECT));
		
	}
}
