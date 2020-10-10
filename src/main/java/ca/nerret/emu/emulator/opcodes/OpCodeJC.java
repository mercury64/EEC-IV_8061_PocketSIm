package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

public class OpCodeJC  extends OpCode implements IOpCode {

	 public OpCodeJC(int opcode, String mnemonic) {
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
	        
	        byte offset = (byte) memory[pc + 1];
	      
	        int newPC = pc + 2;
	        
	        boolean carryFlag = state_.getPswBit(ProgramStatusWord.CARRY);
	        
	        // Jump on Carry
	        // If C=1
	        
	        if ( carryFlag == ProgramStatusWord.SET)
	        {
	        	// Take jump
	            // If jump taken, state_.updateStateTime(4);
	        	
	        	
		        // 8 bit relative
		        // 7 6 5 4 3 2 1 0 | 7 6 5 4 3 2 1 0 
		        //      opcode     |       offset
		        if( (offset & 0x80 >> 8)== 1 )
		        {
		        	offset = (byte) (offset | ~0xff);
		        }
		        
		        newPC = pc + offset + 2;
	        }

	        state_.setPc(newPC);
	    }

}
