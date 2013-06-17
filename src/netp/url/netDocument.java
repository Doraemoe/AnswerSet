package netp.url;

public class netDocument
{
    String hd = new String();
    String doc = new String();
    int pos = 0; 
 
    public netDocument ( String msg ) 
    {
      pos = msg.indexOf("\n\n" );

      if ( pos > 0 )
      {
        char charArray1[] = new char[ pos ];
        StringBuffer buff = new StringBuffer( msg );

        buff.getChars( 0, pos, charArray1, 0 );

        for ( int i=0; i<charArray1.length; ++i )
          hd += charArray1[i];  

        char charArray2[] = new char[ msg.length() - pos -2 ];
        buff.getChars( pos+2, msg.length(), charArray2, 0 );

        for ( int j=0; j<charArray2.length; ++j )
          doc += charArray2[j];
      }
      else
      {
        hd = "";
        doc = msg;
      }
    }

    public String getHeader()
    {
      return hd;
    }

    public String getDoc()
    {
      return doc;
    }
}
