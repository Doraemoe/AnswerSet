package netp.xmldocument;

import netp.xml.*;
import java.util.*;


abstract public class NetpDocument
{
    private String m_docType;
    private String m_rid;
    private String m_time;
    private String m_dir;
    private String m_type; // "req" or "res"

    private boolean bodyCreated=false;

    private BuildTree m_xmlbuilder;
    

    public NetpDocument(String t_docType,String t_type, String t_dir) 
    {
        m_docType = t_docType;
        m_type = t_type;
        m_dir = t_dir;

        Date d=new Date();        
        m_rid=randId();
        m_time = String.valueOf(d.getTime());

        m_xmlbuilder = new BuildTree (m_docType);
    }
   
    public void setReqId(String reqId) 
    {
        m_rid=reqId;
    }

    public void init() 
    {
        if(bodyCreated) return;
        createHead();
        createBody();
        bodyCreated=true;
    }

    static public String randId()
    {
       double seed=Math.random();
       int res=(int) (100000000*seed);
       return "req_"+res;
    }

    /**
     * in case we need more head information 
     */
    protected void createSpecialHead() 
    {
    }

    /**
     * creates the head area for the document
     */
    protected void createHead() 
    {
        BuildNode p_node = new BuildNode ("Head");
        m_xmlbuilder.appendChild(p_node);

        m_xmlbuilder.downTree();

        addChildNode("rid",m_rid);

        addChildNode("time",m_time);

        addChildNode("dir",m_dir);

        addChildNode("type",m_type);

        createSpecialHead();
        m_xmlbuilder.upTree();
    }
   
    /**
     * abstract function - will be overridden by the child function
     */

    protected abstract void pCreateBody();

    protected void createBody() 
    {   
        BuildNode p_node=new BuildNode ("Body");
        m_xmlbuilder.appendChild(p_node);
        m_xmlbuilder.downTree();

        pCreateBody();

        m_xmlbuilder.upTree();
    }
   
    /**
     * @return string
     */
    public String toString() 
    {
       init();
       return m_xmlbuilder.toString();
    }

    protected final void addChildNode(String tag,String val)
    {
       BuildNode p_node = new BuildNode(tag,val);
       m_xmlbuilder.appendChild(p_node);
    }
}
