package ca.nerret.emu.emulator;

public interface ICPU {

	abstract void reset();
	
	abstract void execute();
	
	abstract byte fetch();
	
	abstract void clearSP();
	
}
