package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

/**
 * @author h
 */
public class OpCodeSTB extends OpCode implements IOpCode {

	public OpCodeSTB(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	// RA <-- RB
    @Override
    public final void exec(State state_) {
        int[] memory = state_.getMemory();
        final int pc = state_.getPc();

        byte[] operands;

        short RA = (short) memory[pc + 1];
        short RB;

        int numberOfBytes = 0;
        int stateTime = 0;

        // Note 13:
        // The Immediate address mode is not valid for this instruction
        switch (this.getAddressModeInt())
        {
	        case AddressMode.DIRECT:
	        	numberOfBytes = 3;
	        	stateTime = 4;
	        	break;
	       // Does not exist for this opcode
	       /* case AddressMode.IMMEDIATE:
	        	numberOfBytes = 4;
	        	stateTime = 5; 
	        	break;
	        */
	        case AddressMode.INDIRECT:
	        	numberOfBytes = 3;
	        	stateTime = 7;
	        	break;
	        case AddressMode.INDIRECT_AUTO_INC:
	        	numberOfBytes = 3;
	        	stateTime = 8;
	        	break;
	        case AddressMode.SHORT_INDEXED:
	        	numberOfBytes = 4;
	        	stateTime = 7;
	        	break;	
	        case AddressMode.LONG_INDEXED:
	        	numberOfBytes = 5;
	        	stateTime = 8;
	        	break;	
        }

        operands = new byte[numberOfBytes];
        for (int i = 0; i < numberOfBytes; i++) {
			operands[i] = (byte)memory[pc + i];
		}
        
        // little endian
        byte register =(byte) (operands[1] & 0xff);
        byte value = (byte) (operands[2] & 0xff);
            
        if (this.getAddressModeType() == AddressMode.INDIRECT)
        {
        	register = (byte) (register & 0xfe);

			register = state_.getByteRegister((byte) (register));
			value = state_.getByteRegister((byte)value);
			
			//register = (byte) (register << 2);
			//value = (byte) (value << 2);

        }
    	if ( this.getAddressModeType() == AddressMode.INDIRECT_AUTO_INC )
    	{
    		System.err.println("Not implemented");
    		System.exit(1);
    	}


        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);
        state_.setByteRegister((byte)register, (byte)value);

    }
}
