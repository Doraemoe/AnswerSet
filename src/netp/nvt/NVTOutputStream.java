package netp.nvt;
import java.io.*;

public class NVTOutputStream extends FilterOutputStream {
	int IAC=255;
	public byte CR=13;
	public byte LF=10;
	public NVTOutputStream(OutputStream outStream) {
		super(outStream);
	}
	public void write(int i) throws IOException {
		if(i==IAC) super.write(i);
		super.write(i);
	}
	public void println(String s) {
		try {
			byte[] sBytes=s.getBytes();
			for(int i=0;i<sBytes.length;++i)
				super.write(sBytes[i]);
			super.write(CR);
			super.write(LF);
			super.flush();
		}
		catch (IOException ex) {
		}
	}
}
