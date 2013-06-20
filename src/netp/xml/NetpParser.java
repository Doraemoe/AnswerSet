package netp.xml;

import java.util.Vector;


/**
 * Netp Parser ???
 *
 * @autor UN
 * @version 1.0
 */
public class NetpParser extends ParseTree
{
    ParseNode m_head;
    ParseNode m_body;

    public NetpParser()
    {
        super(); 
    }

    /**
     * @param string ?
     */
    public void netpParse(String str) throws Exception
    {
        parse(str);
        if(hasError){
		    Exception ex=new Exception("xml parser error");
		    throw(ex);
	    }

        m_head = m_root.getChild(0);
        m_body = m_root.getChild(1);
    }

    /**
     * @param options - all, list, default
     */


    public ParseNode getHead()
    {
        return m_head;
    }

    public Vector<String> getHeadList() {
            return m_head.getChildTags();
    }

    public String getHeadValue(String tag) {
        
        Vector<ParseNode> chds=m_head.findChild(tag);
        if(chds.size()==0) return "";

        ParseNode chd=(ParseNode) chds.elementAt(0);

        return chd.getValue();
    }

    /**
     * @param options - all, list , default
     */
    public ParseNode getBody()
    {
        return m_body;
    }
    public Vector<String> getBodyList() {
        return m_body.getChildTags();
    }

    public String getBodyValue(String tag) {
        Vector<ParseNode> chds=m_body.findChild(tag);
        if(chds.size()==0) return "";

        ParseNode chd=(ParseNode) chds.elementAt(0);

        return chd.getValue();
    }


    public ParseNode getSubBody(String tag) {
        Vector<ParseNode> chds=m_body.findChild(tag);
        if(chds.size()==0) return null;
        return chds.elementAt(0);
    }
    public Vector<String> getSubBodyList(String tag) {
        ParseNode chd=getSubBody(tag);
        if(chd==null) return new Vector<String>();
        return chd.getChildTags();
    }
    public String getSubBodyValue(String tag,String chTag) {
        ParseNode chd=getSubBody(tag);
        if(chd==null) return "";
        return chd.getChildValue(chTag);

    }

    public ParseNode getBodyChild(int idx)
    { 
        return m_body.getChild(idx);
    }

    public ParseNode getHeadChild(int idx)
    { 
        return m_head.getChild(idx);
    }
}
