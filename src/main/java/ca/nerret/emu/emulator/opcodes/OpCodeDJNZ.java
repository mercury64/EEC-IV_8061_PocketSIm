package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * 
 * DESCRIPTION: DJNZ decrements by one an 8-bit "B" operand, returns the result to the operand location, 
 *   and performs a program counter(PC) relative jump using asigned displacement value contained in the 3rd 
 *   instruction byte if the result is greater than zero. If the result is zero, a jump is not performed 
 *   and the next sequential program instruction is executed. 
 *   The target label for the jump must be within the following displacement range: -128 to +127 bytes from end of the DJNZ instruction.
 * 
 * @author wwhite
 *
 */
public class OpCodeDJNZ  extends OpCode implements IOpCode {

	 public OpCodeDJNZ(int opcode, String mnemonic) {
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
	        
	        int pc = state_.getPc();
	        
	        byte sourceRb = (byte) memory[pc + 1];
	        byte displacement = (byte) memory[pc + 2];
	       
	        int newPC = pc + 3;

	        // Assembler Format: DJNZ breg, label
	        // Instruction Operation:
	        //		(Rb) <- (Rb) - 1;
	        //		(PC)<-(PC) + Displacement if ((Rb) -1) > 0, or 
	        //		(PC)<-(PC) if ((Rb)-1) != 0.
	        // Execution States: 
	        //		5 if no jump.
	        //		9 if jump taken.
	        // Machine Format:
	        //		[ ^E0 ], [ Source Rb ], [ +-| Displacement ]
	        //		Displacement Range: +127to -128(in bytes)
	        //		DisplacementMSB: 0•+Displacement;1«-Displacement
	        
	        byte sourceRbValue = state_.getByteRegister(sourceRb);
	        sourceRbValue = (byte) (sourceRbValue - 1);
	        
	        state_.setByteRegister(sourceRb, sourceRbValue);
	        
	        if ( sourceRbValue != 0 )
	        {   
		        newPC = pc + displacement + 3;
	        }

	        state_.setPc(newPC);


	    }
		public void setAddressMode(AddressMode addressMode) {
			// TODO Auto-generated method stub
		    AddressMode am = new AddressMode();
		    am.setType(AddressMode.DIRECT);
			super.setAddressMode(am);
			
		}
}
