package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * AN3B - LOGICAL-AND BYTES,THREE OPERANDS
 * 
 * Description:
 *     AN3B logically-ANDs an 8-bit "A" operand and an 8-bit "B" operand, 
 *     and returns the result to an 8-bit "D" operand location
 *     
 * Flags:
 *  Z One if result is zero; else zero.
 *	N One if result MSB is one; else zero.
 *	V,C Set to zero upon instruction execution.
 */
public class OpCodeAN3B  extends OpCode implements IOpCode {

    public OpCodeAN3B(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
     * @see ca.nerret.emu.emulator.opcodes.IOpcode#exec(ca.nerret.emu.emulator.State)
     */
    @Override
    public final void exec(State state_)
    {
        int[] memory = state_.getMemory();
        final int pc = state_.getPc();

        // 0x44, 0x45, 0x46, 0x47 OPCode mask
        // 7 6 5 4 3 2 1 0 (2,7) = 0x11
        // 0 1 0 0 0 1 0 1 = 0x45
        // 0 0 0 1 0 0 0 1 = 
        
        byte[] operands;
        int numberOfBytes = 0;
        int stateTime = 0;

        // Mode Mask
        // (0,1)
        //   high     low 
        // 7 6 5 4  3 2 1 0
        // 0 1 0 1  0 0 0 0 = 0x50 - Direct
        // 0 1 0 1  0 0 0 1 = 0x51 - Immediate
        // 0 1 0 1  0 0 1 0 = 0x52 - Indirect
        // 0 1 0 1  0 0 1 1 = 0x53 - Indexed
        
        // low byte
        // 0x0 = direct
        // 0x1 = immediate
        // 0x2 = indirect
        // 0x3 = indexed
     
        switch (this.getAddressModeInt())
        {
	        case AddressMode.DIRECT:
	        	numberOfBytes = 4;
	        	stateTime = 5;
	        	break;
	        case AddressMode.IMMEDIATE:
	        	numberOfBytes = 4;
	        	stateTime = 5; 
	        	break;
	        case AddressMode.INDIRECT:
	        	numberOfBytes = 4;
	        	stateTime = 7;
	        	break;
	        case AddressMode.SHORT_INDEXED:
	        	numberOfBytes = 5;
	        	stateTime = 7;
	        	break;	
        }
        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);
        
        operands = new byte[numberOfBytes];
        for (int i = 0; i < numberOfBytes; i++) {
			operands[i] = (byte)memory[pc + i];
		}

        byte dest_dwreg = operands[3];
        byte Ra = operands[2];
        byte Rb = operands[1];
        byte result = 0;
        
        if (this.getAddressModeType() == AddressMode.DIRECT)
		{
        	System.err.println("Not Implemented yet.");
        	System.exit(1);
		}
        if (this.getAddressModeType() == AddressMode.IMMEDIATE)
		{
        	// AN3B =data, breg, dreg
        	//(Rd) <- DATA && (Rb)
        	
        	byte data = operands[1];
        	byte breg = state_.getByteRegister(operands[2]);
        	byte dreg = operands[3];
        	
        	
        	result = (byte) state_.doANB(breg, data);
        	dest_dwreg = dreg;
		}
        if (this.getAddressModeType() == AddressMode.INDIRECT)
		{
        	System.err.println("Not Implemented yet.");
        	System.exit(1);
		}
        if (this.getAddressModeType() == AddressMode.INDIRECT_AUTO_INC)
		{
        	System.err.println("Not Implemented yet.");
        	System.exit(1);
		}
        if (this.getAddressModeType() == AddressMode.SHORT_INDEXED)
		{
        	System.err.println("Not Implemented yet.");
        	System.exit(1);
		}
        if (this.getAddressModeType() == AddressMode.LONG_INDEXED)
		{
        	System.err.println("Not Implemented yet.");
        	System.exit(1);
		}
        
        // V,C Settozero upon instruction execution.
		state_.setPswBit(ProgramStatusWord.OVERFLOW, false);
		state_.setPswBit(ProgramStatusWord.CARRY, false);
        
        //short resultTEST = state_.doAdd(Ra, Rb);

        if ( result == 0) // Z Flag
 		{
 			state_.setPswBit(ProgramStatusWord.ZERO, true);
 			
 		}
 		else 
 		{
 			state_.setPswBit(ProgramStatusWord.ZERO, false);
 		
 		}
     		
           
 		if( (result & 0x80) == 1 ) // N Flag
 		{
 			state_.setPswBit(ProgramStatusWord.NEGATIVE, true);
 		}
 		else {
 			state_.setPswBit(ProgramStatusWord.NEGATIVE, false);
 		}

 		
        state_.setByteRegister(dest_dwreg, (byte) result);    
        
        System.out.println( state_ );
    }
}
