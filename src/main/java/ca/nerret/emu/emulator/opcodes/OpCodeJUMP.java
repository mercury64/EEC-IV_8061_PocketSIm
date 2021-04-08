package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

/**
 * OpCode 0x2x is SJMP or SCALL
 */
public class OpCodeJUMP extends OpCode implements IOpCode {

	private int offset;
	
    public int getOffset() {
		return this.offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	public OpCodeJUMP(int opcode, String mnemonic) {
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

        // We need to return to the instruction 3 ahead.
        final int ret = pc + 3;

        int stateTime = 8;
        
        byte byteLo = (byte)memory[pc + 1];
        byte byteHi = (byte)memory[pc + 2];

        int offset  = (short) (byteHi << 8);
        offset = offset  | ((byteLo) & 0xff);

        // Handle negative
        if ( ((byteHi & 0x4) >> 2) == 1)
        {
        	offset = offset | ~0x03ff;
        }

        offset = ret + offset;

        this.setOffset((short)offset & 0xffff);
        
        
        state_.setPc((short)offset  & 0x0000ffff);
        
        state_.updateStateTime(stateTime);
        
        //System.out.println(" JUMP to: " + String.format("0x%02X",this.getOffset()));
        
    }
    
	public void setAddressMode(AddressMode addressMode) {
		// TODO Auto-generated method stub
	    
	   super.setAddressMode(new AddressMode());
		
	}

}
