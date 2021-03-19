package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * OpCode 0x75 AD2B
 */
public class OpCodeAD2B extends OpCode<OpCodeAD2B> implements IOpCode {

	public OpCodeAD2B(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * ca.nerret.emu.emulator.opcodes.IOpcode#exec(ca.nerret.emu.emulator.State)
	 */
	@Override
	public final void exec(State state_) {
		// super.exec(state_);
		int[] memory = state_.getMemory();
		final int pc = state_.getPc();

		byte[] operands;
		int numberOfBytes = 0;
		int stateTime = 0;

		switch (this.getAddressModeInt()) {
		case AddressMode.DIRECT:
			numberOfBytes = 3;
			stateTime = 4;
			break;
		case AddressMode.IMMEDIATE:
			numberOfBytes = 3;
			stateTime = 4;
			break;
		case AddressMode.INDIRECT:
			numberOfBytes = 3;
			stateTime = 6;//7
			break;
		case AddressMode.INDIRECT_AUTO_INC:
			numberOfBytes = 3;
			stateTime = 7;//12
			break;
		case AddressMode.SHORT_INDEXED:
			numberOfBytes = 4;
			stateTime = 6;//11
			break;
		case AddressMode.LONG_INDEXED:
			numberOfBytes = 5;
			stateTime = 7;//12
			break;
		}

		operands = new byte[numberOfBytes];
		for (int i = 0; i < numberOfBytes; i++) {
			operands[i] = (byte) memory[pc + i];
		}

		short sum = 0;
		
		if (this.getAddressModeType() == AddressMode.DIRECT) {
			// Assembler Format: AD2B areg, breg
			// Instruction Operation: (Rb)<-(Ra)+(Rb)
			// Execution Sates: 4
			// Machine Format: [ ^74 ],[ Source Ra ],[ Dest Rb ]
			
			byte areg = (byte)(operands[1]);
			byte breg = (byte)(operands[2]);
			
			sum = state_.doAdd(areg,  breg);
			
			// (RA)<-(RA)+2

			state_.setByteRegister(breg, (byte) (sum & 0xf));
		}
		if (this.getAddressModeType() == AddressMode.IMMEDIATE) {
            byte data  = (byte) (operands[1]);
			byte breg = (byte)(operands[2]);
	        byte bregValue = state_.getByteRegister((byte)breg);
	         
	            sum =  (short) (data + bregValue) ;
	            
	        state_.setByteRegister(breg, (byte) (sum & 0xf));
		}
		if (this.getAddressModeType() == AddressMode.INDIRECT) {
			System.err.println("Not Implemented yet.");
			System.exit(1);
		}
		if (this.getAddressModeType() == AddressMode.INDIRECT_AUTO_INC) {
			// Assembler Format: AD2B (Indirreg)+,breg
			// Instruction Operation: (RB)<-([RA])+(RB); (RA)<-(RA)+2
			// Execution Sates: 7/12
			// Machine Format: [ ^76 ],[ Indirect Ra| 1 MB ],[ Dest Rb ]
			
			byte indirectRA = (byte)(operands[1] & 0xfe);
			byte destRB =  (byte)(operands[2]);
			
			byte valueRA = state_.getByteRegister((byte) indirectRA);
			byte valueRB = state_.getByteRegister((byte) destRB);
			
			// (Ra)<-(Ra)+1
			state_.setByteRegister(indirectRA, (byte)(valueRA + 1));
			valueRA = state_.getByteRegister((byte) valueRA);
			
			state_.setByteRegister(destRB, (byte) (valueRA + valueRB));
		}
		if (this.getAddressModeType() == AddressMode.SHORT_INDEXED) {
			System.err.println("Not Implemented yet.");
			System.exit(1);
		}
		if (this.getAddressModeType() == AddressMode.LONG_INDEXED) {
			System.err.println("Not Implemented yet.");
			System.exit(1);
		}
		state_.setPc(pc + numberOfBytes);
		state_.updateStateTime(stateTime);
	}
}
