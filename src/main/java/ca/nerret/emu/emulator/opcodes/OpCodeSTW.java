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
	
    public OpCodeSTW(int opcode, String mnemonic, int numBytes) {
    	this(opcode, mnemonic);
    	this.setNumberOfBytes(numBytes);
		
		// TODO Auto-generated constructor stub
	}

	// RA <-- RB
    @Override
    public final void exec(State state_) {
    	
    	super.exec(state_);
        
        state_.setWordRegister(this.getOperandLocation(),this.getResult());
        
        return;
        
        /**
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
	       // case AddressMode.IMMEDIATE:
	       // 	numberOfBytes = 4;
	       // 	stateTime = 5; 
	       // 	break;
	        
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
    	if ( this.getAddressModeType() == AddressMode.DIRECT )
    	{
            // AssemblerFormat: STW breg, areg
            // Instruction operation: (RA)<-(RB)
            // Execution states: 4
            // Machine Format: [ ^C0 ],[ DestRA ],[ SourceRB ]
            
            short sourceRB = state_.getWordRegister(registerRB);
            
            state_.setWordRegister((short)registerRA,(short) sourceRB);
    	}
        if (this.getAddressModeType() == AddressMode.INDIRECT)
        {
        	// AssemblerFormat: STW breg, @indirreg
        	// Instruction operation: ([RA])<-(RB)
        	// Execution states: 7/12
        	// Machine Format: [ ^C2 ],[ DestRA |0_MB],[ SourceRB ]
        	registerRA = (short) (registerRA & 0xfe);
        	
        	// [RA]
    		short valueRA = (short) state_.getWordRegister((byte) (registerRA));
    		
    		// RB
    		short valueRB = (short) state_.getWordRegister((byte) (registerRB));
    		 
    		// ([RA])<-(RB);
    		state_.setWordRegister((short) (valueRA), (short) (valueRB));

        }
        
        if (this.getAddressModeType() == AddressMode.INDIRECT_AUTO_INC)
        {
        	

        }

    	if ( this.getAddressModeType() == AddressMode.SHORT_INDEXED )
    	{
			// Assembler Format: STW breg, offset (basereg)
			// Instruction Operation: ([RA] + Offset) <- (RB)
			// Execution States: 7/12
			// Machine Format:[ ^C3 ], [ Base RA | 0 MB ], [ +- | Offset ], [ Source RB ]
			byte baseRA = (byte) ((operands[1] & 0xff) & 0xfe);
			byte offset = (byte) (operands[2] & 0xff);
			byte sourceRB = (byte) (operands[3] & 0xff); 
			
			short basereg = state_.getWordRegister((short) (baseRA & 0xff));
			
			short breg = state_.getWordRegister((byte) sourceRB);
			
			state_.setWordRegister((short)(basereg + offset), breg);
    	}

        if (this.getAddressModeType() == AddressMode.LONG_INDEXED)
        {
        	registerRA = (short) (registerRA & 0xfe);
        	registerRA = state_.getWordRegister((byte) registerRA);
        	
        	dest_reg = (short) ((operands[numberOfBytes-2] << 8) | operands[numberOfBytes-3]);
        	//System.out.println(String.format(" [0x%04X + 0x%04X]", registerRA, dest_reg));
        	
        	dest_reg = (short) (registerRA + dest_reg);
        	src_reg = (short) (operands[4] & 0xff);
        	
        	value = state_.getWordRegister((byte) src_reg);
        	 state_.setWordRegister(dest_reg, value);
        }

        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);
       */
    }
    
	public int execIndirectAutoInc()
	{
		// aa30: c2,19,1c            stw   R1c,[R18++]      [R18++] = R1c;
    	
    	// AssemblerFormat: STW breg, (indirreg)+
    	// Instruction operation: ([RA])<-(RB); (RA)<-(RA) + 1
    	// Execution states: 8/13
    	// Machine Format: [ ^C2 ],[ DestRA |1_MB],[ SourceRB ]
		
    	this.setNumberOfBytes(3);
    	setExecutionStates(8);
    	
    	byte[] operands = this.getOperands(getNumberOfBytes(), getExecutionStates());
    	
    	//registerRA = (short) (registerRA & 0xfe);
    	byte dest_ra = (byte)(operands[1]  &  0xfe);
    	byte source_rb = (byte)operands[2];
    	
    	// [RA]
		short valueRA = (short) getWordRegister((byte) (dest_ra));
		
		// RB
		short valueRB = (short) getWordRegister((byte) (source_rb));
		 
		// ([RA])<-(RB);
		this.setWordRegister((short) (valueRA), (short) (valueRB));

	 	// (RA) <- (RA) + 1
		this.setWordRegister(dest_ra, (short) (valueRA + 2));
	 	//short valueRAinc = (short) (valueRA + 2);
	 	 
	 	//dest_reg = registerRA;
	 	 //  byte indirectRegRA = (byte)operands[1];
	 	 //  indirectRegRA = (byte) (indirectRegRA &  0xfe);
	 	 //  short destRegRB = (byte)operands[2];
	 	   
	 	   // [RA]
	 	 //  short RA = this.getWordRegister(indirectRegRA);
	 	   
	 	   // (RB) <- ([RA])
	 	  // state_.setByteRegister(destRegRB, RA);
	 	   //areg = getByteValue(memory, RA);
	 	  // breg = destRegRB;
	 	   
	 	   // (RA) <- (RA) + 1
	 	  // this.setWordRegister(indirectRegRA, (short) (RA + 2));

	 	 //state_.setWordRegister(dest_reg, value);
	 	   
	 	  return getExecutionStates();
	}
}
