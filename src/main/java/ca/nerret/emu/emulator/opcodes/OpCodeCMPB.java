package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * CMPB - COMPARE BYTES
 * Description: 
 * 	CMPB subtracts an 8-bit "A" operand from an 8-bit "B" operand,
 * 		and then sets and clears the PSW flags based upon the result of the operation.
 * 		The result from the subtraction is not returned.
 * 
 * PSW FLAGS AFFECTED:
 *  Z  N  V  VT C  ST 
 *  √  √  √  √  √  -
 *  
 *  FLAG	SET CONDITION
 *    Z		One if result is zero; else zero.
 *    N		One if result is negative (i.e., reflects the algebraically correct result even if an overflow caused the sign bit to be incorrect);else zero.
 *    V		One if algebraically correct result is too large to be represented in seven bits and sign bit; else zero.
 *   VT		One if V-flag is set any time during the instruction execution; else state of previous VT-flag.
 *    C		One if the unsigned value of the "B "operand is greater than or equal to the unsigned value of the "A" operand; else zero.
 *  
 * 
 * @author Warren White
 */
public class OpCodeCMPB extends OpCode implements IOpCode {

    public OpCodeCMPB(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	@Override
    public final void exec(State state_) {
        int[] memory = state_.getMemory();
        final int pc = state_.getPc();

        byte[] operands;
        int numberOfBytes = 0;
        int stateTime = 0;

        String assemblerFormat =  this.getMnemonic() ;
        String instructionOperation = "";
        
    	System.out.println(state_.pswFlagsToString());
        switch (this.getAddressModeInt())
        {
	        case AddressMode.DIRECT:
	        	assemblerFormat = " areg, breg";
	        	instructionOperation = " (R^^)-(R^);PSWFlags*-CompareResults";
	        	numberOfBytes = 3;
	        	stateTime = 4;
	        	break;
	        case AddressMode.IMMEDIATE:
	        	assemblerFormat = " =data, breg";
	        	instructionOperation = " Rj^)-Data;PSWFlags♦z-yxCwovmutpsraqrpeonRmelskujihltgsf";
	        	numberOfBytes = 3;
	        	stateTime = 4; 
	        	break;
	        case AddressMode.INDIRECT:
	        	assemblerFormat = " @indirreg, breg";
	        	instructionOperation = " (R^^) - ([RJ); PSW Flags <- Compare Results";
	        	numberOfBytes = 3;
	        	stateTime = 6;
	        	break;
	        case AddressMode.INDIRECT_AUTO_INC:
	        	assemblerFormat = "(indirreg)+,breg";
	        	instructionOperation = " (R.) - ([RJ); (R^)«- (R^) + 1; PSW Flags Do88\n" + "Compare Results";
	        	numberOfBytes = 3;
	        	stateTime = 7;
	        	break;
	        case AddressMode.SHORT_INDEXED:
	        	assemblerFormat = "offset(basereg), breg";
	        	instructionOperation = "R^,)-([R_]+Offset);PSWFlags«-CompareResults";
	        	numberOfBytes = 4;
	        	stateTime = 6;
	        	break;	
	        case AddressMode.LONG_INDEXED:
	        	assemblerFormat = "offset(indexreg), breg";
	        	instructionOperation = "(R^^)-([RJ+Offset);PSWFlags CompareResults";
	        	numberOfBytes = 5;
	        	stateTime = 7;
	        	break;
        }
        
        this.getAddressMode().setAssemblerFormat(assemblerFormat);
        
        /*
         * 9b,ee,80,42         cmpb  R42,[Ree+80] 
         * 1001 1011, 1110 1110, 1000 0000, 0100 0010
         * Short Indexed
         * Ree = 0x0780 or 80, 07
         * 80 = 1000 0000 
         * 
         */
        // Handle negative
        //if ( ((byte1 & 0x4) >> 2) == 1)
        //{
        	// ~ compliment
        	// ~0x03ff = 0000 0011 1111 1111
        	// ........= 1111 1100 0000 0000
        	//
        	//offset = offset | ~0x03ff;
        	// offset = 80 = 1000 0000 
       // }
        /*
         *  if(ofst & 0x0080)       // make negative
         *  ofst |= ~0x00ff;
         *  
         *  offset = 80 = 1000 0000 
         *    & 0000 0000 1000 0000
         *    = 0x0080
         *    ~ 0000 0000 1111 1111
         *    ..1111 1111 0000 0000
         *    |= 1111 1111 1000 0000
         *    = 0xff80 = -80
         *    
         */

        operands = new byte[numberOfBytes];
        for (int i = 0; i < numberOfBytes; i++) {
			operands[i] = (byte)memory[pc + i];
		}

        byte areg = 0;
        byte breg = 0;
        byte data;
        byte indirreg;
        byte indirreg_inc;
        byte offset;
        byte basereg;
        byte indexreg;
        
        long cmpResult;
        
        switch (this.getAddressModeInt())
        {
	        case AddressMode.DIRECT:

	        	break;
	        case AddressMode.IMMEDIATE:
	        	
	        	// Assembler Format: CMPB =data, breg
	        	// Instruction Operation: (Rb) - Data; PSW Flags <- Compare Results
	        	// Execution States: 4
	        	// Machine Format: [ ^99 ], [ Data Byte ], [ Source Rb ]
	        	data = (byte)operands[numberOfBytes-2];
	        	areg = data;
	        	breg = state_.getByteRegister(operands[numberOfBytes-1]);
	        	
	        	//System.out.println("Unsigned " + ((areg & 0xf)));
	        	break;
	        case AddressMode.INDIRECT:
	        	System.err.println("Not Implemented");
	     	   System.exit(1);
	        	break;
	        case AddressMode.INDIRECT_AUTO_INC:
	        	System.err.println("Not Implemented");
	     	   System.exit(1);
	        	break;
	        case AddressMode.SHORT_INDEXED:
	        	// Assembler Format: CMPB offset(basereg),breg
	        	// Instruction Operation: (Rb) - ([Ra] + Offset); PSW Flags <- Compare Results
	        	// Execution States: 6/11
	        	// Machine Format: [ ^9B ], [ Base Ra | 0 MB ], [ +-| Offset Byte ], [ Source Rb ]
	        	
	        	breg   = (byte)operands[numberOfBytes-1];
	        	offset = (byte)operands[numberOfBytes-2];
	        	areg   = (byte) (operands[numberOfBytes-3] & 0xfe);
	        	
	        	short offsetLocation = state_.getWordRegister((short) (areg & 0xff));
	        	byte value = this.getByteValue(memory, (short) (offsetLocation + offset));
	        	
	        	byte valueRb =  state_.getByteRegister(breg);
	        	
	        	breg = valueRb;
	        	areg = value;
	        	break;	
	        case AddressMode.LONG_INDEXED:
	        	// Assembler Format: CMPB offset(indexreg),breg
	        	// Instruction Operation: (Rb) - ([Ra] + Offset); PSW Flags <- Compare Results
	        	// Execution States: 7/12
	        	// Machine Format: [ ^9B ], [ Base Ra | 1 MB ], [ Offset lo Byte ], [ +-| Offset Hi Byte ], [ Source Rb ]
	       	    breg   = (byte)operands[numberOfBytes-1];
	        	byte hi_offset = (byte) (operands[numberOfBytes-2]);
	        	byte lo_offset = (byte) (operands[numberOfBytes-3]);
	        	areg   = (byte) (operands[numberOfBytes-4] & 0xfe);

	        	short wordOffset = (short) (hi_offset);
	        	wordOffset = (short) (wordOffset << 8);
	        	wordOffset = (short) (wordOffset | (lo_offset & 0xff));

	        	short indexValue = state_.getWordRegister((short) (areg & 0xff));
			    valueRb = state_.getByteRegister(breg);

	        	//short sOffset = (short) ((offset_hi << 8) | (offset_lo & 0xff));

	        	short offsetWord = (short) (indexValue + wordOffset);

	        	areg = this.getByteValue(memory, offsetWord);
				breg = valueRb;
				//areg = state_.getByteRegister((short) ());
	        	break;
        }
        
    	// TODO:  Need to implement compare, and setting of PSW!!!
		int compare = Short.compare( breg, (byte) (areg & 0xf));

        cmpResult = state_.doByteSub( (byte) (breg & 0xf), (byte) (areg & 0xf));
        
        //System.out.println(" Compare: (" + String.format("0x%04X", breg) + ", "+ String.format("0x%04X", areg) + ")");
		
		if ( compare == 0) // Z Flag
		{
			state_.setPswBit(ProgramStatusWord.ZERO, true);
		}
		else 
		{
			state_.setPswBit(ProgramStatusWord.ZERO, false);
		}
		if( compare < 0 ) // N Flag
		{
			state_.setPswBit(ProgramStatusWord.NEGATIVE, true);
		}
		else
		{
			state_.setPswBit(ProgramStatusWord.NEGATIVE, false);
		}
		
		if ( compare > 0xfe )
		{
			state_.setPswBit(ProgramStatusWord.OVERFLOW, true);
			state_.setPswBit(ProgramStatusWord.OVERFLOW_TRAP, true);
		}
		else {
			state_.setPswBit(ProgramStatusWord.OVERFLOW, false);
		}
		
		if ( (breg & 0xf) >= (areg & 0xf))
		{
			state_.setPswBit(ProgramStatusWord.CARRY, true);
		}
		else {
			state_.setPswBit(ProgramStatusWord.CARRY, false);
		}
        
        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);

        //System.out.println(state_.pswFlagsToString());
 
    }
}
