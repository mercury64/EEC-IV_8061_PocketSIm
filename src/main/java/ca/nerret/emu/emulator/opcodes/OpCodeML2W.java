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
public class OpCodeML2W  extends OpCode implements IOpCode {

	private short Aoperand;
	private short Boperand;
	private short result;
	private short DoperandLocation;

	
    public OpCodeML2W(int opcode, String mnemonic) {
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
    	System.err.println("Direct Not Implemented yet."+ this.getClass().getSimpleName());
    	System.exit(1);
    	numberOfBytes = 3;
    	setExecutionStates(17);

    	byte[] operands = this.getOperands(numberOfBytes, getExecutionStates());
    	
    	byte sourceRa = (byte) (operands[1]);
    	byte destRb = (byte) (operands[2]);

    	byte valueRa = this.getByteValue(sourceRa);
    	byte valueRb = this.getByteValue(destRb);
    	
    	short wordReturned = (short) (valueRb * valueRa);
    	
    	this.setOperandLocation(destRb);
    	this.setResult(wordReturned);
	
	}
	
	public void execImmediate()
	{
    	numberOfBytes = 4;
    	setExecutionStates(27);

    	byte[] operands = this.getOperands(numberOfBytes, getExecutionStates());
    	
    	byte dataByteLow = (byte) (operands[1]);
    	byte dataByteHi  = (byte) (operands[2]);


        short data = (short) (dataByteHi << 8);
        data = (short) (data  | ((dataByteLow) & 0xff));
        
    	byte destRBB = (byte) (operands[3]);
    	short valueRB = this.getWordRegister(destRBB);

    	int doubleWordToReturn = (int) valueRB * data;
        
        
    	this.setOperandLocation(destRBB);
    	this.setResult(doubleWordToReturn);
		
	}

	private void setResult(int doubleWordToReturn) {
		// TODO Auto-generated method stub
		
	}

	public  void execShortIndexed()
    {
    	System.err.println("Direct Not Implemented yet."+ this.getClass().getSimpleName());
    	System.exit(1);
    	// AssemblerFormat: ML2B offset(basereg),breg
    	// InstructionOperation:(RB) <- (Rb)*([Ra]+Offset) 
    	// ExecutionStates: 19/24
    	// MachineFormat: [ ^7F ],[ Base Ra | 0 MB ],[+-| Offset ], [ Dest RB ]
    	numberOfBytes = 4;
    	setExecutionStates(19);//24

    	byte[] operands = this.getOperands(numberOfBytes, getExecutionStates());
    	
    	byte baseRa = (byte) (operands[1] & 0xfe); // mask out mode bit
    	byte offset = operands[2];
    	byte destRB = operands[3];
    	
    	short valueBaseRa = this.getWordRegister((short)(baseRa & 0x00ff));
    	short locationRa = (short) (valueBaseRa + offset);
    	
    	byte valueRa = this.getByteValue(locationRa);
    	byte valueRb = this.getByteRegister((short) (destRB & 0x00ff));
    	
    	short wordResult = (short) (valueRb * valueRa);
    	this.setResult(wordResult);
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
