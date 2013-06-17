package netp.absshape;
import java.awt.*;


public class AbsLine extends AbsShape
{
    public int m_h,m_w;
    public int m_lineWidth;
    public int m_lineCap;
    public AbsLine() {
        super();
        m_h=0;
        m_w=0;
        m_lineWidth=1;
        m_lineCap=0;
    }
    public AbsLine(int x,int y,int w,int h) {
        super();
        setPosition(x,y);
        setSize(w,h);
    }
    public AbsLine(int x,int y,int w,int h,Color c) {
        this(x,y,w,h);
        setColor(c);
    }
    public void setSize(int w,int h) {
        m_h=h;
        m_w=w;
    }
    public void setLineWidth(int w) {
        m_lineWidth=w;
    }
    public void setLineCap(int c) {
        m_lineCap=c;
    }
    public void upsideDown(int y0) // 
    {
        super.upsideDown(y0);
        m_h=-m_h;
    }
    public void draw(Graphics g){
    }
    public void draw(Graphics g,int offX,int offY){
        g.setColor(m_c);
        g.drawLine(m_x-offX,m_y-offY,m_w+m_x-offX,m_h+m_y-offY);

    }
}