package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

/**
 * @author h
 */
public class OpCodeLDW extends OpCode<OpCodeLDW> implements IOpCode {


    public OpCodeLDW(int opcode, String mnemonic) {
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
	        case AddressMode.LONG_INDEXED:
	        	numberOfBytes = 5;
	        	stateTime = 7;
	        	break;
        }

        operands = new byte[numberOfBytes];
        for (int i = 0; i < numberOfBytes; i++) {
			operands[i] = (byte)memory[pc + i];
		}
        System.out.println("LDW bytes:" + numberOfBytes);
        


        //a9d7: a3,01,00,0d,14      ldw   R14,[R0+d00]     R14 = [d00];
        // little endian
        short dest_dwreg = operands[numberOfBytes-1];
        short value  = (short) ((operands[numberOfBytes-2] << 8) | operands[numberOfBytes-3]);;
        
        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);
        state_.setWordRegister(dest_dwreg, value);

    }
}
