package netp.xml;
import java.util.Vector;


/**
 * Class description  .... blah blah 
 *
 * @author  UN
 * @version 1.0
 */
public class BuildNode 
{
    BuildNode root;       
    BuildNode m_parent;           // Parent node of this current node 
    Vector<BuildNode> m_child;
    Vector<XmlAttribute> m_attrs;
    int m_childnum;
    public String m_tag;
    String m_value;
    String m_comment;

    /**
     * Building Code
     * m_childnum is always zero when create new node 
     *
     * @param Tag
     * @param value
     * @param Attributes
     * @param Comment
     */
    public BuildNode (String tag) {
    	this(tag,"",new Vector<XmlAttribute>(),"");
    }
    
    public BuildNode (String tag, String value) {
    	this(tag,value,new Vector<XmlAttribute>(),"");
    }
    
    public BuildNode (String tag, String value, Vector<XmlAttribute> attrs) {
    	this(tag, value, attrs, "");
    }
    
    public BuildNode (String tag, String value, Vector<XmlAttribute> attrs, String comment) {
        m_tag = tag;
        m_value = value;
        m_child = new Vector<BuildNode>();
        m_attrs = attrs;
        m_comment = comment;
        m_childnum = 0; 
        m_parent=null;
        root=null;
    }


    public void setAttribute(String name,String val) {
        XmlAttribute att=new XmlAttribute(name, val);
        m_attrs.add(att);
    }
    /**
     * @param child pointer ? 
     */
    public void addChild(BuildNode child) 
    {
        m_child.addElement(child);
        m_childnum++;	
    }

    public int getNumChild()
    {
        return m_childnum;
    }

    /**
     * @param parent pointer ? 
     */
    public void setParent(BuildNode parent) 
    {
        m_parent = parent;
    }

    /**
     * @param  
     */
    public void setRoot(BuildNode theRoot) 
    {
        root = theRoot;
    }

    /**
     * @param String
     * @param Counter
     * @return String
     */
    public String toString(String str, int tabCount)
    {
        boolean nodata = false;
        StringBuffer buf = new StringBuffer(str);
        StringBuffer tab = new StringBuffer();
        StringBuffer line = new StringBuffer();
      
        //build indentation
        for (int i = 0; i < tabCount; i++)
        {
            tab.append(" ");
        }

        if (m_attrs.isEmpty())
        {
            line.append(tab);
            line.append("<");
            line.append(m_tag);
        }
        else
        {
            int size = m_attrs.size();
            StringBuffer atBuf = new StringBuffer(); 

            for (int i = 0; i < size; i++)
            {
                XmlAttribute attrNode = (XmlAttribute) m_attrs.elementAt(i);
                atBuf.append(attrNode.toString());
            }
            line.append(tab);
            line.append("<");
            line.append(m_tag);
            line.append(" ");
            line.append(atBuf.toString());
        }
		
        if (((m_value==null) ||(m_value.equals(""))) && tabCount != 0 && m_childnum == 0)
        {
            line.append("/>\r\n");
            buf.append(line.toString());
            nodata = true;
        }
        else
        {
            line.append(">");
            line.append(m_value);
            buf.append(line.toString());
        }
        if (m_childnum != 0)
        {
           buf.append("\r\n");
        }
		 
        for(int i = 0; i < m_childnum; i++) 
        {
            BuildNode node = (BuildNode) m_child.elementAt(i);   
            String tmpStr = node.toString("",tabCount+1);
            buf.append(tmpStr);
        }

        if (!nodata && m_childnum != 0)
        {
            buf.append(tab);
            buf.append("</");
            buf.append(m_tag);
            buf.append(">\r\n");
        }
        if (!nodata && m_childnum == 0)
        {
            buf.append("</");
            buf.append(m_tag);
            buf.append(">\r\n");
        }
        return buf.toString(); 
    }
}

