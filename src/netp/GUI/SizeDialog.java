package netp.GUI;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import netp.NetpGeneral;

public class SizeDialog extends Dialog implements ActionListener 
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SizeChangable m_par;
    String m_disp;
    Button b_ok, b_cancel, b_apply;
    Label lb_width, lb_height; 
    TextField tf_width, tf_height;
    String m_id;
    int width, height;

    void orgnizeWindow() 
    {
        Panel dia = new Panel();
        
        lb_width = new Label();
        tf_width = new TextField();
        
        lb_height = new Label();
        tf_height = new TextField();

        dia.add(lb_width);
        dia.add(tf_width);

        add(dia, BorderLayout.NORTH);

        dia.add(lb_height);
        dia.add(tf_height);

        add(dia, BorderLayout.CENTER);
    }

    void orgnizePanel() 
    {
        Panel pnl1 = new Panel();
        Panel pnl2 = new Panel();
        b_ok = new Button("OK");
        b_cancel = new Button("Cancel");
        b_apply = new Button("Apply");

        add(pnl1,BorderLayout.CENTER);

        pnl2.add(b_ok);
        pnl2.add(b_cancel);
        pnl2.add(b_apply);

        add(pnl2,BorderLayout.SOUTH);

        b_ok.addActionListener(this);
        b_cancel.addActionListener(this);
        b_apply.addActionListener(this);
    }

    public SizeDialog ( Frame fm, int cw, int ch, SizeChangable p)
    {
     //   super(owner, title);
        super( fm, "Change Size" );
        m_par = p;
        setLayout(new BorderLayout());
        setModal(true);
        
        orgnizePanel();
        orgnizeWindow();

        tf_width.setText( NetpGeneral.intToString(cw));
        lb_width.setText("Canvas Width");

        tf_height.setText( NetpGeneral.intToString(ch));
        lb_height.setText("Canvas Height");


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
            width = NetpGeneral.stringToInt(tf_width.getText());
            height = NetpGeneral.stringToInt(tf_height.getText());
            m_par.setCanvasSize( width, height );
            dispose();
        }

        if(e.getSource() == b_cancel)
        {
            dispose();
        }

        if(e.getSource() == b_apply) 
        {
            width = NetpGeneral.stringToInt(tf_width.getText());
            height = NetpGeneral.stringToInt(tf_height.getText());
            m_par.setCanvasSize( width, height );
        }
    }
}
