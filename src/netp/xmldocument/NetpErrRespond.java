package netp.xmldocument;

/**
 * Sets up error responses
 *
 * @author Dennis CHEN 
 * @version 1.0
 */
public class NetpErrRespond extends NetpDocument 
{
    private String m_error_msg="Error";
    private String m_error_code="0";

    /**
     * @param error message
     */
    public NetpErrRespond( String t_dir ) 
    {
        super("ERR","Res", t_dir );
    }

    public NetpErrRespond(String t_dir, String t_code, String t_msg) 
    {
        this(t_dir);
        setErrCode(t_code);
        setErrMsg(t_msg);
    }

    public void setErrMsg( String t_msg )
    {
        m_error_msg = t_msg;
    }

    public void setErrCode( String t_code )
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
