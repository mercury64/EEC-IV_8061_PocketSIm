package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

/**
 * OpCode 0x2x is SJMP or SCALL
 */
public class OpCodeCALL extends OpCode implements IOpCode {

	private int offset;
	
    public int getOffset() {
		return this.offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public OpCodeCALL(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		
		setNumberOfBytes(3);
		//this.setAddressMode(null);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
     * @see ca.nerret.emu.emulator.opcodes.IOpcode#exec(ca.nerret.emu.emulator.State)
     */
    @Override
    public final void exec(State state_) {
    	
    	super.exec(state_);
        

    	 int ret = state_.getPc();
    	 
    	  offset = ret + offset;

          // SP <- SP-2; 
          state_.decrementSP();
          
          short stack = state_.getWordRegister((short)0x10);
          // Stack <- PC;
          state_.setWordRegister(stack, (short)ret);
          this.setOffset((short)offset & 0xffff);
          
          
          state_.setPc((short)offset  & 0x0000ffff);
          
          System.out.println(" Call to: " + String.format("0x%02X",this.getOffset()));
    	 
        return;
       /* int[] memory = state_.getMemory();

        final int pc = state_.getPc();

        // We need to return to the instruction 3 ahead.
        final int ret = pc + 3;

        int stateTime = 13;
        
        byte opcode = (byte)memory[pc];
        byte byteLo = (byte)memory[pc + 1];
        byte byteHi = (byte)memory[pc + 2];

        int offset  = (short) (byteHi << 8);
        offset = offset  | ((byteLo) & 0xff);
        
        */
        //2088: ef,4c,89            call  a9d7
        
        // SP <- SP-2; Stack <- PC;
        // PC <- PC+-Displacement
        // PC <- PC+-Displacement (16-bit relative):
        // Need 16-bit relative
        // 7 6 5 4 3 2 1 0  7 6 5 4 3 2 1 0  7 6 5 4 3 2 1 0
        //    OP    	   |    Disp. lo    |   Disp. hi
        //    0xef         |       0x4c     |       89
        // 1 1 1 0 1 1 1 1  0 1 0 0 1 1 0 0   1 0 0 0 1 0 0 1
        // - - - - - - - -  
        // 01001100 10001001
        // = 894c = 10001001 01001100 
        // = 894c + 208b = a9d7


     /*
        // Handle negative
        if ( ((byteHi & 0x4) >> 2) == 1)
        {
        	offset = offset | ~0x03ff;
        }

        offset = ret + offset;

        // SP <- SP-2; 
        state_.decrementSP();
        
        short stack = state_.getWordRegister((short)0x10);
        // Stack <- PC;
        state_.setWordRegister(stack, (short)ret);
        this.setOffset((short)offset & 0xffff);
        
        
        state_.setPc((short)offset  & 0x0000ffff);
        
        state_.updateStateTime(stateTime);
        
        System.out.println(" Call to: " + String.format("0x%02X",this.getOffset()));
        */
        
    }
    
	public int execDirect()
	{

		setExecutionStates(13);
		setNumberOfBytes(3);
		
    	byte[] operands = this.getOperands(getNumberOfBytes(), getExecutionStates());
		
    	// [RA]
		short RA = this.getWordRegister(operands[1]);
		byte byteLo = operands[1];
	    byte byteHi = operands[2];
	    
	    int offset  = (short) (byteHi << 8);
        offset = offset  | ((byteLo) & 0xff);
        
     // Handle negative
        if ( ((byteHi & 0x4) >> 2) == 1)
        {
        	offset = offset | ~0x03ff;
        }

        this.setOffset(offset);
        
		return getExecutionStates();
	}
	
	public void setAddressMode(AddressMode addressMode) {
		// TODO Auto-generated method stub
	    
	   super.setAddressMode(new AddressMode((byte)AddressMode.DIRECT));
		
	}

}
