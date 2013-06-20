package netp.xmldocument;

import netp.xml.*;
import java.util.*;
import netp.*;

public class NetpCmdParser 
{
    private String m_cmd;
    private Hashtable<String, String> m_pars;
    private int m_parnum = 0;

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

    public NetpCmdParser(NetpParser p_parser) 
    {
        if("CMD".equals(p_parser.getDocType()))
        {
            m_pars = new Hashtable<String, String>();
            m_cmd=p_parser.getBodyValue("cmd");
            String par_num_s=p_parser.getBodyValue("par_num");
            m_parnum=NetpGeneral.stringToInt(par_num_s);

            String p_str,p_tag,p_key,p_val;

            for( int i=0;i<m_parnum;++i)
            {
                p_tag="par_"+i;
                p_str=p_parser.getBodyValue(p_tag);
                p_key=getParKey(p_str);
                p_val=getParVal(p_str);
                m_pars.put(p_key,p_val);
            }
        }
    }

    public int getParaSize()
    {
        return m_parnum;
    }
    public int size()
    {
        return m_parnum;
    }

    public String getCode() 
    {
        return m_cmd;
    }

    public String getCmd() 
    {
        return getCode();
    }

    public String getCmdValue(String p_para)
    {
        return getPara(p_para);
    }
   public String getPara(String p_para)
   {
        String val=(String) m_pars.get(p_para);

        if(val==null)
            return "";

        return val;
   }
}

