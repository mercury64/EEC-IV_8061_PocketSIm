package ca.nerret.emu.emulator.cpu;
import ca.nerret.emu.emulator.CPU;

public class EECIV_8061 extends CPU {
	
	RALU ralu;
	
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

	@Override
	public byte fetch() {
		// TODO Auto-generated method stub
		return this.read(this.ralu.getPc());
		
	}

	@Override
	public void clearSP() {
		// TODO Auto-generated method stub
		
		System.out.println("Must set stack pointer when loaded from software.");
		
	}

}
