package netp.GUI;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;



public class MessageDialog extends Dialog implements ActionListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	MsgBoxEnabled m_par;
    String m_disp;
    Button b_exit, b_save, b_netp;
    String m_id;
    Label lb, lb1;
    TextField tf;
    Checkbox ch;


    void orgnizeWindow() 
    {
        setLayout(null);
        lb = new Label("Grid Color");
        lb.setBounds(20,40,80,20);
        add(lb);

        tf = new TextField();
        tf.setBounds(110,40,50,20);
        tf.setText("jc");
        add(tf);

        setLayout(null);
        lb1 = new Label("Show Grid");
        lb1.setBounds(20,70,80,20);
        add(lb1);
      
        ch = new Checkbox("one", null, true);
        ch.setBounds(110,70,20,20);
        add(ch);
     }

    void orgnizePanel() 
    {
        Panel pnl = new Panel();
        b_save = new Button("Save");
        b_exit = new Button("Exit");
        b_netp = new Button("Sound");

        pnl.add(b_exit);
        pnl.add(b_save);
        pnl.add(b_netp);
        pnl.setBounds(10,150,180,30);
        add(pnl);
        b_exit.addActionListener(this);
        b_save.addActionListener(this);
        b_netp.addActionListener(this);
    }

    public MessageDialog (String id,Frame owner,String title, MsgBoxEnabled p, String disp, String def_str)
    {
  
        super(owner, title);
        setLayout(new BorderLayout());
        setModal(true);
        
        orgnizeWindow();
        orgnizePanel();

        m_id = id;
        m_par = p;
        m_disp = disp;
        Toolkit tk = getToolkit();
        int h,w;
        h = tk.getScreenSize().height;
        w = tk.getScreenSize().width;
        setBounds((w - 200) / 2, (h - 250) / 2, 200, 250);
    

    }

//    AudioClip ac;
//    ac = getAudioClip(getDocumentBase(),"sounds/CHORD.WAV");
//    ac.play();
    
    public void actionPerformed(ActionEvent e) 
    {
        if(e.getSource() == b_netp)
        {
            
        }
        if(e.getSource() == b_save) 
        {
            m_par.msgReceived(m_id, "yes");
            // m_par.msgReceived(m_id,"yes_" + tf.getText());
        }
        
        if(e.getSource() == b_exit) 
            m_par.msgReceived(m_id, "no");

        dispose();
    }

}


