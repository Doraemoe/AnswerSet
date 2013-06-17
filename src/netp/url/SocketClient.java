package netp.url;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SocketClient extends Thread {

   protected String host;
   protected int port;

   protected StringBuffer outdoc;
   protected StringBuffer indoc;

   protected Socket sock;
   protected SocketHost par;

   private static int count = 0;	// # of object in memory

   protected boolean isConnect;	


   SocketReadThread rthd;


    public static int m_activenum;

    public void error(Exception e) {
        String res;
        res="<?xml version=\"1.0\"?><ERR><Head></Head><Body><msg>"+e.toString()+"</msg></Body></ERR>";
        e.printStackTrace();
        par.getRespond(res);
    }

   public SocketClient( SocketHost  par, String host, int port ) {
     ++count;	    		
     this.host = host;
     this.port = port;
     this.par = par;

     outdoc=new StringBuffer();
	 indoc=new StringBuffer();
	 isConnect=false;
   }
	public synchronized void sendDoc(String doc) {
		outdoc.append(doc);
	}
	public synchronized void clearSendDoc() throws IOException {
		if(outdoc.length()==0) return;
		PrintWriter out;
		out = new PrintWriter(sock.getOutputStream(),true);
		out.println(outdoc.toString());
		outdoc= new StringBuffer();
	}

	public synchronized void receiveDoc(String s){
		par.getRespond(s);
	}

   public void run() {
     try {
			connect();
			createThreads();
			while(isConnect) {
				clearSendDoc();
				sleep(100);
			}
     }
     catch( Exception e ) 
     { 
        --count;
		isConnect=false;
        error(e);
     }
   }

	public boolean isConnect() {
		return isConnect;
	}
   public void serverClosed() {
	  par.serverClosed();
	  isConnect=false;
      close();
   }
   public void close() {
       isConnect=false;
	   try{
		 sock.close();
	   }
	   catch(Exception e)
	   {
	   }
   }
   public static int getCount()
   {
     return count;
   }

   public void connect() throws UnknownHostException, IOException
   {
		if(!isConnect) {
	        sock = new Socket( host, port );
			isConnect=true;
		}
   }
   
   public void createThreads() throws IOException {
     BufferedReader in;

       in = new BufferedReader(new InputStreamReader( sock.getInputStream()));

       rthd = new SocketReadThread( in, this);
       rthd.start();
    }
}


class SocketReadThread extends Thread 
{
	BufferedReader in;
	SocketClient par;
    public SocketReadThread( BufferedReader inString, SocketClient buf ) {
      super(); 
      par = buf;
      in = inString;
    }   
  
    public void run() {
        String s;
        try{
            while ((s=in.readLine()) !=null ) {
				par.receiveDoc(s);
				sleep(100);
            }
        }
        catch (Exception e) {
        }
        finally {
			par.serverClosed();
        }
    } 

}
