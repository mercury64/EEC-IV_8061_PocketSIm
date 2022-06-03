package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

public class OpCodeJC  extends OpCode implements IOpCode {

	 public OpCodeJC(int opcode, String mnemonic) {
			super(opcode, mnemonic);
			
			this.setNumberOfBytes(2);
			this.setExecutionStates(4);
		}

		/* (non-Javadoc)
	     * @see ca.nerret.emu.emulator.opcodes.IOpcode#exec(ca.nerret.emu.emulator.State)
	     */
	    @Override
	    public final void exec(State state_)
	    {
	    	   
	    	super.exec(state_);
	    	
	        boolean carryFlag = state_.getPswBit((byte) ProgramStatusWord.CARRY);
	        byte offset = this.getByteResult();
	        
	        // Jump on Carry
	        // If C=1
	       // System.out.println(state_.pswFlagsToString());
	        
	        if ( carryFlag == ProgramStatusWord.SET)
	        {
	        	// Take jump
	            // If jump taken, state_.updateStateTime(4);
	        	setExecutionStates(8);
	        	
		        // 8 bit relative
		        // 7 6 5 4 3 2 1 0 | 7 6 5 4 3 2 1 0 
		        //      opcode     |       offset
		        if( (offset & 0x80 >> 8)== 1 )
		        {
		        	offset = (byte) (offset | ~0xff);
		        }
		        
		        state_.setPc(state_.getPc() + offset);
	        }

	    }
	    
	    
		public int execDirect()
		{	
	    	byte[] operands = this.getOperands(getNumberOfBytes(), getExecutionStates());

	    	this.setByteResult(operands[1]);
	    	
		 	return getExecutionStates();
		}
	    
		public void setAddressMode(AddressMode addressMode) {
			// TODO Auto-generated method stub
		    AddressMode am = new AddressMode();
		    am.setType(AddressMode.DIRECT);
			super.setAddressMode(am);
			
		}

}
