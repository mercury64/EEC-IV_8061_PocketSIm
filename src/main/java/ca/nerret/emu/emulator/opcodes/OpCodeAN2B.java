package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * OpCode 0x45 AD3W
 */
public class OpCodeAN2B  extends OpCode implements IOpCode {

    public OpCodeAN2B(int opcode, String mnemonic) {
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
	        case AddressMode.SHORT_INDEXED:
	        	numberOfBytes = 4;
	        	stateTime = 6;
	        	break;	
        }
        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);
        
        operands = new byte[numberOfBytes];
        for (int i = 0; i < numberOfBytes; i++) {
			operands[i] = (byte)memory[pc + i];
		}

        byte dest_dwreg = operands[numberOfBytes-1];
        byte Ra = operands[numberOfBytes-2];
        byte Rb = operands[numberOfBytes-3];
        byte Rd  = (byte) (Ra + Rb) ;
        
        if (this.getAddressModeType() == AddressMode.DIRECT)
		{
        	System.err.println("Not Implemented yet.");
        	System.exit(1);
		}
        if (this.getAddressModeType() == AddressMode.IMMEDIATE)
		{
        	System.err.println("Not Implemented yet.");
        	//System.exit(1);
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
        
        
        short result = state_.doAdd(Ra, Rb);
        state_.setByteRegister(dest_dwreg, (byte) result);    
        
        System.out.println( state_ );
    }
}
