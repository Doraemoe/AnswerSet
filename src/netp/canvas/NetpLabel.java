package netp.canvas;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import netp.NetpGeneral;
import netp.absshape.AbsString;
public class NetpLabel extends NetpCanvasObject
{
    String m_label = "";

    Font m_font=null;
    int m_fzoom=1;
    int m_fontsize;
    int m_fontstyle;
    String m_fontname;

    JMenuItem m_chglb;

    Rectangle m_rect=null;
    public NetpLabel() 
    {
        super();
        type="label";
    }

    public void setInitPoint(Point p)
    {
        super.setInitPoint(p);
        m_fontsize=14;
        m_fontstyle=Font.PLAIN;
        m_label="Label";
//        constructFont();

    }
    void constructFont()
    {
        int fs=getDrawD(m_fontsize);
        m_font=new Font("Courier New",m_fontstyle,fs);
    }
    public void draw(Graphics g)
    {
        
        if(m_font==null) constructFont();
        int x,y;
        x=getDrawX(p0.x);
        y=getDrawY(p0.y);
        g.setColor(getColor());

        int fs=getDrawD(m_fontsize);
        if(fs<=2) {
            int d=getStringLength();
            g.drawLine(x,y,x+d,y);
        }
        else {
            if(fs!=m_font.getSize())
                constructFont();
            g.setFont(m_font);
            g.drawString(m_label, x, y);
        }

        if(isSelected)
        {
            if(m_rect==null)
                constructLabelRect();
            g.drawRect(getDrawX(m_rect.x),
                getDrawY(m_rect.y),
                m_rect.width,m_rect.height);
        }
    }

    public int toPointDistance(Point po)
    {
        if(m_rect==null)
            constructLabelRect();
        Point p=getOriPoint(po);

        return (int) NetpCanvasGeom.ptToFilledRect(p,m_rect);
    }

    protected void init_menu()
    {

        m_chglb=new JMenuItem("Change Label");
        m_chglb.addActionListener(this);
        m_menu.add(m_chglb);
        super.init_menu();
    }

    public void actionPerformed(ActionEvent e)
    {
        Object o=e.getSource();
        if(o==m_chglb){
            changeLabel();
            return;
        }
        super.actionPerformed( e);
    }

    private void changeLabel()
    {
        Object msg[]=new Object[5];
        JTextField lb=new JTextField(m_label);
        JComboBox cb = new JComboBox();
        for(int i=8;i<40;i+=2) {
            cb.addItem(""+i);
        }
        cb.setSelectedItem(""+m_fontsize);
        JRadioButton btp,btb,bti,btib;
        ButtonGroup bg=new ButtonGroup();
        btp=new JRadioButton("Plain");
        btb=new JRadioButton("Bold");
        bti=new JRadioButton("Italic");
        btib=new JRadioButton("Bold & Italic");
        switch(m_fontstyle) {
            case Font.PLAIN: btp.setSelected(true);break;
            case Font.BOLD: btb.setSelected(true);break;
            case Font.ITALIC: bti.setSelected(true);break;
            default: btib.setSelected(true);break;
        }
        bg.add(btp); bg.add(btb);bg.add(bti);bg.add(btib);
        JPanel pn=new JPanel();
        pn.add(btp); pn.add(btb);pn.add(bti);pn.add(btib);
        msg[0]="Please input the label";
        msg[1]=lb;
        msg[2]="Font size";
        msg[3]=cb;
        msg[4]=pn;

        String[] options={"Ok","Cancel"};
		int ret = JOptionPane.showOptionDialog(
            m_par.getHost(),
            msg, 
            "Label",
		    JOptionPane.DEFAULT_OPTION,                 // option type
		    JOptionPane.INFORMATION_MESSAGE,            // message type
		    null,                                       // optional icon, use null to use the default icon
		    options,                                    // options string array, will be made into buttons
		    options[0]                                  // option that should be made into a default button
        );
		if(ret!=0) return;
        setLabel(lb.getText());
        m_fontsize=NetpGeneral.stringToInt((String)cb.getSelectedItem());
        if(btp.isSelected()) m_fontstyle=Font.PLAIN;
        if(btb.isSelected()) m_fontstyle=Font.BOLD;        
        if(bti.isSelected()) m_fontstyle=Font.ITALIC;        
        if(btib.isSelected()) m_fontstyle=Font.BOLD+Font.ITALIC;        
        constructFont();
        constructLabelRect();
        m_par.repaint();
    }

    private void setLabel(String lab)
    {
        m_label = lab;
    }
    public String toString()
    {
        String s;
        s=super.toString();
        s=s+stringToString("label",m_label);
        s=s+intToString("fontsize",m_fontsize);
        s=s+intToString("fontstyle",m_fontstyle);
        return s;
    }
    public void setInit(NetpCanvasObjectParser ps)
    {
        super.setInit(ps);
        m_label=ps.getStringValue("label");
        m_fontsize=ps.getIntValue("fontsize");
        m_fontstyle=ps.getIntValue("fontstyle");
//        constructFont();
    }

    void constructLabelRect()
    {
        FontMetrics fm=m_par.getFontMetrics(m_font);
        int x0,y0,w0,h0;
        x0=p0.x;
        y0=p0.y-fm.getAscent();
        h0=fm.getAscent()+fm.getDescent();
        w0=fm.stringWidth(m_label);
        if(m_rect==null)
            m_rect = new Rectangle(x0,y0,w0,h0);
        else {
            m_rect.setRect(x0,y0,w0,h0); 
        }
    }
    int getStringLength() {
        Font font=new Font("Courier New",m_fontstyle,m_fontsize);
        FontMetrics fm=m_par.getFontMetrics(font);
        return getDrawD(fm.stringWidth(m_label));

    }
    public void dragTo(Point p1,Point p2)
    {
        super.dragTo(p1,p2);
        constructLabelRect();
    }
    public int findMaxX() {
        FontMetrics fm=m_par.getFontMetrics(m_font);
        return p0.x+fm.stringWidth(m_label);

    }
    public int findMaxY() {
        FontMetrics fm=m_par.getFontMetrics(m_font);
        return p0.y+fm.getDescent();
    }
    public Vector<AbsString> GetAbsShape() {
        Vector<AbsString> v = new Vector<AbsString>();
        v.add(new AbsString(p0.x,p0.y,m_label));
        return v;
    }

}

