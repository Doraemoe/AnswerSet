package netp.xmldocument;

import netp.xml.*;
import java.util.*;
import netp.*;

public class NetpListParser 
{
    private String m_cmd;
    private Vector<String> m_pars;
    private int m_listnum = 0;

    public NetpListParser(NetpParser p_parser) 
    {
        if("LIST".equals(p_parser.getDocType()))
        {
            m_pars = new Vector<String>();
            m_cmd=p_parser.getBodyValue("code");
            String par_num_s=p_parser.getBodyValue("list_num");
            m_listnum=NetpGeneral.stringToInt(par_num_s);

            String p_str,p_tag;

            for( int i=0;i<m_listnum;++i)
            {
                p_tag="list_"+i;
                p_str=p_parser.getBodyValue(p_tag);
                m_pars.addElement(p_str);
            }
        }
    }

    public int getListSize()
    {
        return m_listnum;
    }

    public int size()
    {
        return m_listnum;
    }

    public String getCode() 
    {
        return m_cmd;
    }

    public String getListValue(int i)
    {
       return getList(i);
    }

    public String getList(int i)
    {
        String val=(String) m_pars.elementAt(i);

        if(val==null)
            return "";

        return val;
    }
}
