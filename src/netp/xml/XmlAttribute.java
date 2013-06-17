package netp.xml;

public class XmlAttribute {
    String m_key;
    String m_val;

    public XmlAttribute(String k,String v) {
        m_key=k;
	m_val=v;
    }
    public String getKey() {
	return this.m_key;
    }	
    public String getValue() 
    {
        return this.m_val;
    }

    public String toString() 
    {
        StringBuffer buf = new StringBuffer();
            String k = this.m_key;
            String v = this.m_val;
            buf.append(k);
            buf.append("=\"");
            buf.append(v);
            buf.append("\" "); 
        return buf.toString();  
    }
}

