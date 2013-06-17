package netp.nvt;
import java.io.*;

public class NVTInputStream extends FilterInputStream 
{
    byte IAC=(byte) 0xff;
    byte DO=(byte) 0xfd;
    byte WILL=(byte) 0xfb;
    byte CF=13;
    byte LF=10;
    int WONT=252;
    int DONT=254;
    int BUFFER_SIZE=1024;
    OutputStream out;
    byte lineBuffer[]=new byte[BUFFER_SIZE];
    int numBytes=0;

    public NVTInputStream(InputStream inStream,OutputStream outStream) 
    {
        super(inStream);
        out = outStream;
    }

    public int read() throws IOException {
        boolean recIAC;
        int i;
        do {
            recIAC = false;
            i = in.read();
            if(i == -1) 
	        return i;
            byte b = (byte)i;
            if(b == IAC) 
	    {
                recIAC = true;
                int cmd = in.read();
                if(cmd == -1) 
                    return cmd;
                byte b2 = (byte) cmd;
                if(b2 == IAC) 
                    return 255;
                else if(b2 == DO) 
                {
                    int opt = in.read();
                    if(opt == -1) 
                        return opt;
	            out.write(255);
                    out.write(WONT);
                    out.write(opt);
                    out.flush();
                } 
		else if(b2 == WILL)
		{
                    int opt = in.read();
                    if(opt == -1) 
                        return opt;
                    out.write(255);
                    out.write(DONT);
                    out.write(opt);
                    out.flush();
                }
            }
        } while(recIAC);
        
	return i;
    }

    public String readLine() throws IOException 
    {
        byte lastbyte = (byte) 0,b;
        int i,num;
        boolean first;
        StringBuffer bf;
        bf = new StringBuffer();
        first = true;
        num = 0;
        while(true)
	{
            i = read();
            if(i == -1)
                return null;
            b = (byte) i;
            if((b == LF) && (lastbyte == (byte)13)) 
	    {
                return bf.toString();
            }
            if(first)
	    {
                first = false;
            }
            else 
	    {
                bf.append((char) lastbyte);
            }
            lastbyte = b;
            num++;
            if(num > 1000000)
	        return bf.toString();
        }
    }

}
