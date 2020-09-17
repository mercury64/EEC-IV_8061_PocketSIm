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
        }
        state_.setPc(pc + numberOfBytes);
        state_.updateStateTime(stateTime);
        
        operands = new byte[numberOfBytes];
        for (int i = 0; i < numberOfBytes; i++) {
			operands[i] = (byte)memory[pc + i];
		}

        short dest_dwreg = operands[4];
        short src1_Swreg = operands[3];
        short src2_waop  = (short) ((operands[2] << 8) | operands[1]);;
        
        short src1_value =  state_.getWordRegister((byte)src1_Swreg);
     
        short sum =  (short) (src1_value + src2_waop) ;
        
        // DEST, SRC1, SRC2 ADD Dwreg, Swreg, waop
        // (010001aa) (waop) (Swreg) (Dwreg)
        //System.out.println("     DEST, SRC1, SRC2");
        //System.out.println("ADD Dwreg, Swreg, waop");
        
        //System.out.printf("0x%x(AD3W) Dwreg: 0x%x,Swreg: 0x%x,waop: 0x%x  \n", operands[0], dest_dwreg, src1_Swreg, src2_waop);
        //System.out.printf(" 0x%x =  [0x%x] +  0x%x\n", dest_dwreg, src1_value, src2_waop);
       
        state_.setWordRegister(dest_dwreg, sum);
        
    }
}
