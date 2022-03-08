package ca.nerret.emu.processor.dbg.ui.component;

import ca.nerret.emu.processor.Registers;
import ca.nerret.emu.processor.Registers.Register;
import ca.nerret.emu.processor.Registers.SFR_Register;
import ca.nerret.emu.processor.dbg.ui.component.FlagByteBox;

import javax.swing.*;
import java.awt.*;

public class SpecialFunctionRegistersPanel extends JPanel {

    private final ByteBox zeroHi;
    private final ByteBox zeroLo = new ByteBox("Zero Register (Lo)", 0);

    private final ByteBox xIndex = new ByteBox("X Index", 0);
    private final ByteBox yIndex = new ByteBox("Y Index", 0);

    private final ByteBox stackPointerHi = new ByteBox("Stack Pointer (Hi)", 0);
    private final ByteBox stackPointerLo = new ByteBox("Stack Pointer (Lo)", 0);

    private final ByteBox programCounterHi = new ByteBox("Program Counter (Hi)", 0, new Color(9, 178, 0));
    private final ByteBox programCounterLo = new ByteBox("Program Counter (Lo)", 0);

    private final FlagByteBox statusRegisterHi = new FlagByteBox("Program Status Word(PSW) (HI)", 0x0, "I1111111".toCharArray());
    private final FlagByteBox statusRegisterLo = new FlagByteBox("Program Status Word(PSW) (LO)", 0x0, "ZNVTCZzS".toCharArray());
 //private final FlagByteBox statusRegister = new FlagByteBox("Status Register", 0x0, "NV BDIZC".toCharArray());
	private Registers registers;

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        refreshValues();
    }

    public SpecialFunctionRegistersPanel(Registers registers) {
        this.registers = registers;

        this.zeroHi = new ByteBox(SFR_Register.ZERO_LO.getReadDescription(), 0);
        
        setLayout(new GridLayout(6,2));

        add(zeroHi);
        add(zeroLo);
        

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

        zeroHi.setValue(registers.getSFRRegister(Registers.SFR_Register.ZERO_HI).getRawValue());
        //xIndex.setValue(registers.getRegister(Registers.SFR_Register.X_INDEX).getRawValue());
        //yIndex.setValue(registers.getRegister(Registers.SFR_Register.Y_INDEX).getRawValue());
        stackPointerHi.setValue(0x01);
        //stackPointerLo.setValue(registers.getRegister(Registers.SFR_Register.STACK_POINTER_LOW).getRawValue());
       // programCounterHi.setValue(registers.getRegister(Registers.Register.PROGRAM_COUNTER_HI).getRawValue());
        //programCounterLo.setValue(registers.getRegister(Registers.Register.PROGRAM_COUNTER_LOW).getRawValue());

        //statusRegisterHi.setValue(registers.getRegister(Registers.SFR_Register.PSW_Flag).getRawValue());
        //statusRegisterLo.setValue(registers.getRegister(Registers.SFR_Register.STATUS_FLAGS).getRawValue());
    }
}
