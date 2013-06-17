package netp.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class ReadFile
{
    protected String fileName;

    public ReadFile( String inputFile )
    {
      this.fileName = inputFile;
    }

    public String getLine()
    {   
       File name = new File ( fileName );
       StringBuffer buff = new StringBuffer();
       String temp;

       try 
       {
         RandomAccessFile file = new RandomAccessFile( name, "r" );

         while(( temp = file.readLine()) != null )
         {
           buff.append( temp + "\n" );
         }
           
         try
         {
           file.close();
         }
         catch ( IOException e )
         {
           System.err.println("Can't close file: " + fileName );
         }
       }
       catch ( IOException e2 )
       {
         System.err.println("Can't open file: " + fileName );
         System.exit(1);
       }
       return buff.toString();
    }
  
    public static void main( String args[] )
    {
      String buff;
      String fileName = "";

      if ( args[0].length() != 0 )
        fileName = args[0];
      else
      {
        System.out.println("Usage: java ReadFile filename\n");
        System.exit(0);
      }

      ReadFile app = new ReadFile(fileName);

      buff = app.getLine();

System.out.println("Read from file:\n" + buff );
     
      System.exit( 0 );
    }
}
