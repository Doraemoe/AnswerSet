package netp.xml;
import java.util.Vector;

public class ParseNode 
{
    Vector<ParseNode> m_child;
    int m_childnum;
    boolean m_parseover = false;
    String m_tag;
    String m_value;
    Vector<XmlAttribute> m_attrs;
    ParseTree m_tree;

    ParseNode m_parent;

    final static int START=1, END_EMPTY=2, END=3, XML=4, COMMENT=5;

    public String getAttribute(String id)
    {
        int i,s=m_attrs.size();
        XmlAttribute ab;
        for(i=0;i<s;++i)
        {
            ab=(XmlAttribute) m_attrs.elementAt(i);
            if(id.equals(ab.getKey()))
                return ab.getValue();
        }
        return "";
    }
    public ParseNode(ParseTree tree) 
    {
        this(tree,null);
    }

    public ParseNode(ParseTree tree, ParseNode par) 
    {
        m_tree = tree;
        m_tag="";
        m_value="";
        m_attrs=new Vector<XmlAttribute>();
        m_child= new Vector<ParseNode>();
        m_childnum=0;
    }

    public void parse() 
    {
        int type;
        String tag;

        if (m_tag.length()==0) 
        {
            m_tag = m_tree.getNextTag();
        }
        if (m_parseover) return;
        if (m_tree.hasError) return;
        type = locateType(m_tag);

        if ((type == START) || (type==END_EMPTY))
        {
            locateAttrs(m_tag);
            m_tag = locateTag(m_tag);
        }
        if (type == END_EMPTY)
        {
            m_parseover = true;
            return;
        }

        m_value = m_value + m_tree.getNextValue();
        if (m_tree.hasError)
            return;
        tag = m_tree.getNextTag();
		
        if (m_tree.hasError)
            return;
        if (tag.equals ("/" + m_tag)) 
        {
            m_parseover=true;
            return;
        }

        ParseNode node = new ParseNode(m_tree);

        m_child.addElement(node);
        m_childnum = m_child.size();

        node.m_tag = tag;
        node.parse();

        parse();
    }

    public Vector<XmlAttribute> locateAttrs (String tag)
    {
        Vector<XmlAttribute> attrs;
        int offset;
        int pos, pos1, pos2;
        String attrsStr;

        attrs = new Vector<XmlAttribute>();
        pos = tag.indexOf(" ", 0);

        if (pos == -1)
            return attrs;
        else
        {
            int len = tag.length();
            String lastChar = tag.substring(len-1, len);
            if (lastChar.equals("/"))
                offset = 2;  
            else
                offset = 0;
            attrsStr = tag.substring(pos+1, len - offset);
            offset = 0;
            boolean flag = true;
            while (flag)
            {
                pos1 = attrsStr.indexOf("=", offset);
                if (pos1 == -1)
                {
                    break;
                }
                // Get attribute key
                String key = attrsStr.substring(offset, pos1).trim();

                offset = pos1 + 1;
                pos2 = attrsStr.indexOf("\"", offset);
                offset = pos2 + 1;
                pos2 = attrsStr.indexOf("\"", offset);	

                // Get attribute value
                String val = attrsStr.substring(pos1 + 2, pos2).trim();
       
                // create object attribute to add key and value
                XmlAttribute atNode = new XmlAttribute(key,val);
                m_attrs.addElement(atNode);

                offset = pos2+1;
            }
            return attrs;
        }	
    }

    public String locateTag (String tag)
    {
        int pos = tag.indexOf(" ", 0);
        if (pos == -1)
        {
            int len = tag.length();
            String lastChar = tag.substring(len-1, len);
            if (lastChar.equals("/"))
                return tag = tag.substring(0, len-1);
            return tag;
        }
        else
        {
            tag = tag.substring(0, pos);
            return tag;	
        }		
    }

