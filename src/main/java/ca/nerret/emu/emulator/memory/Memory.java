package ca.nerret.emu.emulator.memory;

import ca.nerret.emu.env.RoxByte;
import ca.nerret.emu.env.RoxWord;

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
    
    /**
     * Return a block of bytes
     *
     * @param from the required blocks starting memory address
     * @param to the required blocks ending memory address
     * @return the entire block [<code>from</code> ... <code>to</code>] as an array
     */
    RoxByte[] getBlock(RoxWord from, RoxWord to);

}
