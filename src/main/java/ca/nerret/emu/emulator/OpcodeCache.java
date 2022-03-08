package ca.nerret.emu.emulator;

import java.util.HashMap;
import java.util.Map;

import ca.nerret.emu.emulator.opcodes.IOpCode;
import ca.nerret.emu.emulator.opcodes.OpCode06;
import ca.nerret.emu.emulator.opcodes.OpCodeCLRB;
import ca.nerret.emu.emulator.opcodes.OpCodeCLRW;
import ca.nerret.emu.emulator.opcodes.OpCodeCMPB;
import ca.nerret.emu.emulator.opcodes.OpCodeCMPW;
import ca.nerret.emu.emulator.opcodes.OpCodeCPLB;
import ca.nerret.emu.emulator.opcodes.OpCodeCPLW;
import ca.nerret.emu.emulator.opcodes.OpCodeSCALL;
import ca.nerret.emu.emulator.opcodes.OpCodeSJMP;
import ca.nerret.emu.emulator.opcodes.OpCodeSTB;
import ca.nerret.emu.emulator.opcodes.OpCodeSTW;
import ca.nerret.emu.emulator.opcodes.OpCode31;
import ca.nerret.emu.emulator.opcodes.OpCodeAD2B;
import ca.nerret.emu.emulator.opcodes.OpCodeAD2W;
import ca.nerret.emu.emulator.opcodes.OpCodeAD3W;
import ca.nerret.emu.emulator.opcodes.OpCodeAN2B;
import ca.nerret.emu.emulator.opcodes.OpCodeAN3B;
import ca.nerret.emu.emulator.opcodes.OpCodeC3;
import ca.nerret.emu.emulator.opcodes.OpCodeCALL;
import ca.nerret.emu.emulator.opcodes.OpCodeCD;
import ca.nerret.emu.emulator.opcodes.OpCodeDI;
import ca.nerret.emu.emulator.opcodes.OpCodeDIVW;
import ca.nerret.emu.emulator.opcodes.OpCodeDJNZ;
import ca.nerret.emu.emulator.opcodes.OpCodeEI;
import ca.nerret.emu.emulator.opcodes.OpCodeSKP;
import ca.nerret.emu.emulator.opcodes.OpCodeINCB;
import ca.nerret.emu.emulator.opcodes.OpCodeINCW;
import ca.nerret.emu.emulator.opcodes.OpCodeJB;
import ca.nerret.emu.emulator.opcodes.OpCodeJC;
import ca.nerret.emu.emulator.opcodes.OpCodeJE;
import ca.nerret.emu.emulator.opcodes.OpCodeJGTU;
import ca.nerret.emu.emulator.opcodes.OpCodeJLEU;
import ca.nerret.emu.emulator.opcodes.OpCodeJNB;
import ca.nerret.emu.emulator.opcodes.OpCodeJNC;
import ca.nerret.emu.emulator.opcodes.OpCodeJNE;
import ca.nerret.emu.emulator.opcodes.OpCodeJUMP;
import ca.nerret.emu.emulator.opcodes.OpCodeLDB;
import ca.nerret.emu.emulator.opcodes.OpCodeLDW;
import ca.nerret.emu.emulator.opcodes.OpCodeLDZBW;
import ca.nerret.emu.emulator.opcodes.OpCodeML2B;
import ca.nerret.emu.emulator.opcodes.OpCodeML2W;
import ca.nerret.emu.emulator.opcodes.OpCodeNEGW;
import ca.nerret.emu.emulator.opcodes.OpCodeNOP;
import ca.nerret.emu.emulator.opcodes.OpCodeORRB;
import ca.nerret.emu.emulator.opcodes.OpCodePOPW;
import ca.nerret.emu.emulator.opcodes.OpCodePUSHP;
import ca.nerret.emu.emulator.opcodes.OpCodePUSHW;
import ca.nerret.emu.emulator.opcodes.OpCodeRET;
import ca.nerret.emu.emulator.opcodes.OpCodeSB2B;
import ca.nerret.emu.emulator.opcodes.OpCodeSB2W;
import ca.nerret.emu.emulator.opcodes.OpCodeSB3W;
import ca.nerret.emu.emulator.opcodes.OpCodeSHLDW;

public final class OpcodeCache {

    private static final Map<Byte, OpCode> _OPCODES = new HashMap<>();
    
