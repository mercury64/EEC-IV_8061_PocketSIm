package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.State;

public class OpCodeJE  extends OpCode implements IOpCode {

	 public OpCodeJE(int opcode, String mnemonic) {
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
		        	numberOfBytes = 4;
		        	stateTime = 5;
		        	break;
		        case AddressMode.IMMEDIATE:
		        	numberOfBytes = 4;
		        	stateTime = 5; 
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

	        byte dest_dwreg = operands[3];
	        byte Ra = operands[2];
	        byte Rb = operands[1];
	        byte Rd  = (byte) (Ra & Rb) ;
	        
	        state_.setByteRegister(dest_dwreg, Rd);    
	        //state_.setP.setAdditionFlags(state_, operands);

	    }

}
