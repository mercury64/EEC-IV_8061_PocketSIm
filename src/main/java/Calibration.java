import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Calibration {

    private static final int SIZE_OF_EECIV = 0xdfff;
    private static final int SIZE_OF_MEM_SPACE = 0xFFFF;
    private static final int MASK_0xFF = 0xFF;
    
	private String fileName;
	
	public Calibration(String fileName)
	{
		if (fileName == null || fileName == "")
		{
			fileName = "KID2.bin";
		}
		this.setFileName(fileName);
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public int[] readFile()
	{
        byte[] program = new byte[SIZE_OF_EECIV];
        int[] convertedProgram = new int[SIZE_OF_MEM_SPACE];
        
		File file = new File(this.getFileName());
		
	    try (DataInputStream data = new DataInputStream(new FileInputStream(file)))
	    {

	        data.read(program);
	        
	        for (int i = 0; i < program.length; i++)//program.length
	        {
	            convertedProgram[i + 0x2000] = (int) program[i] & MASK_0xFF;
	        }

	    } catch (FileNotFoundException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    return convertedProgram;
	}
}
