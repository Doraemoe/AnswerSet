package netp.canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import netp.absshape.AbsLine;
import netp.absshape.AbsRect;

public class NetpRect extends NetpLine
{
    protected JMenuItem m_outlook,m_fillcolor;

    protected Color m_bgColor=Color.white;
    protected boolean m_hasShade=false;
    protected boolean m_filled=false;
    protected boolean m_doubleLine=false;

    public int getWidth() {
        return p2.x-p0.x;
    }
    public int getHeight() {
        return p2.y-p0.y;
    }

    public void setSize(int w,int h) {
        p2.x=p0.x+w;
        p2.y=p0.y+h;
    }

    public Color getBGColor() {
        if(m_bgColor==null) return Color.white;
        return m_bgColor;
    }
    public void setBGColor(Color c){
    	m_bgColor=c;
    }
    public boolean hasShade() {
        return m_hasShade;
    }
    public void setHasShade(boolean b){
    	m_hasShade=b;
    }
    public void setIsFilled(boolean b){
    	m_filled=b;
    }
    public boolean isFilled() {
        return m_filled;
    }
    public boolean isDoubleLine() {
        return m_doubleLine;
    }
    public void setIsDoubleLine(boolean b){
    	m_doubleLine=b;
    }
    public NetpRect() 
    {
        super();
        type="rect";
    }
 
    public int toPointDistance(Point po)
    {
        Point p=getOriPoint(po);
        return (int) NetpCanvasGeom.ptToFilledRect(p.x,p.y,p0.x,p0.y, p2.x, p2.y);
    }

    protected void rectDraw(Graphics g) {
        int x0,y0,x2,y2;
        x0=getDrawX(p0.x);
        y0=getDrawY(p0.y);
        x2=getDrawX(p2.x);
        y2=getDrawY(p2.y);
        int d=getDrawD(5),d0=getDrawD(3),d1=getDrawD(6);
        if(hasShade()) {
            g.setColor(new Color(125,125,125,125));
            g.fillRect(x0+d,y2,x2-x0-d,d);
            g.fillRect(x2,y0+d,d,y2-y0);
        }
        if(isFilled()) {
            g.setColor(getBGColor());
            g.fillRect(x0,y0,x2-x0,y2-y0);
        }
        g.setColor(getColor());
        g.drawRect(x0,y0,x2-x0,y2-y0);

        if(isDoubleLine()) 
            g.drawRect(x0+d0,y0+d0,x2-x0-d1,y2-y0-d1);

        if(isSelected) 
        {
            g.drawRect(x0-3,y0-3,5,5);
            g.drawRect(x2-3,y2-3,5,5);
        }
    }
    public void draw(Graphics g)
    {
        rectDraw(g);
    }
    protected void init_menu() {
        super.init_menu();
        m_outlook=new JMenuItem("Outlook...");
        m_menu.add(m_outlook);
        m_outlook.addActionListener(this);

        m_fillcolor=new JMenuItem("Filled Color");
        m_menu.add(m_fillcolor);
        m_fillcolor.addActionListener(this);

    }
    public void actionPerformed(ActionEvent e) {
        Object s=e.getSource();
        if(s==m_outlook) {
            changeOutlook();
            return;
        }
        if(s==m_fillcolor) {
            changeFilledColor();
            return;
        }

        super.actionPerformed(e);
    }
    protected void changeOutlook() {
        Object[] message = new Object[3];

		JCheckBox hasShade=new JCheckBox("Has Shade",m_hasShade);
        JCheckBox hasFilled=new JCheckBox("Has Filled Color",m_filled);
        JCheckBox doubleLine = new JCheckBox("Double Line",m_doubleLine);

        message[0] = hasShade;
		message[1] = hasFilled;
		message[2] = doubleLine;

        String[] options = {
		    "Ok",
		    "Cancel"
		};
        int result = JOptionPane.showOptionDialog(
		    null,
		    message,                                    // the dialog message array
		    "Rectangle Feature", // the title of the dialog window
		    JOptionPane.DEFAULT_OPTION,                 // option type
		    JOptionPane.INFORMATION_MESSAGE,            // message type
		    null,                                       // optional icon, use null to use the default icon
		    options,                                    // options string array, will be made into buttons
		    options[0]                                  // option that should be made into a default button
		);
        if(result==0) {
            m_hasShade=hasShade.isSelected();
            m_filled=hasFilled.isSelected();
            m_doubleLine=doubleLine.isSelected();
            m_par.repaint();
		}
    }
    public void changeFilledColor() {
        if(m_bgColor==null) m_bgColor=Color.white;
        Color tmp=JColorChooser.showDialog(null,
                               "Select Filled Color",
                               m_bgColor);
        if(tmp!=null) {
            m_bgColor=tmp;
            m_par.repaint();
        }    
    }

    public void showMenu(Point p)
    {
        if(m_menu==null) {
            m_menu = new NetpPopupMenu();            
            init_menu();
        }
        if(m_filled) m_fillcolor.setEnabled(true);
        else m_fillcolor.setEnabled(false);
        m_menu.show(m_par,p.x,p.y);
    }
    public String toString() {
        String s=super.toString();
        if(!m_bgColor.equals(Color.white))
            s=s+colorToString("bgcolor",m_bgColor);
        if(m_hasShade)
            s=s+booleanToString("hasShade",m_hasShade);
        if(m_filled)
            s=s+booleanToString("hasFilledColor",m_filled);
        if(m_doubleLine)
            s=s+booleanToString("hasDoubleLine",m_doubleLine);
        return s;

    }
    public void setInit(NetpCanvasObjectParser ps)
    {
        m_bgColor=ps.getColorValue("bgcolor",Color.white);
        m_hasShade=ps.getBooleanValue("hasShade",false);
        m_filled=ps.getBooleanValue("hasFilledColor",false);
        m_doubleLine=ps.getBooleanValue("hasDoubleLine",false);
        super.setInit(ps);
    }
    protected Vector getRectAbsShape() {
        AbsRect al=new AbsRect();
        al.setPosition(p0.x,p0.y);
        al.setColor(getColor());
        al.setSize(p2.x-p0.x,p2.y-p0.y);

        if(isFilled()) { // this part doesn't work well
            al.setFillColor(getBGColor());
        }

        Vector v = new Vector();
        v.add(al);

        if(isDoubleLine()) {
            al = new AbsRect();
            al.setPosition(p0.x+3,p0.y+3);
            al.setColor(getColor());
            al.setSize(p2.x-p0.x-6,p2.y-p0.y-6);
            v.add(al);

        }
        if(hasShade()) {
            AbsLine ll;
            ll=new AbsLine();
            ll.setPosition(p0.x+5,p2.y+3);
            ll.setSize(p2.x-p0.x,0);
            ll.setColor(Color.lightGray);
            ll.setLineWidth(5);
            v.add(ll);
            ll=new AbsLine();
            ll.setPosition(p2.x+3,p0.y+5);
            ll.setSize(0,p2.y-p0.y);
            ll.setColor(Color.lightGray);
            ll.setLineWidth(5);
            v.add(ll);
        }

        return v;
    }
    public Vector GetAbsShape() {
        return getRectAbsShape();
    }
    public boolean preferDrawFirst() {
        return true;
    }

}
