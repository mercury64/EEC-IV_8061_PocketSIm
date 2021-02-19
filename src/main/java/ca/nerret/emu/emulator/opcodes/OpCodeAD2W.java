package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * OpCode 0x66 AD2W
 */
public class OpCodeAD2W extends OpCode<OpCodeAD2W> implements IOpCode {

	public OpCodeAD2W(int opcode, String mnemonic) {
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
			numberOfBytes = 4;
			stateTime = 5;
			break;
		case AddressMode.INDIRECT:
			numberOfBytes = 3;
			stateTime = 6;
			break;
		case AddressMode.INDIRECT_AUTO_INC:
			numberOfBytes = 3;
			stateTime = 7;
			break;
		case AddressMode.SHORT_INDEXED:
			numberOfBytes = 4;
			stateTime = 6;
			break;
		case AddressMode.LONG_INDEXED:
			numberOfBytes = 5;
			stateTime = 7;
			break;
		}

		operands = new byte[numberOfBytes];
		for (int i = 0; i < numberOfBytes; i++) {
			operands[i] = (byte) memory[pc + i];
		}

		// DEST, SRC1, SRC2 ADD Dwreg, Swreg, waop
		// (010001aa) (waop) (Swreg) (Dwreg)
		// System.out.println(" DEST, SRC1, SRC2");
		// System.out.println("ADD Dwreg, Swreg, waop");

		// System.out.printf("0x%x(AD3W) Dwreg: 0x%x,Swreg: 0x%x,waop: 0x%x \n",
		// operands[0], dest_dwreg, src1_Swreg, src2_waop);
		// System.out.printf(" 0x%x = [0x%x] + 0x%x\n", dest_dwreg, src1_value,
		// src2_waop);
		if (this.getAddressModeType() == AddressMode.DIRECT) {
			System.err.println("Not Implemented yet.");
			System.exit(1);
		}
		if (this.getAddressModeType() == AddressMode.IMMEDIATE) {
			System.err.println("Not Implemented yet.");
			System.exit(1);
		}
		if (this.getAddressModeType() == AddressMode.INDIRECT) {
			System.err.println("Not Implemented yet.");
			System.exit(1);
		}
		if (this.getAddressModeType() == AddressMode.INDIRECT_AUTO_INC) {
			// Assembler Format: AD2W (Indirreg)+,breg
			// Instruction Operation: (RB)<-([RA])+(RB); (RA)<-(RA)+2
			// Execution Sates: 7/12
			// Machine Format: [ ^66 ],[ Indirect RA| 1 MB ],[ Dest RB ]
			
			byte indirectRA = (byte)(operands[1] & 0xfe);
			byte destRB =  (byte)(operands[2]);
			
			short valueRA = state_.getWordRegister((byte) indirectRA);
			short valueRB = state_.getWordRegister((byte) destRB);
			
			// (RA)<-(RA)+2
			state_.setWordRegister(indirectRA, (short)(valueRA + 2));
			valueRA = state_.getWordRegister((short) valueRA);
			
			state_.setWordRegister(destRB, (short) (valueRA + valueRB));
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
