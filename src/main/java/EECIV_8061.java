
public class EECIV_8061 extends CPU {

	@Override
	public void reset() {
		// TODO Auto-generated method stub

		this.PSW_FLAGS = 0x7f00; // Reset PSW.
		// 0 1 1 1  1 1 1 1 0 0 0 0 0 0 0 0 	
		
		this.pc = 0x2000; // Program start.

	}

	
}
