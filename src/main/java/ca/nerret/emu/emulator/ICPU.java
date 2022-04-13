package ca.nerret.emu.emulator;

import ca.nerret.emu.emulator.cpu.Registers;

public interface ICPU {

	abstract void reset();
	
	abstract void execute();
	
	abstract byte opCodeFetch();
	
	abstract void clearSP();
	
	abstract void interrogate();
	

    /**
     * @return the {@link Registers} being used
     */
    public Registers getRegisters();
}
