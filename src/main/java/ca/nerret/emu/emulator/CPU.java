package ca.nerret.emu.emulator;
import ca.nerret.emu.emulator.cpu.RALU;
import ca.nerret.emu.emulator.opcodes.IOpCode;

public abstract class CPU implements ICPU {

	private BUS bus;
	protected RALU ralu;
	
	// Program Status Word
	public int PSW_FLAGS;
	public byte instructionRegister;
	
	public String instructionDecoderPLA;
	public String timingDecoderPLA;
	
	
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
		return this.ralu;
	}
	
	public void clock()
	{
		byte _opcode = 0;
		byte _nextByte = 0;

		// Interrogate |PAUSE input then |RESET input, and interrupt controller, in that order.
		// CPUE then takes appropriate action based on results.
		
		//System.out.println(String.format("Machine State: %s",machineState));
		
		if (machineState == STATE_0) // First machine state
		{
			System.out.print(String.format("0x%x ",this.ralu.getPc()));

			// fetch
			this.instructionRegister = this.opCodeFetch();

			// Update SPC of all external program memory devices.
			machineState++;
			cycles++;

		}
		else if(machineState == STATE_1) // Second machine state
		{
			
			// decode
			this.opcode = this.decode(instructionRegister);
			

			
			System.out.println(String.format("%s", this.opcode));
			
			
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
			
			

		}
		else if(machineState == STATE_3) // Fourth machine state
		{

		}
				
		if (this.cycles == 0)
		{
			machineState = STATE_0;
		}

		// Debug, logger, whatever:
	}
	
	private OpCode decode(byte _opcode) {
		return OpcodeCache.get(_opcode);
	}

	public boolean complete() {
		boolean complete = (cycles == 0);
		return complete;
	}
	
	@Override
	public byte opCodeFetch() {

		interrogate();
		
		byte fetched = 0x0;
		int pc = this.ralu.getPc();
		
		fetched = this.read(pc++);
		
		this.ralu.setPc(pc);
		
		return fetched;
		
	}

	@Override
	public void clearSP() {
		// TODO Auto-generated method stub
		
		System.out.println("Must set stack pointer when loaded from software.");
		
	}
	

}
