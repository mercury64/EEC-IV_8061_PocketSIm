package ca.nerret.emu.emulator.cpu;

import ca.nerret.emu.emulator.CPU;
import ca.nerret.emu.emulator.memory.Memory;

public class EECIV_8061 extends CPU {

	 private final Registers registers;
	 private final Memory memory;
	 private final RALU alu;

	 
    public EECIV_8061(final Memory memory, final Registers registers) {
        this.memory = memory;
        this.registers = registers;
        this.alu = new RALU(registers);
    }
	@Override
	public void reset() {
		super.reset();
		
		this.ralu = new RALU();
		
		this.PSW_FLAGS = 0x7f00; // Reset PSW.
		// 0 1 1 1  1 1 1 1 0 0 0 0 0 0 0 0 	
		
		this.ralu.setPc((short)0x2000); // Program start.

		//this.clearSP();
	}

	@Override
	public void execute() {
		// TODO Auto-generated method stub
		
	}

	
	public void interrogate() {
		// TODO Auto-generated method stub
		
		// check !RESET input, !PAUSE input, and INTERUPT controller.
		
		/** Depending upon the interrogation at the beginning of the opcode fetch, the 8061 will: 
		 * - reset if the RESET input is active, 
		 * - enter the pause mode if the PAUSE input is active,
		 * - vector to an interrupt routine if an unmasked interrupt is active and interrupt service is enabled,
		 * - or fetch the next opcode if all previous conditions are inactive or disabled.
		 */
		
		
	}

	@Override
	/**
     * @return the {@link Registers} being used
     */
    public Registers getRegisters(){
        return registers;
    }


}
