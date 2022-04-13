package ca.nerret.emu.processor.op;

import ca.nerret.emu.mem.Memory;
import ca.nerret.emu.processor.EEC8061_RALU;
import ca.nerret.emu.processor.Registers;

/**
 * A {@link com.rox.emu.processor.mos6502.Mos6502} instruction which can be {@code perform}ed
 * in a given {@link com.rox.emu.processor.mos6502.Mos6502} environment.
 */
@FunctionalInterface
interface EEC8061Instruction {
    /**
     * Perform this operation in the specified environment
     *
     * @param alu
     * @param registers
     * @param memory
     */
    void perform(final EEC8061_RALU alu,
                 final Registers registers,
                 final Memory memory);
}
