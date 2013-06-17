package netp.canvas;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.util.Hashtable;
abstract public class NetpCanvasSubObject
{
    protected NetpCanvasObject m_par;
    protected String m_sub_id;
    protected String m_sub_type;

    protected int m_status;

    final static public int MINDIS=3;

    protected void init_menu()
    {
    }

    public NetpCanvasSubObject(String subid,String type,NetpCanvasObject par)
    {
        m_par=par;
        m_sub_id=subid;
        m_sub_type=type;
        m_status=0;
        init_menu();
    }

    public int getStatus()
    {
        return m_status;
    }
    public void setStatus(int s)
    {
        m_status=s;
    }
    public Point getPosition()
    {
        return m_par.getSubObjPosition(m_sub_id);
    }
    public String getParId()
    {
        return m_par.getId();
    }
    public String getSubId()
    {
        return m_sub_id;
    }

    public String getSubObjType()
    {
        return m_sub_type;
    }
    public void setSubObjType(String tp)
    {
        m_sub_type=tp;
    }
    public void receiveCmd(String cmd)
    {
    }

    public void receiveCmd(String cmd,Hashtable para)
    {
    }

    public void msgReceived(String id, String ans)
    {
    }

    abstract public void draw(Graphics g ) ;


    public boolean checkIfClicked(Point p_point)
    {
        if(toPointDistance(p_point)<MINDIS) 
        {
            return true;
        }
        return false;
    }

    public int toPointDistance(Point po)
    {
        Point pp=getPosition();
        Point p=m_par.getOriPoint(po);
        return (int) NetpCanvasGeom.ptToPt(p.x,p.y,pp.x,pp.y);
    }

    public Toolkit getToolkit()
    {
        return m_par.getToolkit();
    }

    public void showMenu(Point p)
    {
    }

    public void hideMenu()
    {
    }

    public Point getLocationOnScreen()
    {
        return m_par.getLocationOnScreen();
    }
    public boolean isPlug()
    {
        return false;
    }
    public boolean isSocket()
    {
        return false;
    }
    public NetpCanvasSubObject findSubObjectById(String p_id,String p_sub_id)
    {
        return m_par.findSubObjectById(p_id,p_sub_id);
    }

    public String toString()
    {
        String sb;
        sb=m_par.intToString(m_sub_id+"_status",m_status);
        return sb;
    }
    public void setInit(NetpCanvasObjectParser ps)
    {
        m_status=ps.getIntValue(m_sub_id+"_status");
    }

}

