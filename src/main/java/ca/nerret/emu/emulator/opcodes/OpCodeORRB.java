package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * OpCode 0x45 AD3W
 */
public class OpCodeORRB  extends OpCode implements IOpCode {

    public OpCodeORRB(int opcode, String mnemonic) {
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
        final int pc = state_.getPc();

        // 0x44, 0x45, 0x46, 0x47 OPCode mask
        // 7 6 5 4 3 2 1 0 (2,7) = 0x11
        // 0 1 0 0 0 1 0 1 = 0x45
        // 0 0 0 1 0 0 0 1 = 
        
        byte[] operands;
        int numberOfBytes = 0;
        int stateTime = 0;

        // Mode Mask
        // (0,1)
        //   high     low 
        // 7 6 5 4  3 2 1 0
        // 0 1 0 1  0 0 0 0 = 0x50 - Direct
        // 0 1 0 1  0 0 0 1 = 0x51 - Immediate
        // 0 1 0 1  0 0 1 0 = 0x52 - Indirect
        // 0 1 0 1  0 0 1 1 = 0x53 - Indexed
        
        // low byte
        // 0x0 = direct
        // 0x1 = immediate
        // 0x2 = indirect
        // 0x3 = indexed
     
        switch (this.getAddressModeInt())
        {
	        case AddressMode.DIRECT:
	        	numberOfBytes = 3;
	        	stateTime = 4;
	        	break;
	        case AddressMode.IMMEDIATE:
	        	numberOfBytes = 3;
	        	stateTime = 4; 
	        	break;
	        case AddressMode.INDIRECT:
	        	numberOfBytes = 3;
	        	stateTime = 6;
	        	break;
	        case AddressMode.INDIRECT_AUTO_INC:
	        	numberOfBytes = 3;
	        	stateTime = 7;
	        	break;
	        case AddressMode.SHORT_INDEXED:
	        	numberOfBytes = 4;
	        	stateTime = 6;
	        	break;	
        }
        
        operands = new byte[numberOfBytes];
        for (int i = 0; i < numberOfBytes; i++) {
			operands[i] = (byte)memory[pc + i];
		}

        //byte dest_dwreg = operands[1];
        byte Ra = operands[1];
        byte Rb = operands[2];
        byte result = 0;
        
		if (this.getAddressModeType() == AddressMode.INDIRECT_AUTO_INC)
		{
		
			//2073: 92,15,1c            orb   R1c,[R14++]      R1c |= [R14++]; R1c | [R14++]
			// register [R14++]
			Ra = (byte) (Ra & 0xfe);
			short register_value = state_.getWordRegister((short) (Ra));// [R32]
			state_.setWordRegister((short) (Ra - 1), (short) (register_value + 1));	
		 
			// register [value]
			//int mem_index = register_value & 0xff;
			//byte tmp_register_value =  (byte) memory[(int)mem_index];
			//Ra = (byte) tmp_register_value;	
		    Ra = getByteValue(memory, register_value);
		    
			result = (byte) state_.doORRB(state_.getByteRegister(Rb), Ra);
		}


        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);
        
        state_.setByteRegister(Rb, (byte) result);    
        
    }
    
	private byte getByteValue(int[] memory, short location) {
	 	   int index = (int)location & 0xffff; // byte index, LSB
	 	   //int index2 = (int)location+1 & 0xffff;// byte index2, MSB
	 	   
	 	   byte value = (byte) memory[(int)index]; // LSB
	 	  // short value2 = (short) memory[(int)index2]; // MSB
	  	  
	 	   //short RA = (short) (value2 << 8 |  value & 0xff); // put MSB | LSB
	 	   
	 	   //value = RA;
			return value;
		}
}
