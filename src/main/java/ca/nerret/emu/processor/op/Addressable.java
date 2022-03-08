package ca.nerret.emu.processor.op;

import ca.nerret.emu.env.RoxByte;
import ca.nerret.emu.mem.Memory;
import ca.nerret.emu.processor.EEC8061_RALU;
import ca.nerret.emu.processor.Registers;

/**
 * A wrapper for an {@link AddressedValueInstruction} for addressing the {@link RoxByte} in it's argument to make it "Addressable"<br/>
 *<br/>
 * XXX Perhaps a better name is required?
 */
@FunctionalInterface
interface Addressable {
    /**
     * Address a value in the provided environment and use it in a {@link AddressedValueInstruction}
     *
     * @param r The Registers associated with the desired environment
     * @param m The Memory associated with the desired environment
     * @param alu The Arithmetic Logic Unit (ALU) associated with the desired environment
     * @param i The {@link AddressedValueInstruction} called using the addressed value
     */
    void address(final Registers r,
                 final Memory m,
                 final EEC8061_RALU alu,
                 final AddressedValueInstruction i);
}
