package ca.nerret.emu.emulator.opcodes;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * 
 * Jump on Not Carry
 * JNC/JLTU/JNGEU
 * If C=0
 */
public class OpCodeJNC extends OpCode implements IOpCode {

    public OpCodeJNC(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		this.setNumberOfBytes(2);
		this.setExecutionStates(4);
	}

	/* (non-Javadoc)
     * @see ca.nerret.emu.emulator.opcodes.IOpcode#exec(ca.nerret.emu.emulator.State)
     */
    @Override
    public final void exec(State state_) {
        
    	super.exec(state_);
    	
    	
        //state_.setWordRegister(this.getOperandLocation(),this.getResult());
    	byte offset = this.getByteResult();
    	
        //boolean carryFlag = state_.getPswBit(ProgramStatusWord.CARRY);
        boolean carryFlag = state_.getPswBit((byte) ProgramStatusWord.CARRY);
        
        // Jump on Not Carry
        // if C=0
        if ( carryFlag == ProgramStatusWord.CLEAR)
        {
        	// Take jump
            // If jump taken
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
