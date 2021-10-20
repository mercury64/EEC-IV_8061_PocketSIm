package ca.nerret.emu.emulator;
import ca.nerret.emu.emulator.cpu.RALU;
import ca.nerret.emu.emulator.opcodes.IOpCode;

public abstract class CPU implements ICPU {

	private BUS bus;
	private RALU ralu;
	
	// Program Status Word
	public int PSW_FLAGS;
	public byte instructionRegister;
	
	protected short sp;
	
	private int machineState;
	
	private static int STATE_0 = 0;
	private static int STATE_1 = 1;
	private static int STATE_2 = 2;
	private static int STATE_3 = 3;
	

	
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
	
	public void reset()
	{
		this.machineState = 0;
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
	
	public RALU getRalu()
	{
		return ralu;
	}
	
	public void clock()
	{
		byte _opcode = 0;
		byte _nextByte = 0;
		
		// Interrogate |PAUSE input then |RESET input, and interrupt controller, in that order.
		// CPUE then takes appropriate action based on results.
		
		if (machineState == STATE_0) // First machine state
		{
			// fetch
			this.instructionRegister = this.fetch();

			// Update SPC of all external program memory devices.
			machineState++;
			cycles++;

		}
		else if(machineState == STATE_1) // Second machine state
		{
			
			// decode
			this.opcode = this.decode(instructionRegister);
			
			// Debug, logger, whatever:
			System.out.print(String.format("%s", this.opcode));
			
			// exec
			this.cycles = this.opcode.getExecutionStates();
			
			//machineState++;
			// number of bytes
			if(this.opcode.getNumberOfBytes() == 1)
			{
				machineState = STATE_0;
			}
			
			this.opcode.exec(getRalu());
		}
		else if(machineState == STATE_2) // Third machine state
		{
			// fetch second instruction byte(address of first operand byte)
			// fetch from external memory
			
			System.out.println(String.format(" 0x%02X", this.fetch()));

		}
		else if(machineState == STATE_3) // Fourth machine state
		{

		}
				
		if (this.cycles == 0)
		{
			machineState = STATE_0;
		}
	}
	
	private OpCode decode(byte _opcode) {
		return OpcodeCache.get(_opcode);
	}

	public boolean complete() {
		boolean complete = (cycles == 0);
		return complete;
	}
}
