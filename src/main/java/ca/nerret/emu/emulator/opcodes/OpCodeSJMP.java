package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

/**
 * OpCode 0x2x is SJMP or SCALL
 */
public class OpCodeSJMP extends OpCode implements IOpCode {

	private int offset;
	
    public int getOffset() {
		return this.offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public OpCodeSJMP(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
     * @see ca.nerret.emu.emulator.opcodes.IOpcode#exec(ca.nerret.emu.emulator.State)
     */
    @Override
    public final void exec(State state_) {
        int[] memory = state_.getMemory();

        final int pc = state_.getPc();

        final int ret = pc + 2; // We need to return to the instruction 2 ahead.

        int stateTime = 0;
        
        byte byte1 = (byte)memory[pc];
        byte byte2 = (byte)memory[pc + 1];

        int offset  = (short) ((byte1 & 0x3) << 8);
        offset = offset  | ((byte2) & 0xff);
        
        // PC <- PC+-Displacement (11-bit relative):
        // Need 11-bit relative
        // 7 6 5 4 3|2 1 0  7 6 5 4 3 2 1 0
        //    OP    |    Displacement
        //    0x21         |       0x1d
        // 0 0 1 0 0 0 0 1  0 0 0 1 1 1 0 1
        // - - - - - - 0 1  0 0 0 1 1 1 0 1
        // 0001 0001 1101
        // = 11d = + 0x2004 = 0x2121
        
        // 7 6 5 4 3 2 1 0|7 6 5 4 3 2 1 0
        //     0x2f       |       0x01
        // 0 0 1 0 1 1 1 1|0 0 0 0 0 0 0 1
        // - - - - - - 1 1|0 0 0 0 0 0 0 1
        //    0x3         | 0x0      0x1
        //  0x701
        
        // Bit 3
        switch ( (byte1 & 0x8) >> 3 )
        {
	        case 0:
	            // SJMP, 20 to 27 where b2-b0 specify the sign bit and
	            // the 2 MSB of the address displacement B3 is always 0;
	            stateTime = 8;
	          
	        	break;
	        case 1:
	            // SCALL, 28 to 2f, b2-b0 specifiy the sign bit and
	            // the 2 MSB of address displacement., B3 is always 1;
	            stateTime = 13;
	        	break;
	        
        }
     
        // Handle negative
        if ( ((byte1 & 0x4) >> 2) == 1)
        {
        	// ~ compliment
        	// ~0x03ff = 0000 0011 1111 1111
        	// ........= 1111 1100 0000 0000
        	// 
        	offset = offset | ~0x03ff;
        }

        offset = ret + offset;

        this.setOffset(offset);
        
        state_.setPc(offset);
        
        state_.updateStateTime(stateTime);
        
    }
    
    public String toString()
    {
    	String out = super.toString();
    	out = out + " Jump to: " + String.format("0x%02X",this.getOffset()) + System.lineSeparator();
    	
    	return out;
    }
}
