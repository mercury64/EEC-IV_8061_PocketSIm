package ca.nerret.emu.emulator;

public class State {

    private int _sp; 
    private int _pc = 0x2000; // Program start.
    private int[] _memory; 
    
    private long[] register_memory ;
    
	public ProgramStatusWord psw = new ProgramStatusWord();
    public int PSW_FLAGS;
    
    private int _a; 
    private int _b; 
    private int _c; 
    private int _d; 
    private int _e; 
    private int _h; 
    private int _l; 
   
    private int _state_time = 0;

    /**
     * Constructor.
     * @param memory_ int[]
     */
    public State(int[] memory_) {
        _memory = memory_.clone();
        this.register_memory = new long[0x1fff];
        
        updateStateTime(_state_time + 1);
    }

    /**
     * Getter.
     * @return int
     */
    public final int getA() {
        return _a;
    }
    /**
     * Setter.
     * @param a_ int
     */
    public final void setA(int a_) {
        _a = a_;
    }
    /**
     * Getter.
     * @return int
     */
    public final int getB() {
        return _b;
    }
    /**
     * Setter.
     * @param b_ int
     */
    public final void setB(int b_) {
        _b = b_;
    }
    /**
     * Getter.
     * @return int
     */
    public final int getC() {
        return _c;
    }
    /**
     * Setter.
     * @param c_ int
     */
    public final void setC(int c_) {
        _c = c_;
    }
    /**
     * Getter.
     * @return int
     */
    public final int getD() {
        return _d;
    }
    /**
     * Setter.
     * @param d_ int
     */
    public final void setD(int d_) {
        _d = d_;
    }
    /**
     * Getter.
     * @return int
     */
    public final int getE() {
        return _e;
    }
    /**
     * Setter.
     * @param e_ int
     */
    public final void setE(int e_) {
        _e = e_;
    }
    /**
     * Getter.
     * @return int
     */
    public final int getH() {
        return _h;
    }
    /**
     * Setter.
     * @param h_ int
     */
    public final void setH(int h_) {
        _h = h_;
    }
    /**
     * Getter.
     * @return int
     */
    public final int getL() {
        return _l;
    }
    /**
     * Setter.
     * @param l_ int
     */
    public final void setL(int l_) {
        _l = l_;
    }
    /**
     * Getter.
     * @return int
     */
    public final int getSp() {
        return _sp;
    }
    /**
     * Setter.
     * @param sp_ int
     */
    public final void setSp(int sp_) {
        _sp = sp_;
    }
    /**
     * Getter.
     * @return int
     */
    public final int getPc() {
        return _pc;
    }
    /**
     * Setter.
     * @param pc_ int
     */
    public final void setPc(int pc_) {
        _pc = pc_;
    }
    /**
     * Getter.
     * @return int[]
     */
    public final int[] getMemory() {
        return _memory;
    }
    /**
     * Setter.
     * @param memory_ int[]
     */
    public final void setMemory(int[] memory_) {
        _memory = memory_.clone();
    }
    
	public int getStateTime() {
		return this._state_time;
	}

	public void updateStateTime(int _state_time) {
		this._state_time += _state_time;
	}

	// Could be byte, word or double word register
	// Value could be byte, word, dword, etc.
	public void setByteRegister(byte reg, byte value) {

		System.out.println(
				" Set Register:" + 
				String.format("R%02X", reg) + " = " + 
				String.format("0x%02X", value)
				);
				
		register_memory[reg] = value;
		
	}
	
    public void setPswBit(byte bit, boolean value)
    {
    	this.psw.setBit(bit, value);
    }
    
	public boolean getPswBit(byte bit) {
		// TODO Auto-generated method stub
		return this.psw.getBit(bit);
	}
	
	
	public void setZNFlag(long result)
	{
		// check if result is zero
		if ( result == 0) {
			this.setPswBit(ProgramStatusWord.ZERO, true);
		}
		else 
		{
			this.setPswBit(ProgramStatusWord.ZERO, false);
		}
        
		
        // check if result is negative
		if (result < 0) {
			this.setPswBit(ProgramStatusWord.NEGATIVE, true);
		}
		else
		{
			this.setPswBit(ProgramStatusWord.NEGATIVE, false);
		}
		

	}
	
	public void setCFlag(long RB, long RA)
	{
		
	}
	public void setPswFlags(long result, boolean Z, boolean N, boolean C, boolean V, boolean VT, boolean ST) {

		
		// check if result set carry 
		this.setPswBit(ProgramStatusWord.CARRY, false);    
        this.setPswBit(ProgramStatusWord.OVERFLOW, false);
        this.setPswBit(ProgramStatusWord.OVERFLOW_TRAP, false);
        this.setPswBit(ProgramStatusWord.STICKY_BIT, false);
		
	}
	
	void calc_SZP(byte value) {
	    if (value == 0) set_Z(); else clear_Z();
	    if (value & 0x80) set_S(); else clear_S();
	    if (parity[value]) set_P(); else clear_P();
	}
	 
	void calc_AC(byte val1, byte val2) {
	    if (((val1 & 0x0F) + (val2 & 0x0F)) > 0x0F) {
	        set_AC();
	    } else {
	        clear_AC();
	    }
	}
	 
	void calc_AC_carry(byte val1, byte val2) {
	    if (((val1 & 0x0F) + (val2 & 0x0F)) >= 0x0F) {
	        set_AC();
	    } else {
	        clear_AC();
	    }
	}
	 
	void calc_subAC(byte val1, byte val2) {
	    if ((val2 & 0x0F) <= (val1 & 0x0F)) {
	        set_AC();
	    } else {
	        clear_AC();
	    }
	}
	 
	void calc_subAC_borrow(byte val1, byte val2) {
	    if ((val2 & 0x0F) < (val1 & 0x0F)) {
	        set_AC();
	    } else {
	        clear_AC();
	    }
	}
    
	public byte getByteRegister(byte register) {
		
		byte value = (byte)register_memory[register];
		
		System.out.println(" Get Register:" + String.format("R%02X",register) + " = " + String.format("0x%02X",value));
		
		return value;
	}
	
	public short getWordRegister(byte register) {
		// TODO Auto-generated method stub
		byte reg1 = register;
		byte reg2 = (byte) (reg1 + 1);
		short temp = (short) (this.register_memory[reg1] & 0xffff |  ((this.register_memory[reg2] & 0xffff) << 8));
		
		System.out.println(" Get Word Register:" + String.format("R%02X",register) + " = " + String.format("0x%04X",temp));
		
		return temp;
	}

	public void setWordRegister(short dest_dwreg, short value) {
		register_memory[dest_dwreg] = value  & 0xffff ;
		System.out.println(" Set Word Register:" + String.format("0x%02X",dest_dwreg) + " = " + String.format("0x%04X",value));
	}

	public Object setWordRegister(short rB) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * overflow detection
	 * 
	 * @param v1
	 * @param v2
	 */
	public long doSub(final short v1, final short v2) {
		
		long diff =  v1 - v2;
	     
		this.PSW_FLAGS &= ~(ProgramStatusWord.F_N|ProgramStatusWord.F_V|ProgramStatusWord.F_Z|ProgramStatusWord.F_C);
	     
	    if ((((~v2 & v1 & ~diff) | (v2 & ~v1 & diff)) & 0x8000) != 0)
	    {
	    	System.out.println("short overflow sub(" + v1 + ", " + v2 + ")");
	    }
	      
	    return diff;
		
	}
}
