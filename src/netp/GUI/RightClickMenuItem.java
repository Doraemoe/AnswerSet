package netp.GUI;
import java.awt.Graphics;
import java.awt.Point;

public abstract class RightClickMenuItem 
{
    int m_height;
    int m_min_width;
    int m_width; // assigned by the parent.

    RightClickMenu m_par;
    int m_status; // 0 disable, 1 normal, 2 mouse on
    final static int DISABLE=0,NORMAL=1,MOUSE_ON=2;   

    String m_index;
    int m_x,m_y;  // the drawing point

    public boolean isMouseOn(Point p) 
    {
        if(p.x < m_x) return false;
        if(p.y < m_y) return false;
        if(p.y > (m_y + m_height)) return false;
        if(p.x > (m_x + m_width)) return false;
        return true;
    }
    
    public boolean isClicked(Point p)
    {
        if(m_status == DISABLE) return false;
        return isMouseOn(p);
    }

    public int checkChangeStatus(Point p) 
    {
        int oldStatus = m_status;
        if(m_status == DISABLE)
            return 0;
        if(isMouseOn(p)) 
        {
            m_status = MOUSE_ON;
        }
        else m_status = NORMAL;
        if(m_status == oldStatus) return 0;
        return 1;
    }

    public void set_status(int s) 
    {
        m_status = s;
    }

    public void draw(Graphics g) 
    {    }
    
    void calSize()
    {     }
    
    public RightClickMenuItem(String idx) 
    {
        m_index = idx;
        m_x=RightClickMenu.LEFT_MARGIN;
    }

    public String getIndex() 
    {
        return m_index;
    }
    
    public void setY(int y) 
    {
        m_y=y;
    }
    
    public int getHeight() 
    {
        return m_height;
    }

    public int getWidth() 
    {
        return m_width;
    }
    
    public int getMinWidth() 
    {
        return m_min_width;
    }
    
    public void setWidth(int w) 
    {
        m_width=w;
    }
}
