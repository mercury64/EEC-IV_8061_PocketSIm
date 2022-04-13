package ca.nerret.emu.env;

import java.util.Objects;

public class DoubleWord {
    private final int doubeWordValue;

    /** Binary digit place values */
    private static final int[] PLACE_VALUE = {  
    		0x0001,0x0002,0x0004,0x0008, // word 1
    		0x0010,0x0020,0x0040,0x0080,
    		0x0100,0x0200,0x0400,0x0800,
    		0x1000,0x2000,0x4000,0x8000,
    		
    		0x00010000,0x00020000,0x00040000,0x00080000, // word 2
    		0x00100000,0x00200000,0x00400000,0x00800000,
    		0x01000000,0x02000000,0x04000000,0x08000000,
    		0x10000000,0x20000000,0x40000000,0x80000000

    };

    public static final DoubleWord ZERO = new DoubleWord(0);

    private DoubleWord(int wordValue) {
        this.doubeWordValue = wordValue;
    }

    /**
     * Create a {@link RoxWord} from the given high and low {@link RoxByte}s
     */
    public static DoubleWord from(final RoxWord highByte,
                               final RoxWord lowByte){
        return new DoubleWord(highByte.getRawValue() << 16 | lowByte.getRawValue() << 8);
    }

    /**
     * Create a {@link RoxWord} with the given {@link RoxByte} as the lowest significant byte.
     */
    public static DoubleWord from(final RoxWord lowByte){
        return new DoubleWord(lowByte.getRawValue());
    }

    /**
     * Extract a literal {@link RoxWord} from the first two least significant bytes of the given {@link int}
     */
    public static DoubleWord fromLiteral(final int literalValue) {
        return new DoubleWord(literalValue & 0xFFFFFFFF);
    }

    /**
     * Take the {@link RoxWord word} and move it's sign bit to the most significant
     * bit of an {@link int} representation of it
     *
     * @return This {@link RoxWord word} as it's {@link int} representation.
     */
    public int getAsInt() {
        return intFromTwosComplimented(doubeWordValue);
    }

    private int intFromTwosComplimented(int doubeWordValue){
        if (isBitSet(15))
            return -((-doubeWordValue) & 0xFFFFFFFF);
        else
            return doubeWordValue;
    }

    /**
     * @param bitToTest bit number (<code>0-7</code>) of the bit to test
     * @return weather the specified bit is set in this byte
     */
    public boolean isBitSet(int bitToTest) {
        validateBit(bitToTest);
        return (doubeWordValue & PLACE_VALUE[bitToTest]) == PLACE_VALUE[bitToTest];
    }

    private void validateBit(final int bit){
        if ((bit < 0) || (bit > 31))
            throw new ArrayIndexOutOfBoundsException("Bit #"+ bit +" is out of range, expected (0-31)");
    }

    /**
     * @return The least significant {@link RoxByte byte} (lsb) of this {@link RoxWord word}
     */
    public RoxWord getLowByte() {
        return RoxWord.fromLiteral(doubeWordValue & 0xFFFF);
    }

    /**
     * @return The most significant {@link RoxByte byte} (msb) of this {@link RoxWord word}
     */
    public RoxWord getHighByte() {
        return RoxWord.fromLiteral(doubeWordValue >> 16);
    }

    /**
     * @return Return an {@link int} with identical two least significant bytes to this {@link RoxWord word}
     */
    public int getRawValue(){
        return doubeWordValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null) return false;

        if (o instanceof Integer)
            return (doubeWordValue == DoubleWord.fromLiteral((Integer)o).getRawValue());
        if (o instanceof RoxWord)
            return doubeWordValue == DoubleWord.from((RoxWord)o).getRawValue();
        else if (getClass() != o.getClass())
            return false;

        return doubeWordValue == ((DoubleWord) o).doubeWordValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(doubeWordValue);
    }

    @Override
    public String toString() {
        return "DoubleWordValue {" + getAsInt() +"}";
    }
}