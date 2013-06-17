package netp.xml;

import java.util.Vector;

/**
 * Parse Tree Comment .. 
 *
 * @author UN
 * @version 1.0
 */
public class ParseTree 
{
    ParseNode m_root;
    String m_str="";
    int m_strpoint=0;
    boolean hasError = false;
    String errorMsg = "";

	public boolean hasError()
	{
		return hasError;
	}
	public String getErrorMessage()
	{
		return errorMsg;
	}
	public ParseNode getRoot()
	{
		return m_root;
	}
    public ParseTree() 
    {
        m_root = new ParseNode(this);
    }
   
    /**
     * @param ??
     * @return Error
     */
    public void parse(String inp) 
    {
        hasError = false;
        if(inp==null) 
        {
            hasError=true;
            errorMsg="Input string is null";
            return;
        }
        if(inp.length()==0) 
        {
            hasError=true;
            errorMsg="Input string is 0 length";
            return;
        }
        m_str = inp;
        m_strpoint = 0;
        m_root.parse();
    }
   
    /**
     * Gets the next tag 
     *
     * @return The required tag
     */
    public String getNextTag() 
    {
        boolean validTag = false;
        String tag = "";
        while (!validTag) 
        {
            int pos1 = m_str.indexOf("<", m_strpoint);
            int pos2 = m_str.indexOf(">", pos1 + 1);

            if ((pos1 == -1) || (pos2 == -1)) 
            { 
                hasError = true;
                errorMsg = "Can't find the tag";
                break; 
            }  

            tag = m_str.substring(pos1 + 1, pos2);

            //System.out.println("tag is: " + tag);

            m_strpoint = pos2 + 1;
            String firstChar = tag.substring(0,1);

            if (firstChar.equals("?") || firstChar.equals("!"))
                validTag = false;
            else
                validTag = true;
        }
        return tag;
    }
   
   
    /**
     * gets the Next Value
     *
     * @return Value - String 
     */
    public String getNextValue() 
    {
        int pos1 = m_str.indexOf ("<", m_strpoint);
        String str = "";
        if (pos1 == -1) 
        { 
            hasError = true;
            errorMsg = "Can't find the value";
            return str;
        }

        str = m_str.substring(m_strpoint, pos1);
        m_strpoint= pos1;
        return str;
    }
	
    public String getDocType()
    {
        return m_root.getTag();
    }

    public int getTotalChild()
    {
        return m_root.getTotalChild();
    }
	public int getChildNum()
	{
        return m_root.getChildNum();
	}
	public Vector getChildTags()
	{
        return m_root.getChildTags();
	}

    /**
     * Gets part list
     *
     * @param Level
     * @return list
     */
    public Vector getPartList(int level)
    {
        Vector partlist = new Vector();
        partlist = m_root.getPartList(level);
        return partlist;
    }
	
    /**
     * Gets list for part 
     *
     * @param part
     * @return value list
     */
    public Vector getListForPart(int part)
    {
        Vector vallist = new Vector();
        vallist = m_root.getListForPart(part);
        return vallist;
    }
   
    /**
     * Gets item
     *
     * @param part
     * @param field
     * @return Value
     */
    public String getItem(int part, String field)
    {
        String val = m_root.getItem(part, field);
        return val;
    }
	
    
    /**
     * converts object to string
     */
    public String toString() 
    {
       return m_root.toString();
    }
}
