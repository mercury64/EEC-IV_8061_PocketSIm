package ca.nerret.emu.emulator;

public class SpecialFunctionRegisters extends AbstractMemory implements Memory {

	
	protected SpecialFunctionRegisters(String name,  final byte[] registers) {
		super(name);
		// TODO Auto-generated constructor stub
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
}
