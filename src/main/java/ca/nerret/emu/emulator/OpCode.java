package ca.nerret.emu.emulator;

import ca.nerret.emu.emulator.opcodes.IOpCode;

public abstract class OpCode <T extends OpCode <T>> implements IOpCode {
 
	private String mnemonic;
	private int opcode;
	private int stateTimes;
	private AddressMode addressMode;

	public OpCode(int opcode, String mnemonic)
	{
		this.setOpcode(opcode);
		this.setMnemonic(mnemonic); 
	}
	
	@Override
	public void exec(State state_) {
		// TODO Auto-generated method stub
		
	}

	public String getMnemonic() {
		return mnemonic;
	}

	public void setMnemonic(String mnemonic) {
		this.mnemonic = mnemonic;
	}

	public int getOpcode() {
		return opcode;
	}

	public void setOpcode(int opcode) {
		this.opcode = opcode;
	}

	public void setAddressMode(AddressMode mode) {
		// TODO Auto-generated method stub
	    
	    this.addressMode = mode;
	}
	
	public AddressMode getAddressMode()
	{
		return this.addressMode;
	}
	
	public void setStateTimes(int time)
	{
		this.stateTimes += time;
	}
	

	public int getAddressModeType() {
		return addressMode.getType();
	}
	
	protected short getWordValue(int[] memory, short location) {
		int index = (int)location & 0xffff; // byte index, LSB
		int index2 = (int)location+1 & 0xffff;// byte index2, MSB
	   
		short value = (short) memory[(int)index]; // LSB
		short value2 = (short) memory[(int)index2]; // MSB
	  
		short RA = (short) (value2 << 8 |  value & 0xff); // put MSB | LSB
	   
		value = RA;
		
		return value;
	}

	@Override
	public int getAddressModeInt() {
		return this.addressMode.getType();
	}

	
	public String toString()
	{
		return " " + this.mnemonic + " " + String.format("0x%02X",this.opcode) + System.lineSeparator() 
			+ " " + this.addressMode.toString() + System.lineSeparator() ;
	}
	
	/**
	 * Be sure to cast byte to short using: (short) (indirreg & 0xff) 
	 * 
	 * @param memory
	 * @param location
	 * @return
	 */
	protected byte getByteValue(int[] memory, short location) {
	 	   int index = (int)location & 0xffff; // byte index, LSB
	 	   //int index2 = (int)location+1 & 0xffff;// byte index2, MSB
	 	   
	 	   byte value = (byte) memory[(int)index]; // LSB
	 	  // short value2 = (short) memory[(int)index2]; // MSB
	  	  
	 	   //short RA = (short) (value2 << 8 |  value & 0xff); // put MSB | LSB
	 	   
	 	   System.out.println(" Get Byte from address: " + String.format("0x%02X",location));
	 	   //value = RA;
			return value;
		}
}
