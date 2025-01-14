package ca.nerret.emu.processor.op;

import ca.nerret.emu.UnknownOpCodeException;
import ca.nerret.emu.env.RoxByte;
import ca.nerret.emu.env.RoxWord;
import ca.nerret.emu.mem.Memory;
import ca.nerret.emu.processor.EEC8061_RALU;
import ca.nerret.emu.processor.Registers;

/**
 * An enum representing possible addressing modes for {@link Mos6502OpCode}
 *
 * @author Ross Drew
 */
public enum EEC8061AddressingMode implements Addressable {
	
    /** Expects no argument */
    DIRECT("Direct", 1, (r, m, a, i) -> i.perform(a,r,m,null)),

    /** Expects a one byte argument that is a literal value for use in the operation */
    IMMEDIATE("Immediate", 2, (r, m, a, i) -> {
        final RoxWord argAddress = a.getAndStepProgramCounter();
        final RoxByte argument = m.getByte(argAddress);
        i.perform(a,r,m, argument);
    }),

    /** Expects a one byte argument that contains a zero page address to use in the operation. Can be indexed
     *  as {@link #ZERO_PAGE_X} or {@link #ZERO_PAGE_Y} */
    ZERO_PAGE("Zero Page", 2, (r, m, a, i) -> {
        final RoxWord argumentAddress = a.getAndStepProgramCounter();
        final RoxWord pointer = RoxWord.from(m.getByte(argumentAddress));
        final RoxByte value = m.getByte(pointer);
        final RoxByte newValue = i.perform(a, r, m, value);
        m.setByteAt(pointer, newValue);
    }),

    /** Expects a one byte argument that contains a zero page address and the X Register to be filled with an
     *  offset value, to use in the operation */
    ZERO_PAGE_X("Zero Page [X]", 2, (r, m, a, i) -> {
        final RoxWord argumentAddress = a.getAndStepProgramCounter();
        final RoxByte argumentValue = m.getByte(argumentAddress);

        final RoxByte offset = r.getRegister(Registers.Register.X_INDEX);
        final RoxWord valueAddress = RoxWord.fromLiteral(argumentValue.getRawValue() + offset.getRawValue());

        final RoxByte value = m.getByte(valueAddress);
        final RoxByte newValue = i.perform(a, r, m, value);
        m.setByteAt(valueAddress, newValue);
    }),

    /** Expects a one byte argument that contains a zero page address and the Y Register to be filled with an
     *  offset value, to use in the operation */
    ZERO_PAGE_Y("Zero Page [Y]", 2, (r, m, a, i) -> {
        final RoxWord argumentAddress = a.getAndStepProgramCounter();
        final RoxByte argumentValue = m.getByte(argumentAddress);

        final RoxByte offset = r.getRegister(Registers.Register.Y_INDEX);
        final RoxWord valueAddress = RoxWord.fromLiteral(argumentValue.getRawValue() + offset.getRawValue());

        final RoxByte value = m.getByte(valueAddress);
        final RoxByte newValue = i.perform(a, r, m, value);
        m.setByteAt(valueAddress, newValue);
    }),

    /** Expects a 2 byte argument that contains an absolute address for use in the operation. Can be indexed
     *  as {@link #ABSOLUTE_X} or {@link #ABSOLUTE_Y} */
    ABSOLUTE("Absolute", 3, (r, m, a, i) -> {
        final RoxWord argumentHiByteAddress = a.getAndStepProgramCounter();
        final RoxWord argumentLoByteAddress = a.getAndStepProgramCounter();
        final RoxWord pointer = RoxWord.from(m.getByte(argumentHiByteAddress),
                                             m.getByte(argumentLoByteAddress));

        final RoxByte value = m.getByte(pointer);
        final RoxByte newValue = i.perform(a, r, m, value);

        m.setByteAt(pointer, newValue);
    }),

    /** Expects a 2 byte argument that contains an absolute address and the X Register to be filled with an
     *  offset value, to use in the operation */
    ABSOLUTE_X("Absolute [X]", 3, (r, m, a, i) -> {
        final RoxWord argumentHiByteAddress = a.getAndStepProgramCounter();
        final RoxWord argumentLoByteAddress = a.getAndStepProgramCounter();
        final RoxWord pointer = RoxWord.from(m.getByte(argumentHiByteAddress),
                                             m.getByte(argumentLoByteAddress));

        final RoxByte offset = r.getRegister(Registers.Register.X_INDEX);
        final RoxWord valueAddress = RoxWord.fromLiteral(pointer.getRawValue() + offset.getRawValue());

        final RoxByte value = m.getByte(valueAddress);
        final RoxByte newValue = i.perform(a, r, m, value);
        m.setByteAt(valueAddress, newValue);
    }),

    /** Expects a 2 byte argument that contains an absolute address and the Y Register to be filled with an
     *  offset value, to use in the operation */
    ABSOLUTE_Y("Absolute [Y]", 3, (r, m, a, i) -> {
        final RoxWord argumentHiByteAddress = a.getAndStepProgramCounter();
        final RoxWord argumentLoByteAddress = a.getAndStepProgramCounter();
        final RoxWord pointer = RoxWord.from(m.getByte(argumentHiByteAddress),
                m.getByte(argumentLoByteAddress));

        final RoxByte offset = r.getRegister(Registers.Register.Y_INDEX);
        final RoxWord valueAddress = RoxWord.fromLiteral(pointer.getRawValue() + offset.getRawValue());

        final RoxByte value = m.getByte(valueAddress);
        final RoxByte newValue = i.perform(a, r, m, value);
        m.setByteAt(valueAddress, newValue);
    }),

