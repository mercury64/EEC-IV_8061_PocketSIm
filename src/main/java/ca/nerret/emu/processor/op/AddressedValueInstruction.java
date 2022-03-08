package ca.nerret.emu.processor.op;

import ca.nerret.emu.emulator.OpCode;
import ca.nerret.emu.env.RoxByte;
import ca.nerret.emu.mem.Memory;
import ca.nerret.emu.processor.EEC8061_RALU;
import ca.nerret.emu.processor.Registers;

/**
 * An instruction that can be {@code perform}ed on an addressed {@link RoxByte}.
 */
@FunctionalInterface
interface AddressedValueInstruction {
    /**
     * Perform an operation, in a specified environment using a {@link RoxByte} addressed externally.
     *
     * @param alu The Arithmetic Logic Unit (ALU) associated with the desired environment
     * @param registers The Registers associated with the desired environment
     * @param memory The Memory associated with the desired environment
     * @param value The addressed value that the operation is performed on
     * @return The {@link RoxByte} result of performing the operation in the specified environment
     */
    RoxByte perform(
    				final EEC8061_RALU ralu,
                    final Registers registers,
                    final Memory memory,
                    final RoxByte value);

}
