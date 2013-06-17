package netp.canvas;
import java.util.*;
import java.awt.*;
import netp.*;
public class NetpCanvasObjectParser
{
    private Hashtable m_pars;

    public NetpCanvasObjectParser(String p_par)
    {
        m_pars=new Hashtable();
        StringTokenizer st = new StringTokenizer(p_par,";");
        String p_str;
        while(st.hasMoreTokens())
        {
            p_str=st.nextToken();
            addPar(p_str);
        }

    }
    private void addPar(String t_str)
    {
        if(t_str==null) return;
        if(t_str.length()<3) return;
        int p;
        p=t_str.indexOf(':');
        if(p==-1) return;
        String p_key,p_val;
        p_key=t_str.substring(0,p);
        p_val=t_str.substring(p+1);
        m_pars.put(p_key,p_val);
    }
    public int getIntValue(String p_key)
    {
        String p_val;
        p_val=(String) m_pars.get(p_key);
        return NetpGeneral.stringToInt(p_val);
    }
    public String getStringValue(String p_key)
    {
        String p_val;
        p_val=(String) m_pars.get(p_key);
        return p_val;
    }
    public Point getPointValue(String p_key)
    {
        String p_val;
        p_val=(String) m_pars.get(p_key);
        return NetpGeneral.stringToPt(p_val);
    }
    public Color getColorValue(String p_key)
    {
        String p_val;
        p_val=(String) m_pars.get(p_key);
        return NetpGeneral.stringToColor(p_val);
    }
    public Color getColorValue(String p_key,Color def) {
        String p_val;
        p_val=(String) m_pars.get(p_key);
        if(p_val!=null)
            return NetpGeneral.stringToColor(p_val);
        return def;

    }
    public boolean getBooleanValue(String p_key)
    {
        String p_val;
        p_val=(String) m_pars.get(p_key);

        if ( "1".equals(p_val) )
           return NetpGeneral.stringToBoolean(p_val);
        else
           return false;
    }
    public boolean getBooleanValue(String p_key,boolean v)
    {
        String p_val;
        p_val=(String) m_pars.get(p_key);
        if(p_val==null) return v;
        if ( "1".equals(p_val) )
           return NetpGeneral.stringToBoolean(p_val);
        else
           return false;
    }

}
