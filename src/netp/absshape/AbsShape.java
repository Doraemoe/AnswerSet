package netp.absshape;
import java.awt.*;


public class AbsShape
{
    public int m_x,m_y;
    public Color m_c;

    public void draw(Graphics g) {
        draw(g,0,0);
    }
    public void draw(Graphics g,int offX,int offY) {

    }

    public AbsShape() {
        m_c=Color.black;
        m_x=0;m_y=0;
    }
    public void setColor (Color c) {
        m_c=c;
    }
    public void setPosition(int x,int y) {
        m_x=x;
        m_y=y;
    }
    public void upsideDown(int y0) // 
    {
        m_y=2*y0-m_y;
    }
    public void shift(int dx,int dy) {
        m_x+=dx;
        m_y+=dy;
    }
}