package netp;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Point;
import java.util.StringTokenizer;

import netp.GUI.MessageBoxSimple;

/**
 * This interface is used for a Component can have a pop up menu 
 *
 * @version     1.0, 2001-01-28
 * @since       JDK1.0
 */

public class NetpGeneral
{
    static private Point sTp(String s, String dem)
    {
        try
        {
            StringTokenizer st = new StringTokenizer(s, dem );
            int x, y;
            x = Integer.parseInt(st.nextToken());
            y = Integer.parseInt(st.nextToken());

            Point p = new Point(x, y );
            return p;
        }
        catch (Exception e)
        {
            return new Point(0,0);
        }
    }
    static public Point stringToPoint( String s ) {
      return NetpGeneral.sTp(s,",");
    }

    static public String ptToString(Point p)
    {
        if(p==null) return "";
        return p.x+"_"+p.y;
    }
    static public Point stringToPt(String s)
    {
      return NetpGeneral.sTp(s,"_");
    }
    static public String colorToString(Color c) {
        int r,g,b;
        if(c==null) return "0_0_0";
        r=c.getRed();
        g=c.getGreen();
        b=c.getBlue();
        return r+"_"+g+"_"+b;
    }
    static public Color stringToColor(String s) {
        try{
            StringTokenizer st= new StringTokenizer(s,"_");
            String r,g,b;
            r=st.nextToken();
            g=st.nextToken();
            b=st.nextToken();
            int ir,ig,ib;
            ir=Integer.parseInt(r);
            ig=Integer.parseInt(g);
            ib=Integer.parseInt(b);
            return new Color(ir,ig,ib);
        }
        catch (Exception e) {
            return null;
        }

    }
    static public String booleanToString(boolean b) {
        if(b) return "1";
        return "0";
    }
    static public boolean stringToBoolean(String s) {
        if(s.length()==0) return false;
        s.toLowerCase();
        if(s.equals("yes")) return true;
        if(s.equals("1")) return true;
        if(s.equals("on")) return true;
        if(s.equals("y")) return true;
        return false;
    }

    static public String intToString(int inp) {
        return ""+inp;
    }
    static public int stringToInt(String s) {
        int i=0;
        if(s==null) return 0;
        try{
            i=Integer.parseInt(s);
        }
        catch (NumberFormatException e) {
            i=0;
        }
        return i;
    }
	static public double stringToDouble(String s) {
        double d=0.0;
        if(s==null) return d;
        try{
            d=Double.parseDouble(s);
        }
        catch (NumberFormatException e) {
            d=0.0;
        }
        return d;

	}
    static public void messageBox(String title,String disp) {

        Frame fm=new Frame();
        MessageBoxSimple bx=new MessageBoxSimple(fm,title,disp);
        bx.setVisible(true);

    }

    static public String findStringBetween(String strInp,String strBeg,String strEnd){
    	String strTmp;
    	int iEndIdx;
    	strTmp=findStringAfter(strInp,strBeg);
    	iEndIdx=strTmp.indexOf(strEnd);
    	if(iEndIdx<=0) return "";
    	return strTmp.substring(0, iEndIdx);
    }
    
    static public String findStringAfter(String strInp,String strBeg){
    	int iStrIdx;
    	iStrIdx=strInp.indexOf(strBeg);
    	if(iStrIdx==-1)return "";
    	return strInp.substring(iStrIdx+strBeg.length());
    }
    
    
    /* if field = "host" and line = "host = 192.168.0.77"
     * this method will return "192.168.0.77"
     * if line start with #, this method will return ""
     */
    static public String getParameterValue (String field, String line)
    {
	String linestr;
        String parameter, value;

        linestr = line.trim();

        if ( linestr.startsWith("#") || linestr.length() == 0 )
          return "";

        parameter = linestr.substring(0, linestr.indexOf("="));
        value = linestr.substring(linestr.indexOf("=")+1);

        if ( field.equals(parameter.trim()) )
          return value.trim();
        else
          return "";
      }

      static public String findLangSet( String inputStr, int index, int langSet ) {
         String outStr = "";
         switch ( langSet ) {
            case 0:
              outStr = inputStr;
              break;
            case 1:
              outStr = findSimCha( index );
              break;
            case 2:
              outStr = findTriCha( index );
              break;
            default:
             outStr = inputStr; 
         }
         return outStr;
      }

      static public String findSimCha(  int idx ) {
        String str_holder[] = { 
          "\u56B4\u91CD\u8B66\u5831",  /* critical alarm */
          "\u4E3B\u8981\u8B66\u5831",  /* major alarm */
          "\u6B21\u8981\u8B66\u5831",  /* minor alarm */
          "\u5E38\u898F\u4E8b\u4EF6",  /* normal event */
          "\u589E\u52A0\u65B0\u7DB2\u7D61", /* add new newwork */
          "\u589E\u52A0\u65B0\u7DB2\u7D61\u88DD\u7F6E", /* add new net device */
          "\u589E\u52A0\u96C6\u7DDA\u5668", /* add new hub */
          "\u589E\u52A0\u65B0\u526F\u7DB2\u7D61", /* add sub network */
          "\u589E\u52A0\u93C8\u63A5",   /* add link */
          "\u589E\u52A0\u6A19\u7C64",   /* add label */
          "\u589E\u52A0\u4E0A\u4E00\u7D1A\u5716\u793A", /* add up level */
          "\u6027\u8CEA", /* properity */
          "\u8A2D\u7F6E\u5716\u8B5C\u8EAB\u4EFD", /* set map id */
          "\u66F4\u6539\u8EAB\u4EFD",  /* change ID  */
          "\u66F4\u6539\u540D\u7A31",  /* change name */
          "\u66F4\u6539\u5716\u793A",  /* change icon */
          "\u986F\u793A\u6458\u8981",  /* view brief */
          "\u522A\u9664",              /* delete */
          "\u7DB2\u7D61\u5716\u8B5C",  /* netmap */
          "\u65B0\u7DB2\u7D61\u5716\u8B5C",  /* new netmap */
          "\u5132\u5B58\u7DB2\u7D61\u5716\u8B5C",  /* save netmap */
          "\u88DD\u8F09\u7DB2\u7D61\u5716\u8B5C"   /* load netmap */
        }; 

        return str_holder[idx];
      }

      static public String findTriCha( int idx ) {
        String out = "";

        return out;
      }
}

