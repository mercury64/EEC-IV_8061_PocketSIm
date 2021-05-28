import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

import ca.nerret.emu.LogFormatter8061;
import ca.nerret.emu.emulator.OpcodeCache;
import ca.nerret.emu.emulator.State;
import ca.nerret.emu.emulator.opcodes.IOpCode;

public class EECIV_PocketSIM {

    private final static Logger logger = Logger.getAnonymousLogger();
    
    State state;

	public void init()
	{
		logger.setUseParentHandlers(false);
	    Handler logHandler = null;
		try {
			logHandler = new FileHandler("cpu8061.log");
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    logHandler.setFormatter(new LogFormatter8061());
	    logger.addHandler(logHandler);
	   
	   // Calibration KID2 = new Calibration("KID2/KID2patchedNew.bin");
	   Calibration KID2 = new Calibration("KID2/KID2.bin");
	   // Calibration KID2 = new Calibration("KID2/0x100_v001.bin"); 
	   //Calibration KID2 = new Calibration("KID2/JFA2.bin");	
	   // Calibration KID2 = new Calibration("KID2/KID2.bin");
	   //Calibration KID2 = new Calibration("KID2/0x100_v001.bin"); 
	   //Calibration KID2 = new Calibration("KID2/T4M0.bin"); 
	   //Calibration KID2 = new Calibration("KID2/P3M.bin"); 
		int[] fileBytes = KID2.readFile();
   
		state = new State(fileBytes);
       
        //Emulator emu = new Emulator(state);
        //emu.start();
        

		
		// opcode.exec(_state);
		
		//Memory memory = new Memory();
		

	    
	}
	
	public void run()
	{
        int count = 1;
        
       //state.setPc(17330);
        
		while (true) {
		        
			//System.out.println(String.format("0x%04X",state.getPc()));
			
			int instruction = state.getMemory()[state.getPc()];
			
			IOpCode opcode = OpcodeCache.get(instruction,(byte)state.getMemory()[state.getPc()+1]);
			
			if (opcode == null)
			{
				System.out.println(String.format("0x%04X: ", state.getPc()));
				System.exit(1);
			}
			
			
			if (state.getPc() == 0x2340)
			{
				//pause
				System.out.println(String.format("** LOG 0x%04X", state.getWordRegister((short) 0x0410)));
			}
			
			//logger.info( String.format("0x%04X: ", state.getPc()) );
			//System.out.println(String.format(" STACK SFR10 0x%04X", state.getWordRegister((short) 0x10)));
			//System.out.println(String.format(" STACK R26 0x%04X", state.getWordRegister((short) 0x26)));
			System.out.print(String.format("0x%04X: %s", state.getPc(), opcode));
			opcode.exec(state);
			
			logger.info( String.format("%s", opcode) );
			    
			//System.out.println("CONSOLE STATUS " + String.format("0x%04X",state.getWordRegister((short) 0xd00)));
		/*	System.out.println("Scalar 0x2204: " + state.getMemory()[0x2204]);
			System.out.println("Reg 0x410: " + state.getWordRegister((short) 0x0410));
			System.out.println("Reg 0x402: " + state.getWordRegister((short) 0x0402));
			System.out.println("Reg 0x403: " + state.getWordRegister((short) 0x0403));
			System.out.println("Reg 0x404: " + state.getWordRegister((short) 0x0404));
			System.out.println("Scalar 0x2005: " + state.getMemory()[0x2005]);
			*/
			//System.out.println(state.pswFlagsToString());
			if (count == 200000) {
				System.err.println("Count reached, ending");
				break;
			}
			count++;
		}
	}
	
	public static void main(String[] args) 
	{
	
		 EECIV_PocketSIM eeciv = new EECIV_PocketSIM();
		 eeciv.init();
	     
		 System.out.println("Program Start:");
	        
		 eeciv.run();
		
	}

}
