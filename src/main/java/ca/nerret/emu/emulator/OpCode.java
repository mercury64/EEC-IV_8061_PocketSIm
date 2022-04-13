package ca.nerret.emu.emulator;

import ca.nerret.emu.emulator.opcodes.IOpCode;

public abstract class OpCode <T extends OpCode <T>> implements IOpCode {
 
	private String mnemonic;
	private int opcode;
	private int stateTimes;
	protected AddressMode addressMode;

    protected int numberOfBytes = 0;
    protected int executionStates = 0;
	
	private int pc;
	private int[] memory;
	private State state;
	
	private short result;
	private short operandLocation;

	public OpCode(int opcode, String mnemonic)
	{
		this.setOpcode(opcode);
		this.setMnemonic(mnemonic); 
	}
	
	@Override
	public void exec(State state) {

		this.state = state;
		this.memory = state.getMemory();
        this.pc = state.getPc();


        if (this.getAddressModeType() == AddressMode.DIRECT)
		{
        	this.execDirect();
		}
        if (this.getAddressModeType() == AddressMode.IMMEDIATE)
		{
        	this.execImmediate();
		}
        if (this.getAddressModeType() == AddressMode.INDIRECT)
		{
        	this.execIndirect();
		}
        if (this.getAddressModeType() == AddressMode.INDIRECT_AUTO_INC)
		{
        	this.execIndirectAutoInc();
		}
        if (this.getAddressModeType() == AddressMode.SHORT_INDEXED)
		{
        	this.execShortIndexed();

		}
        if (this.getAddressModeType() == AddressMode.LONG_INDEXED)
		{
        	this.execLongIndexed();
		}
	}
	
	@Override
	public int exec() {

		int opcodePC = 0;
        if (this.getAddressModeType() == AddressMode.DIRECT)
		{
        	opcodePC = this.execDirect();
		}
        if (this.getAddressModeType() == AddressMode.IMMEDIATE)
		{
        	opcodePC = this.execImmediate();
		}
        if (this.getAddressModeType() == AddressMode.INDIRECT)
		{
        	opcodePC = this.execIndirect();
		}
        if (this.getAddressModeType() == AddressMode.INDIRECT_AUTO_INC)
		{
        	opcodePC = this.execIndirectAutoInc();
		}
        if (this.getAddressModeType() == AddressMode.SHORT_INDEXED)
		{
        	opcodePC = this.execShortIndexed();
		}
        if (this.getAddressModeType() == AddressMode.LONG_INDEXED)
		{
        	opcodePC = this.execLongIndexed();
		}
        
        return opcodePC;
	}

	public byte[] getOperands(int count, int executionStates2) {
        byte[] operands;	

        operands = new byte[count];
        for (int i = 0; i < count; i++) {
			operands[i] = (byte)state.getMemory()[pc + i];
			 this.state.setPc(pc + i);
		}
       
        
        this.state.updateStateTime(getExecutionStates());
        
        return operands;
		
	}

	public int execDirect()
	{
    	System.err.println("Direct Not Implemented yet. "+ this.getClass().getSimpleName());
    	System.exit(1);
		return getExecutionStates();
	}
	public int execImmediate()
	{
    	System.err.println("Immediate Not Implemented yet. "+ this.getClass().getSimpleName());
    	System.exit(1);
		return getExecutionStates();
	}
	public int execIndirect()
	{
    	System.err.println("Indirect Not Implemented yet. "+ this.getClass().getSimpleName());
    	System.exit(1);
		return getExecutionStates();
	}
	public int execIndirectAutoInc()
	{
    	System.err.println("Indirect Auto Increment Not Implemented yet. "+ this.getClass().getSimpleName());
    	System.exit(1);
		return getExecutionStates();
	}

	public int execShortIndexed()
	{
    	System.err.println("Short Indexed Not Implemented yet in: " + this.getClass().getSimpleName());

    	System.exit(1);
		return getExecutionStates();
	}

	public int execLongIndexed()
	{
    	System.err.println("Long Indexed Not Implemented yet. "+ this.getClass().getSimpleName());
    	System.exit(1);
		return getExecutionStates();
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
	
	protected short getWordValue(short location) {
		int index = (int)location & 0x0000ffff; // byte index, LSB
		int index2 = (int)location+1 & 0x0000ffff;// byte index2, MSB
	   
		short value = (short) memory[(int)index]; // LSB
		short value2 = (short) memory[(int)index2]; // MSB
	  
		short RA = (short) (value2 << 8 |  value & 0xff); // put MSB | LSB
	   
		value = RA;
		
		return value;
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
	 	   int index = (int)location & 0x0000ffff; // byte index, LSB
	 	   //int index2 = (int)location+1 & 0xffff;// byte index2, MSB
	 	   
	 	   byte value = (byte) memory[(int)index]; // LSB
	 	  // short value2 = (short) memory[(int)index2]; // MSB
	  	  
	 	   //short RA = (short) (value2 << 8 |  value & 0xff); // put MSB | LSB
	 	   
	 	  // System.out.println(" Get Byte from address: " + String.format("0x%02X",location));
	 	   //value = RA;
			return value;
		}
	
	protected byte getByteValue(short location) {
	 	   int index = (int)location & 0x0000ffff; // byte index, LSB
	 	   //int index2 = (int)location+1 & 0xffff;// byte index2, MSB
	 	   
	 	   byte value = (byte) memory[(int)index]; // LSB
	 	  // short value2 = (short) memory[(int)index2]; // MSB
	  	  
	 	   //short RA = (short) (value2 << 8 |  value & 0xff); // put MSB | LSB
	 	   
	 	  // System.out.println(" Get Byte from address: " + String.format("0x%02X",location));
	 	   //value = RA;
			return value;
		}
	public byte getByteRegister(short register) {
		return this.state.getByteRegister(register);
	}
	public short getWordRegister(short register) {
		return this.state.getWordRegister(register);
	}


	public int getIntRegister(byte operandRB) {
		return this.state.getIntRegister(operandRB);
	}

	public short setSubResult(short RB, short RA) {
		 short result = state.doSub(RB, RA);
		 this.setResult(result);
		return result;
	}

	public void setResult(short result) {
		this.result = result;
	}
	
	protected void setOperandLocation(short operandLocation)
	{
		this.operandLocation = operandLocation;
	}

	protected short getOperandLocation() {
		
		return this.operandLocation;
	}

	protected short getResult() {
		
		return result;
	}

	public int getExecutionStates() {
		return executionStates;
	}

	public void setExecutionStates(int executionStates) {
		this.executionStates = executionStates;
	}
	
    public int getNumberOfBytes() {
		return this.numberOfBytes;
	}

	public void setNumberOfBytes(int numberOfBytes) {
		this.numberOfBytes = numberOfBytes;
	}
}
