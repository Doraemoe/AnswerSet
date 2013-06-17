package netp.GUI;

import java.applet.Applet;
import java.awt.Frame;
import java.awt.Graphics;

import netscape.javascript.JSObject;

public class CalApplet extends Applet
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Frame fm;
    NetpCalendar cal;
    String sdate="";

    public void init()
    {
       fm = new Frame("Calendar Applet");
       cal = new NetpCalendar(this);
       fm.add(cal);
       fm.setBounds(100,100,200,220);
    }

    public void showCalendar(String d)
    {
       cal.initdates(d);
       fm.setVisible(true);
       fm.toFront();
    }

    public void paint(Graphics g) 
    {
       g.drawString(sdate,10,10);
    }

    public void receiveDate(String date)
    {
       String disp;
       sdate=date;
       repaint();
       fm.setVisible(false);
       JSObject jsroot = JSObject.getWindow(this);
       disp="getDate('"+ date +"')";
       jsroot.eval(disp);
    }

    public void closeCalendar()
    {
       fm.setVisible(false);
    }
}
