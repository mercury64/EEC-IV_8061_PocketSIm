package ca.nerret.emu.processor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.nerret.emu.env.RoxByte;
import ca.nerret.emu.env.RoxWord;


/**
 * 
 * The register/arithmetic logic unit(RALU)is the computational section of the 8061. 
 * It can perform 17-bit addition and subtraction, signed and unsigned multiplication and division, increment, decrement, and shift functions in addition to logical functions on two variables.
 * The RALU can operate directly on data bytes and words, and several 8061 instructions(ASRDW, ML2W, ML3W. DiVW, NORM,SHLDW, and SHRDW) enable RALU double-word operations.
 * The RALU provides zero detect for the upper and lower bytes separately, and the carry out of the 7th and 15th bits are available.
 * The RALU also maintains the flags that are used in all conditional jump operations and the address in the program counter.
 * The RALU can operate directly with all internal memory-mapped registers.
 * The RALU consists of a 17-bitALU,a program counter,a processor status word register,a delay register,andthreetemporaryregisters:accumuiator,assembly register,and multiply register. 
 * All RALU data inputs and outputs are via the A-bus and D-bus.
 * 
 * @author wwhite
 *
 */
public class EEC8061_RALU {

    private static final Logger log = LoggerFactory.getLogger(Registers.class);

    
	private final EEC_8061_ALU alu;
	private RoxWord programCounter;
	
	 private final RoxWord programStatusWord;
	
    public EEC8061_RALU(EEC_8061_ALU alu) {
		
		this.alu = alu;
		
		this.programStatusWord = RoxWord.fromLiteral(0x7f00);
		
		this.setProgramCounter(RoxWord.fromLiteral(0x2000));
	}

	public EEC_8061_ALU getAlu() {
		return alu;
	}

	public enum PSW_Flag {
    	STICKY_BIT(0x0),
    	UNSIGNED(0x1),
    	SIGNED(0x2),
    	CARRY(0x3),
    	OVERFLOW_TRAP(0x4),
    	OVERFLOW(0x5),
    	NEGATIVE(0x6),
    	ZERO(0x7),
    	FIXED_LOGIC_ONE1(0x8),
    	FIXED_LOGIC_ONE2(0x9),
    	FIXED_LOGIC_ONE3(0xa),
    	FIXED_LOGIC_ONE4(0xb),
    	FIXED_LOGIC_ONE5(0xc),
    	FIXED_LOGIC_ONE6(0xd),
    	FIXED_LOGIC_ONE7(0xe),
    	INTERRUPT_SERVICE(0xf);
    	
    	private final int index;
    	private short register;
    	
    	PSW_Flag(int index)
    	{
    		this.index = index;
    		this.reset();
    	}
    	
    	public void reset()
    	{
    		this.register = 0x7f00;
    	}
    }

	public RoxWord getProgramCounter() {
		return programCounter;
	}

	public void setProgramCounter(RoxWord programCounter) {
		this.programCounter = programCounter;
		
		  log.debug("'R+:Program Counter' := {} [ {} | {} ]",
				  programCounter);
	}

    /**
     * Increment the Program Counter then return it's value
     *
     * @return the new value of the Program Counter
     */
    public RoxWord getNextProgramCounter(){
    	setProgramCounter(RoxWord.fromLiteral(getProgramCounter().getRawValue()+1));
        return getProgramCounter();
    }

    /**
     * Get the Program Counter value then increment
     *
     * @return the value of the Program Counter
     */
    public RoxWord getAndStepProgramCounter(){
        final RoxWord pc = getProgramCounter();
        setProgramCounter(RoxWord.fromLiteral(getProgramCounter().getRawValue()+1));
        return pc;
    }

	public RoxWord getPSW() {
		// TODO Auto-generated method stub
		return programStatusWord;
	}


}
