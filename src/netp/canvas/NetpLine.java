package netp.canvas;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

import netp.absshape.AbsLine;

public class NetpLine extends NetpCanvasObject
{
    protected int dragType=0;
    final static int SHIFTDRAG=0,HEADDRAG=1,TAILDRAG=2;
    protected Point p2=null;

    protected NetpCanvasPlug m_head_plug=null; /* in the head in fact it is tail in wire */
    protected NetpCanvasPlug m_tail_plug=null;

    public void setEndPoint(Point p) {
        p2=p;
    }
    public Point getSubObjPosition(String sub_id)
    {
        if("tail_plug".equals(sub_id))
            return new Point(p0.x,p0.y);
        if("head_plug".equals(sub_id))
            return new Point(p2.x,p2.y);
        return null;
    }

    protected void setHeadPlug(String p_plug_type,String p_socket_type)
    {
        m_head_plug=new NetpCanvasPlug("head_plug",this);
        m_head_plug.setSubObjType(p_plug_type);
        m_head_plug.setSocketType(p_socket_type);
        if(m_sub_obj_list==null) m_sub_obj_list=new Vector<String>();
        m_sub_obj_list.addElement("head_plug");
    }

    protected void setTailPlug(String p_plug_type,String p_socket_type)
    {
        m_tail_plug=new NetpCanvasPlug("tail_plug",this);
        m_tail_plug.setSubObjType(p_plug_type);
        m_tail_plug.setSocketType(p_socket_type);
        if(m_sub_obj_list==null) m_sub_obj_list=new Vector<String>();
        m_sub_obj_list.addElement("tail_plug");
    }


    public NetpCanvasSubObject findSubObjectById(String p_sub_id)
    {
        if("tail_plug".equals(p_sub_id))
            return m_tail_plug;
        if("head_plug".equals(p_sub_id))
            return m_head_plug;
        return null;
    }
    public NetpLine() 
    {
        super();
        type="line";
    }

    public void setInitPoint(Point p)
    {
        if(p==null) p=new Point(0,0);
        super.setInitPoint(p);
        p2=new Point(p.x+20,p.y+20);
    }

    protected void headDrag(Point p)
    {

        if(isTailPluged()) return;
        plugDrag(p,m_tail_plug);
        p0.x=p.x;
        p0.y=p.y;
    }
 
    protected NetpCanvasSocket plugDrag(Point p, NetpCanvasPlug pg)
    {

        String p_socket_type;
        if(pg==null) return null;
        if(pg.getStatus()==NetpCanvasSocket.NCS_OCCUPIED)
            return null;
        p_socket_type=pg.getSocketType();
        
        NetpCanvasSubObject p_ob;
        p_ob = m_par.findSocket(p,p_socket_type);

        if((p_ob==null)||(p_ob.getStatus()==NetpCanvasSocket.NCS_OCCUPIED))
        {
            pg.setStatus(NetpCanvasSocket.NCS_EMPTY);
            return null;
        }
        pg.setStatus(NetpCanvasSocket.NCS_ACTIVE);
        return (NetpCanvasSocket) p_ob;
    }
    protected void tryToPlug(Point p, NetpCanvasPlug pg)
    {
        NetpCanvasSocket p_ob;
        p_ob=plugDrag(p, pg);
        if(p_ob==null) return;
        m_par.plugIn(getId(),pg.getSubId(),p_ob.getParId(),p_ob.getSubId());
    }

    protected void tailDrag(Point p)
    {
        if(isHeadPluged()) return;

        plugDrag(p,m_head_plug);
        p2.x=p.x;
        p2.y=p.y;
    }
    public boolean isHeadPluged()
    {
        if(m_head_plug==null) return false;
        if(m_head_plug.getStatus()!=NetpCanvasSocket.NCS_OCCUPIED) return false;
        return true;
    }
    public boolean isTailPluged()
    {
        if(m_tail_plug==null) return false;
        if(m_tail_plug.getStatus()!=NetpCanvasSocket.NCS_OCCUPIED) return false;
        return true;
    }

    public void forceMove(String p_sub_id,Point p)
    {
        if("tail_plug".equals(p_sub_id))
        {
            p0.x=p.x;
            p0.y=p.y;
        }
        if("head_plug".equals(p_sub_id)){
            p2.x=p.x;
            p2.y=p.y;
        }
    }


    protected void shiftDrag(Point p1,Point pp2)
    {
        if(!isHeadPluged()) {
            p2.x+=(pp2.x-p1.x);
            p2.y+=(pp2.y-p1.y);
        }
        if(!isTailPluged())
        {
            p0.x+=(pp2.x-p1.x);
            p0.y+=(pp2.y-p1.y);
        }
    }

    public void dragTo(Point p1,Point pp2)
    {
        switch(dragType)
        {
            case SHIFTDRAG:
                shiftDrag(p1,pp2);   
                break;
            case HEADDRAG:
                headDrag(pp2);
                break;
            case TAILDRAG:
                tailDrag(pp2);
                break;
        }
        boundryCheck();
    }

    public void stopDrag(Point p)
    {
        switch(dragType)
        {
            case SHIFTDRAG:
                break;
            case HEADDRAG:
                if(m_tail_plug!=null)
                    tryToPlug(p,m_tail_plug);
                break;
            case TAILDRAG:
                if(m_head_plug!=null)
                    tryToPlug(p,m_head_plug);
                break;
        }
    }
    public void draw(Graphics g)
    {
        int x,y,x2,y2;
        x=getDrawX(p0.x);
        y=getDrawY(p0.y);
        x2=getDrawX(p2.x);
        y2=getDrawY(p2.y);

        g.setColor(getColor());
        g.drawLine(x,y,x2,y2);

        if(isSelected) 
        {
            g.drawRect(x-3,y-3,5,5);
            g.drawRect(x2-3,y2-3,5,5);
        }
    }

    public int toPointDistance(Point po)
    {
        Point p=getOriPoint(po);
        return (int) NetpCanvasGeom.ptToSeg(p0.x, p0.y, p2.x, p2.y, p.x, p.y);
    }

    public void startDrag(Point p) 
    {
        dragType=SHIFTDRAG;

        if(NetpCanvasGeom.ptToPt(p,p0.x,p0.y)<MINDIS) 
        {
            dragType=HEADDRAG;
            return;
        }

        if(NetpCanvasGeom.ptToPt(p,p2)<MINDIS) 
        {
            dragType = TAILDRAG;
            return;
        }
    }

    public Cursor getDragCursor() 
    {
        if(dragType == SHIFTDRAG)
            return Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
        return Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
    }
    public String toString(){
        String s;
        s=super.toString();
        s=s+pointToString("p2",p2);
        return s;
    }

    public void setInit(NetpCanvasObjectParser ps)
    {
        super.setInit(ps);
        p2=ps.getPointValue("p2");
    }

    public int findMaxX() {
        if(p2.x>p0.x) return p2.x;
        return p0.x;
    }
    public int findMaxY() {
        if(p2.y>p0.y) return p2.y;
        return p0.y;
    }
    public Vector<AbsLine> GetAbsShape() {
        AbsLine al=new AbsLine();
        al.setPosition(p0.x,p0.y);
        al.setColor(getColor());
        al.setSize(p2.x-p0.x,p2.y-p0.y);
        Vector<AbsLine> v = new Vector<AbsLine>();
        v.add(al);
        return v;
    }
}
