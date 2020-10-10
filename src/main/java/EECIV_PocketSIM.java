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
	   

	    Calibration KID2 = new Calibration("KID2/KID2.bin");
		
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
        
		while (true) {
		        
			//System.out.println(String.format("0x%04X",state.getPc()));
			
			int instruction = state.getMemory()[state.getPc()];
			
			IOpCode opcode = OpcodeCache.get(instruction,(byte)state.getMemory()[state.getPc()]);
			
			
			if (state.getPc() == 0xa9df)
			{
				//pause
				System.out.println("Break Point");
			}
			logger.info( String.format("0x%04X: ", state.getPc()) );
			opcode.exec(state);
			logger.info( String.format("%s", opcode) );
			    
			//System.out.println(state);
			    
			//System.out.println("PSW: " + state.psw.getMemory());
			if (count == 5000) break;
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