    public int locateType (String tag)
    {
        int type;
        String firstChar = tag.substring(0,1);
        if (firstChar.equals("/"))
            type = END;
        else if (firstChar.equals("?"))
            type = XML;
        else if (firstChar.equals("!"))
            type = COMMENT;
        else 
        {
            int len = tag.length();
            String lastChar = tag.substring(len-1, len);
            if (lastChar.equals("/"))
                type = END_EMPTY;
            else
                type = START;
        } 
        return type;
    }

    public String getTag()
    {
        return m_tag;
    }
    /** return the number of children of this node */
    public int getTotalChild()
    {
        return m_child.size();
    }

    public int getChildNum() {
        return m_childnum;
    }
    /** return the vector of all the children's tag */

    public Vector<String> getPartList(int level)
    {
        return getChildTags();
    }
    
    public Vector<String> getChildTags() {
        Vector<String> partlist = new Vector<String>();
        int num = m_child.size();

        for(int i = 0 ; i < num ; i++) 
        {
            ParseNode node = (ParseNode) m_child.elementAt(i);

            String tag =node.getTag();
            partlist.addElement(tag);
        }
        return partlist;

    }


    public String getValue() {
        return m_value;
    }
    public Vector<String> getListForPart(int part)
    {
        Vector<String> childvalue = new Vector<String>();
        if (part == 0)
        {
            int num = m_child.size();
            String value = "";
            for(int i = 0 ; i < num ; i++) 
            {
                ParseNode node = (ParseNode) m_child.elementAt(i);
                value = node.getValue();
                childvalue.addElement(value);       //childvalue.[$p_tag] = $p_value;
            }
            return childvalue;
        }
        else
        {
            int num = m_child.size();
            if (num == 0)
                return childvalue;
            if (part >= num)
                return childvalue;

            ParseNode node = (ParseNode) m_child.elementAt(part);
            childvalue = node.getListForPart(0);
            return childvalue;
        }
    }

    /* return a vector of children with the input tag */

	public ParseNode findFirstChild(String tag)
	{
		Vector<ParseNode> v=findChild(tag);
		if(v.size()==0) return null;
		return (ParseNode) v.elementAt(0);
	}
    public Vector<ParseNode> findChild(String tag) {
        Vector<ParseNode> ret=new Vector<ParseNode>();
        for(int i=0;i<m_childnum;++i) {
            if(getChild(i).getTag().equals(tag)) {
                ret.addElement(getChild(i));
            }
        }
        return ret;
    }

    public String getItem(int part, String field)
    {
        int num = m_child.size();
        String tag, val;
        for(int i = 0; i < num; i++) 
        {
            ParseNode node = (ParseNode) m_child.elementAt(i);
            
            tag = node.m_tag;
            val = node.m_value;
            if (tag.equals(field))
            {
                return val;
            }
        }
        ParseNode subnode = (ParseNode) m_child.elementAt(part);
        val = subnode.getItem(part, field);

        return val;
    }
    /* return the value of the first child match the tag */
    public String getChildValue(String tag) {
        Vector<ParseNode> chds=findChild(tag);
        if(chds.size()==0) return "";
        ParseNode chd = chds.elementAt(0);
        return chd.getValue();
    }
    
    public String toString()
    {
        StringBuffer buf = new StringBuffer();

        buf.append("<");
        buf.append(m_tag); 
        if (m_attrs.size() != 0)
        {
            buf.append(" ");
            int size = m_attrs.size(); 
            for (int i = 0; i < size; i++)
            {
                XmlAttribute attrNode = (XmlAttribute) m_attrs.elementAt(i);
                buf.append(attrNode.toString());
            } 
                        
        }
        buf.append(">\n");

        for(int i = 0; i < m_childnum; i++) 
            buf.append(getChild(i).toString());
        buf.append(m_value.trim());
        buf.append("\n");
        buf.append("</");
        buf.append(m_tag);
        buf.append(">\n");

        return buf.toString();
    }

    public ParseNode getChild(int i) {
        return (ParseNode) m_child.elementAt(i);
    }
    public ParseNode getParent() {
        return m_parent;
    }

}

