package ca.nerret.emu.processor.op.util;

import ca.nerret.emu.UnknownOpCodeException;
import ca.nerret.emu.processor.EEC8061;
import ca.nerret.emu.processor.op.EEC8061AddressingMode;
import ca.nerret.emu.processor.op.EEC8061OpCode;
import ca.nerret.emu.processor.op.EEC8061Operation;

import java.util.EnumSet;

import static ca.nerret.emu.processor.op.EEC8061Operation.*;

/**
 * Utility for converting internal {@link Mos6502} {@link Mos6502OpCode}
 * representation names to human readable descriptions
 *
 * e.g. <code>ADC_I</code> &rarr; "<em>ADC (Immediate)</em>"
 *
 * @author Ross Drew
 */
public class OpCodeConverter {
	/**
	 * The separator used to delimit different elements in the {@link String} enum
	 * id
	 */
	public static final String TOKEN_SEPARATOR = "_";
	/**
	 * The index of the op-code name in the {@link String} enum id, using the token
	 * delimiter {@value TOKEN_SEPARATOR}
	 */
	public static final int CODE_I = 0;
	/**
	 * The index of the addressing mode token in the {@link String} enum id, using
	 * the token delimiter {@value TOKEN_SEPARATOR}
	 */
	public static final int ADDR_I = CODE_I + 1;
	/**
	 * The index of the indexing mode token in the {@link String} enum id, using the
	 * token delimiter {@value TOKEN_SEPARATOR}
	 */
	public static final int INDX_I = ADDR_I + 1;

	private OpCodeConverter() {
		/* Used to hide implicitly public constructor for a utility class */}

	/**
	 * Extract a command line op-code of the {@link Mos6502} instruction set from
	 * the {@link String} representation of an {@link Mos6502OpCode}.
	 *
	 * @param internalOpCodeName the {@link String} representation of an
	 *                           {@link Mos6502OpCode}
	 * @return A {@link String} instruction from the {@link Mos6502} instruction
	 *         set, associated with the intended {@link Mos6502OpCode}
	 */
	public static String getOpCode(String internalOpCodeName) {
		final String[] tokens = internalOpCodeName.split(TOKEN_SEPARATOR);
		return tokens[CODE_I];
	}

	/**
	 * @param internalOpCodeName {@link String} representation of an
	 *                           {@link Mos6502OpCode}
	 * @return the {@link Mos6502Operation} associated with this
	 *         {@link Mos6502OpCode}
	 */
	public static EEC8061Operation getOperation(final String internalOpCodeName) {
		return valueOf(getOpCode(internalOpCodeName));
	}

	/**
	 * Extract the {@link Mos6502} {@link Mos6502AddressingMode} from the
	 * {@link String} representation of an {@link Mos6502OpCode}.
	 *
	 * @param internalOpCodeName the {@link String} representation of an
	 *                           {@link Mos6502OpCode}
	 * @return An {@link Mos6502AddressingMode} object that represents the intended
	 *         addressing mode of the {@link Mos6502OpCode} in question
	 */
	public static EEC8061AddressingMode getAddressingMode(String internalOpCodeName) {
		final String[] tokens = internalOpCodeName.split(TOKEN_SEPARATOR);
		if (tokens.length <= ADDR_I) {
			// XXX Write this less ugly
			if (EnumSet.of(JSR, BPL, BMI, BVC, BVS, BCC, BCS, BNE, BEQ).contains(getOperation(internalOpCodeName)))
				return EEC8061AddressingMode.RELATIVE;

			return EEC8061AddressingMode.IMPLIED;
		}

		final String addressingModeDescriptor = tokens[ADDR_I];

		// XXX Not pretty but necessary for proper test coverage - update if JaCoCo ever
		// learns to deal with it
		final String indexToken = (tokens.length <= INDX_I) ? "" : tokens[INDX_I];
		
		
		if ("I".equals(addressingModeDescriptor))
			return EEC8061AddressingMode.IMMEDIATE;
		else if ("A".equals(addressingModeDescriptor))
			return EEC8061AddressingMode.ACCUMULATOR;
		else if ("Z".equals(addressingModeDescriptor))
			return withIndexing(EEC8061AddressingMode.ZERO_PAGE, indexToken);
		else if ("ABS".equals(addressingModeDescriptor))
			return withIndexing(EEC8061AddressingMode.ABSOLUTE, indexToken);
		else if ("IND".equals(addressingModeDescriptor))
			return withIndexing(EEC8061AddressingMode.INDIRECT, indexToken);
		else if ("D".equals(addressingModeDescriptor))
			return withIndexing(EEC8061AddressingMode.DIRECT, indexToken);
		else if ("R".equals(addressingModeDescriptor))
			return withIndexing(EEC8061AddressingMode.RELATIVE, indexToken);
		else
			throw new UnknownOpCodeException("Unrecognised addressing mode " + addressingModeDescriptor,
					internalOpCodeName);
	}
	
	private static EEC8061AddressingMode assemblerFormat(final EEC8061AddressingMode addressingMode,
			final String indexToken) {
		if ("IX".equalsIgnoreCase(indexToken))
			return addressingMode.xIndexed();
		else if ("IY".equalsIgnoreCase(indexToken))
			return addressingMode.yIndexed();
		else
			return addressingMode;
	}

	private static EEC8061AddressingMode withIndexing(final EEC8061AddressingMode addressingMode,
			final String indexToken) {
		if ("IX".equalsIgnoreCase(indexToken))
			return addressingMode.xIndexed();
		else if ("IY".equalsIgnoreCase(indexToken))
			return addressingMode.yIndexed();
		else
			return addressingMode;
	}
}
