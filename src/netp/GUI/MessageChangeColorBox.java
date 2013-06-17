package netp.GUI;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MessageChangeColorBox extends Dialog implements ActionListener, ColorChangable 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MsgChgColorEnabled m_par;
    String m_disp;
    Button b_ok, b_cancel, b_apply, bt_color;
    TextField t_color;
    Color drawcolor=Color.black,bkcolor=Color.white;

    Color ncolor;

    boolean canopencolorbox = true;

    void orgnizePanel() 
    {
        Panel pnl1 = new Panel();
        Panel pnl2 = new Panel();

        bt_color = new Button( "Change Color" );

        b_ok = new Button("OK");
        b_cancel = new Button("Cancel");
        b_apply = new Button("Apply");

        pnl1.add(bt_color);
        add(pnl1,BorderLayout.CENTER);

        pnl2.add(b_ok);
        pnl2.add(b_cancel);
        pnl2.add(b_apply);
        add(pnl2,BorderLayout.SOUTH);

        b_ok.addActionListener(this);
        b_cancel.addActionListener(this);
        b_apply.addActionListener(this);
        bt_color.addActionListener(this);
    }

    public MessageChangeColorBox( Frame fm, String title,MsgChgColorEnabled p, String disp)
    {
        super( fm, title);
        setLayout(new BorderLayout());
        setModal(true);
        
        orgnizePanel();

        m_par = p;
        m_disp = disp;

        Toolkit tk = getToolkit();
        int h,w;
        h = tk.getScreenSize().height;
        w = tk.getScreenSize().width;
        setBounds((w - 400) / 2, (h - 150) / 2, 400, 150);
    }
    
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource() == b_ok) 
        {
            m_par.chgColorReceived( ncolor );
            dispose();
        }

        if(e.getSource() == b_cancel)
        {
            dispose();
        }

        if(e.getSource() == b_apply) 
        {
            m_par.chgColorReceived( ncolor );
        }

        if(e.getSource() == bt_color )
        {
          if(canopencolorbox) 
          {
            canopencolorbox=false;
            Frame fm=new Frame();
            ColorDialog cd=new ColorDialog(fm,drawcolor, MessageChangeColorBox.this);
            fm.setVisible(true);
            cd.setVisible(true);
          }
        }
    }

    public void setColor(Color c)
    {
       drawcolor=c;
       ncolor=c;
    }

    public void releaseColorHold() 
    {
       canopencolorbox=true;
    }
}
