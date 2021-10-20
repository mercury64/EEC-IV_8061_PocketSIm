package ca.nerret.emu.emulator.cpu.ralu;

import ca.nerret.emu.emulator.cpu.Register;

/**
 * 5-3.4.1 ACCUMULATOR
 * The 16-bitaccumulatorIsa non-addressableregisterusedtoholdanoperand atthebeginningof a RALU operation. 
 * The upper byte,lower byte,orwhole accumulatorissometimes loaded with data priortotheoperation.
 * Thesignofthelowerbyteintheaccumulatorcan beextendedtotheupper byte. 
 * The contents ofthe accumulator can be shifted rightor leftone position during each processor clockcycle.
 * Theaccumulatorcanbeconcatenated withthemultiplyregisterastheupperhalfof a32-bitwordforshiftoperations.
 *  Atthebeginningofa multiplication,theaccumulatoriscleared tozero,and atthe end ofthe multiplication,itcontainsthe upper halfofthe product. 
 *  Atthe beginning of a divide operation, the accumulator contains the upper half of the dividend, and at the end of the division,itcontainstheremainder. 
 *  The bits in the low byte of the accumulator can be tested fora "1"or"0"forthe"Jump IfBitn= 1"instruction,"Jump ifBitn^ 1"instruction,orforcon ditional branch instructions.

 * @author wwhite
 *
 */
public class Accumulator extends Register {


	short accumulatorRegister;
	short multiplyRegister;
	
	void shiftRight() {
	}
	void shiftLeft() {
	}
}
