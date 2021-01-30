package ca.nerret.emu.emulator;

import java.util.Arrays;

public class State {

    private int _sp; 
    private int _pc = 0x2000; // Program start.
    private int[] _memory; 
    
    private byte[] register_memory ;
    
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
    	
    	this.PSW_FLAGS = 0x7f00; // Reset PSW.
    	
        _memory = memory_.clone();
        this.register_memory = new byte[0x1fff];
        
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


	public void setByteRegister(short reg, byte value) {

		
		System.out.println(
				" Set Byte Register:" + 
				String.format("R%02X", (byte)reg) + 
				" = " + 
				String.format("0x%02X", value)
				);
				
		int index = (byte)reg & 0xff;
		if ( index > 0x1fff )
		{
			System.err.println("RAM buffer out of range :" + String.format("R0x%04X", (short)reg));
			System.exit(1);
		}
		register_memory[index] = value;
		
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
	
	/*
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
    */
	public byte getByteRegister(short register) {
		
		int regIndex = register &0xffff;
		byte value = register_memory[regIndex];
		
		System.out.println(" Get Register:" + String.format("R%02X",regIndex) + " = " + String.format("0x%02X",value));
		
		return value;
	}
	
	public short getWordRegister(short register) {
        
		byte lo = this.register_memory[register];
        byte hi = this.register_memory[register + 1];
        short sHiByte =  (short) ((hi & 0xff) << 8);
        short sLoByte = (short) (lo & 0xff);
		
        short temp = (short) (sHiByte | sLoByte);
		
		System.out.println(" Get Word Register:" + 
				String.format("R%02X",register) + 
				" = " + 
				String.format("0x%04X",temp)
			);
		
		return temp;
	}

	public void setWordRegister(short dest_dwreg, byte value) {
	
		register_memory[dest_dwreg] = value;
		System.out.println(" Set Word Register with a Byte:" + 
				String.format("0x%02X",dest_dwreg) + 
				" = " + 
				String.format("0x%04X",value));
	}
	
	public void setWordRegister(short dest_dwreg, short value) {
        

        if ( dest_dwreg <= 0x10)
        {
        	this.setSFR(dest_dwreg, value);
        }
        else if( dest_dwreg >= 0x12 && dest_dwreg <= 0xff)
        {
        	this.setGeneralRegister(dest_dwreg, value);
        }
        else
        {
        	this.setRAM(dest_dwreg, value);
        }
	}
	
	private void setRAM(short dest_dwreg, short value)
	{
        byte hi = (byte) ((value >> 8) & 0xff ) ;
        byte lo = (byte) (value  & 0xff) ;
        
        
		register_memory[dest_dwreg] = lo;
		register_memory[dest_dwreg + 1] = hi;
		System.out.println(
				" Set word in RAM:" + 
						String.format("0x%02X",dest_dwreg) + 
						" = " + 
						String.format("0x%04X",value) +
						String.format("[hi: 0x%02X ",hi) + String.format("lo: 0x%02X]",lo) +
						String.format("= 0x%02X%02X",lo,hi)
				);
	}
	
	private void setGeneralRegister(short dest_dwreg, short value)
	{
        byte hi = (byte) ((value >> 8) & 0xff ) ;
        byte lo = (byte) (value  & 0xff) ;
        
        
		register_memory[dest_dwreg] = lo;
		register_memory[dest_dwreg + 1] = hi;
		System.out.println(
				" Set Word Register:" + 
						String.format("0x%02X",dest_dwreg) + 
						" = " + 
						String.format("0x%04X",value) +
						String.format("[hi: 0x%02X ",hi) + String.format("lo: 0x%02X]",lo) +
						String.format("= 0x%02X%02X",lo,hi)
				);
	}

	private void setSFR(short dest_dwreg, short value) {
    	System.out.println("Set SFR");
    	
    	switch (dest_dwreg)
    	{
    	case 0x06:
    		System.out.println("Setting IO_Timer:");
    		break;
    	case 0x0e:
    		System.out.println("Setting HSI_Time:");
    		break;
    	case 0x10: // Stack Pointer
    		this._sp = value;
    		System.out.println("Setting Stack Pointer:");
    		break;
    	}
    	
    	this.setGeneralRegister(dest_dwreg, value);
	}

	public Object setWordRegister(short rB) {
		// TODO Auto-generated method stub
		return null;
	}

	public short doAdd(final short v1, final short v2) {

		boolean shortToBoolean = false;
		
	     long sum = v1+v2;
	     
			this.PSW_FLAGS &= ~( ProgramStatusWord.F_Z
								|ProgramStatusWord.F_N
								|ProgramStatusWord.F_C
								|ProgramStatusWord.F_V
								);
	    
	     if((short)sum == 0)
	    	 this.PSW_FLAGS |= ProgramStatusWord.F_Z;
	     else if((short)sum < 0)
	    	 this.PSW_FLAGS |= ProgramStatusWord.F_N;
	     
	    shortToBoolean = ((short) ( ~((v1^v2) & (v1^sum))  & 0x8000)) != 0;
	     
	    if(shortToBoolean)
	    {
	    	 this.PSW_FLAGS |= ProgramStatusWord.F_V | ProgramStatusWord.F_VT;
	    }
	    shortToBoolean =  (short)(sum & 0xffff0000) != 0;
	    if(shortToBoolean)
	    {
	    	this.PSW_FLAGS |= ProgramStatusWord.F_C;
	    }
	     return (short) sum;
	    }
	
	
	public short doByteSub(final byte Rb, final byte Ra) {

		long diff =  Rb - Ra;
	     
		this.modifySubPSW(diff, Rb, Ra);
	      
	    return (short) diff;
		
	}
	
	/**
	 * overflow detection
	 * 
	 * @param v1
	 * @param v2
	 */
	public short doSub(final short v1, final short v2) {

		long diff =  v1 - v2;
	     
		this.modifySubPSW(diff, v1, v2);
	      
	    return (short) diff;
		
	}

	private void modifySubPSW(long diff, final short v1, final short v2) {

		boolean shortToBoolean = false;
		
		this.PSW_FLAGS &= ~(ProgramStatusWord.F_N|ProgramStatusWord.F_V|ProgramStatusWord.F_Z|ProgramStatusWord.F_C);
	    
		if((short)diff == 0)
		{
			this.PSW_FLAGS |= ProgramStatusWord.F_Z;
		}
		else if((short)diff < 0)
		{
			this.PSW_FLAGS |= ProgramStatusWord.F_N;
		}
		
		shortToBoolean = ((short) ( ((v1^v2) & (v1^diff))  & 0x8000)) != 0;
		System.out.println("Short to Boolean: " + shortToBoolean);
		
		if( shortToBoolean )
		{
			this.PSW_FLAGS |= ProgramStatusWord.F_V;
		}
		
		shortToBoolean =  (short)(diff & 0xffff0000)  != 0;
		
		System.out.println("Short to Boolean: " + shortToBoolean);
		
	    if( !shortToBoolean )
	    {
	    	this.PSW_FLAGS |= ProgramStatusWord.F_C;
	    }
		
		
	    if ((((~v2 & v1 & ~diff) | (v2 & ~v1 & diff)) & 0x8000) != 0)
	    {
	    	System.out.println("short overflow sub(" + v1 + ", " + v2 + ")");
	    }
		
	}

	@Override
	public String toString() {
		return "State [_sp=" + _sp + ", _pc=" + _pc + ", _memory="  + ", register_memory="
				+ ", PSW_FLAGS=" + this.pswFlagsToString() + ", _a=" + _a
				+ ", _b=" + _b + ", _c=" + _c + ", _d=" + _d + ", _e=" + _e + ", _h=" + _h + ", _l=" + _l
				+ ", _state_time=" + _state_time + "]";
	}

	public short doORRB(byte rb, byte ra) {
		 byte orrb  = (byte) (rb | ra) ;
		 
		 this.PSW_FLAGS &= ~(ProgramStatusWord.F_N|ProgramStatusWord.F_Z);

			if((short)orrb == 0)
			{
				this.PSW_FLAGS |= ProgramStatusWord.F_Z;
			}
			else if((short)orrb < 0)
			{
				this.PSW_FLAGS |= ProgramStatusWord.F_N;
			}
		return orrb;
	}
	
	public String pswFlagsToString()
	{
		String pswTableHeader 			= "________________________\n";
		pswTableHeader = pswTableHeader + "| Z | N | V | VT| C | ST|\n";
		pswTableHeader = pswTableHeader + "------------------------\n";
		
		String zeroFlag = Integer.toBinaryString( (this.PSW_FLAGS & ProgramStatusWord.F_Z)  >>> ProgramStatusWord.ZERO) ;
		String negativeFlag = Integer.toBinaryString( (this.PSW_FLAGS & ProgramStatusWord.F_N) >>> ProgramStatusWord.NEGATIVE ) ;
		String overflowFlag = Integer.toBinaryString( this.PSW_FLAGS & ProgramStatusWord.F_V) ;
		String overflowTrapFlag = Integer.toBinaryString( this.PSW_FLAGS & ProgramStatusWord.F_VT) ;
		String carryFlag = Integer.toBinaryString( (this.PSW_FLAGS & ProgramStatusWord.F_C)  >>> ProgramStatusWord.CARRY) ;
		String stickyBitFlag = Integer.toBinaryString( this.PSW_FLAGS & ProgramStatusWord.F_ST) ;
		
		String flagValues = "| " + zeroFlag + " | " + negativeFlag + " | " + overflowFlag + " | " + overflowTrapFlag
				+ " | " + carryFlag + " | " + stickyBitFlag + " |\n";
		
		String pswFlagsString = Integer.toBinaryString(this.PSW_FLAGS);//;String.format("%04X",this.PSW_FLAGS) ;
		pswFlagsString = "PSW FLAGS : " + String.format("%16s", pswFlagsString).replace(' ', '0'); 
		
		return pswTableHeader + flagValues + pswFlagsString;
	}

	public void decrementSP() {
		this._sp = this._sp - 2;
		this.setWordRegister((short)0x10, (short)this._sp);
		
	}

	public void incrementSP() {
		this._sp = this._sp + 2;
		this.setWordRegister((short)0x10, (short)this._sp);
	}	
}
