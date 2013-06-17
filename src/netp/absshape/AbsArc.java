package netp.absshape;
import java.awt.Color;

public class AbsArc extends AbsShape
{
    public int m_h,m_w;
    public Color m_fillColor;
    public AbsArc() {
        super();
        m_c=Color.black;
        m_fillColor=Color.black;
        m_x=0;m_y=0;
    }
    public AbsArc(int x,int y, int w,int h) {
        this();
        setPosition(x,y);
        setSize(w,h);
    }
    public void setFillColor (Color c) {
        m_fillColor=c;
    }
    public void setSize(int w,int h) {
        m_w=w;
        m_h=h;
    }
    public void upsideDown(int y0) // 
    {
        super.upsideDown(y0);
        m_h=-m_h;
    }
}