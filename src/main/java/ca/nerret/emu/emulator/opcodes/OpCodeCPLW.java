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
public class OpCodeCPLW extends OpCode<OpCodeCPLW> implements IOpCode {

	private short result;
	private short operandLocation;
	
    public OpCodeCPLW(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	@Override
    public final void exec(State state) {

      super.exec(state);

      state.setWordRegister(getOperandLocation(),getResult());
     
      
    }

	public void setAddressMode(AddressMode addressMode) {
		// TODO Auto-generated method stub
	    
	   super.setAddressMode(new AddressMode((byte)AddressMode.DIRECT));
		
	}
	
	public void execDirect()
	{
		// Assembler Format: CPLW breg
		// Instruction Operation:  (RB)<- 1's Complement(RB)
		// Execution States: 4
		// MachineFormat: [ ^02 ], [ Source RB ]
    	numberOfBytes = 2;
    	setExecutionStates(4);

    	byte[] operands = this.getOperands(numberOfBytes, getExecutionStates());
    	
    	byte sourceRB = operands[1];
    	
    	short breg = this.getWordValue(sourceRB);
    	
    	breg = (short) (~breg);

    	this.setOperandLocation((short)(sourceRB & 0x00ff));
    	this.setResult(breg);
    	
	}
}
