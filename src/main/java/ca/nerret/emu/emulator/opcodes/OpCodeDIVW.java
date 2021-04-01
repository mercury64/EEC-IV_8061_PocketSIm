package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * OpCode 0x8c, 8d, 8e, 8f
 * 
 * Description: 
 *   DIVW DIVWdividesa32-bit"B"operand(dividend)bya16-bit"A"operand(divisor)usingunsigned arithmetic. 
 *   Thefinal16-bitquotientisreturnedtothe"B'operandlowwordlocationandthe16-bit remainder(alwayspositive)isreturnedtothe"B"operandhighwordbcation. 
 *   DIVBperformsa signed divide if preceded with a signed prefix instruction.
 */
public class OpCodeDIVW  extends OpCode implements IOpCode {

	private short Aoperand;
	private short Boperand;
	private short result;
	private short DoperandLocation;

	
    public OpCodeDIVW(int opcode, String mnemonic) {
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
		/*
		 * AssemblerFormat:
		 * InstructionOperation: 
		 * ExecutionStates: 
		 * Machine Format:
		 * DIVW areg,breg (Rgg)/(R^^);(RggLowWord)«-Quotient;(RggHighWord)*-Remainder
26
*80 Source Dest Rgg
		 */
    	numberOfBytes = 3;
    	executionStates = 26;

    	byte[] operands = this.getOperands(numberOfBytes, executionStates);
    	
    	byte sourceRA = (byte) (operands[1]);
    	byte destRBB = (byte) (operands[2]);

    	short valueRA = this.getWordRegister(sourceRA);
    	int valueRBB = this.getIntRegister(destRBB);
    	
    	int wordReturned = 0;
    	try
    	{
    		wordReturned = (int) (valueRBB / valueRA);
    		System.err.println("NotImplemented: Need quotient and remainder");
    		System.exit(1);
    	}
    	catch(ArithmeticException e)
    	{
    		
    	}
    	
    	this.setOperandLocation(destRBB);
    	this.setResult((short) wordReturned);
	
	}

	public  void execShortIndexed()
    {
    	// AssemblerFormat: ML2B offset(basereg),breg
    	// InstructionOperation:(RB) <- (Rb)*([Ra]+Offset) 
    	// ExecutionStates: 19/24
    	// MachineFormat: [ ^7F ],[ Base Ra | 0 MB ],[+-| Offset ], [ Dest RB ]

		System.err.println("NotImplemented");
		System.exit(1);
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
