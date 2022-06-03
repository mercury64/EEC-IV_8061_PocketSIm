package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

public class OpCodeJE  extends OpCode implements IOpCode {

	 public OpCodeJE(int opcode, String mnemonic) {
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
	        
	        boolean zeroFlag = state_.getPswBit((byte)ProgramStatusWord.ZERO);
	        
	    	byte displacement = this.getByteResult();
	    	
	       // System.out.println(state_.pswFlagsToString());
	        // Jump on =
	        // Instruction Operation: (PC)<-(PC)+Displacement if Z=1,or (PC) unchanged if Z=0.
	        // If Z=1
	        
	        if ( zeroFlag == ProgramStatusWord.SET)
	        {
	        	// Take jump
	            // If jump taken, state_.updateStateTime(4);
	        	
	        	setExecutionStates(8);
		        // 8 bit relative
		        // 7 6 5 4 3 2 1 0 | 7 6 5 4 3 2 1 0 
		        //      opcode     |       offset
		        if( (displacement & 0x80 >> 8)== 1 )
		        {
		        	displacement = (byte) (displacement | ~0xff);
		        }
		        
		        state_.setPc(state_.getPc() + displacement);
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
