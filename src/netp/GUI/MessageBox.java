package netp.GUI;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageBox extends Dialog implements ActionListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MsgBoxEnabled m_par;
    String m_disp;
    Button b_yes,b_no;
    Label lb;
    String m_id;

    void orgnizeWindow() 
    {
        lb = new Label(m_disp);
        lb.setBackground(new Color(204,204,204));
        add(lb, BorderLayout.CENTER);
    }

    void orgnizePanel() 
    {
        Panel pnl = new Panel();
        b_yes = new Button("Yes");
        b_no = new Button("No");
        pnl.add(b_yes);
        pnl.add(b_no);
        add(pnl,BorderLayout.SOUTH);
        b_yes.addActionListener(this);
        b_no.addActionListener(this);
    }

    public MessageBox (String id,Frame owner,String title,MsgBoxEnabled p,String disp)
    {
        super(owner,title);
        m_id = id;
        m_par = p;
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
        if(e.getSource() == b_yes) 
        {
            m_par.msgReceived(m_id,"yes");
        }
        else
            m_par.msgReceived(m_id,"no");
        dispose();
    }
}
