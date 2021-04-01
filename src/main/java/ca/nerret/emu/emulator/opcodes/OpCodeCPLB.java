package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

/**
 * NEGW - Negate Word
 * 
 * DESCRIPTION: 
 * 	NEGW performs the 2's complement negation of a 16-bit "B" operand and returns the result to the
 *  operand location.
 * 
 * 
 * PSW FLAGS AFFECTED:
 *  Z  N  V  VT C  ST 
 *  y  y  y  y  y  -
 *  
 *  
 *  
 *  
 * @author Warren White
 */
public class OpCodeCPLB extends OpCode<OpCodeCPLB> implements IOpCode {

	private short result;
	private short operandLocation;
	
    public OpCodeCPLB(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	@Override
    public final void exec(State state) {
		System.err.println(this.getClass().getName() + " Not Implemented Yet.");
		System.exit(1);
      super.exec(state);
      
      state.setWordRegister(getOperandLocation(),getResult());
     
      
    }

	public void setAddressMode(AddressMode addressMode) {
		// TODO Auto-generated method stub
	    
	   super.setAddressMode(new AddressMode((byte)AddressMode.DIRECT));
		
	}
	
	public void execDirect()
	{
		// Assembler Format: NEGW breg
		// Instruction Operation:  (RB)<-0-(RB)
		// Execution States: 4
		// MachineFormat: [ ^03 ], [ Source RB ]
    	numberOfBytes = 2;
    	executionStates = 4;

    	byte[] operands = this.getOperands(numberOfBytes, executionStates);
    	
    	byte sourceRB = operands[1];
    	
    	short breg = this.getWordValue(sourceRB);
    	
    	breg = (short) (breg ^ 0xffff);
    	breg= (short) (breg+1);
    	
    	this.setOperandLocation((short)(sourceRB & 0x00ff));
    	this.setResult(breg);
    	
	}
}
