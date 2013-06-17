package netp.GUI;

import java.applet.Applet;
import java.awt.Frame;
import java.awt.Graphics;

import netscape.javascript.JSObject;

public class TimeApplet extends Applet
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Frame fm;
    NetpTime time;
    String stime="";

    public void init()
    {
       fm = new Frame("Time Applet");
       time = new NetpTime(this);
       fm.add(time);
       fm.setBounds(100,100,200,220);
    }

    public void showTime(String t)
    {
       time.inittimes(t);
       fm.setVisible(true);
       fm.toFront();
    }

    public void paint(Graphics g)
    {
       g.drawString(stime,10,10);
    }

    public void receiveTime(String time)
    {
       String disp;
       stime=time;
       repaint();
       fm.setVisible(false);
       JSObject jsroot = JSObject.getWindow(this);
       disp="getTime('"+ time +"')";
       jsroot.eval(disp);
     }

     public void closeTime() 
     {
        fm.setVisible(false);
     }
}