    static {
        
        _OPCODES.put((byte) 0x00, new OpCodeSKP(0x00, "SKP"));

        _OPCODES.put((byte) 0x03, new OpCodeNEGW(0x03, "NEGW"));

        _OPCODES.put((byte) 0x02, new OpCodeCPLW(0x02, "CPLW"));
        _OPCODES.put((byte) 0x12, new OpCodeCPLB(0x12, "CPLB"));
        
        _OPCODES.put((byte) 0x11, new OpCodeCLRB(0x11, "CLRB"));
        _OPCODES.put((byte) 0x01, new OpCodeCLRW(0x01, "CLRW"));
        
        _OPCODES.put((byte) 0xE7, new OpCodeJUMP(0xe7, "JUMP"));
        
        _OPCODES.put((byte) 0x0D, new OpCodeSHLDW(0x0d, "SHLDW"));
        
        _OPCODES.put((byte) 0x20, new OpCodeSJMP(0x20, "SJMP"));
        _OPCODES.put((byte) 0x21, new OpCodeSJMP(0x21, "SJMP"));
        _OPCODES.put((byte) 0x22, new OpCodeSJMP(0x22, "SJMP"));
        _OPCODES.put((byte) 0x23, new OpCodeSJMP(0x23, "SJMP"));
        _OPCODES.put((byte) 0x24, new OpCodeSJMP(0x24, "SJMP"));
        _OPCODES.put((byte) 0x25, new OpCodeSJMP(0x25, "SJMP"));
        _OPCODES.put((byte) 0x26, new OpCodeSJMP(0x26, "SJMP"));
        _OPCODES.put((byte) 0x27, new OpCodeSJMP(0x27, "SJMP"));
        
        _OPCODES.put((byte) 0x28, new OpCodeSCALL(0x28, "SCALL"));
        _OPCODES.put((byte) 0x29, new OpCodeSCALL(0x29, "SCALL"));
        _OPCODES.put((byte) 0x2a, new OpCodeSCALL(0x2a, "SCALL"));
        _OPCODES.put((byte) 0x2b, new OpCodeSCALL(0x2b, "SCALL"));
        _OPCODES.put((byte) 0x2c, new OpCodeSCALL(0x2c, "SCALL"));
        _OPCODES.put((byte) 0x2d, new OpCodeSCALL(0x2d, "SCALL"));
        _OPCODES.put((byte) 0x2e, new OpCodeSCALL(0x2e, "SCALL"));
        _OPCODES.put((byte) 0x2f, new OpCodeSCALL(0x2f, "SCALL"));

        _OPCODES.put((byte) 0xEF, new OpCodeCALL(0xef, "CALL"));
        _OPCODES.put((byte) 0xF0, new OpCodeRET(0xf0, "RET"));

        _OPCODES.put((byte) 0x07, new OpCodeINCW(0x07, "INCW"));
        _OPCODES.put((byte) 0x17, new OpCodeINCB(0x17, "INCB"));
        
        _OPCODES.put((byte) 0xFA, new OpCodeDI(0xfa, "DI"));
        _OPCODES.put((byte) 0xFB, new OpCodeEI(0xfb, "EI"));
        _OPCODES.put((byte) 0xFF, new OpCodeNOP(0xff, "NOP"));
 
        _OPCODES.put((byte) 0x98, new OpCodeCMPB(0x98, "CMPB"));
        _OPCODES.put((byte) 0x99, new OpCodeCMPB(0x99, "CMPB"));
        _OPCODES.put((byte) 0x9a, new OpCodeCMPB(0x9a, "CMPB"));
        _OPCODES.put((byte) 0x9b, new OpCodeCMPB(0x9b, "CMPB"));
        
        _OPCODES.put((byte) 0x88, new OpCodeCMPW(0x88, "CMPW"));
        _OPCODES.put((byte) 0x89, new OpCodeCMPW(0x89, "CMPW"));
        _OPCODES.put((byte) 0x8a, new OpCodeCMPW(0x8a, "CMPW"));
        _OPCODES.put((byte) 0x8b, new OpCodeCMPW(0x8b, "CMPW"));
        
        _OPCODES.put((byte) 0x64, new OpCodeAD2W(0x64, "AD2W"));
        _OPCODES.put((byte) 0x65, new OpCodeAD2W(0x65, "AD2W"));
        _OPCODES.put((byte) 0x66, new OpCodeAD2W(0x66, "AD2W"));
        _OPCODES.put((byte) 0x67, new OpCodeAD2W(0x67, "AD2W"));
 
        _OPCODES.put((byte) 0x44, new OpCodeAD3W(0x44, "AD3W"));
        _OPCODES.put((byte) 0x45, new OpCodeAD3W(0x45, "AD3W"));
        _OPCODES.put((byte) 0x46, new OpCodeAD3W(0x46, "AD3W"));
        _OPCODES.put((byte) 0x47, new OpCodeAD3W(0x47, "AD3W"));
        
        _OPCODES.put((byte) 0x68, new OpCodeSB2W(0x68, "SB2W"));
        _OPCODES.put((byte) 0x69, new OpCodeSB2W(0x69, "SB2W"));
        _OPCODES.put((byte) 0x6a, new OpCodeSB2W(0x6a, "SB2W"));
        _OPCODES.put((byte) 0x6b, new OpCodeSB2W(0x6b, "SB2W"));

        _OPCODES.put((byte) 0x48, new OpCodeSB3W(0x48, "SB3W"));
        _OPCODES.put((byte) 0x49, new OpCodeSB3W(0x49, "SB3W"));
        _OPCODES.put((byte) 0x4a, new OpCodeSB3W(0x4a, "SB3W"));
        _OPCODES.put((byte) 0x4b, new OpCodeSB3W(0x4b, "SB3W"));
        
        _OPCODES.put((byte) 0x70, new OpCodeAN2B(0x70, "AN2B"));
        _OPCODES.put((byte) 0x71, new OpCodeAN2B(0x71, "AN2B"));
        _OPCODES.put((byte) 0x72, new OpCodeAN2B(0x72, "AN2B"));
        _OPCODES.put((byte) 0x73, new OpCodeAN2B(0x73, "AN2B"));
        
        _OPCODES.put((byte) 0x74, new OpCodeAD2B(0x74, "AD2B"));
        _OPCODES.put((byte) 0x75, new OpCodeAD2B(0x75, "AD2B"));
        _OPCODES.put((byte) 0x76, new OpCodeAD2B(0x76, "AD2B"));
        _OPCODES.put((byte) 0x77, new OpCodeAD2B(0x77, "AD2B"));
        
        _OPCODES.put((byte) 0x50, new OpCodeAN3B(0x50, "AN3B"));
        _OPCODES.put((byte) 0x51, new OpCodeAN3B(0x51, "AN3B"));
        _OPCODES.put((byte) 0x52, new OpCodeAN3B(0x52, "AN3B"));
        _OPCODES.put((byte) 0x53, new OpCodeAN3B(0x53, "AN3B"));
                        
        _OPCODES.put((byte) 0xB0, new OpCodeLDB(0xb0, "LDB"));
        _OPCODES.put((byte) 0xB1, new OpCodeLDB(0xb1, "LDB"));
        _OPCODES.put((byte) 0xB2, new OpCodeLDB(0xb2, "LDB"));
        _OPCODES.put((byte) 0xB3, new OpCodeLDB(0xb3, "LDB"));
        
        _OPCODES.put((byte) 0xA0, new OpCodeLDW(0xa0, "LDW"));
        _OPCODES.put((byte) 0xA1, new OpCodeLDW(0xa1, "LDW"));
        _OPCODES.put((byte) 0xA2, new OpCodeLDW(0xa2, "LDW"));
        _OPCODES.put((byte) 0xA3, new OpCodeLDW(0xa3, "LDW"));
        
        _OPCODES.put((byte) 0xAC, new OpCodeLDZBW(0xac, "LDZBW"));
        _OPCODES.put((byte) 0xAD, new OpCodeLDZBW(0xad, "LDZBW"));
        _OPCODES.put((byte) 0xAE, new OpCodeLDZBW(0xae, "LDZBW"));
        _OPCODES.put((byte) 0xAF, new OpCodeLDZBW(0xaf, "LDZBW"));

        _OPCODES.put((byte) 0x7B, new OpCodeSB2B(0x7b, "SB2B"));
        
        _OPCODES.put((byte) 0x7F, new OpCodeML2B(0x7f, "ML2B"));
        _OPCODES.put((byte) 0x6D, new OpCodeML2W(0x6d, "ML2W"));
        
        _OPCODES.put((byte) 0x8C, new OpCodeDIVW(0x8c, "DIVW"));
        
        
        _OPCODES.put((byte) 0xC4, new OpCodeSTB(0xc4, "STB"));
        _OPCODES.put((byte) 0xC6, new OpCodeSTB(0xc6, "STB"));
        _OPCODES.put((byte) 0xC7, new OpCodeSTB(0xc7, "STB"));
        
        _OPCODES.put((byte) 0xC0, new OpCodeSTW(0xc0, "STW"));
        _OPCODES.put((byte) 0xC2, new OpCodeSTW(0xc2, "STW"));
        _OPCODES.put((byte) 0xC3, new OpCodeSTW(0xc3, "STW"));

        _OPCODES.put((byte) 0xCC, new OpCodePOPW(0xcc, "POPW"));
        _OPCODES.put((byte) 0xCE, new OpCodePOPW(0xce, "POPW"));
        _OPCODES.put((byte) 0xCF, new OpCodePOPW(0xcf, "POPW"));

        _OPCODES.put((byte) 0xF2, new OpCodePUSHP(0xf2, "PUSHP"));

        _OPCODES.put((byte) 0xC8, new OpCodePUSHW(0xc8, "PUSHW"));
        _OPCODES.put((byte) 0xC9, new OpCodePUSHW(0xc9, "PUSHW"));
        _OPCODES.put((byte) 0xCA, new OpCodePUSHW(0xca, "PUSHW"));
        _OPCODES.put((byte) 0xCB, new OpCodePUSHW(0xcb, "PUSHW"));

     
        _OPCODES.put((byte) 0xD1, new OpCodeJLEU(0xd1, "JLEU"));
        _OPCODES.put((byte) 0xD3, new OpCodeJNC(0xd3, "JNC"));
        _OPCODES.put((byte) 0xD7, new OpCodeJNE(0xd7, "JNE"));
        _OPCODES.put((byte) 0xD9, new OpCodeJGTU(0xd9, "JGTU"));
        _OPCODES.put((byte) 0xDF, new OpCodeJE(0xdf, "JE"));
        _OPCODES.put((byte) 0xDB, new OpCodeJC(0xdb, "JC"));
        
        _OPCODES.put((byte) 0xE0, new OpCodeDJNZ(0xe0, "DJNZ"));
        
        _OPCODES.put((byte) 0x30, new OpCodeJNB(0x30, "JNB"));
        _OPCODES.put((byte) 0x31, new OpCodeJNB(0x31, "JNB"));
        _OPCODES.put((byte) 0x32, new OpCodeJNB(0x32, "JNB"));
        _OPCODES.put((byte) 0x33, new OpCodeJNB(0x33, "JNB"));
        _OPCODES.put((byte) 0x34, new OpCodeJNB(0x34, "JNB"));
        _OPCODES.put((byte) 0x35, new OpCodeJNB(0x35, "JNB"));
        _OPCODES.put((byte) 0x36, new OpCodeJNB(0x36, "JNB"));
        _OPCODES.put((byte) 0x37, new OpCodeJNB(0x37, "JNB"));

        _OPCODES.put((byte) 0x38, new OpCodeJB(0x38, "JB"));
        _OPCODES.put((byte) 0x39, new OpCodeJB(0x39, "JB"));
        _OPCODES.put((byte) 0x3A, new OpCodeJB(0x3a, "JB"));
        _OPCODES.put((byte) 0x3B, new OpCodeJB(0x3b, "JB"));
        _OPCODES.put((byte) 0x3C, new OpCodeJB(0x3c, "JB"));
        _OPCODES.put((byte) 0x3D, new OpCodeJB(0x3d, "JB"));
        _OPCODES.put((byte) 0x3E, new OpCodeJB(0x3e, "JB"));
        _OPCODES.put((byte) 0x3F, new OpCodeJB(0x3f, "JB"));


        _OPCODES.put((byte) 0x90, new OpCodeORRB(0x90, "ORRB"));
        _OPCODES.put((byte) 0x91, new OpCodeORRB(0x91, "ORRB"));
        _OPCODES.put((byte) 0x92, new OpCodeORRB(0x92, "ORRB"));
        _OPCODES.put((byte) 0x93, new OpCodeORRB(0x93, "ORRB"));

    }

    private OpcodeCache() { 
    	
    	// 
    }

    public static OpCode get(int instruction_)
		{
    		return get(instruction_, (byte)0x00);
		}
    /**
     * Get the IOPcode for the current instruction.
     * @param instruction_ Where the state machine pc is at.
     * @param pc 
     * @return the IOpcode
     */
    public static OpCode get(int instruction_, byte firstByte) {
    	
    	int opCode = instruction_;
    	 
    	OpCode opc = _OPCODES.get((byte)opCode);
    	
        try 
        {
        	if( opc.getNumberOfBytes() > 1)
        	{
        		opc.setAddressMode(new AddressMode((byte)instruction_, firstByte));
        	}
        	else
        	{
        		opc.setAddressMode(new AddressMode((byte)instruction_));
        	}
    	}
        catch(Exception e)
        {
            System.err.println("OPCODE Not found: " + String.format("0x%02X: ", opCode));

            //System.out.println("Exception: " + e.toString());
            return null;
        }

        return opc;
    }
}
