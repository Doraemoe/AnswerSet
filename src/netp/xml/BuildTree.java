package netp.xml;
import java.util.Vector;


/**
 * Building the Tree .. ??  Seems to be very similar to BuildNode ...
 *
 * @author
 * @version
 */
public class BuildTree 
{
    BuildNode m_root;      /* the root node */
    BuildNode m_node;   /* the pointer of current node */
    String m_tag;
    String m_version    = "1.0";
    String m_comment;
    String m_value;
    String m_str;
    int m_strpoint;
    Vector<XmlAttribute> m_attrs;

    /**
     * Building Code
     *
     * @param Tag
     * @param Attributes
     * @param Comment
     * @param Value
     */
    public BuildTree(String tag)
    {
        this(tag, new Vector<XmlAttribute>(), "", "");
    }
    public BuildTree (String tag, Vector<XmlAttribute> attrs, String comment, String value)
    {
        m_tag = tag;
        m_attrs = attrs;
        m_comment = comment;
        m_value = value;
        m_root = new BuildNode(tag, value, attrs, comment);
        m_node = m_root;
    }

    /**
     * @param child pointer
     */
    public BuildNode getRoot() {
        return m_root;
    }
    public void appendChild (BuildNode child)
    {
        child.setParent(m_node);
        child.setRoot(m_root);
        m_node.addChild(child);	
    }

    public void downTree ()
    {
        int num = m_node.m_childnum;
        BuildNode child = (BuildNode) m_node.m_child.elementAt(num - 1);
        m_node = child;	
    }

    public void upTree ()
    {
        BuildNode parent = m_node.m_parent;
        m_node = parent;
    }

    /**
     * @param child index
     * @return Node
     */
    public BuildNode getChild (int childIdx)
    {
        BuildNode node = (BuildNode) m_root.m_child.elementAt(childIdx);
        return node;
    }

    /**
     * @return child 
     */
    public int getTotalChild ()
    {
        int numChild = m_root.getNumChild();
        return numChild;
    }

    /**
     * @param File name
     * @return String
     */
    public String toString ()
    {
        String version = m_version;

        StringBuffer buf = new StringBuffer("<?xml version=\"");
        buf.append(version);
        buf.append("\"");
        buf.append("?>\r\n");

      //  String tmpStr = buf.toString();		
        String tmpStr = "";
        buf.append(m_root.toString(tmpStr, 0));
        return buf.toString(); 
    }


    /**
     * @param File
     */
    public void toFile (String file)
    {
       // String ret = this.toString("");
       // fp = fopen (file, "w");
       // fwrite (fp, ret);
       // fclose (fp);
    }
}