    /** Expects a one byte argument that contains a zero page address that contains the two byte address,
     *  to use in the operation.  Can be indexed as {@link #INDIRECT_X} or {@link #INDIRECT_Y} */
    INDIRECT("Indirect", 2, (r, m, a, i) -> {}),

    /** <i>Indexed indirect</i>: Expects a one byte argument and an offset in the X Register added together they
     *  give an address in Zero Page that itself contains a two byte address to be used in the operation */
    INDIRECT_X("Indirect, X", 2, (r, m, a, i) -> {
        final RoxByte offset = r.getRegister(Registers.Register.X_INDEX);
        final RoxWord argumentAddress = a.getAndStepProgramCounter();
        final RoxWord pointerAddress = RoxWord.fromLiteral(m.getByte(argumentAddress).getRawValue() + offset.getRawValue());

        final RoxWord pointer = m.getWord(pointerAddress);
        final RoxByte value = m.getByte(pointer);

        final RoxByte newValue = i.perform(a, r, m, value);
        m.setByteAt(pointer, newValue);
    }),

    /** <i>Indirect indexed</i>: Expects a one byte argument and an offset in the Y Register.  A two byte address
     *  is fetched from the Zero Page location pointed to by the argument, the offset is added to this address which
     *  gives the two byte address to be used in the operation  */
    INDIRECT_Y("Indirect, Y", 2, (r, m, a, i) -> {
        final RoxWord argumentAddress = a.getAndStepProgramCounter();
        final RoxWord argument = RoxWord.from(m.getByte(argumentAddress));

        final RoxByte offset = r.getRegister(Registers.Register.Y_INDEX);
        final RoxWord pointerBase = m.getWord(argument);
        final RoxWord pointer = RoxWord.fromLiteral(pointerBase.getRawValue() + offset.getRawValue());
        final RoxByte value = m.getByte(pointer);

        final RoxByte newValue = i.perform(a, r, m, value);
        m.setByteAt(pointer, newValue);
    }),

    /** Expects no argument, operation will be performed using the Accumulator Register*/
    ACCUMULATOR("Accumulator", 1, (r, m, a, i) -> {
        final RoxByte value = r.getRegister(Registers.Register.ACCUMULATOR);
        r.setRegister(Registers.Register.ACCUMULATOR, i.perform(a, r, m, value));
    }),

    /** Expects a one byte argument that is the offset for a branch instruction */
    RELATIVE("Relative", 2, (r, m, a, i) -> {
        final RoxWord argumentAddress = a.getAndStepProgramCounter();
        final RoxByte argument = m.getByte(argumentAddress);

        //Pass the offset (127 bytes forward or 128 back)
        i.perform(a, r, m, argument);
    });

    private final String name;
    /* Bytes required to address this instruction including a byte for the opcode and then it's arguments */
    private final int instructionBytes;

    private final Addressable address;

    @Override
    public void address(Registers r, Memory m, EEC8061_RALU alu, AddressedValueInstruction instruction) {
        address.address(r, m, alu, instruction);
    }


    EEC8061AddressingMode(final String name,
                          final int instructionBytes,
                          final Addressable address) {
        this.name = name;
        this.instructionBytes = instructionBytes;
        this.address = address;
    }

    /**
     * @return the {@link String} description of this addressing mode
     */
    public String getDescription(){
        return name;
    }

    /**
     * @return the number of bytes needed to make up this instruction, including the instruction byte
     */
    public int getInstructionBytes(){
        return this.instructionBytes;
    }

    /**
     * Convert this addressing mode, to x indexed. Instructions that can be X Indexed are:-
     * <ul>
     *     <li>{@link #ZERO_PAGE}</li>
     *     <li>{@link #ABSOLUTE}</li>
     *     <li>{@link #INDIRECT}</li>
     * </ul>
     * @return the {@link EEC8061AddressingMode} that corresponds to this {@link EEC8061AddressingMode}, but indexed by X
     * @throws UnknownOpCodeException if there is an attempt to X index an addressing mode that cannot be X indexed
     */
    public EEC8061AddressingMode xIndexed() {
        if (this == ZERO_PAGE){
            return ZERO_PAGE_X;
        }else if (this == ABSOLUTE) {
            return ABSOLUTE_X;
        }else if (this == INDIRECT){
            return INDIRECT_X;
        }else{
            throw new UnknownOpCodeException(this + " cannot be X indexed", this);
        }
    }

    /**
     * Convert this addressing mode, to y indexed. Instructions that can be Y Indexed are:-
     * <ul>
     *     <li>{@link #ZERO_PAGE}</li>
     *     <li>{@link #ABSOLUTE}</li>
     *     <li>{@link #INDIRECT}</li>
     * </ul>
     * @return the {@link EEC8061AddressingMode} that corresponds to this {@link EEC8061AddressingMode}, but indexed by Y
     * @throws UnknownOpCodeException if there is an attempt to Y index an addressing mode that cannot be Y indexed
     */
    public EEC8061AddressingMode yIndexed(){
        if (this == ZERO_PAGE){
            return ZERO_PAGE_Y;
        }else if (this == ABSOLUTE){
            return ABSOLUTE_Y;
        }else if (this == INDIRECT){
            return INDIRECT_Y;
        }else{
            throw new UnknownOpCodeException(this + " cannot be Y indexed", this);
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
