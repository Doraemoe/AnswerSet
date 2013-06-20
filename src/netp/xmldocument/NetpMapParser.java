package netp.xmldocument;

import netp.xml.*;
import java.util.*;
import netp.*;

public class NetpMapParser 
{
    private String m_cmd;
    private Hashtable<String, String> m_pars;
    private int m_mapnum = 0;

    private static String getParKey(String par)
    {
        int pos;
        pos=par.indexOf('=');

        if(pos<=0) 
            return "";
        return par.substring(0,pos);
    }

    private static String getParVal(String par)
    {
        int pos;
        pos=par.indexOf('=');

        if(pos<=0) 
            return "";

        if(par.length()==(pos+1))
            return "";

        return par.substring(pos+1);
    }

    public NetpMapParser(NetpParser p_parser) 
    {
        if("MAP".equals(p_parser.getDocType()))
        {
            m_pars = new Hashtable<String, String>();

            m_cmd=p_parser.getBodyValue("code");
            String par_num_s=p_parser.getBodyValue("map_num");
            m_mapnum=NetpGeneral.stringToInt(par_num_s);

            String p_str,p_tag,p_key,p_val;

            for( int i=0;i<m_mapnum;++i)
            {
                p_tag="map_"+i;
                p_str=p_parser.getBodyValue(p_tag);
                p_key=getParKey(p_str);
                p_val=getParVal(p_str);
                m_pars.put(p_key,p_val);
            }
        }
    }

    public int getMapSize()
    {
        return m_mapnum;
    }

    public int size()
    {
        return m_mapnum;
    }

    public String getCode() 
    {
        return m_cmd;
    }

    public String getMapValue(String p_para)
    {
        return getMap(p_para);
    }

    public String getMap(String p_para)
    {
        String val=(String) m_pars.get(p_para);

        if(val==null)
            return "";

        return val;
    }
    public Enumeration<String> getMapList()
    {
        return m_pars.keys();
    }
}

