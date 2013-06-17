package netp.GUI;


import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.TextEvent;
import java.awt.event.TextListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.StringTokenizer;
import java.util.Vector;

import netscape.javascript.JSObject;

public class AccountSelecter extends Applet
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Frame fm;
	AccountSelecterP pan;

    public void init()
    {
       fm = new Frame("Account Selecter");
       String accts = getParameter("account");

       pan = new AccountSelecterP(this,accts);
       fm.add(pan);
       fm.setBounds(100,100,250,300);
		fm.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					closeFrame();
				}
			}
		);
    }

    public void showAccount(String d)
    {
       fm.setVisible(true);
       fm.toFront();
    }

    public void paint(Graphics g) 
    {
//       g.drawString(sdate,10,10);
    }

    public void receiveAccount(String actid)
    {
//System.out.println(actid);
       String disp;
       repaint();
       fm.setVisible(false);
       JSObject jsroot = JSObject.getWindow(this);
       disp="getAccount('"+ actid +"')";
       jsroot.eval(disp);
    }

    public void closeFrame()
    {
//System.out.println("close frame");
       fm.setVisible(false);
    }
	static public void main(String arg[]) {
		AccountSelecter as=new AccountSelecter();
		as.init();
		as.showAccount("");
	}
}




//implements ActionListener, MouseListener
class AccountSelecterP extends Panel implements ActionListener,TextListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	java.awt.List list ;
	Vector ids,names;
	Vector qids;
	TextField tid,tname;

	Button b_ok,b_cancel;
	AccountSelecter m_par;

    public AccountSelecterP(AccountSelecter par,String acts)
    {
       super();
       m_par=par;
       init();
       innerinit(acts);
	   setDisplay();

    }



    public void init()
    {
		list=new List() ;
		ids=new Vector();
		names=new Vector();
		qids=new Vector();
		tid=new TextField(10);
		tname=new TextField(10);
		tid.addTextListener(this);
		tname.addTextListener(this);

		b_ok=new Button("OK");
		b_cancel=new Button("Cancel");
		
		b_ok.addActionListener(this);
		b_cancel.addActionListener(this);
		list.addActionListener(this);

		Panel pleft,pright;
		pleft=new Panel();
		pright=new Panel();
		setLayout(new BorderLayout(1,0));
		add(pleft,BorderLayout.NORTH);
		add(pright,BorderLayout.SOUTH);
		pleft.setLayout(new GridLayout(1,0));
		pleft.add(new Label("Code:"));
		pleft.add(tid);
		pleft.add(new Label("Name:"));
		pleft.add(tname);
		pright.add(b_ok);
		pright.add(b_cancel);

		add(list,BorderLayout.CENTER);
	}


    public void actionPerformed (ActionEvent e)
    {
      if((e.getSource()==b_ok)||(e.getSource()==list)) 
      {
		int idx=list.getSelectedIndex();
		if(idx<0) {m_par.closeFrame(); return;}
		if(idx>=qids.size()) {m_par.closeFrame();return;}
		String cd=(String) qids.elementAt(idx);
        m_par.receiveAccount(cd);
        return;
      }
      m_par.closeFrame();
    }

	public void parseAccount(String inp) {
		StringTokenizer st=new StringTokenizer(inp,",:");
		String id,name;
		while(st.hasMoreTokens()) {
			id=st.nextToken();
			name=st.nextToken();
			ids.addElement(id);
			names.addElement(name);
		}
	}
	boolean needDisplayItem(int idx) {
		String sid=tid.getText(),sname=tname.getText();
		String id,name;
		int len;
		try {
			if(sid!=null){
				len=sid.length();
				if(len>0) {
					id=(String)ids.elementAt(idx);
					id=id.substring(0,len);
					if(!id.equals(sid)) return false;
				}
			}
			if(sname!=null) {
				len=sname.length();
				if(len>0) {
					sname=sname.toUpperCase();
					name=(String)names.elementAt(idx);
					name=name.toUpperCase();
					len=name.indexOf(sname);
					if(len<0) return false;
				}
			}
		}
		catch (IndexOutOfBoundsException e) {
			return false;
		}
		return true;
	}
	void setDisplay() {
		qids.clear();
		list.removeAll();
		int i,s=ids.size();
		for(i=0;i<s;++i) {
			if(needDisplayItem(i)) {
				qids.addElement(ids.elementAt(i));
				list.add(""+ids.elementAt(i)+" : "+names.elementAt(i));
			}
		}
		repaint();
	}
	public void textValueChanged(TextEvent e) {
		setDisplay();
	}
	void innerinit(String acts) {
	parseAccount(acts);

}
}







