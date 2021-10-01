import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.OpcodeCache;
import ca.nerret.emu.emulator.opcodes.IOpCode;

abstract class CPU implements ICPU {

	private BUS bus;
	
	// Program Status Word
	public int PSW_FLAGS;
	
	protected int pc;

	// Addressing Modes
	
	// Opcodes
	
	// clock cycles
	
	// reset 
	
	// irq
	
	// fetch()
	// fetched = 0x00;
	
	// addres_abs
	// addres_rel
	// opcode
	// cycles
	
	private OpCode opcode;

	private int cycles;
	
	public CPU()
	{
		this.reset();
	}
	
	public void connectBus(BUS b)
	{
		this.bus = b;
	}
	
	public void write(short address, byte data)
	{
		bus.write(address, data);
	}
	
	public byte read(int address)
	{
		return bus.read(address, true);
	}
	
	public void clock()
	{
		byte _opcode = 0;
		byte _nextByte = 0;
		
		if (this.cycles == 0)
		{
			
			// fetch
			_opcode = this.read(pc++);
			_nextByte = this.read(pc);
			
			// decode
			this.opcode = this.decode(_opcode,_nextByte);
			
			// exec
			this.cycles = this.opcode.exec();
		}
		
		this.cycles--;
	}
	
	private OpCode decode(byte _opcode,byte _nextByte) {
		return OpcodeCache.get(_opcode,_nextByte);
	}
}
