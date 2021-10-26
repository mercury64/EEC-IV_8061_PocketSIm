package ca.nerret.emu.emulator;

public abstract class AbstractMemory implements Memory {

    protected String name;

    protected AbstractMemory(final String name) {
        this.name = name;
    }
    
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public byte getByte(short address) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setByte(short address, byte value) {
		// TODO Auto-generated method stub

	}

	@Override
	public short getWord(short address) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setWord(short address, short value) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getDoubleWord(short address) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void seDoubletWord(int address, short value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getBytes(short address, byte[] destination, int length) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setBytes(short address, byte[] source, int length) {
		// TODO Auto-generated method stub

	}

}
