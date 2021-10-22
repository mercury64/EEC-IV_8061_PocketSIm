package ca.nerret.emu.emulator;

public interface Memory {

	public static final short slaveProgramCounter = 0;
	
	String getName();

    int getSize();

    default void clear() {}

    byte getByte(short address);

    void setByte(short address, byte value);

    short getWord(short address);

    void setWord(short address, short value);
    
    int getDoubleWord(short address);

    void seDoubletWord(int address, short value);

    void getBytes(short address, byte[] destination, int length);

    void setBytes(short address, byte[] source, int length);
}
