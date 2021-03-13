package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * JLEU - Jump if less than or equal, unsigned
 * 
 * DESCRIPTION: JLEUandJNGTUperformaprogramcounter(PC)relativejumpusingan8-bitsigneddisplace ment value contained in the 2nd instruction byte if either the carry(0)flag is"0"or the zero(Z)flag is "1" in the processor status word (PSW) register; othenwise, the next sequential program instruction is executed. The target label for the jump must be within the following displacement range:-128to+127bytesfrom theend ofthe instruction.
 * 
 * @author wwhite
 *
 */
public class OpCodeJLEU  extends OpCode implements IOpCode {

	 public OpCodeJLEU(int opcode, String mnemonic) {
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
	        
	        byte offset = (byte) memory[pc + 1];
	      
	        int newPC = pc + 2;
	        
	        // No Flags affected.
	        boolean zeroFlag = state_.getPswBit(ProgramStatusWord.ZERO);
	        boolean notCarryFlag = !(state_.getPswBit(ProgramStatusWord.CARRY));
	        
	    	// AssemblerFormat: JLEU or JNGTU
	        // InstructionOperation:
	        	//	(PC) <- (PC) + Displacement if (!C+Z) = 1, or
	        	//	(PC) unchanged if (!C+Z) = 0.
	    	// ExecutionStates: 4 if no jump, 8 if jump taken.
	    	// MachineFormat: [ ^D1 ],[+-| Displacement ]
	        
	        System.out.println(state_.getPsw());
	        
	        if ( (notCarryFlag && zeroFlag) == ProgramStatusWord.SET)
	        {
	        	// Take jump
	            // If jump taken, state_.updateStateTime(4);
	        	
	        	
		        // 8 bit relative
		        // 7 6 5 4 3 2 1 0 | 7 6 5 4 3 2 1 0 
		        //      opcode     |       offset
		        if( (offset & 0x80 >> 8)== 1 )
		        {
		        	offset = (byte) (offset | ~0xff);
		        }
		        
		        newPC = pc + offset + 2;
	        }

	        state_.setPc(newPC);

	    }

}
