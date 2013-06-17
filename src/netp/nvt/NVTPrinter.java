package netp.nvt;
import java.io.IOException;

public class NVTPrinter extends Thread 
{
    NVTInputStream inStream;
    NVTOutputStream outStream;
    int mode;
    public NVTPrinter(NVTInputStream in) 
    {
        super();
        inStream=in;
    }
    public int getMode()
    {
       return mode;
    }
  
    public void run() 
    {
        boolean finished = false;
	StringBuffer buf = new StringBuffer();
        boolean login_flag = false;
        boolean passwd_flag = false;

        try {
            do {
                int i = inStream.read();
                if(i == -1)
                {
                    finished = true;
		}
                else 
                {
                    char tmp = (char)i;
                    System.out.print(tmp);
                    System.out.flush();
                    buf.append(tmp);

                    String s = buf.toString(); 
                    int idx = s.indexOf("login: ");
                    if (idx != -1 && login_flag == false)
                    {
System.out.println("found login");
         mode = 1; 
                        login_flag = true;
                    }
                    
                    idx = s.indexOf("Password: ");
                    if (idx != -1 && passwd_flag == false)
                    {
                        passwd_flag = true;
                    }

                    buf = new StringBuffer(s);

                    yield();
                }

            } while(!finished);
            System.out.println("\nConnection broken.");

   System.out.println("buf :" + buf.toString());
            System.exit(0);
        }
        catch(IOException ex) 
        {
            System.out.println("NVTPrinter error");
            System.exit(1);
        }
    }
}
