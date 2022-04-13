package ca.nerret.emu.processor;

import ca.nerret.emu.env.RoxByte;
import ca.nerret.emu.env.RoxWord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static ca.nerret.emu.processor.Registers.Register.*;

/**
 * A representation of the MOS 6502 CPU registers
 *
 * @author Ross Drew
 */
public class Registers {
    private static final Logger log = LoggerFactory.getLogger(Registers.class);
	
    public static final String READ = null;

    /**
     * A single registerValue for a MOS 6502 containing information on registerValue id and name
     */
    public enum Register {
        ACCUMULATOR(0),
        Y_INDEX(1),
        X_INDEX(2),

        STACK_POINTER_LOW(5),
        STACK_POINTER_HI(6),
        STATUS_FLAGS(7),
        PSW_Flag(8);

        private final String description;
        private final int index;

        Register(int index){
            this.index = index;
            description = prettifyName(name());
        }

        private static String prettifyName(String originalName){
            String name = originalName.replaceAll("_"," ")
                                      .toLowerCase()
                                      .replace("hi","(High)")
                                      .replace("low","(Low)");

            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            int spaceIndex = name.indexOf(' ');
            if (spaceIndex > 0)
                name = name.substring(0, spaceIndex) + name.substring(spaceIndex, spaceIndex+2).toUpperCase() + name.substring(spaceIndex+2);
            return name;
        }

        public String getDescription(){
            return description;
        }

        public int getIndex(){
            return this.index;
        }
    }
    
    /**
     * A single registerValue for a MOS 6502 containing information on registerValue id and name
     */
    public enum SFR_Register {
    	
        ZERO_LO(0, 'E', "Zero Register", "Note"),
        ZERO_HI(1, 'E', "Zero Register", "Note"),
        LSO_PORT(2, 'B', "LSO Port (Non-Bidirectional)", "LSO Port (Non-Bidirectional)"),
        IO_PORT(3, 'B', "Bidirectional I/O Port", "Bidirectional I/O Port"),
        AD_LO(4, 'B', "A/D Result Register Low Byte", "A/D Command Register (Channel #)"),
        AD_HI(5, 'B', "A/D Result Register High Byte", "Watchdog Timer"),
        MASTER_IO_TIMER_LO(6, 'W', "Master I/O Timer Lo Byte", "Note"),
        MASTER_IO_TIMER_HI(7, 'W', "Master I/O Timer hi Byte", "Note"),
        INT_MASK(8, 'B', "Interrupt Mask Register", "Interrupt Mask Register"),
        INT_PENDING(9, 'B', "Interrupt Pending Register", "Interrupt Pending Register"),
        IO_STATUS(0xa, 'B', "I/O Status Register", "I/O Status Register"),
        INPUT_SAMPLE(0xb, 'B', "Input Sample Register", "Note"),
        HSI_MASK(0xc, 'B', "HSI Data Mask Register", "HSI Data Mask Register"),
        HSI_HOLDING(0xd,'B', "HSI Data Holding Register", "HSO Command Holding Register"),
        HSI_TIMER_LO(0xe,'W', "HSI Time Holding Register Low Byte", "HSO Time Holding Register Low Byte"),
        HSI_TIMER_HI(0xf,'W', "HSI Time Holding Register High Byte", "HSO Time Holding Register High Byte"),
        STACK_PTR_LO(0x10, 'E', "Stack Pointer Low Byte", "Stack Pointer Low Byte"),
        STACK_PTR_HI(0x11, 'E', "Stack Pointer High Byte", "Stack Pointer High Byte");

        private final int index;
		private char typeOfAccess;
		private String readDescription;
		private String writeDescription;

        SFR_Register(int index, char typeOfAccess, String readDescription, String writeDescription){
            this.index = index;
            this.typeOfAccess = typeOfAccess;
            this.readDescription = readDescription;
            this.writeDescription = writeDescription;
      
        }

        public int getIndex(){
            return this.index;
        }

		public char getTypeOfAccess() {
			return typeOfAccess;
		}

		public String getReadDescription() {
			return readDescription;
		}

		public String getWriteDescription() {
			return writeDescription;
		}
    }
    
    /**
     * A MOS 6502 status flag
     */
    public enum Flag {
        CARRY(0),
        ZERO(1),
        IRQ_DISABLE(2),
        DECIMAL_MODE(3),
        BREAK(4),
        UNUSED(5),
        OVERFLOW(6),
        NEGATIVE(7);

