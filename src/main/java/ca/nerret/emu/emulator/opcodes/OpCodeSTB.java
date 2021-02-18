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
        byte registerRb = value;
        
    	if ( this.getAddressModeType() == AddressMode.DIRECT )
    	{
        	System.err.println("Not Implemented");
     	   System.exit(1);
        	
    	}
    	if ( this.getAddressModeType() == AddressMode.SHORT_INDEXED )
    	{
			// Assembler Format: STB breg, offset (basereg)
			// Instruction Operation: ([Ra] + Offset) <- (Rb)
			// Execution States: 7/12
			// Machine Format:[ ^C7 ], [ Base Ra | 0 MB ], [ +- | Offset ], [ Source Rb ]
			byte baseRa = (byte) (register & 0xfe);
			byte offset = (byte) (operands[2] & 0xff);
			byte sourceRb = (byte) (operands[3] & 0xff); 
			
			short basereg = state_.getWordRegister((short) (baseRa & 0xff));
			
			byte breg = state_.getByteRegister((byte) sourceRb);
			
			state_.setWordRegister((short)(basereg + offset), breg);
    	}
    	if ( this.getAddressModeType() == AddressMode.LONG_INDEXED )
    	{
    		System.err.println("Not Implemented");
  	   		System.exit(1);
     	}
    	
        if (this.getAddressModeType() == AddressMode.INDIRECT)
        {
        	// Assembler Format: STB breg, @indirreg
        	// Instruction Operation: ([Ra])<- (Rb)
        	// Execution States: 7/12 
        	// MachineFormat: [ ^C6 ], [ Dest Ra | 1 MB ], [ Source Rb ]
        	
        	// 2076: c6,1a,1c            stb   R1c,[R1a]        [R1a] = R1c;
        	byte destRa = (byte) (operands[1] & 0xfe);

        	
        	destRa = state_.getByteRegister((short) (destRa & 0xff));
        	value = state_.getByteRegister((short) (destRa & 0xff));
			
			//register = (byte) (register << 2);
			//value = (byte) (value << 2);
			
			 state_.setByteRegister((byte)destRa, (byte)value);

        }
    	if ( this.getAddressModeType() == AddressMode.INDIRECT_AUTO_INC )
    	{
           	// AssemblerFormat: STB breg, (indirreg)+
        	// Instruction operation: ([Ra])<-(Rb); (Ra)<-(Ra) + 1
        	// Execution states: 8/13
        	// Machine Format: [ ^C6 ],[ DestRa |1_MB],[ SourceRb ]
        	register = (byte) (register & 0xfe);
    		
        	// [RA]
    		short valueRa = (short) state_.getWordRegister((byte) (register));
    		
    		// RB
    		byte valueRb = (byte) state_.getByteRegister((byte) (registerRb));
    		 
    		// ([RA])<-(RB);
    		state_.setWordRegister((short) (valueRa), (short) (valueRb));

		 	// (RA) <- (RA) + 1
		 	valueRa = (short) (valueRa + 1);
		 	
		 	state_.setWordRegister((short)register, valueRa);
    	}


        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);
       

    }
}
