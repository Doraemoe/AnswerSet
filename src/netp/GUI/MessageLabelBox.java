package netp.GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;

public class MessageLabelBox extends MessageBox
{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	TextField tf;

    void orgnizeWindow() 
    {
        lb=new Label(m_disp);
        lb.setBackground(new Color(204,204,204));
        tf=new TextField();
        tf.setBackground(new Color(204,204,204));
        Panel pl=new Panel();
        pl.add(lb);
        pl.add(tf);
        add(pl, BorderLayout.CENTER);
    }

    public MessageLabelBox (String id,Frame owner,String title,MsgBoxEnabled p,String disp,String def_str)
    {
        super (id,owner,title,p,disp);
        tf.setText(def_str);
    }

    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource() == b_yes) 
        {
            m_par.msgReceived(m_id,"yes_"+tf.getText());
        }
        else
            m_par.msgReceived(m_id,"no");
        dispose();
    }
}
