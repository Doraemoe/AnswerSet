package netp.GUI;
import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.TextArea;

public class SimpleInfoBox extends Dialog
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String m_disp;
    TextArea a_disp;

    void orgnizeWindow() 
    {
        a_disp = new TextArea( m_disp, 5, 40, 1 );
        a_disp.setEditable( false );
        add(a_disp, BorderLayout.CENTER);
    }

    public SimpleInfoBox (Frame owner,String title,String disp)
    {
        super(owner,title);
        m_disp = disp;
        setBounds(30, 50, 350, 150);
        setLayout(new BorderLayout());

        orgnizeWindow();
    }
}
