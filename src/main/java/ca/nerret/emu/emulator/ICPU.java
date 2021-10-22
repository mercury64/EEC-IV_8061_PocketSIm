package ca.nerret.emu.emulator;

public interface ICPU {

	abstract void reset();
	
	abstract void execute();
	
	abstract byte opCodeFetch();
	
	abstract void clearSP();
	
	abstract void interrogate();
}
