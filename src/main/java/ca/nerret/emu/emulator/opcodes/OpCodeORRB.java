package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * OpCode 0x45 AD3W
 */
public class OpCodeORRB  extends OpCode<OpCodeORRB> implements IOpCode {

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
        
        if (this.getAddressModeType() == AddressMode.DIRECT)
		{
        	System.err.println("Not Implemented yet.");
        	System.exit(1);
		}
        if (this.getAddressModeType() == AddressMode.IMMEDIATE)
		{
        	// Assembler Format: ORRB =data,breg
        	// Instruction Operation: (Rb) <- Data + (Rb)
        	// Execution States: 4
        	// Machine Format: [ ^91 ], [ Data Byte ], [ Dest Rb ]
        	
        	byte data = operands[1];
        	byte RbValue = state_.getByteRegister((short) (Rb & 0xff));
        	
        	result = (byte) (data | RbValue);
        	
        	
        	
        	System.out.print(" ORRB: " + result +":");
        	result =  (byte) state_.doORRB(RbValue, data);
        	System.out.println(result);
		}
        if (this.getAddressModeType() == AddressMode.INDIRECT)
		{
        	System.err.println("Not Implemented yet.");
        	System.exit(1);
		}
        if (this.getAddressModeType() == AddressMode.SHORT_INDEXED)
		{
        	System.err.println("Not Implemented yet.");
        	System.exit(1);
		}
        if (this.getAddressModeType() == AddressMode.LONG_INDEXED)
		{
        	System.err.println("Not Implemented yet.");
        	System.exit(1);
		}
        
		if (this.getAddressModeType() == AddressMode.INDIRECT_AUTO_INC)
		{
			// AssemblerFormat: ORRB (lndirreg)+,brag
			// instruction Operation: (Rb)<-([Ra])+(Rb); (Ra)<-(Ra) + 1
			// ([Ra]) the value stored in Ra
			// )+( logical OR operation.
			
			// Execution Sfafes; 7/12
			// MachineFormat: [ ^92 ], [ Indeirect Ra | 1 MB ], [ Dest Rb ]

			//2073: 92,15,1c            orb   R1c,[R14++]      R1c |= [R14++]; R1c | [R14++]
			// register [R14++]
			
			// (Ra)<-(Ra) + 1
			Ra = (byte) (Ra & 0xfe);
			short RaAddress = state_.getWordRegister( Ra );// [R32]
			 
			byte value = getByteValue(memory, RaAddress);
			
			state_.setWordRegister((short) Ra, (short) (RaAddress + 1));	
		 
			result = (byte) state_.doORRB(state_.getByteRegister(Rb), value);
		}


        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);
        
        state_.setByteRegister(Rb, (byte) result);  
        
		state_.setPswBit(ProgramStatusWord.OVERFLOW, false);
		state_.setPswBit(ProgramStatusWord.CARRY, false);
        
    }
}
