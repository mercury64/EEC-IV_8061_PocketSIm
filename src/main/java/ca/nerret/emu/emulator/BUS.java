package ca.nerret.emu.emulator;

public class BUS {
	
	public CPU cpu;

	//public Memory ram;
	private byte[] ram;
	
	private Calibration cal;
	
	public BUS(CPU cpu)
	{
		this.cpu = cpu;
		ram = new byte[64 * 1024]; // 0xFFFF = 64k
		cpu.connectBus(this);

	}
	
	public BUS() {
		
	}

	public CPU getCpu() {
		return cpu;
	}

	public void setCpu(CPU cpu) {
		this.cpu = cpu;
	}

	public void write(short address, byte data)
	{
		// write address space 0-2000 only
		if (address >= 0x0000 && address <= 0x1fff)
		{
			ram[address] = data;
		}
		
	}
	
	public byte read(short address)
	{
		return this.read(address, true);
	}
	
	public byte read(int address, boolean readOnly)
	{
		byte data = 0x00;
		
		if (address >= 0x0000 && address <= 0x1fff)
		{
			data = ram[address];
		}
		
		if (address >= 0x2000 && address <= 0xffff)
		{
			data = ram[address];
		}
		
		return data;
		
	}

	public Calibration getCalibration() {
		return cal;
	}

	public void setCalibration(Calibration cal) {
		this.cal = cal;
		this.ram = cal.read();
		
	}

}
