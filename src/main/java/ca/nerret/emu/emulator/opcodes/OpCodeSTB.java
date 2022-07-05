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
    public OpCodeSTB(int opcode, String mnemonic, int numBytes) {
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
	       //case AddressMode.IMMEDIATE:
	        //	numberOfBytes = 4;
	        //	stateTime = 5; 
	        //	break;
	        
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
    		// Assembler Format: STB breg, areg
			// Instruction Operation: (Ra) <- (Rb)
			// Execution States: 4
			// Machine Format:[ ^C4 ], [ Dest Ra ], [ Source Rb ]
    		
    		byte destRa = (byte) operands[1];
    		byte sourceRb =  (byte) operands[2];
			
    		byte areg = state_.getByteRegister((short) (sourceRb  & 0xff));
			
			state_.setByteRegister((byte)destRa, (byte)areg);

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
			
			byte breg = state_.getByteRegister((short) (sourceRb  & 0xff));
			
			state_.setWordRegister((short)(basereg + offset), breg);
    	}
    	if ( this.getAddressModeType() == AddressMode.LONG_INDEXED )
    	{
        	byte indexRa = (byte) (register & 0xfe);
        	short offset = (short) ((operands[3] << 8) | operands[2]);
        	byte sourceRb = (byte) (operands[4] & 0xff); 
        	
        	short indexValue = state_.getWordRegister((short) (indexRa & 0xfe));
			byte breg = state_.getByteRegister((short) (sourceRb  & 0xff));
			
			state_.setByteRegister((short)(indexValue + offset), breg);
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
        * 
        */

    }
    
	public int execDirect()
	{
    	// numberOfBytes = 3;
    	// stateTime = 4;
		this.setNumberOfBytes(3);
    	setExecutionStates(4);
    	
		// Assembler Format: STB breg, areg
		// Instruction Operation: (Ra) <- (Rb)
		// Execution States: 4
		// Machine Format:[ ^C4 ], [ Dest Ra ], [ Source Rb ]
		
    	byte[] operands = this.getOperands(getNumberOfBytes(), getExecutionStates());
    	
		byte destRa = (byte) operands[1];
		byte sourceRb =  (byte) operands[2];
		
		byte areg = this.getByteRegister((short) (sourceRb  & 0xff));
		
		this.setByteRegister((byte)destRa, (byte)areg);
    	
		return getExecutionStates();
	}
	
	public int execIndirect()
	{
		this.setNumberOfBytes(3);
    	setExecutionStates(7);
    	
    	// Assembler Format: STB breg, @indirreg
    	// Instruction Operation: ([Ra])<- (Rb)
    	// Execution States: 7/12 
    	// MachineFormat: [ ^C6 ], [ Dest Ra | 1 MB ], [ Source Rb ]
    	
    	// 2076: c6,1a,1c            stb   R1c,[R1a]        [R1a] = R1c;

    	byte[] operands = this.getOperands(getNumberOfBytes(), getExecutionStates());
    	
		byte destRa = (byte) operands[1];
		byte sourceRb =  (byte) operands[2];

    	short value = this.getWordRegister((short) (destRa));
    	sourceRb = this.getByteRegister((short) (sourceRb & 0xff));
		
    	this.setWordRegister(value, (byte)sourceRb);
    	
		return getExecutionStates();
	}
}
