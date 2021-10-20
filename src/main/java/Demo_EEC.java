import ca.nerret.emu.emulator.BUS;
import ca.nerret.emu.emulator.CPU;
import ca.nerret.emu.emulator.Calibration;
import ca.nerret.emu.emulator.MotherBoard;
import ca.nerret.emu.emulator.cpu.EECIV_8061;

public class Demo_EEC {

	public static void main(String[] args) {
		
		Calibration KID2 = new Calibration("KID2/KID2.bin");
		
		MotherBoard mb = new MotherBoard();
		
		CPU cpu = new EECIV_8061();
		BUS bus = new BUS();
		
		mb.addCPU(cpu);
		mb.addBUS(bus);
		

		bus.setCalibration(KID2);
		
		mb.connectComponents();
		
		while (true) {
			do
			{
				mb.clock();
			}
			while (!mb.getCPU().complete());
		}	   
	}
}
