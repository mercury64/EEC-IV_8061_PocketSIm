package ca.nerret.emu.emulator;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import ca.nerret.emu.LogFormatter8061;
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

	public void setAddressMode(AddressMode addressMode) {
		// TODO Auto-generated method stub
	    
	    this.addressMode = addressMode;
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

	@Override
	public int getAddressModeInt() {
		return this.addressMode.getType();
	}

	
	public String toString()
	{
		return " " + this.mnemonic + " " + String.format("0x%02X",this.opcode) + System.lineSeparator() 
			+ " " + this.addressMode.toString();
	}

	
}
