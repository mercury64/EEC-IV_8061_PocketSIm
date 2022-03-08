package ca.nerret.emu.env;

public class Bit {
	
	byte bitNo;
	String symbol;
	String meaning;
	public boolean value;
	
	public Bit(byte bitNo, String symbol, String meaning, boolean value) {
		this.bitNo = bitNo;
		this.symbol = symbol;
		this.meaning = meaning;
		this.value = value;
	}
	
	public byte getBitNo() {
		return bitNo;
	}

	public void setBitNo(byte bitNo) {
		this.bitNo = bitNo;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getMeaning() {
		return meaning;
	}

	public void setMeaning(String meaning) {
		this.meaning = meaning;
	}

	public boolean getValue()
	{
		return this.value;
	}
	
	public void setValue(boolean value)
	{
		this.value = value;
	}
	
	public String toString() {
		return "0x"+this.bitNo + "\t" + this.symbol + "\t" + this.meaning + "\t" + this.value;
	}
	
}