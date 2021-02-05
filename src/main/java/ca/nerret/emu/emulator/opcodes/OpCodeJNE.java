package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

public class OpCodeJNE  extends OpCode implements IOpCode {

	 public OpCodeJNE(int opcode, String mnemonic) {
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
	        
	        boolean zeroFlag = state_.getPswBit(ProgramStatusWord.ZERO);
	        
	        // Instruction Operation: 
	        // (PC)<-(PC + Displacement if Z=0,
	        // or (PC)unchanged if Z=1.
	        
	        if ( zeroFlag == ProgramStatusWord.CLEAR)
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
		public void setAddressMode(AddressMode addressMode) {
			// TODO Auto-generated method stub
		    AddressMode am = new AddressMode();
		    am.setType(AddressMode.DIRECT);
			super.setAddressMode(am);
			
		}
}
