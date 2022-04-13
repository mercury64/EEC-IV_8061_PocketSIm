package ca.nerret.emu.processor;

import ca.nerret.emu.emulator.OpcodeCache;
import ca.nerret.emu.emulator.opcodes.IOpCode;
import ca.nerret.emu.env.RoxByte;
import ca.nerret.emu.env.RoxWord;
import ca.nerret.emu.mem.Memory;
import ca.nerret.emu.processor.op.EEC8061OpCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static ca.nerret.emu.processor.Registers.*;

/**
 * A emulated representation of MOS 6502, 8 bit
 * microprocessor functionality.
 *
 * XXX: At this point, we are only emulating the NES custom version of the 6502
 *
 * @author Ross Drew
 */
public class EEC8061 {
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final Memory memory;
    private final Registers registers;
    private final EEC_8061_ALU alu;
    private final EEC8061_RALU ralu;

    public EEC8061(final Memory memory, final Registers registers) {
        this.memory = memory;
        this.registers = registers;
        this.alu = new EEC_8061_ALU(registers);
        this.ralu = new EEC8061_RALU(alu);
    }

    /**
     * Reset the CPU; akin to firing the Reset pin on a 6502.<br/>
     * <br/>
     * This will
     * <ul>
     *     <li>Set Accumulator &rarr; <code>0</code></li>
     *     <li>Set Indexes &rarr; <code>0</code></li>
     *     <li>Status register &rarr; <code>0x34</code></li>
     *     <li>Set PC to the values at <code>0xFFFC</code> and <code>0xFFFD</code></li>
     *     <li>Reset Stack Pointer &rarr; 0xFF</li>
     * </ul>
     * <br/>
     * Note: IRL this takes 6 CPU cycles but we'll cross that bridge IF we come to it-
     */
    public void reset(){
       log.debug("RESETTING...");
       registers.setRegister(Register.ACCUMULATOR, RoxByte.ZERO);
       registers.setRegister(Register.X_INDEX, RoxByte.ZERO);
       registers.setRegister(Register.Y_INDEX, RoxByte.ZERO);
       registers.setRegister(Register.STATUS_FLAGS, RoxByte.fromLiteral(0x34));

       this.ralu.setProgramCounter(RoxWord.fromLiteral(0x2000));
       
       registers.setRegister(Register.STACK_POINTER_LOW, RoxByte.fromLiteral(0xFF)); 
       log.debug("...READY!");
    }

    /**
     * Fire an <b>I</b>nterrupt <b>R</b>e<b>Q</b>uest; akin to setting the IRQ pin on a 6502.<br/>
     * <br>
     * This will stash the PC and Status registers and set the Program Counter to the values at
     * <code>0xFFFE</code> and <code>0xFFFF</code> where the <b>I</b>nterrupt <b>S</b>ervice
     * <b>R</b>outine is expected to be
     */
    public void irq() {
        log.debug("IRQ!");
        registers.setFlag(Flag.IRQ_DISABLE);

       
        pushRegister(Register.STATUS_FLAGS);

    }

    /**
     * Fire a <b>N</b>on <b>M</b>askable <b>I</b>nterrupt; akin to setting the NMI pin on a 6502.<br/>
     * <br>
     * This will stash the PC and Status registers and set the Program Counter to the values at <code>0xFFFA</code>
     * and <code>0xFFFB</code> where the <b>I</b>nterrupt <b>S</b>ervice <b>R</b>outine is expected to be
     */
    public void nmi() {
        log.debug("NMI!");
        registers.setFlag(Flag.IRQ_DISABLE);

        pushRegister(Register.STATUS_FLAGS);

    }

    /**
     * @return the {@link Registers} being used
     */
    public Registers getRegisters(){
        return registers;
    }

    /**
     * Execute the next program instruction as per {@link Registers#getNextProgramCounter()}
     *
     * @param steps number of instructions to execute
     */
    public void step(int steps){
        for (int i=0; i<steps; i++)
            step();
    }

    /**
     * Execute the next program instruction as per {@link Registers#getNextProgramCounter()}
     */
    public void step() {
        log.debug("STEP >>>");

      
        int programRaw = nextProgramByte().getRawValue();
        
        final EEC8061OpCode opCode = EEC8061OpCode.from(programRaw);

		IOpCode opcode = OpcodeCache.get(programRaw);

			System.out.println(opcode);
		//	System.exit(1);

        //Execute the opcode
        log.debug("Instruction: {}...", opCode.getOpCodeName());
        switch (opCode){
            case JMP_ABS: //this is hard to deal with using my functional enums approach
                ralu.setProgramCounter(nextProgramWord());
            break;

            case JMP_IND: //this is hard to deal with using my functional enums approach
            	ralu.setProgramCounter(getWordOfMemoryAt(nextProgramWord()));
            break;

            default:
                opCode.perform(ralu, registers, memory);
                break;
        }
    }

    private RoxByte getRegisterValue(Register registerID){
        return registers.getRegister(registerID);
    }

    /**
     * Return the next byte from program memory, as defined
     * by the Program Counter.<br/>
     * <br/>
     * <em>Increments the Program Counter by 1</em>
     *
     * @return byte {@code from mem[ PC[0] ]}
     */
    private RoxByte nextProgramByte(){
       return getByteOfMemoryAt(ralu.getAndStepProgramCounter());
    }

    /**
     * Combine the next two bytes in program memory, as defined by
     * the Program Counter into a word so that:-><br/>
     * <br/>
     * PC[0] = high order byte<br/>
     * PC[1] = low order byte<br/>
     *<br/><br/>
     * <em>Increments the Program Counter by 1</em>
     *
     * @return word made up of both bytes
     */
    private RoxWord nextProgramWord(){
       return RoxWord.from(nextProgramByte(), nextProgramByte());
    }

    private void pushRegister(Register registerID){
        push(getRegisterValue(registerID));
    }

    /**
     * @param value {@link RoxByte} to push to the stack
     */
    private void push(RoxByte value){
        log.debug("PUSH {}(0b{}) to mem[0x{}]", value.toString(),
                                                Integer.toBinaryString(value.getRawValue()),
                                                Integer.toHexString(getRegisterValue(Register.STACK_POINTER_LOW).getRawValue()).toUpperCase());

       setByteOfMemoryAt(RoxWord.from(RoxByte.fromLiteral(0x01), getRegisterValue(Register.STACK_POINTER_LOW)), value);
       registers.setRegister(Register.STACK_POINTER_LOW, RoxByte.fromLiteral(getRegisterValue(Register.STACK_POINTER_LOW).getRawValue() - 1));
    }

    private RoxByte getByteOfMemoryAt(RoxWord location){
       final RoxByte memoryByte = memory.getByte(RoxWord.fromLiteral(location.getRawValue()));
       log.debug("Got 0x{} from mem[{}]", Integer.toHexString(memoryByte.getRawValue()), location.getRawValue());
       return memoryByte;
    }

    private void setByteOfMemoryAt(RoxWord location, RoxByte newByte){
       memory.setByteAt(RoxWord.fromLiteral(location.getRawValue()), newByte);
       log.debug("Stored 0x{} at mem[{}]", Integer.toHexString(newByte.getRawValue()), location.getRawValue());
    }

    private RoxWord getWordOfMemoryAt(RoxWord location) {
       final RoxWord memoryWord = memory.getWord(location);
       log.debug("Got 0x{} from mem[{}]", Integer.toHexString(memoryWord.getRawValue()), location.toString());
       return memoryWord;
    }

	public RoxWord getPC() {
		// TODO Auto-generated method stub
		return this.ralu.getProgramCounter();
	}
}
