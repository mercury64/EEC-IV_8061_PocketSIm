package ca.nerret.emu.emulator;

public class MotherBoard {

	private CPU cpu;
	private BUS bus;
	
	public void addCPU(CPU cpu) {
		this.cpu = cpu;
	}

	public void addBUS(BUS bus) {
		this.bus = bus;	
	}

	public void connectComponents() {
		this.cpu.connectBus(this.bus);
	}

	public CPU getCPU() {
		// TODO Auto-generated method stub
		return this.cpu;
	}

	public void clock() {
		
		this.cpu.clock();
		
	}
}
