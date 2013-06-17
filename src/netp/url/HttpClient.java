package netp.url;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLEncoder;
import java.net.UnknownHostException;

public class HttpClient extends Thread {

   protected String host;
   protected int port;
   protected String url;
   protected String header;
   protected StringBuffer doc = new StringBuffer();
   protected String send_doc;
   protected Socket sock;

   protected String ser_header;
   protected String ser_doc;
   protected HttpHost par;

   private static int count = 0;	// # of object in memory

   ReadThread rthd;
   WriteThread wthd=null;

   RdBufSync readBuf;
   WrBufSync writeBuf;
    public static int m_activenum;

    public void error(Exception e) {
        String res;
        res="<?xml version=\"1.0\"?><ERR><Head></Head><Body><msg>"+e.toString()+"</msg></Body></ERR>";
        e.printStackTrace();
        par.getRespond(res);
    }

   public HttpClient( HttpHost  par, String host, int port, String url ) {
     ++count;	    		
     this.host = host;
     this.port = port;
     this.url = url;
     this.par = par;

     this.header = "";
     this.send_doc = "";
     this.ser_header = "";
     this.ser_doc = "";
   }

   public void run() {
     try {
        connect();
        build_send_doc();

        readBuf = new RdBufSync();
        writeBuf = new WrBufSync();

        writeBuf.addWrBuf(send_doc);

        createThreads();

        while( !rthd.getReadFinished() ) 
        {
            wthd.checkError();
            sleep( 100 );
        }        
        --count;
        par.getRespond( rthd.getSerRep());
        sock.close();
     }
     catch( Exception e ) 
     { 
        --count;
   //     error(e);
     }
/*
     finally
     {
       --count;
System.out.println("HttpClient: finally: count: " + count + "\n");
     }
*/
   }

   public static int getCount()
   {
     return count;
   }

   public void add_par( String par_type, String content ) {
       try {
		doc.append(par_type + "=" + URLEncoder.encode(content,"UTF-8"));
	} catch (UnsupportedEncodingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   }

   protected void build_header() 
   {
     header = "POST /" + url + " HTTP/1.0\r\n" +
	       "Referer:http://" + host + "/" + url + "\r\n" +
               "Connection:Keep-Alive\r\n" +
               "User-agent: VCMS(version 1.0)\r\n" +
               "Host: " + this.host + ":" + port + "\r\n" +
               "Accept:image/gif,image/x-xbitmap, image/jpeg, image/pjpeg, image/png, *\r\n" +
               "Accept-Encoding:gzip\r\n" +
               "Accept-Language:en\r\n" +
               "Accept-Charset:iso-8859-1,*,utf-8\r\n" +
               "Content-type:application/x-www-form-urlencoded\r\n" +
               "Content-length: " + doc.length() +"\r\n" +
               "\r\n"; 
   }

   protected void build_send_doc() 
   {
     build_header();
     send_doc = header + doc.toString();
   }

   public void connect() throws UnknownHostException, IOException
   {
        sock = new Socket( host, port );
   }
   
   public String getHost() 
   {
     return ( host );
   }

   public int getPort() {
     return( port );
   }

   public String getSerHeader() {
     return( ser_header );
   }

   public String getSerDoc() {
     return( ser_doc );
   }

   public void createThreads() throws IOException {
     PrintWriter out = null;
     BufferedReader in = null;


       out = new PrintWriter( sock.getOutputStream(), true );
       in = new BufferedReader(new InputStreamReader( sock.getInputStream()));

       rthd = new ReadThread( in, readBuf );
       rthd.start();

       wthd = new WriteThread( out, writeBuf );
       wthd.start();
    }
}

class ReadThread extends Thread 
{
    private RdBufSync rBuf;
    BufferedReader in;
    boolean readfinished=false;
    boolean hasError=false;
    public ReadThread( BufferedReader inString, RdBufSync buf ) {
      super(); 
      rBuf = buf;
      in = inString;
    }   
  
    public void run() {
        String s;
        StringBuffer tmp = new StringBuffer();
        try{
            while ((s=in.readLine()) !=null ) {
                tmp.append( s + "\n" );
           //     rBuf.addRdBuf( s );
            }
            rBuf.addRdBuf( tmp.toString());
        }
        catch (Exception e) {
System.out.println("Read error: " + e.toString());

            hasError=true;
        }
        finally {
            readfinished=true;
            hasError=false;
        }
    } 

    public boolean getReadFinished() throws Exception{
//      if(hasError) throw new Exception ("Read Thread Error");
      hasError=false;
      return readfinished;
    }

    public String getSerRep() {
      return rBuf.getRdBuf();
    }
}

class WriteThread extends Thread  {
    private WrBufSync wBuf;
    PrintWriter out;
    boolean hasError=false;

    public WriteThread( PrintWriter outString, WrBufSync buf ) {
      super(); 
      wBuf = buf;
      out = outString;
    }   

    public void checkError() throws Exception {
        if(hasError) throw new Exception("WriteThread Error");
    }
    public void run() {
        try {
          String outdoc = wBuf.getWrBuf();
          if(outdoc.length()>0)
               out.println( outdoc );
        }
        catch (Exception e) {
            hasError=true;
        }
    } 
}

class WrBufSync {
    private StringBuffer wrBuf = new StringBuffer();

    public WrBufSync() {
    }

    public synchronized void addWrBuf(String str) {
      wrBuf.append(str);
    }

    public synchronized String getWrBuf() {
      String buf = wrBuf.toString();  
      wrBuf = new StringBuffer();   

      return buf;
    }
}

class RdBufSync {
    private StringBuffer rdBuf = new StringBuffer("");

    public RdBufSync() {
    }

    public synchronized void addRdBuf(String str) {
      rdBuf.append(str);
    }

    public synchronized String getRdBuf() {
      String buf = rdBuf.toString();  
      return buf;
    }
}
