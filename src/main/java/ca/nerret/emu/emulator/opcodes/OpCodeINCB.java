package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * 
 */
public class OpCodeINCB extends OpCode implements IOpCode {

    public OpCodeINCB(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		this.setNumberOfBytes(2);
		// TODO Auto-generated constructor stub
	}
    

	/* (non-Javadoc)
     * @see ca.nerret.emu.emulator.opcodes.IOpcode#exec(ca.nerret.emu.emulator.State)
     */
    @Override
    public final void exec(State state_) {
    	
		
    	super.exec(state_);
    	
    	byte value = (byte) this.getResult();
        
    	state_.setByteRegister(this.getOperandLocation(),value);
        
    	if ( value == 0) // Z,C Flag
 		{
 			state_.setPswBit(ProgramStatusWord.ZERO, true);
 			state_.setPswBit(ProgramStatusWord.CARRY, true);
 		}
 		else 
 		{
 			state_.setPswBit(ProgramStatusWord.ZERO, false);
 			state_.setPswBit(ProgramStatusWord.CARRY, false);
 		}
 		
         //One if result is in the range of'*80to'*FF;else zero.
 		if( value >= 0x80 && value <= 0xff ) // N Flag
 		{
 			state_.setPswBit(ProgramStatusWord.NEGATIVE, true);
 		}
 		else {
 			state_.setPswBit(ProgramStatusWord.NEGATIVE, false);
 		}
 		
 		if ( value > 0x7f )
 		{
 			state_.setPswBit(ProgramStatusWord.OVERFLOW, true);
 			state_.setPswBit(ProgramStatusWord.OVERFLOW_TRAP, true);
 		}
 		else {
 			state_.setPswBit(ProgramStatusWord.OVERFLOW, false);
 		}
        return;

     	
 		//return getExecutionStates();
    	
/**
        int[] memory = state_.getMemory();
        int pc = state_.getPc();
        
        byte register = (byte)memory[pc + 1] ;
        byte value = 0 ;
        
        value = (byte) (1 +  state_.getByteRegister(register));
       
        state_.setByteRegister( register,value );
        
        // Now increment the pc by 3
        state_.setPc(pc + _PC_INCR_2);
        
       
		*/
    }
    
	public int execDirect()
	{
		setExecutionStates(4);
		
		byte[] operands = this.getOperands(getNumberOfBytes(), getExecutionStates());
		
		byte Rb = (byte)operands[1];
		
		byte RbValue = getByteRegister(Rb);
		
		RbValue++;
		
	    this.setResult(RbValue);
	  	this.setOperandLocation(Rb);
		return getExecutionStates();
	}
	public void setAddressMode(AddressMode mode) {
		// TODO Auto-generated method stub
	    
	    this.addressMode = new AddressMode();
	    this.addressMode.setType(AddressMode.DIRECT);
	}
	
}
