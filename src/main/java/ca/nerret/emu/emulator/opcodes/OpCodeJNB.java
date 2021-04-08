package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * JNB - Jump if bit equals zero
 * 
 * Description: 
 * 		JNB performs a test on a specified bit(0 thru 7) in an 8-bit "B" operand register.
 * 		If the tested bit is "0", a program counter(PC) relative jump using an 8-bit signed displacement value contained in the 3rd instruction byte occurs.
 *      If the tested bit is "1", a jump is not performed and the next sequential program instruction is executed.
 *      The target label for the jump must be within the following displacement range:-128 to +127 bytes from the end of the instruction.
 * 
 * PSW Flags Affected:
 * 		(No flags affected)
 * 
 * 
 * @author wwhite
 *
 */
public class OpCodeJNB  extends OpCode implements IOpCode {

	 public OpCodeJNB(int opcode, String mnemonic) {
			super(opcode, mnemonic);
			byte opcodeByte = (byte) (opcode & 0B00000111);
			this.setMnemonic("JNB" + opcodeByte);
		}

		/* (non-Javadoc)
	     * @see ca.nerret.emu.emulator.opcodes.IOpcode#exec(ca.nerret.emu.emulator.State)
	     */
	    @Override
	    public final void exec(State state_)
	    {
	    	   
	    	int[] memory = state_.getMemory();
	        
	        int pc = state_.getPc();
	        
	        byte breg = (byte) memory[pc + 1];
	        byte value = state_.getByteRegister(breg);
	        byte displacement = (byte) memory[pc + 2];
	        
	        int newPC = pc + 3;
	        
	        //a9e1: 3c,45,1e            jb    B4,R45,aa02      if (?B4_ATMR2_FLAG_45? = 0)  {
	        //a9e4: 38,0a,1b            jb    B0,Ra,aa02       if (B0_HSO_OVF = 0)  {

	        byte bitToTest = (byte) (this.getOpcode() & 0B00000111);
	        String opcodeString = Integer.toBinaryString(bitToTest);

			//System.out.println( " Bit to test: " + bitToTest+  String.format(" 0B%8s", opcodeString).replace(' ', '0')) ; 
			//System.out.println(" On register " + String.format("R%02X", breg) + " Value: " + Integer.toBinaryString(value));
			
			int setBit = (value >>> bitToTest) & 0x1;
			
			System.out.println(setBit);
	        if ( setBit == 0 )
	        {
	        	// Take jump
	            // If jump taken, state_.updateStateTime(4);
	        	
	        	newPC = newPC + displacement;
	        }

	        state_.setPc(newPC);
	    }
	    
	    public String toString()
	    {
	    	String toReturn = super.toString();
			return toReturn;
	    	
	    }

}
