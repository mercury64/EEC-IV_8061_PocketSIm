package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * 
 * DESCRIPTION: CMPW subtracts a 16-bit "A" operand from a 16-bit "B"operand,
 *   and then sets and clears the PSW flags based upon the result of the operation.
 * The result from the subtraction is not returned.
 * 
 * PSW FLAGS AFFECTED:
 *  Z  N  V  VT C  ST 
 *  √  √  √  √  √  -
 *  
 *  FLAG	SET CONDITION
 *    Z		One if result is zero; else zero.
 *    N		One if result is negative (i.e., reflects the algebraically correct result even if an overflow caused the sign bK to be incorrect);else zero.
 *    V		One if algebraically correct result istoo largeto be represented in fifteen bits and sign bit; else zero.
 *   VT		One if V-fiag is set anytime during the instruction execution; else state of previous VT-fiag.
 *    C		Oneiftheunsignedvalueofthe"B"operandisgreaterthanorequaltotheunsigned value ofthe"A"operand;elsezero.
 *  
 * @author Warren White
 */
public class OpCodeCMPW extends OpCode implements IOpCode {

    public OpCodeCMPW(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}
    
    public OpCodeCMPW(int opcode, String mnemonic, int numBytes) {
    	this(opcode, mnemonic);
    	this.setNumberOfBytes(numBytes);
		
		// TODO Auto-generated constructor stub
	}

