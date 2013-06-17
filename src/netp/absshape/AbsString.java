package netp.absshape;
import java.awt.*;

public class AbsString extends AbsShape
{
    public String m_str;
    public Font m_fnt;
    public final static int ALIGN_LEFT=0,ALIGN_CENTER=1,ALIGN_RIGHT=2;
    public int m_align;
    public AbsString() {
        super();
        m_str="";
        m_align=ALIGN_LEFT;
    }
    public AbsString(int x,int y,String s, Color c) {
        this(x,y,s);
        setColor(c);
    }
    public AbsString(int x, int y,String s) {
        this();
        setPosition(x,y);
        setString(s);
    }
    public void setFont (Font f) {
        m_fnt=f;
    }
    public void setString(String s) {
        m_str=s;
    }
    public void draw(Graphics g,int offX,int offY) {
        g.setColor(m_c);
        g.drawString(m_str,m_x-offX,m_y-offY);
    }
    public void setAlign(int v) {
        m_align=v;
    }
}