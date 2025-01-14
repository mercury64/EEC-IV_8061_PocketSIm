

import ca.nerret.emu.UnknownOpCodeException;
import ca.nerret.emu.emulator.Calibration;
import ca.nerret.emu.env.RoxByte;
import ca.nerret.emu.env.RoxWord;
import ca.nerret.emu.mem.Memory;
import ca.nerret.emu.mem.SimpleMemory;
import ca.nerret.emu.processor.EEC8061;
import ca.nerret.emu.processor.Registers;
import ca.nerret.emu.processor.dbg.ui.component.MemoryPanel;
import ca.nerret.emu.processor.dbg.ui.component.Registers8061Panel;
import ca.nerret.emu.processor.dbg.ui.component.SpecialFunctionRegistersPanel;
import ca.nerret.emu.processor.op.EEC8061AddressingMode;
import ca.nerret.emu.processor.op.EEC8061OpCode;
import ca.nerret.emu.processor.util.EEC8061Compiler;
import ca.nerret.emu.processor.util.Program;
import ca.nerret.emu.rom.InesRom;

import javax.swing.*;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * A DebuggerWindow for debugging 6502 CPU code
 *
 * @author Ross Drew
 */
final class DebuggerWindow extends JFrame {
    private EEC8061 processor;
    private Memory memory;
    private Registers registers;

    private Registers8061Panel newRegisterPanel;
    private SpecialFunctionRegistersPanel sfrPanel;
    

    private String instructionName = "...";
    private final JLabel instruction = new JLabel(instructionName);

    private final DefaultListModel<String> listModel;

    private DebuggerWindow() {
        super("8061 EEC-IV Debugger");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        init();

        listModel = new DefaultListModel<>();
        instruction.setHorizontalAlignment(JLabel.CENTER);

        setLayout(new BorderLayout());
        add(instruction, BorderLayout.NORTH);
        add(getInstructionScroller(), BorderLayout.EAST);
        add(getControlPanel(), BorderLayout.SOUTH);
        add(getMemoryPanel(), BorderLayout.WEST);
        add(getCenterPanel(), BorderLayout.CENTER);

        loadProgram(getProgram());
        setVisible(true);
        pack();
    }

    private JComponent getCenterPanel(){
        JTabbedPane centerPane = new JTabbedPane();

        centerPane.add("Registers", newRegisterPanel);
        centerPane.add("Code", getCodeInput());
        centerPane.add("SFR", sfrPanel);

        return centerPane;
    }

    private JComponent getCodeInput(){
        final JTextPane codeArea = getCodeArea();
        final JScrollPane codeScroller = new JScrollPane(codeArea);
        final JButton compilerButton = new JButton("Compile");
        compilerButton.addActionListener(e -> compile(codeArea.getText()));

        final JPanel codePanel = new JPanel();
        codePanel.setLayout(new BorderLayout());
        codePanel.add(codeScroller, BorderLayout.CENTER);
        codePanel.add(compilerButton, BorderLayout.SOUTH);

        return codePanel;
    }

    private JTextPane getCodeArea(){
        final StyleContext sc = new StyleContext();
        final DefaultStyledDocument doc = new DefaultStyledDocument(sc);

        final JTextPane codeArea = new JTextPane(doc);
        codeArea.setBackground(new Color(0x25401C));
        codeArea.setCaretColor(new Color(0xD1E8CE));

        final Style bodyStyle = sc.addStyle("body", null);
        bodyStyle.addAttribute(StyleConstants.Foreground, new Color(0x789C6C));
        bodyStyle.addAttribute(StyleConstants.FontSize, 13);
        bodyStyle.addAttribute(StyleConstants.FontFamily, "monospaced");
        bodyStyle.addAttribute(StyleConstants.Bold, true);

        doc.setLogicalStyle(0, bodyStyle);

        return codeArea;
    }