	@Override
    public final void exec(State state_) {
        
		
    	super.exec(state_);
        
        //state_.setWordRegister(this.getOperandLocation(),this.getResult());
    	System.err.print(" PSW set flags not implemented: ");
    	System.out.println("PSW Flags <- Compare Results");
    	

    	short data = 0;
		short breg = 0;
		

        switch (this.getAddressModeInt())
        {
	        case AddressMode.DIRECT:
	        	data = getWordRegister(this.getSourceRA());
	        	breg = getWordRegister(this.getSourceRB());
	        	break;
	        case AddressMode.IMMEDIATE:
	        	data = (short) (this.getDataHi() << 8 |   this.getDataLo() & 0xff);
	    		breg = getWordRegister(this.getSourceRB());
	        	break;
	        case AddressMode.INDIRECT:

	        	break;
	        case AddressMode.SHORT_INDEXED:

	        	break;	
        }
    	// TODO:  Need to implement compare, and setting of PSW!!!
		int compare = Short.compare(breg, data);

    	if(compare >= -2)
    	{
    		System.out.println(compare);
    		
    	}
		
		

		// do_sub(reg_r16(OP2), OP1);
		//state_.doSub(breg	, data);
		//long cmpResult = doSub(breg, data);
		System.out.println(" COMPARE Results: " + compare);
		if ( compare == 0) // Z Flag
		{
			state_.setPswBit(ProgramStatusWord.ZERO, true);
		}
		else {
			state_.setPswBit(ProgramStatusWord.ZERO, false);
		}
		
		if( compare < 0 ) // N Flag
		{
			state_.setPswBit(ProgramStatusWord.NEGATIVE, true);
		}
		else {
			state_.setPswBit(ProgramStatusWord.NEGATIVE, false);
		}
		
		if ( compare > 0xfffe )
		{
			state_.setPswBit(ProgramStatusWord.OVERFLOW, true);
			state_.setPswBit(ProgramStatusWord.OVERFLOW_TRAP, true);
		}
		else {
			state_.setPswBit(ProgramStatusWord.OVERFLOW, false);
		}
		
		if ( (int) breg >= (int) data)
		{
			state_.setPswBit(ProgramStatusWord.CARRY, true);
		}
		else {
			state_.setPswBit(ProgramStatusWord.CARRY, false);
		}
		
		
        return;
        
		/**
	 	  *
	 	  *
	 	  *
	 	
	 	int[] memory = state_.getMemory();
		 
        final int pc = state_.getPc();

        byte[] operands;
        int numberOfBytes = 0;
        int stateTime = 0;

        switch (this.getAddressModeInt())
        {
	        case AddressMode.DIRECT:
	        	numberOfBytes = 3;
	        	stateTime = 4;
	        	break;
	        case AddressMode.IMMEDIATE:
	        	numberOfBytes = 4;
	        	stateTime = 5; 
	        	break;
	        case AddressMode.INDIRECT:
	        	numberOfBytes = 3;
	        	stateTime = 6;
	        	break;
	        case AddressMode.SHORT_INDEXED:
	        	numberOfBytes = 4;
	        	stateTime = 6;
	        	break;	
        }
        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);
        
        operands = new byte[numberOfBytes];
        for (int i = 0; i < numberOfBytes; i++) {
			operands[i] = (byte)memory[pc + i];
		}
        
        
         * 9b,ee,80,42         cmpb  R42,[Ree+80] 
         * 1001 1011, 1110 1110, 1000 0000, 0100 0010
         * Short Indexed
         * Ree = 0x0780 or 80, 07
         * 80 = 1000 0000 
         * ***  ^ is negative!!!
         * this is for CMPB, should be similar for CMPW
         
        // Handle negative
        //if ( ((byte1 & 0x4) >> 2) == 1)
        //{
        	// ~ compliment
        	// ~0x03ff = 0000 0011 1111 1111
        	// ........= 1111 1100 0000 0000
        	//
        	//offset = offset | ~0x03ff;
       // }
        	
        	
        // LSB
        // second byte, shift to high word position
        // first byte, mask to low word position
    	short RA = 0;
		short RB = 0;
   
		if (this.getAddressModeType() == AddressMode.DIRECT)
		{
			// Assembler Format: CMPW areg,breg
			// InstructionOperation: (RB)-(RA); PSWFiags <- Compare Results
			// MachineFormat: "[ ^88 ], [ Source RA ], [ Source RB ]
			// ADDRESS MODES:
			// DIRECT AssemblerFormat:
			// InstructionOperation:
			// ExecutionStates: 4
				
			RA = state_.getWordRegister(operands[numberOfBytes-2]);
			RB = state_.getWordRegister(operands[numberOfBytes-1]);
		}
		if (this.getAddressModeType() == AddressMode.IMMEDIATE)
		{
			RA = (short) (operands[2] << 8 |   operands[1] & 0xff);
			RB = state_.getWordRegister(operands[numberOfBytes-1]);
			
		}
		if (this.getAddressModeType() == AddressMode.SHORT_INDEXED)
		{
	    	RA = state_.getWordRegister((byte)operands[numberOfBytes-1]);
			RB = state_.getWordRegister(operands[numberOfBytes-2]);
			
		}
    	// TODO:  Need to implement compare, and setting of PSW!!!
		int compare = Short.compare(RB, RA  );
		
		// do_sub(reg_r16(OP2), OP1);
		//state_.doSub((short) state_.setWordRegister(RB), RA);
		long cmpResult = state_.doSub( RB, RA);
		
		//System.out.println(" Compare: (" + String.format("0x%04X", RB) + ", "+ String.format("0x%04X", RA) + ")");
	//	System.out.println(state_.pswFlagsToString());
		
		if ( compare == 0) // Z Flag
		{
			state_.setPswBit(ProgramStatusWord.ZERO, true);
		}
		else {
			state_.setPswBit(ProgramStatusWord.ZERO, false);
		}
		
		if( compare < 0 ) // N Flag
		{
			state_.setPswBit(ProgramStatusWord.NEGATIVE, true);
		}
		else {
			state_.setPswBit(ProgramStatusWord.NEGATIVE, false);
		}
		
		if ( compare > 0xfffe )
		{
			state_.setPswBit(ProgramStatusWord.OVERFLOW, true);
			state_.setPswBit(ProgramStatusWord.OVERFLOW_TRAP, true);
		}
		else {
			state_.setPswBit(ProgramStatusWord.OVERFLOW, false);
		}
		
		if ( (int) RB >= (int) RA)
		{
			state_.setPswBit(ProgramStatusWord.CARRY, true);
		}
		else {
			state_.setPswBit(ProgramStatusWord.CARRY, false);
		}
		//System.out.println(state_.pswFlagsToString());
		 * 
		 * 
		 */
		
    }
	
	public int execDirect()
	{
    	setExecutionStates(4);
    	
    	byte[] operands = this.getOperands(getNumberOfBytes(), getExecutionStates());
    	
    	this.setSourceRA(operands[1]);
    	this.setSourceRB(operands[2]);

	 	return getExecutionStates();
	}
	
	public int execImmediate()
	{
    	setExecutionStates(5);
    	
    	byte[] operands = this.getOperands(getNumberOfBytes(), getExecutionStates());
    	
    	this.setDataLo(operands[1]);
    	this.setDataHi(operands[2]);
    	this.setSourceRB(operands[3]);

	 	return getExecutionStates();
	}
}
