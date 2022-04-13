package ca.nerret.emu.emulator;

import ca.nerret.emu.env.Bit;

public class ProgramStatusWord {
	
	Bit psw[];

	private short register;
			
	public final static byte STICKY_BIT = 0x0;
	public final static byte UNSIGNED = 0x1;
	public final static byte SIGNED = 0x2;
	public final static byte CARRY = 0x3;
	public final static byte OVERFLOW_TRAP = 0x4;
	public final static byte OVERFLOW = 0x5;
	public final static byte NEGATIVE = 0x6;
	public final static byte ZERO = 0x7;
	public final static byte FIXED_LOGIC_ONE1 = 0x8;
	public final static byte FIXED_LOGIC_ONE2 = 0x9;
	public final static byte FIXED_LOGIC_ONE3 = 0xa;
	public final static byte FIXED_LOGIC_ONE4 = 0xb;
	public final static byte FIXED_LOGIC_ONE5 = 0xc;
	public final static byte FIXED_LOGIC_ONE6 = 0xd;
	public final static byte FIXED_LOGIC_ONE7 = 0xe;
	public final static byte INTERRUPT_SERVICE = 0xf;
	
	public final static boolean SET = true; 
	public final static boolean CLEAR = false;
	// 0000 0000 0000 0000
    public static final int F_ST 	  = 0x0001;
    public static final int F_CZ	  = 0x0002;
    public static final int F_NZ      = 0x0004;
    public static final int F_C       = 0x0008;
    public static final int F_VT	  = 0x0010;
    public static final int F_V 	  = 0x0020;
    public static final int F_N 	  = 0x0040;
    public static final int F_Z 	  = 0x0080;
    static final int F_BIT8   = 0x0100;
    static final int F_BIT9   = 0x0200;
    static final int F_BIT10  = 0x0400;
    static final int F_BIT11  = 0x0800;
    static final int F_BIT12  = 0x1000;
    static final int F_BIT13  = 0x2000;
    static final int F_BIT14  = 0x4000;
    public static final int F_I  	  = 0x8000;


	
	public ProgramStatusWord()
	{
		psw = new Bit[16];

		this.reset();
	}
	
	public void reset()
	{
		this.register = 0x7f00;
		
		psw[STICKY_BIT] = new Bit(STICKY_BIT,"ST", "Sticky Bit Flag", false);
		psw[UNSIGNED] = new Bit(UNSIGNED,"C.!Z", "Unsigned, > Flag", false);
		psw[SIGNED] = new Bit(SIGNED,"N+Z", "Signed, <= Flag", false);
		psw[CARRY] = new Bit(CARRY,"C", "Carry Flag", false);
		psw[OVERFLOW_TRAP] = new Bit(OVERFLOW_TRAP,"VT", "Overflow Trap Flag", false);
		psw[OVERFLOW] = new Bit(OVERFLOW,"V", "Overflow Flag", false);
		psw[NEGATIVE] = new Bit(NEGATIVE,"N", "Negative Flag", false);
		psw[ZERO] = new Bit(ZERO,"Z", "Zero Flag", false);
		psw[FIXED_LOGIC_ONE1] = new Bit(FIXED_LOGIC_ONE1,"\"1\"", "Fixed Logic One", true);
		psw[FIXED_LOGIC_ONE2] = new Bit(FIXED_LOGIC_ONE2,"\"1\"", "Fixed Logic One", true);
		psw[FIXED_LOGIC_ONE3] = new Bit(FIXED_LOGIC_ONE3,"\"1\"", "Fixed Logic One", true);
		psw[FIXED_LOGIC_ONE4] = new Bit(FIXED_LOGIC_ONE4,"\"1\"", "Fixed Logic One", true);
		psw[FIXED_LOGIC_ONE5] = new Bit(FIXED_LOGIC_ONE5,"\"1\"", "Fixed Logic One", true);
		psw[FIXED_LOGIC_ONE6] = new Bit(FIXED_LOGIC_ONE6,"\"1\"", "Fixed Logic One", true);
		psw[FIXED_LOGIC_ONE7] = new Bit(FIXED_LOGIC_ONE7,"\"1\"", "Fixed Logic One", true);
		psw[INTERRUPT_SERVICE] = new Bit(INTERRUPT_SERVICE,"I", "Interrupt Enable", false);
	}
	
	public void setBit(byte index, boolean value)
	{
		this.psw[index].value = value;
		
		this.setRegister();
	}
	
	public void setRegister()
	{
		for (int i = psw.length-1; i >= 0; i--)
		{
			this.register <<= 1;
			
			if ( psw[i].value )
			{
				this.register |= 1;
			}
			else
			{
				this.register |= 0;
			}
		}
	}
	
	public boolean getBit(byte index) {
		
		return this.psw[index].value;
	}
	
	public short getRegister()
	{
		return this.register;
	}
	
	
	public String toString()
	{
		String toReturn = "";
		for (int i = psw.length-1; i >= 0; i--)
		{
			//Bit bit : psw) {
			toReturn += psw[i].toString() + "\n";

		}
		
		return toReturn;
	}
	
	public static void main(String[] args)
	{
		ProgramStatusWord psw = new ProgramStatusWord();
		
		psw.setBit(CARRY, SET);
		System.out.println(psw.register);
		System.out.println(psw);
		System.out.println(psw.getRegister());
		
		psw.setBit(ZERO, SET);
		System.out.println(psw.register);
		System.out.println(psw);
		System.out.println(psw.getRegister());
		
		psw.reset();
		System.out.println(psw.register);
		System.out.println(psw);
		System.out.println(psw.getRegister());
		
		
		
	}


}


