package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * 
 * DESCRIPTION: PUSHPdecrementsthestackpointer(SP)bytwo,andthenstorestheprocessorstatusword (PSW)atthestacklocationpointedtobytheSP. 
 * TheSPmustbeanevenaddressforproper 8061/8065 stack operations.
 * @author wwhite
 *
 */
public class OpCodePUSHP  extends OpCode implements IOpCode {

	 public OpCodePUSHP(int opcode, String mnemonic) {
			super(opcode, mnemonic);
			// TODO Auto-generated constructor stub
		}

		/* (non-Javadoc)
	     * @see ca.nerret.emu.emulator.opcodes.IOpcode#exec(ca.nerret.emu.emulator.State)
	     */
	    @Override
	    public final void exec(State state_)
	    {
	    	int[] memory = state_.getMemory();
	        
	        int pc = state_.getPc();
	        
	        int newPC = pc + 1;

	        state_.decrementSP();

	        short stackPtr = state_.getWordRegister((short) 0x10);//stack
			short psw = (short)state_.getPSWFlags();
			state_.setWordRegister(stackPtr, psw);
	        state_.setPc(newPC);


	    }
		public void setAddressMode(AddressMode addressMode) {
			// TODO Auto-generated method stub
		    AddressMode am = new AddressMode();
		    
			super.setAddressMode(am);
			
		}
}
