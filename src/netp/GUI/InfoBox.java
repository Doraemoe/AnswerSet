package netp.GUI;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class InfoBox extends Dialog implements ActionListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String m_disp;
    Button b_ok;
    TextArea a_disp;

    void orgnizeWindow() 
    {
        a_disp = new TextArea( m_disp, 5, 40, 1 );
        a_disp.setEditable( false );
        add(a_disp, BorderLayout.CENTER);
    }

    void orgnizePanel() 
    {
        Panel pnl = new Panel();
        b_ok = new Button("OK");
        pnl.add(b_ok);
        add(pnl,BorderLayout.SOUTH);
        b_ok.addActionListener(this);
    }

    public InfoBox (Frame owner,String title,String disp)
    {
        super(owner,title);
        m_disp = disp;
        Toolkit tk = getToolkit();
        int h,w;
        h = tk.getScreenSize().height;
        w = tk.getScreenSize().width;
        setBounds((w - 350) / 2, (h - 150) / 2, 350, 150);
        setLayout(new BorderLayout());
        setModal(true);

        orgnizePanel();
        orgnizeWindow();
    }

    public void actionPerformed(ActionEvent e) 
    {
        dispose();
    }
}
