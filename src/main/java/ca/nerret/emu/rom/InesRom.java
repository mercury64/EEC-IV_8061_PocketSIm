package ca.nerret.emu.rom;

import ca.nerret.emu.mem.ReadOnlyMemory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

/**
 * A representation of an iNES ROM file.
 *
 * @author Ross Drew
 */
public final class InesRom {
    private static final Logger log = LoggerFactory.getLogger(InesRom.class);

    private static byte [] PREFIX = new byte[] {'N', 'E', 'S', 0x1A};

    /** The predefined PRG ROM block size */
    public static final int PRG_ROM_BLOCK_SIZE = 16384;
    /** The predefined CHR ROM block size */
    public static final int CHR_ROM_BLOCK_SIZE = 8192;
    /** The size of of the trainer in bytes if present */
    public static final int TRAINER_SIZE = 512;

    private final InesRomHeader header;

    private final ReadOnlyMemory programRom;
    private final ReadOnlyMemory characterRom;
    private final ReadOnlyMemory trainerRom;

    private final byte[] footer;

    private InesRom(final InesRomHeader header,
                    final byte[] trainerRom,
                    final byte[] prgRom,
                    final byte[] chrRom,
                    final byte[] footer){
        this.header = header;

        this.programRom = new ReadOnlyMemory(prgRom);
        this.characterRom = new ReadOnlyMemory(chrRom);
        this.trainerRom = new ReadOnlyMemory(trainerRom);

        this.footer = footer;
    }

    /**
     * Generate an {@link InesRom} from the provided bytes
     */
    public static InesRom from(final byte[] bytes) {
        final InesRomHeader newHeader = processHeader(bytes);
        int offset = InesRomHeader.HEADER_SIZE;

        byte[] trainer = newHeader.getRomControlOptions().isTrainerPresent() ? extractBinaryData(bytes, TRAINER_SIZE, offset) : new byte[] {};
        offset += trainer.length;

        final byte[] program = extractBinaryData(bytes, newHeader.getPrgBlocks() * PRG_ROM_BLOCK_SIZE, offset);
        offset += program.length;

        byte[] character = {};
        if (newHeader.getChrBlocks() > 0) {
            character = extractBinaryData(bytes, newHeader.getChrBlocks() * CHR_ROM_BLOCK_SIZE, offset);
        } else {
            //CHR RAM
            log.error("CHR RAM Not implemented...");
        }
        offset += character.length;

        byte[] footer = new byte[] {};
        if (bytes.length > offset){
            footer = extractBinaryData(bytes, bytes.length-offset, offset);
        }

        return new InesRom(newHeader, trainer, program, character, footer);
    }

    private static byte[] extractBinaryData(final byte[] bytes, final int byteCount, final int offset) {
        return Arrays.copyOfRange(bytes, offset,offset+byteCount);
    }

    private static InesRomHeader processHeader(final byte[] bytes){
        if (bytes.length < InesRomHeader.HEADER_SIZE)
            throw new UnknownRomException("Invalid iNES header: Expected " + InesRomHeader.HEADER_SIZE + " byte header, rom is only " + bytes.length + " bytes.");

        for (int i=0; i<PREFIX.length; i++)
            if (bytes[i] != PREFIX[i]) throw new UnknownRomException("Invalid iNES header: iNES prefix missing.");

        int prgRomBlocks = bytes[4];
        int chrRomBlocks = bytes[5];
        RomControlOptions romControlOptions = new RomControlOptions(bytes[6], bytes[7]);

        return new InesRomHeader("NES ROM", prgRomBlocks, chrRomBlocks, romControlOptions);
    }

    public String getDescription() {
        return header.getDescription();
    }

    public InesRomHeader getHeader(){
        return header;
    }

    public ReadOnlyMemory getProgramRom(){
        return this.programRom;
    }

    public ReadOnlyMemory getCharacterRom(){
        return this.characterRom;
    }

    public ReadOnlyMemory getTrainerRom() {
        return this.trainerRom;
    }

    public byte[] getFooter() {
        return footer;
    }

    /**
     * Entry point for getting ROM information
     *
     * @param args <code>[0]</code> = Path to ROM file
     */
    public static void main(String[] args) throws IOException {
//        final String romPath = args[0];
//        System.out.println("Opening '" + romPath + "'");
//
//        final File romFile = new File(romPath);
//        byte[] romBytes = Files.readAllBytes(romFile.toPath());
//
//        final InesRom rom = InesRom.from(romBytes);
//
//        System.out.println("Opened " + rom.getDescription() + " (" + rom.getHeader().getDescription() + ")");
//        System.out.println("\tProgram Blocks: " + rom.getHeader().getPrgBlocks());
//        System.out.println("\tCharacter Blocks: " + rom.getHeader().getChrBlocks());
//        System.out.println("\tVersion: " + rom.getHeader().getRomControlOptions().getVersion());
//        System.out.println("\tMirroring: " + rom.getHeader().getRomControlOptions().getMirroring().name());
//        System.out.println("\tMapper Number: " + rom.getHeader().getRomControlOptions().getMapperNumber());
//        System.out.println("\tFooter: " + rom.getFooter().length + " bytes");
//        System.out.println("\t----------------");
//        System.out.println("\tTrainer: " + (rom.getHeader().getRomControlOptions().isTrainerPresent() ? "YES" : "NO"));
//        System.out.println("\tRAM: " + (rom.getHeader().getRomControlOptions().isRamPresent() ? "YES" : "NO"));
//        System.out.println("\tPlaychoice 10: " + (rom.getHeader().getRomControlOptions().isPlayChoice10() ? "YES" : "NO"));
//        System.out.println("\tVS Uni: " + (rom.getHeader().getRomControlOptions().isVsUnisystem() ? "YES" : "NO"));
    }
}
