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

public class MessageBoxSimple extends Dialog implements ActionListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String m_disp;
    Button b_yes;
    Label lb;

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
        pnl.add(b_yes);
        add(pnl,BorderLayout.SOUTH);
        b_yes.addActionListener(this);
    }

    public MessageBoxSimple (Frame owner,String title,String disp)
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
