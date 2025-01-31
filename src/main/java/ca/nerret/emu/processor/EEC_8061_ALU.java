package ca.nerret.emu.processor;

import ca.nerret.emu.env.RoxByte;
import ca.nerret.emu.env.RoxWord;

/**
 * Arithmetic Logic Unit for a {@link Mos6502}.<br/>
 * <br/>
 * Operations:
 * <ul>
 *  <li> {@link #adc} </li>
 *  <li> {@link #sbc} </li>
 *  <li> {@link #or} </li>
 *  <li> {@link #xor} </li>
 *  <li> {@link #and} </li>
 *  <li> {@link #asl} </li>
 *  <li> {@link #rol} </li>
 *  <li> {@link #lsr} </li>
 *  <li> {@link #ror} </li>
 * </ul>
 *
 * XXX think about multi byte addition - using this calculating > 1 byte memory doesn't work, of course.
 */
public class EEC_8061_ALU {

    private final Registers registers;

    public EEC_8061_ALU(Registers registers) {
        this.registers = registers;
    }

    /**
     * <b>Sets the {@link Registers} carry flag to the carry of the operation</b><br/>
     * <br/> 
     * Return the addition of <code>byteA</code>, <code>byteB</code> and the contents of the {@link Registers} carry
     * flag.<br/>
     * <br>
     * The carry flag is used for multi-byte addition, so it should be cleared at the start of any addition
     * that doesn't need to take into account a carry from a previous one.
     *
     * @return the result of <code>byteA ADD byteB</code>
     */
    public RoxByte adc(final RoxByte byteA, final RoxByte byteB){
        int carry = registers.getFlag(Registers.Flag.CARRY) ? 1 : 0;

        final RoxWord result = RoxWord.fromLiteral(byteA.getRawValue() + byteB.getRawValue() + carry);

        registers.setFlagTo(Registers.Flag.CARRY, result.getHighByte().isBitSet(0));

        if (isAdcOverflow(byteA, byteB, result))
            registers.setFlag(Registers.Flag.OVERFLOW);

        return result.getLowByte();
    }

    /**
     * Is the sign of both inputs different from the sign of the adc result i.e. bit 7 set on the result of
     * <code>((a^result) & (b^result))</code>
     *
     * @param inputByteA first {@link RoxByte} to compare
     * @param inputByteB second {@link RoxByte} to compare
     * @param result {@code true} of overflow, {@code false} otherwise
     * @return if the result of adc(inputByteA,inputByteB) should cause an overflow bit
     */
    private boolean isAdcOverflow(final RoxByte inputByteA,
                                  final RoxByte inputByteB,
                                  final RoxWord result) {
        return and(xor(inputByteA, result.getLowByte()),
                   xor(inputByteB, result.getLowByte())).isBitSet(7);
    }

    /**
     * <b>Sets the {@link Registers} carry flag to the borrow of the operation</b><br/>
     * <br/>
     * Return the subtraction of <code>byteB</code> from <code>byteA</code> using the contents of the
     * {@link Registers} carry flag as a borrow.<br/>
     * <br/>
     * This is effectively an {@link #adc} operation with <code>byteB</code> converted to it's ones compliment.
     * Combined with a <em>loaded carry flag</em> used as a borrow, this gives subtraction via twos compliment
     * addition.<br/>
     * <br>
     * <pre>
     *                                           (1) - carry in
     * byteA =>    0000 1010  [10]   =>    0000 1010
     * byteB =>  - 0000 0100  [4]    =>  + 1111 1011   [4, ones complimented]
     *           -----------             -----------
     *                                 (1) 0000 0110   [6]     =>  10 - 4 = 6
     *                      carry out _/
     * </pre>
     * This means the opposite of {@link #adc}s carry behaviour is expected.  Any usage that doesn't need
     * to take into account of a previous bytes borrow, should load the carry flag before the operation to
     * get normal behaviour.
     *
     * @see #adc(RoxByte, RoxByte)
     * @return the result of the SBC operation
     */
    public RoxByte sbc(RoxByte byteA, RoxByte byteB) {
        return adc(byteA, byteB.asOnesCompliment());
    }

    /**
     * @return the result of <code>byteA OR byteB</code><br/>
     */
    public RoxByte or(RoxByte byteA, RoxByte byteB) {
        return RoxByte.fromLiteral(byteA.getRawValue() | byteB.getRawValue());
    }

    /**
     * @return the result of <code>byteA AND byteB</code><br/>
     */
    public RoxByte and(RoxByte byteA, RoxByte byteB) {
        return RoxByte.fromLiteral(byteA.getRawValue() & byteB.getRawValue());
    }

    /**
     * @return the result of <code>byteA XOR byteB</code><br/>
     */
    public RoxByte xor(RoxByte byteA, RoxByte byteB) {
        return RoxByte.fromLiteral(byteA.getRawValue() ^ byteB.getRawValue());
    }

    /**
     * Shift bits left and write a zero into the low order bit, setting the carry to whatever
     * is shifted out of the high order bit.
     *
     * @return the result of <code>ASL byteA</code>
     */
    public RoxByte asl(RoxByte byteA) {
        final RoxWord result = RoxWord.fromLiteral((byteA.getRawValue() << 1));
        registers.setFlagTo(Registers.Flag.CARRY, result.getHighByte().isBitSet(0));
        return result.getLowByte();
    }

    /**
     * Shift bits left and write the contents of the carry flag into the low order bit, setting
     * the carry to whatever is shifted out of the high order bit.
     *
     * @return the result of <code>ROL byteA</code>
     */
    public RoxByte rol(RoxByte byteA) {
        int carry = registers.getFlag(Registers.Flag.CARRY) ? 1 : 0;
        final RoxWord result = RoxWord.fromLiteral((byteA.getRawValue() << 1) + carry);
        registers.setFlagTo(Registers.Flag.CARRY, result.getHighByte().isBitSet(0));
        return result.getLowByte();
    }

    /**
     * Shift bits right and write a zero into the high order bit, setting the carry to whatever
     * is shifted out of the low order bit.
     *
     * @return the result of <code>LSR byteA</code>
     */
    public RoxByte lsr(RoxByte byteA) {
        final RoxByte result = RoxByte.fromLiteral((byteA.getRawValue() >> 1));
        registers.setFlagTo(Registers.Flag.CARRY, byteA.isBitSet(0));
        return result;
    }

    /**
     * Shift bits right and write the contents of the carry flag into the high order bit,
     * setting the carry to whatever is shifted out of the low order bit.
     *
     * @return the result of <code>ROR byteA</code>
     */
    public RoxByte ror(RoxByte byteA) {
        int carry = registers.getFlag(Registers.Flag.CARRY) ? 0b10000000 : 0;
        final RoxByte result = RoxByte.fromLiteral((byteA.getRawValue() >> 1) | carry);
        registers.setFlagTo(Registers.Flag.CARRY, byteA.isBitSet(0));
        return result;
    }
}
