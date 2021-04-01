package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * OpCode 0x45 AD3W
 */
public class OpCodeAD3W  extends OpCode implements IOpCode {

    public OpCodeAD3W(int opcode, String mnemonic) {
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
        // 7 6 5 4 3 2 1 0
        // 0 1 0 0 0 1 0 0 = 0x44 - Direct
        // 0 1 0 0 0 1 0 1 = 0x45 - Immediate
        // 0 1 0 0 0 1 1 0 = 0x46 - Indirect
        // 0 1 0 0 0 1 1 1 = 0x47 - Indexed
        // 0x0 = direct
        // 0x1 = immediate
        // 0x2 = indirect
        // 0x3 = indexed
     
        switch (this.getAddressModeInt())
        {
	        case AddressMode.DIRECT:
	        	numberOfBytes = 4;
	        	stateTime = 5;
	        	break;
	        case AddressMode.IMMEDIATE:
	        	numberOfBytes = 5;
	        	stateTime = 6; 
	        	break;
	        case AddressMode.INDIRECT:
	        	numberOfBytes = 4;
	        	stateTime = 7;
	        	break;
	        case AddressMode.SHORT_INDEXED:
	        	numberOfBytes = 5;
	        	stateTime = 7;
	        	break;	
	        case AddressMode.LONG_INDEXED:
	        	numberOfBytes = 6;
	        	stateTime = 8;
	        	break;	
        }
        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);
        
        operands = new byte[numberOfBytes];
        for (int i = 0; i < numberOfBytes; i++) {
			operands[i] = (byte)memory[pc + i];
		}

        byte dest_dwreg = 0;

        short sum = 0;
        

        if (this.getAddressModeType() == AddressMode.DIRECT)
		{
        	System.err.println("Sub class Not Implemented yet.");
        	System.exit(1);
		}
        if (this.getAddressModeType() == AddressMode.IMMEDIATE)
		{
        	// Rfc = c0a8
        	//state_.setWordRegister((short)0xfc, (short)0xc0a8);
        	//state_.setWordRegister((short)0xfd, (short)0xc0);
            dest_dwreg = operands[4];
            byte src1_Swreg = operands[3];
            short src2_waop  = (short) ((operands[2] << 8) | operands[1]);;
            
            short src1_value =  state_.getWordRegister(src1_Swreg);
         
            sum =  (short) (src1_value + src2_waop) ;
            
		}
        if (this.getAddressModeType() == AddressMode.INDIRECT)
		{
        	System.err.println("Not Implemented yet.");
        	System.exit(1);
		}
        if (this.getAddressModeType() == AddressMode.INDIRECT_AUTO_INC)
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

        state_.setWordRegister(dest_dwreg, sum);
        
    }
}
