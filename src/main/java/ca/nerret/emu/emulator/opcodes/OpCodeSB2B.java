package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * OpCode 0x48,49,4a,4b SB3W
 * 
 * Description: 
 *   SB2W subtracts a 16-bit "A" operand from a 16-bit "B" operand,
 *   and returns the result to a 16-bit "D" operand location. 
 *   The operands are sign-extended prior to the subtraction.
 */
public class OpCodeSB2B  extends OpCode implements IOpCode {

	private short Aoperand;
	private short Boperand;
	private short result;
	private short DoperandLocation;

	
    public OpCodeSB2B(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
     * @see ca.nerret.emu.emulator.opcodes.IOpcode#exec(ca.nerret.emu.emulator.State)
     */
    @Override
    public final void exec(State state_)
    {
    	super.exec(state_);
       
        state_.setWordRegister(this.getOperandLocation(),this.getResult());

    }
    
	public void execDirect()
	{
    	numberOfBytes = 3;
    	setExecutionStates(4);

    	byte[] operands = this.getOperands(numberOfBytes, getExecutionStates());
    	
    	byte sourceRa = (byte) (operands[1]);
    	byte destRb = (byte) (operands[2]);

    	byte valueRA = this.getByteValue(sourceRa);
    	byte valueRB = this.getByteValue(destRb);
    	
    	this.setSubResult(valueRB, valueRA);
    	
    	this.setOperandLocation(destRb);
	
	}

	public  void execShortIndexed()
    {
    	// AssemblerFormat: SB2W offset(basereg),breg
    	// InstructionOperation:(RB) <- (RB)-([RA]+Offset) 
    	// ExecutionStates: 6/11
    	// MachineFormat: [ ^6B ],[ Base RA | 0 MB ],[+-| Offset ], [ Dest RB ]
    	numberOfBytes = 4;
    	setExecutionStates(6);//11

    	byte[] operands = this.getOperands(numberOfBytes, getExecutionStates());
    	
    	byte baseRA = (byte) (operands[1] & 0xfe); // mask out mode bit
    	byte offset = operands[2];
    	byte destRB = operands[3];
    	
    	short valueBaseRA = this.getWordValue(baseRA);
    	short locationRA = (short) (valueBaseRA + offset);
    	
    	short valueRA = this.getWordValue(locationRA);
    	short valueRB = this.getWordValue(destRB);
    	
    	this.setSubResult(valueRB, valueRA);
    	this.setOperandLocation(destRB);
    }

	public short setSubResult(short valueRB, short valueRA) {
		 this.result = super.setSubResult(valueRB, valueRA);
		
		 return this.result;
	}

	private void setOperandLocation(byte destRD) {
		
    	this.DoperandLocation = (short)(destRD & 0xff);		
	}

	protected short getOperandLocation() {
		
		return this.DoperandLocation;
	}

	protected short getResult() {
		
		return this.result;
	}


}
