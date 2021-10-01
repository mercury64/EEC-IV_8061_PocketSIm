package ca.nerret.emu.emulator.opcodes;

import ca.nerret.emu.emulator.AddressMode;
import ca.nerret.emu.emulator.State;


public interface IOpCode<T> {

    /**
     * Increment the pc counter by 1.
     */
    int _PC_INCR_1 = 1;
    /**
     * Increment the pc counter by 2.
     */
    int _PC_INCR_2 = 2;
    /**
     * Increment the pc counter by 3.
     */
    int _PC_INCR_3 = 3;
    /**
     * Shift by 8.
     */
    int _SHIFT = 8;
    /**
     * Bitmask for unsigned logic in java.
     */
    int _UNSIGNED_MASK = 0xFF;
    /**
     * Perform the OpCodes task on the State machine.
     * @param state_ The State machine.
     */
    void exec(final State state_);
	int exec();
	
    
	void setAddressMode(AddressMode addressMode);
	AddressMode getAddressMode();
	int getAddressModeInt();
	
	int execDirect();
	int execImmediate();
	int execIndirect();
	int execIndirectAutoInc();
	int execShortIndexed();
	int execLongIndexed();


}
