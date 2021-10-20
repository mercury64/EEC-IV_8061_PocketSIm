package ca.nerret.emu.emulator.cpu;

import ca.nerret.emu.emulator.BUS;
import ca.nerret.emu.emulator.State;

/**
 * 
 * The register/arithmetic logic unit(RALU)isthecomputationalsectionofthe8061. 
 * It can perform 17-bit addition and subtraction, signed and unsigned multiplication and division, increment, decrement, and shift functions in addition to logical functions on two variables.
 * The RALU can operate directly on data bytes and words,and several8061 instructions(ASRDW, ML2W, ML3W. DiVW, NORM,SHLDW,andSHRDW)enableRALU double-word operations.
 * The RALU provides zero detect for the upper and lower bytes separately, and the carry out of the 7th and 15th bits are available.
 * The RALU also maintains the flags that are used in all conditional jump operations and the address in the program counter.
 * The RALU can operate directly with all internal memory-mapped registers.
 * The RALU consists of a 17-bitALU,a program counter,a processor status word register,a delay register,andthreetemporaryregisters:accumuiator,assembly register,and multiply register. 
 * All RALU data inputs and outputs are via the A-bus and D-bus.
 * 
 * @author wwhite
 *
 */
public class RALU extends State {

	private BUS addressBus;
	private BUS dataBus;
	
	
	// 8-bit A-BUS
	// 16-bit D-BYS
	
	private Register accumulatorTemp;
	private Register multiplyRegTemp;
	private Register assemblyRegTemp;
	


	// program counter 16

	
	// multiply reg 16
	
	// accumulator 16
	
	// assembly reg 16
	
	// PSW Reg
	private Register pswRegister;
	
	// ALU 17-bit
	private Register registerALU;

	// Delay Reg
	private Register delayReg;
	
	public RALU()
	{
		
	}
	
	public RALU(int[] memory_) {
		super(memory_);
		// TODO Auto-generated constructor stub
	}
	public RALU(byte[] ram) {
		// TODO Auto-generated constructor stub
		int[] memory = new int[ram.length];
		
		int count = 0;
		for (byte r: ram) {
			memory[count++] = (int)r;
		}
		
		new RALU(memory);
	}

	public void connectDataBus(BUS bus) {
		this.dataBus = bus;
		
	}
	
}
