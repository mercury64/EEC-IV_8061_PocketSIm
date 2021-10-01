
public class Demo_EEC {

	public static void main(String[] args) {
		
		Calibration KID2 = new Calibration("KID2/KID2.bin");
		
		CPU cpu = new EECIV_8061();
		BUS eeciv = new BUS(cpu);
		
		eeciv.setCal(KID2);
		eeciv.cpu.clock();
		
		   
	}
	

}