    private JComponent getMemoryPanel(){
//        JPanel memoryPanel = new JPanel();
//        memoryPanel.setLayout(new BorderLayout());
//
//        final Map<String, Component> memoryComponents = getMemoryComponents();
//
//        final Object[] objects = memoryComponents.keySet().toArray();
//        final String[] memoryStrings = new String[objects.length];
//        for (int i=0; i<objects.length; i++){
//            memoryStrings[i] = (String)objects[i];
//        }
//        JComboBox petList = new JComboBox(memoryStrings);
//
//        memoryPanel.add(petList, BorderLayout.NORTH);
//        memoryPanel.add(memoryComponents.get("1"));
//
//        return memoryPanel;

        JTabbedPane memoryTabs = new JTabbedPane();

        final Map<String, Component> memoryComponentBlocks = getMemoryComponents();
        for (String memoryKey : memoryComponentBlocks.keySet()) {
            memoryTabs.addTab(memoryKey, memoryComponentBlocks.get(memoryKey));
        }

        return memoryTabs;
    }

    private Map<String, Component> getMemoryComponents(){
        final Map<String, Component> memoryBlocks = new LinkedHashMap<>();

        final String[] blocks2 = new String[260];
        for (int i=0; i<260; i++){
            int start = i * 256;
            blocks2[i] = ""+ i;//(i + " (" + start + "-" + (start + 255) + ")");
        }

        for (int i=0; i<7; i++)
//        for (int i=0; i<blocks2.length; i++)
            memoryBlocks.put(blocks2[i], getMemoryComponent(i * 256));

        return memoryBlocks;
    }

    private Component getMemoryComponent(int fromMemoryAddress){
        final MemoryPanel memoryPanel = new MemoryPanel();
        final JScrollPane scrollPane = new JScrollPane();

        memoryPanel.setMemory(memory, fromMemoryAddress);
        memoryPanel.linkTo(processor.getRegisters());

        scrollPane.setViewportView(memoryPanel);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        return scrollPane;
    }

    private JComponent getInstructionScroller(){
        final JList<String> instructionList = new JList<>(listModel);
        final JScrollPane scrollPane = new JScrollPane(instructionList);
        scrollPane.setPreferredSize(new Dimension(300, 200));
        return scrollPane;
    }

    private JPanel getControlPanel() {
        JButton stepButton = new JButton("Step >>");
        stepButton.addActionListener(e -> step());

        JButton resetButton = new JButton("Reset!");
        resetButton.addActionListener(e -> loadProgram(getProgramFromFile()));

        JButton eecButton = new JButton("Load EEC .bin");
        eecButton.addActionListener(e -> loadProgram(getEECProgram()));

        JPanel controls = new JPanel();
        controls.setLayout(new FlowLayout());
        controls.add(eecButton);
        controls.add(resetButton);
        controls.add(stepButton);

        return controls;
    }

    private RoxByte[] getEECProgram()
    {
    	Calibration KID2 = new Calibration("KID2/KID2.bin");
    	int[] intArray = KID2.readFile();
    	RoxByte[] roxByteArray = RoxByte.fromIntArray(intArray);
    	
    	return roxByteArray;
    }
    private RoxByte[] getProgramFromFile() {
        final File file = new File( "src" +  File.separator + "main" +  File.separator + "resources" + File.separator + "rom" + File.separator + "SMB1.NES");

        System.out.println("Loading '" + file.getAbsolutePath() + "'...");

        final FileInputStream fis;
        byte fileContent[] = {};
        try {
            fis = new FileInputStream(file);
            fileContent= new byte[(int)file.length()];
            fis.read(fileContent);
        } catch (IOException e ) {
            e.printStackTrace();
        }

        final InesRom rom = InesRom.from(fileContent);

        Memory prgRom = rom.getProgramRom();
        return prgRom.getBlock(RoxWord.ZERO, RoxWord.fromLiteral(prgRom.getSize()-1));
    }

