package netp.xmldocument;

/**
 * Sets up error responses
 *
 * @author Dennis CHEN 
 * @version 1.0
 */
public class NetpAckRespond extends NetpDocument 
{
    private String m_error_msg="Acknowledge";
    private String m_error_code="0";

    /**
     * @param error message
     */
    public NetpAckRespond( String t_dir ) 
    {
        super("ACK","Res", t_dir );
    }
 
    public NetpAckRespond(String t_dir, String t_code, String t_msg) 
    {
        this(t_dir);
        setAckCode(t_code);
        setAckMsg(t_msg);
    }

    public void setAckMsg( String t_msg )
    {
        m_error_msg = t_msg;
    }

    public void setAckCode( String t_code )
    {
        m_error_code = t_code;
    }
   
    /**
     * creates the body area of the message
     */
    protected void pCreateBody() 
    {
        addChildNode("code",m_error_code);
        addChildNode("msg",m_error_msg);
    }
}
