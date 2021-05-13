package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.emulator.ProgramStatusWord;
import ca.nerret.emu.emulator.State;

/**
 * OpCode 0x0D SHLDW
 * 
 * Description: 
 *   SHLDW performs a left shift of a 32-bit "B' operand based upon a shift count (SC) contained in
 *   either the four LSBs of the 2nd instruction byte or the five LSBs of any memory location internal to
 *   the microprocessor IC chip. The address of the memory location is then contained within the 2nd
 *   instruction byte in lieu of a shift count. A shift count contained within the instruction can range from
 *   0 to 15; whereas, a shift count contained within a memory location can range from 0 to 31.
 *
 *   During the shift, the operand MSBs are shifted through the carry while the LSBs are filled with
 *   zeros. The 32-bit result is returned to the operand location and the last bit shifted out is saved in the
 *   carry (C) flag.
 */
public class OpCodeSHLDW extends OpCode implements IOpCode {

	private short origninalOperand;
	private byte shiftcount;
	private long result32bit;

    private short result;
    private short DoperandLocation;

    public OpCodeSHLDW(int opcode, String mnemonic) {
		super(opcode, mnemonic);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
     * @see ca.nerret.emu.emulator.opcodes.IOpcode#exec(ca.nerret.emu.emulator.State)
     */
    @Override
    public final void exec(State state_)
    {
    	super.exec(state_);
       
        //state_.setWordRegister(this.getOperandLocation(),this.getResult());

    }

    public void execDirect()
    {
        // Assembler Fomat: SHLDW screg, breg
        // Instruction Operation: C <- [b31 -B OPERAND- b0]<-0
        // Execution States: 8 + 1/each shift
        // Machine Format: [ ^0D ], [ Source RSC ], [ Source RBB ]
        numberOfBytes = 3;
        executionStates = 8;//12
        // 8 6138: 0d,01,34             shldw R34,1              R34L = R34L * 2;     
        byte[] operands = this.getOperands(numberOfBytes, executionStates);

        byte screg = operands[1];
        byte basereg = operands[2];
        // :SHLL lreg, breg                    is op8=0x0d; breg & (highb != 0x0); lreg {
        // local source = lreg;
        // local shift = breg;
        // setShiftLeftCarryFlag(source, shift);
        // local res = source << shift;
        // $(Z) = (res == 0);
        // $(V) = 0;
        // $(VT) = $(VT) | $(V);
        // lreg = res;
    }

    public void execImmediate()
    {

        //:SHLL lreg, "#"immed8               is op8=0x0d; immed8 & (highb = 0x0); lreg {
        //local source = lreg;
        //local shift = immed8;
        //setShiftLeftCarryFlag(source, shift);
        //local res = source << shift;
        //$(Z) = (res == 0);
        //$(V) = 0;
        //$(VT) = $(VT) | $(V);
        //lreg = res;

    }
	public  void execSSSSShortIndexed()
    {
    	// AssemblerFormat: SB3W offset(basereg),breg,dreg
    	// InstructionOperation:(RD) <- (RB)-([RA]+Offset) 
    	// ExecutionStates: 7/12
    	// MachineFormat: [ ^4B ],[ Base RA | 0 MB ],[+-| Offset ],[ Source RB ], [ Dest RD ]
    	numberOfBytes = 5;
    	executionStates = 7;//12

    	byte[] operands = this.getOperands(numberOfBytes, executionStates);
    	
    	byte baseRA = (byte) (operands[1] & 0xfe); // mask out mode bit
    	byte offset = operands[2];
    	byte sourceRB = operands[3];
    	byte destRD = operands[4];
    	
    	short valueBaseRA = this.getWordValue(baseRA);
    	short locationRA = (short) (valueBaseRA + offset);
    	
    	short valueRA = this.getWordValue(locationRA);
    	short valueRB = this.getWordValue(sourceRB);
    	
    	this.setSubResult(valueRB, valueRA);
    	this.setOperandLocation(destRD);
    }

	public short setSubResult(short valueRB, short valueRA) {
		 this.result =super.setSubResult(valueRB, valueRA);
		
		 return this.result;
	}

	private void setOperandLocation(byte destRD) {
		
    	this.DoperandLocation = (short)(destRD & 0xff);		
	}

	protected short getOperandLocation() {
		
		return this.DoperandLocation;
	}

	protected short getResult() {
		
		return this.result;
	}


}
