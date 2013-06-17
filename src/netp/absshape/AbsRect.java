package netp.absshape;
import java.awt.*;


public class AbsRect extends AbsLine
{
    public Color fillColor;
    public AbsRect() {
        super();
        fillColor=null;
    }
    public AbsRect(int x,int y,int w,int h,Color c) {
        this(x,y,w,h);
        setColor(c);
    }
    public AbsRect(int x,int y, int w, int h) {
        this();
        setPosition(x,y);
        setSize(w,h);

    }
    public void setFillColor(Color c) {
        fillColor=c;
    }

    public void draw(Graphics g,int offX,int offY){
        g.setColor(m_c);
        g.drawRect(m_x-offX,m_y-offY,m_w,m_h);
    }

}