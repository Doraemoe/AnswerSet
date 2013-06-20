package netp.xmldocument;
import java.util.*;
import netp.*;

public class NetpListDocument extends NetpDocument 
{
    private String m_cmd;
    private Vector<String> m_pars;

    public NetpListDocument(String t_dir) 
    {
        super("LIST","Res",t_dir);
        m_cmd="";
        m_pars=new Vector<String>();
    }
   
    protected void pCreateBody() 
    {

        int i,s;
        String v,tag;

        addChildNode("code", m_cmd);
        s=m_pars.size();
        addChildNode("list_num", NetpGeneral.intToString(s));
	
        for(i=0; i<m_pars.size(); ++i)
        {
            v=(String) m_pars.elementAt(i);
            tag="list_"+i;
            addChildNode(tag,v);
        }
    }

    public void addList(String val) {
        m_pars.addElement(val);
    }

    public void setCode(String t_code)
    {
        m_cmd=t_code;
    }

    public void setCmd( String t_cmd )
    {
        setCode(t_cmd);
    }
}
