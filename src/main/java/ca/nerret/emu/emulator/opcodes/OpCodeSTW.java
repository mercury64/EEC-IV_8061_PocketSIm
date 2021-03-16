package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

/**
 * @author h
 */
public class OpCodeSTW extends OpCode implements IOpCode {

	public OpCodeSTW(int opcode, String mnemonic) {
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

        short dest_reg, value = 0;
        short src_reg;
        
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
        short registerRA = (short) (operands[1] & 0xff);
        short registerRB = (short) (operands[2] & 0xff);
            
        dest_reg = registerRA;
        if (this.getAddressModeType() == AddressMode.INDIRECT)
        {
        	// AssemblerFormat: STW breg, @indirreg
        	// Instruction operation: ([RA])<-(RB)
        	// Execution states: 7/12
        	// Machine Format: [ ^C2 ],[ DestRA |0_MB],[ SourceRB ]

        	System.err.println("Not Implemented");
     	   System.exit(1);
        }
        
        if (this.getAddressModeType() == AddressMode.INDIRECT_AUTO_INC)
        {
        	// aa30: c2,19,1c            stw   R1c,[R18++]      [R18++] = R1c;
        	
        	// AssemblerFormat: STW breg, (indirreg)+
        	// Instruction operation: ([RA])<-(RB); (RA)<-(RA) + 1
        	// Execution states: 8/13
        	// Machine Format: [ ^C2 ],[ DestRA |1_MB],[ SourceRB ]
        	registerRA = (short) (registerRA & 0xfe);
    		
        	// [RA]
    		short valueRA = (short) state_.getWordRegister((byte) (registerRA));
    		
    		// RB
    		short valueRB = (short) state_.getWordRegister((byte) (registerRB));
    		 
    		// ([RA])<-(RB);
    		state_.setWordRegister((short) (valueRA), (short) (valueRB));

		 	// (RA) <- (RA) + 1
		 	//state_.setWordRegister(registerRA, (short) (registerRA + 2));
		 	   value = (short) (valueRA + 2);
		 	  dest_reg = registerRA;
		 	/*   byte indirectRegRA = operands[numberOfBytes-2];
		 	   indirectRegRA = (byte) (indirectRegRA &  0xfe);
		 	   short destRegRB = operands[numberOfBytes-1];
		 	   
		 	   // [RA]
		 	   short RA = state_.getWordRegister(indirectRegRA);
		 	   
		 	   // (RB) <- ([RA])
		 	  // state_.setByteRegister(destRegRB, RA);
		 	   areg = getByteValue(memory, RA);
		 	   breg = destRegRB;
		 	   
		 	   // (RA) <- (RA) + 1
		 	   state_.setWordRegister(indirectRegRA, (short) (RA + 1));
*/

        }
		if (this.getAddressModeType() == AddressMode.SHORT_INDEXED)
		{
			System.err.println("Not Implemented");
			System.exit(1);
		}
        if (this.getAddressModeType() == AddressMode.LONG_INDEXED)
        {
        	registerRA = (short) (registerRA & 0xfe);
        	registerRA = state_.getWordRegister((byte) registerRA);
        	
        	dest_reg = (short) ((operands[numberOfBytes-2] << 8) | operands[numberOfBytes-3]);
        	System.out.println(String.format(" [0x%04X + 0x%04X]", registerRA, dest_reg));
        	
        	dest_reg = (short) (registerRA + dest_reg);
        	src_reg = (short) (operands[4] & 0xff);
        	
        	value = state_.getWordRegister((byte) src_reg);
        	
        }

        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);
        state_.setWordRegister(dest_reg, value);

    }
}
