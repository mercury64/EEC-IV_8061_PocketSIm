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
public class OpCodeSB3W  extends OpCode implements IOpCode {

	private short Aoperand;
	private short Boperand;
	private short result;
	private short DoperandLocation;

	
    public OpCodeSB3W(int opcode, String mnemonic) {
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

	public  void execShortIndexed()
    {
    	// AssemblerFormat: SB3W offset(basereg),breg,dreg
    	// InstructionOperation:(RD) <- (RB)-([RA]+Offset) 
    	// ExecutionStates: 7/12
    	// MachineFormat: [ ^4B ],[ Base RA | 0 MB ],[+-| Offset ],[ Source RB ], [ Dest RD ]
    	numberOfBytes = 5;
    	setExecutionStates(7);//12

    	byte[] operands = this.getOperands(numberOfBytes, getExecutionStates());
    	
    	byte baseRA = (byte) (operands[1] & 0xfe); // mask out mode bit
    	byte offset = operands[2];
    	byte sourceRB = operands[3];
    	byte destRD = operands[4];
    	
    	short valueBaseRA = this.getWordValue(baseRA);
    	short locationRA = (short) (valueBaseRA + offset);
    	
    	short valueRA = this.getWordValue(locationRA);
    	short valueRB = this.getWordValue(sourceRB);
    	
    	this.setSubResult(valueRB, valueRA);
    	this.setOperandLocation(destRD);
    }

	public short setSubResult(short valueRB, short valueRA) {
		 this.result =super.setSubResult(valueRB, valueRA);
		
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
