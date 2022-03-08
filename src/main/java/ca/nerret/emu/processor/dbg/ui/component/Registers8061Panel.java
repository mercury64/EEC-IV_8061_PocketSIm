package ca.nerret.emu.processor.dbg.ui.component;

import ca.nerret.emu.processor.Registers;
import ca.nerret.emu.processor.dbg.ui.component.FlagByteBox;

import javax.swing.*;
import java.awt.*;

public class Registers8061Panel extends JPanel {
    private final Registers registers;

    private final ByteBox accumulator = new ByteBox("Accumulator", 0);

    private final ByteBox xIndex = new ByteBox("X Index", 0);
    private final ByteBox yIndex = new ByteBox("Y Index", 0);

    private final ByteBox stackPointerHi = new ByteBox("Stack Pointer (Hi)", 0);
    private final ByteBox stackPointerLo = new ByteBox("Stack Pointer (Lo)", 0);

    private final ByteBox programCounterHi = new ByteBox("Program Counter (Hi)", 0, new Color(9, 178, 0));
    private final ByteBox programCounterLo = new ByteBox("Program Counter (Lo)", 0);

    private final FlagByteBox statusRegisterHi = new FlagByteBox("Program Status Word(PSW) (HI)", 0x0, "I1111111".toCharArray());
    private final FlagByteBox statusRegisterLo = new FlagByteBox("Program Status Word(PSW) (LO)", 0x0, "ZNVTCZzS".toCharArray());
 //private final FlagByteBox statusRegister = new FlagByteBox("Status Register", 0x0, "NV BDIZC".toCharArray());

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        refreshValues();
    }

    public Registers8061Panel(Registers registers) {
        this.registers = registers;

        setLayout(new GridLayout(6,2));

        add(Box.createHorizontalGlue());
        add(accumulator);

        add(Box.createHorizontalGlue());
        add(xIndex);

        add(Box.createHorizontalGlue());
        add(yIndex);

        add(stackPointerHi);
        add(stackPointerLo);

        add(programCounterHi);
        add(programCounterLo);

        add(statusRegisterHi);
        add(statusRegisterLo);

        refreshValues();
    }

    private void refreshValues() {
        if (registers == null)
            return;

        accumulator.setValue(registers.getRegister(Registers.Register.ACCUMULATOR).getRawValue());
        xIndex.setValue(registers.getRegister(Registers.Register.X_INDEX).getRawValue());
        yIndex.setValue(registers.getRegister(Registers.Register.Y_INDEX).getRawValue());
        stackPointerHi.setValue(0x01);
        stackPointerLo.setValue(registers.getRegister(Registers.Register.STACK_POINTER_LOW).getRawValue());
       // programCounterHi.setValue(registers.getRegister(Registers.Register.PROGRAM_COUNTER_HI).getRawValue());
        //programCounterLo.setValue(registers.getRegister(Registers.Register.PROGRAM_COUNTER_LOW).getRawValue());

        statusRegisterHi.setValue(registers.getRegister(Registers.Register.PSW_Flag).getRawValue());
        statusRegisterLo.setValue(registers.getRegister(Registers.Register.STATUS_FLAGS).getRawValue());
    }
}