        private final int index;
        private final int placeValue;
        private final String description;

        private static String prettifyName(String originalName){
            String name = originalName.replaceAll("_"," ").toLowerCase();
            name = name.substring(0, 1).toUpperCase() + name.substring(1);
            if (name.contains(" ")) {
                int spaceIndex = name.indexOf(' ');
                name = name.substring(0, spaceIndex) + name.substring(spaceIndex, spaceIndex + 2).toUpperCase() + name.substring(spaceIndex + 2);
            }
            return name;
        }

        Flag(int index) {
            this.index = index;
            this.placeValue = (1 << index);
            description = prettifyName(name());
        }

        public String getDescription() {
            return description;
        }

        public int getIndex() {
            return index;
        }

        public int getPlaceValue() {
            return placeValue;
        }
    }

    private final RoxByte[] registerValue;
    private final RoxByte[] sfregisterValue;

    public Registers(){
       
		registerValue = new RoxByte[9];
        sfregisterValue = new RoxByte[18];
        Arrays.fill(registerValue, RoxByte.ZERO);
        //Arrays.fill(sfregisterValue, RoxByte.ZERO);
        
        setRegister(STACK_POINTER_LOW, RoxByte.fromLiteral(0b11111111));
        setRegister(STATUS_FLAGS, RoxByte.fromLiteral(0b00000000));
    }

    public Registers(final RoxByte[] registerValue){
        this.sfregisterValue = new RoxByte[18];
		this.registerValue = Arrays.copyOf(registerValue, 8);
        setRegister(STACK_POINTER_LOW, RoxByte.fromLiteral(0b11111111));
    }

    /**
     * @param register the registerValue to set
     * @param value to set the registerValue to
     */
    public void setRegister(Register register, RoxByte value){
        log.debug("'R:{}' := {}", register.getDescription(), value);
        registerValue[register.getIndex()] = (value == null ? RoxByte.ZERO : value);
    }

    /**
     * @param register from which to get the value
     * @return the value of the desired registerValue
     */
    public RoxByte getSFRRegister(SFR_Register sfregister){
        return registerValue[sfregister.getIndex()];
    }
    
    /**
     * @param register from which to get the value
     * @return the value of the desired registerValue
     */
    public RoxByte getRegister(Register register){
        return registerValue[register.getIndex()];
    }

 
    /**
     * @param flag flag to test
     * @return <code>true</code> if the specified flag is set, <code>false</code> otherwise
     */
    public boolean getFlag(Flag flag) {
        return registerValue[STATUS_FLAGS.getIndex()].isBitSet(flag.getIndex());
    }

    /**
     * @param flag for which to set the state
     * @param state to set the flag to
     */
    public void setFlagTo(Flag flag, boolean state) {
        if (state)
            setFlag(flag);
        else
            clearFlag(flag);
    }

    /**
     * Set* the specified flag of the status register to 1
     *
     * @param flag for which to set to true
     */
    public void setFlag(Flag flag) {
        log.debug("'F:{}' -> SET", flag.description);
        registerValue[STATUS_FLAGS.getIndex()] = registerValue[STATUS_FLAGS.getIndex()].withBit(flag.getIndex());
    }

    /**
     * Set/clear the specified flag of the status register to 0
     *
     * @param flag to be cleared
     */
    public void clearFlag(Flag flag){
        log.debug("'F:{}' -> CLEARED", flag.getDescription());
        registerValue[STATUS_FLAGS.getIndex()] = registerValue[STATUS_FLAGS.getIndex()].withoutBit(flag.getIndex());
    }

    /**
     * @param value to set the status flags based on
     */
    public void setFlagsBasedOn(RoxByte value){
        setZeroFlagFor(value.getRawValue());
        setNegativeFlagFor(value.getRawValue());
    }

    /**
     * Set zero flag if given argument is 0
     */
    private void setZeroFlagFor(int value){
        setFlagTo(Flag.ZERO, value == 0);
    }

    /**
     * Set negative flag if given argument is 0
     */
    private void setNegativeFlagFor(int value){
        setFlagTo(Flag.NEGATIVE, isNegative(value));
    }

    private boolean isNegative(int fakeByte){
        return RoxByte.fromLiteral(fakeByte).isNegative();
    }

    public Registers copy(){
        return new Registers(registerValue);
    }
}
