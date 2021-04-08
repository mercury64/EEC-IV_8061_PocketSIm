package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
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
public class OpCodePUSHW extends OpCode<OpCodePUSHW> implements IOpCode {

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

        operands = new byte[numberOfBytes];
        for (int i = 0; i < numberOfBytes; i++) {
			operands[i] = (byte)memory[pc + i];
		}
        
    	short RA = 0;
		short RB = 0;
   
		state_.decrementSP();
		if (this.getAddressModeType() == AddressMode.DIRECT)
		{
			RA = state_.getWordRegister(operands[numberOfBytes-1]);
			RB = state_.getWordRegister((short) 0x10);//stack
			
			state_.setWordRegister(RB, RA);
		}
		if (this.getAddressModeType() == AddressMode.IMMEDIATE)
		{
			RA = (short) (operands[2] << 8 |   operands[1] & 0xff);
			RB = state_.getWordRegister((short) 0x10);//stack
			
			state_.setWordRegister(RB, RA);
			
		}
		if (this.getAddressModeType() == AddressMode.INDIRECT)
		{
			RA = (short) (operands[numberOfBytes-1]  & 0xfe);
			RB = state_.getWordRegister((short) 0x10);//stack
			
			// [RA]
        	RA = state_.getWordRegister(RA);
        	
            byte byteLo = (byte)memory[RA];
            byte byteHi = (byte)memory[RA + 1];

            int offset  = (short) (byteHi << 8);
            offset = offset  | ((byteLo) & 0xff);
			RA = (short) offset;
        	// ([SP]) <- ([RA])
			state_.setWordRegister(RB, RA);
			
		}
		if (this.getAddressModeType() == AddressMode.INDIRECT_AUTO_INC)
		{
			short indirectRA = (short) (operands[numberOfBytes-1]  & 0xfe);
			RB = state_.getWordRegister((short) 0x10);//stack
			
			// RA
			RA = state_.getWordRegister(indirectRA);
	    	   
			// [RA]
	    	short RAvalue = this.getWordValue(memory, RA);
	    	   
			// ([SP]) <- ([RA])
			state_.setWordRegister(RB, RAvalue);

	    	// (RA) <- (RA) + 2
	    	state_.setWordRegister(indirectRA, (short)(RA + 2));
			
		}
		if (this.getAddressModeType() == AddressMode.SHORT_INDEXED)
		{
	    	//RA = state_.getWordRegister((byte)operands[numberOfBytes-1]);
			//RB = state_.getWordRegister(operands[numberOfBytes-2]);
			
		}
		if (this.getAddressModeType() == AddressMode.LONG_INDEXED)
		{
			// PUSHW offset (indexreg)
			// (SP)<-(SP)-2;// done above for all PUSHW
			// ([SP])<-([Ra]+Offset)
			
			// [ ^CB ], [ IndexRa| 0x1 MB ], [ Offset Lo Byte ], [ +- | Offset Hi Byte ]
			
	    	//RA = state_.getWordRegister((byte)operands[numberOfBytes-1]);
			//RB = state_.getWordRegister(operands[numberOfBytes-2]);
        	byte indexRa  = (byte) (operands[numberOfBytes-3] & 0xfe);
        	RB = state_.getWordRegister((short) 0x10);//stack
        	byte offset_lo = operands[numberOfBytes-2];
        	byte offset_hi = operands[numberOfBytes-1];
        	
        	short Ravalue = state_.getByteRegister((byte) indexRa);
        	
        	short offset = (short) ((offset_hi << 8) | (offset_lo & 0xff));
        	
        	//System.out.println(String.format(" [0x%04X + 0x%04X]", Ravalue, offset));
        	//System.out.println(String.format("Offset hi & lo : [0x%04X + 0x%04X]", (short) (offset_hi << 8), offset_lo));
        	
        	Ravalue = (short) (Ravalue + offset);
        	
        	state_.setWordRegister(RB, Ravalue);
		}
		
        //int sp = (state.getSP() - 2) & 0xffff;
        //state.setSP(sp);
        //mem.setWord((state.getSS() << 4) + sp, (short) value);
        
        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);

    }
}
