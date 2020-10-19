package ca.nerret.emu.emulator;

public class AddressMode {

	public static final int DIRECT = 0;
	public static final int IMMEDIATE = 1;
	public static final int INDIRECT = 2;
	public static final int INDIRECT_AUTO_INC = 3;
	public static final int SHORT_INDEXED = 4;
	public static final int LONG_INDEXED = 5;
	
	public int type;
	public String name;
	private boolean autoIncrement;
	
	public AddressMode(byte opcode)
	{
		byte firstByte = 0x0;
		new AddressMode(opcode, firstByte);
	}
	public AddressMode(byte opcode, byte firstByte)
	{
		this.autoIncrement = false;
		
		   switch (opcode & 0x03)
	        {
	        case 0x0:
	        	this.setType(DIRECT);
	        	break;
	        case 0x1:
	        	this.setType(IMMEDIATE);
	        	break;
	        case 0x2:
	        	this.setType(INDIRECT);
	        	if ( (firstByte & 0x1) == 1 )
	        	{
	        		this.setType(INDIRECT_AUTO_INC);
	        	}
	        	
	        	break;
	        case 0x3:
	        	
	        	this.setType(SHORT_INDEXED);
	        	if ( (firstByte & 0x1) == 1 )
	        	{
	        		this.setType(LONG_INDEXED);
	        	}
	        	
	        	break;	
	        }
	}

	public AddressMode() {
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "AddressMode [type=" + type + ", name=" + name + "]";
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
		
		switch (this.type)
        {
	        case DIRECT:
	        	this.name = "Direct";
	        	break;
	        case IMMEDIATE:
	        	this.name = "Immediate";
	        	break;
	        case INDIRECT:
	        	this.name = "Indirect";
	        	break;
	        case INDIRECT_AUTO_INC:
	        	this.name = "Indirect Auto Increment";
	        	this.setAutoIncrement(true);
	        	break;	
	        case SHORT_INDEXED:
	        	this.name = "Short Indexed";
	        	break;	
	        case LONG_INDEXED:
	        	this.name = "Long Indexed";
	        	break;	
	        	
        }
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public void setAutoIncrement(boolean autoIncrement) {
		this.autoIncrement = autoIncrement;
	}
}
