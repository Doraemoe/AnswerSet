package netp.xmldocument;
import java.util.*;
import netp.*;

public class NetpMapDocument extends NetpDocument 
{
    private String m_cmd;
    private Hashtable m_pars;

    public NetpMapDocument(String t_dir) 
    {
        super("MAP","Req",t_dir);
        m_cmd="";
        m_pars=new Hashtable();
    }
   
    protected void pCreateBody() 
    {
        addChildNode("code", m_cmd);
        addChildNode("map_num", NetpGeneral.intToString(m_pars.size()));

        int i =0;
        String k,v,par,tag;


       for(Enumeration e= m_pars.keys(); e.hasMoreElements();)
       {
            k=(String) e.nextElement();
            v=(String) m_pars.get(k);
            tag="map_"+i;
            par=k+"="+v;
            addChildNode(tag,par);
            i++;
      }
    }

    public void setPara( String t_par, String t_val )
    {
        m_pars.put(t_par,t_val);
    }
    public void setMap( String t_par, String t_val )
    {
        m_pars.put(t_par,t_val);
    }
    public void setCmd( String t_cmd )
    {
        m_cmd = t_cmd;
    }
    public void setCode( String t_cmd )
    {
        m_cmd = t_cmd;
    }

}
