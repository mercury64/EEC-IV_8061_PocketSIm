package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * PUSHW - Push Word on Stack
 * 
 * DESCRIPTION: PUSHW decrements the stack pointer(SP) by two, and then transfers a 16-bit "A" operand to the 
 * 	stack location pointed to by the stack pointer(SP).The SP must be an even address for proper 8061/8065 stack operations.

 * 
 * PSW FLAGS AFFECTED:
 *  Z  N  V  VT C  ST 
 *  -  -  -  -  -  -
 *  
 *  (No flags affected.)
 *  
 *  
 * @author Warren White
 */
public class OpCodePUSHW extends OpCode implements IOpCode {

    public OpCodePUSHW(int opcode, String mnemonic) {
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

        switch (this.getAddressModeInt())
        {
	        case AddressMode.DIRECT:
	        	numberOfBytes = 2;
	        	stateTime = 8;
	        	break;
	        case AddressMode.IMMEDIATE:
	        	numberOfBytes = 3;
	        	stateTime = 9; 
	        	break;
	        case AddressMode.INDIRECT:
	        	numberOfBytes = 2;
	        	stateTime = 11;
	        	break;
	        case AddressMode.INDIRECT_AUTO_INC:
	        	numberOfBytes = 2;
	        	stateTime = 12;
	        	break;
	        case AddressMode.SHORT_INDEXED:
	        	numberOfBytes = 3;
	        	stateTime = 11;
	        	break;	
	        case AddressMode.LONG_INDEXED:
	        	numberOfBytes = 4;
	        	stateTime = 12;
	        	break;
        }
        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);
        
        operands = new byte[numberOfBytes];
        for (int i = 0; i < numberOfBytes; i++) {
			operands[i] = (byte)memory[pc + i];
		}
        
        //int sp = (state.getSP() - 2) & 0xffff;
        //state.setSP(sp);
        //mem.setWord((state.getSS() << 4) + sp, (short) value);
        
        System.err.println("Not Implemented");
    }
}