    private RoxByte[] getProgram(){
        int data_offset = 0x32;
        int MPD = data_offset + 0x10;
        int MPR = data_offset + 0x11;
        int TMP = data_offset + 0x20;
        int RESAD_0 = data_offset + 0x30;
        int RESAD_1 = data_offset + 0x31;

        int valMPD = 7;
        int valMPR = 4;

//        Program countToTenProgram = new Program().with( OpCode.LDX_I, 10,
//                                                        OpCode.LDA_I, 0,
//                                                        OpCode.CLC,
//                                                        OpCode.ADC_I, 0x01,
//                                                        OpCode.DEX,
//                                                        OpCode.CPX_I, 0,
//                                                        OpCode.BNE, 0b11110111);

        Program multiplicationProgram = new Program().with( 
        		EEC8061OpCode.LDA_I, valMPD,
        		EEC8061OpCode.STA_Z, MPD,
        		EEC8061OpCode.LDA_I, valMPR,
        		EEC8061OpCode.STA_Z, MPR,
        		EEC8061OpCode.LDA_I, 0,         //<---- start
        		EEC8061OpCode.STA_Z, TMP,       //Clear
        		EEC8061OpCode.STA_Z, RESAD_0,   //...
        		EEC8061OpCode.STA_Z, RESAD_1,   //...
        		EEC8061OpCode.LDX_I, 8,         //X counts each bit
                                                //:MULT(18)
        		EEC8061OpCode.LSR_Z, MPR,       //LSR(MPR)
        		EEC8061OpCode.BCC, 13,          //Test carry and jump (forward 13) to NOADD

        		EEC8061OpCode.LDA_Z, RESAD_0,   //RESAD -> A
        		EEC8061OpCode.CLC,              //Prepare to add
        		EEC8061OpCode.ADC_Z, MPD,       //+MPD
        		EEC8061OpCode.STA_Z, RESAD_0,   //Save result
        		EEC8061OpCode.LDA_Z, RESAD_1,   //RESAD+1 -> A
        		EEC8061OpCode.ADC_Z, TMP,       //+TMP
        		EEC8061OpCode.STA_Z, RESAD_1,   //RESAD+1 <- A
                                                //:NOADD(35)
        		EEC8061OpCode.ASL_Z, MPD,       //ASL(MPD)
        		EEC8061OpCode.ROL_Z, TMP,       //Save bit from MPD
        		EEC8061OpCode.DEX,              //--X
        		EEC8061OpCode.BNE, 0b11100111   //Test equal and jump (back 24) to MULT
        );

        return multiplicationProgram.getProgramAsByteArray();
    }

    private void init(){
        registers = new Registers();
        memory = new SimpleMemory();
        processor = new EEC8061(memory, registers);

        newRegisterPanel = new Registers8061Panel(registers);
        sfrPanel = new SpecialFunctionRegistersPanel(registers);
    }

    public void loadProgram(RoxByte[] program){
        reset();
        memory.setBlock(RoxWord.ZERO, program);
    }

    public void reset(){
        processor.reset();
        memory.reset();
        listModel.clear();

        invalidate();
        revalidate();
        repaint();
    }

    public void step(){
        upDateWithNextInstruction();

        processor.step();
        invalidate();
        repaint();
    }

    public void compile(String programText){
        final EEC8061Compiler compiler = new EEC8061Compiler(programText);
        try {
            final Program program = compiler.compileProgram();
            final RoxByte[] programAsByteArray = program.getProgramAsByteArray();
            loadProgram(programAsByteArray);
        }catch (UnknownOpCodeException e){
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void upDateWithNextInstruction() {
        final Registers registers = processor.getRegisters();
        final RoxWord pointer = processor.getPC();
        final RoxByte instr = memory.getByte(pointer);

        StringBuilder arguments = new StringBuilder();
        for (int i=0; i<getArgumentCount(instr.getRawValue()); i++ ){
            RoxWord n = RoxWord.fromLiteral(pointer.getRawValue() + (i+1));
            arguments.append(" " + MemoryPanel.asHex(memory.getByte(n).getRawValue()));
        }

        instructionName = EEC8061OpCode.from(instr.getRawValue()).toString();
        final String instructionLocation = MemoryPanel.asHex(pointer.getRawValue());
        final String instructionCode = MemoryPanel.asHex(instr.getRawValue());
        final String completeInstructionInfo = "[" + instructionLocation + "] (" + instructionCode + arguments.toString() + ") :" + instructionName;

        instruction.setText(completeInstructionInfo);
        listModel.add(0, completeInstructionInfo);
    }

    private int getArgumentCount(int instr) {
        final EEC8061OpCode opCode = EEC8061OpCode.from(instr);
        final EEC8061AddressingMode addressingMode = opCode.getAddressingMode();
        return addressingMode.getInstructionBytes() - 1;
    }

    public static void main(String[] args){
        new DebuggerWindow();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(1200, 500);
    }
}
