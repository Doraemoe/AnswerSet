package netp.canvas;

import java.util.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import netp.*;
import netp.absshape.*;
public class NetpPolyLine extends NetpLine
{
    final static int MIDPOINTDRAG=5;
    protected int m_dragp;
    protected Vector m_interpoints;
    protected int m_inter_num;

    JMenuItem m_adp,m_dep,m_adj;


    final static int MIDPOINT=1,BASEPOINT=2,ENDPOINT=3,SEGPOINT=4,OUTPOINT=5;
    public NetpPolyLine() {
        super();
        type="polyline";
    }
    public void setInitPoint(Point p)
    {
        super.setInitPoint(p);
        m_interpoints=new Vector();
        m_inter_num=0;
    }

    protected void shiftDrag(Point p1,Point pp2)
    {

        super.shiftDrag(p1,pp2);
        int i;
        Point p;
        for(i=1;i<=m_inter_num;++i) {
            p=getPoint(i);
            p.x+=pp2.x-p1.x;
            p.y+=pp2.y-p1.y;
        }
    }
    public void midPointDrag(Point p)
    {
        Point pp;
        pp=getPoint(m_dragp);;
        pp.x=p.x;
        pp.y=p.y;
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
            case MIDPOINTDRAG:
                midPointDrag(pp2);
                break;
        }
        boundryCheck();
    }
    public void removeAllMidPoint() {
        m_interpoints=new Vector();
        m_inter_num=0;
    }
    public void draw(Graphics g)
    {
        int x,y,x2,y2;
        x=getDrawX(p0.x);
        y=getDrawY(p0.y);
        

        int i;
        Point p;

        g.setColor(getColor());

        for(i=1;i<=m_inter_num;++i) 
        {
            p=getPoint(i);
            x2=getDrawX(p.x);
            y2=getDrawY(p.y);
            g.drawLine(x,y,x2,y2);
            if(isSelected)
                g.drawRect(x-3,y-3,5,5);
            x=x2;
            y=y2;
        }

        x2=getDrawX(p2.x);
        y2=getDrawY(p2.y);

        g.drawLine(x,y,x2,y2);

        if(isSelected) 
        {
            g.drawRect(x-3,y-3,5,5);
            g.drawRect(x2-3,y2-3,5,5);
        }
    }


    public Point getPoint (int i)
    {
        if(i==0) return p0;
        if(i==(m_inter_num+1)) return p2;

        if((i>0)&&(i<=m_inter_num))
            return (Point) m_interpoints.elementAt(i-1);
        return null;
    }

    protected int toLineDistance(Point p, int i)
    {
        Point pp1,pp2;
        pp1=getPoint(i);
        pp2=getPoint(i+1);
        return (int)NetpCanvasGeom.ptToSeg(pp1.x,pp1.y,pp2.x,pp2.y,p.x,p.y);
    }

    public int toPointDistance(Point po)
    {
        Point p=getOriPoint(po);

        int dist=1000,d;
        int i;

        for(i=0;i<=m_inter_num;++i)
        {
            d=toLineDistance(p,i);
            if(d<dist) dist=d;
        }
        return dist;
    }

    public int findPointType(Point p)
    {
        Point pp;
        int i;
        for(i=1;i<=m_inter_num;++i){
            pp=getPoint(i);
            if(NetpCanvasGeom.ptToPt(p.x,p.y,pp.x,pp.y)<MINDIS) {
                m_dragp=i;
                return MIDPOINT;
            }
        }
        if(NetpCanvasGeom.ptToPt(p,p0.x,p0.y)<MINDIS) 
            return BASEPOINT;
        if(NetpCanvasGeom.ptToPt(p,p2)<MINDIS) 
            return ENDPOINT;

        for(i=0;i<=m_inter_num;++i) {
            if(toLineDistance(p,i)<MINDIS) {
                m_dragp=i;
                return SEGPOINT;
            }
        }
        return OUTPOINT;
    }
    public void startDrag(Point p) 
    {
        int ptype = findPointType(p);
        switch(ptype)
        {
            case MIDPOINT: dragType=MIDPOINTDRAG; break;
            case BASEPOINT: dragType=HEADDRAG; break;
            case ENDPOINT: dragType=TAILDRAG; break;
            default: dragType=SHIFTDRAG;
        }
    }

    public Cursor getDragCursor() 
    {
        if(dragType == SHIFTDRAG)
            return Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
        return Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR);
    }

    protected void init_menu()
    {
        m_adp=new JMenuItem("Add Mid Point");
        m_dep=new JMenuItem("Delete Mid Point");
        m_adj=new JMenuItem("Adjust Wire");
        m_menu.add(m_adp);
        m_menu.add(m_dep);
        m_menu.add(m_adj);
        m_adp.addActionListener(this);
        m_dep.addActionListener(this);
        m_adj.addActionListener(this);
        super.init_menu();
    }

    public void addPoint(Point p,int i)
    {
        m_interpoints.insertElementAt(p,i);
        m_inter_num=m_interpoints.size();
        return;
    }

    public void addPoint()
    {
        Point pa,pb;
        pa=getPoint(m_dragp);
        pb=getPoint(m_dragp+1);
        Point p=new Point((pa.x+pb.x)/2,(pa.y+pb.y)/2);
        addPoint(p,m_dragp);
    }

    public void removePoint(Point p)
    {
        m_interpoints.removeElement(p);
        m_inter_num=m_interpoints.size();
    }

    public void actionPerformed(ActionEvent e)
    {
        Object o=e.getSource();
        if(o==m_adp) 
        {
            addPoint();
            m_par.repaint();
            return;
        }

        if(o==m_dep)
        {
            removePoint(getPoint(m_dragp));
            m_par.repaint();
            return;
        }
        if(o==m_adj) {
            adjustPoint();
            m_par.repaint();
            return;
        }
        super.actionPerformed(e);
    }
    public String toString()
    {
         String s;
        s=super.toString();
        s=s+stringToString("midp",midPointToString());                
        return s;
    }
    private String midPointToString()
    {
        StringBuffer sb=new StringBuffer();
        sb.append(NetpGeneral.intToString(m_inter_num));
        sb.append(",");
        int i;
        Point p;
        for(i=1;i<=m_inter_num;++i)
        {
            p=getPoint(i);
            sb.append(NetpGeneral.ptToString(p));
            sb.append(",");
        }
        return sb.toString();
    }
    public void setInit(NetpCanvasObjectParser ps)
    {
        super.setInit(ps);
        String p_mid=ps.getStringValue("midp");
        initMid(p_mid);        
    }
    private void initMid(String p_mid)
    {
        StringTokenizer tk=new StringTokenizer(p_mid,",");
        String p_num;
        p_num=tk.nextToken();
        int i_num,i;
        Point p;
        i_num=NetpGeneral.stringToInt(p_num);

        m_interpoints = new Vector();
        m_inter_num=0;
        for(i=0;i<i_num;++i)
        {
            p=NetpGeneral.stringToPt(tk.nextToken());
            addPoint(p,i);
        }        
    }
    public void showMenu(Point p)
    {
        int ptype = findPointType( p);
        if(m_menu==null) {
            m_menu = new NetpPopupMenu();            
            init_menu();
        }
        m_adp.setEnabled(false);
        m_dep.setEnabled(false);
        switch (ptype) {
            case MIDPOINT: m_dep.setEnabled(true); break;
            case SEGPOINT: m_adp.setEnabled(true);break;
        }
        super.showMenu(p);
    }
    public void adjustPoint()
    {
       Point pp1, pp2;
       int dx, dy, dis;

        pp2=p0;
        if(m_inter_num==0) return; // can't adjust when no mid point
   for(int i=0;i<=m_inter_num;i++)
   {
       pp1=pp2;

        pp2=getPoint(i+1);
       if (i==m_inter_num)
       {
         pp2 = (Point) m_interpoints.elementAt(i-1);
         pp1=p2;
       } 
      dx=Math.abs(pp2.x-pp1.x);
      dy=Math.abs(pp2.y-pp1.y);
      dis=(int)Math.sqrt(dx*dx+dy*dy);
      if ((dx*2) < dis)
      { 
         pp2.x=pp1.x;
      } 
      else if ((dy*2) < dis)
           {
              pp2.y=pp1.y;
           } 
           else
              {
                if ((pp2.x>pp1.x)&&(pp2.y>pp1.y))
                   {
                     pp2.x = (int)(pp1.x+dis/1.414);
                     pp2.y = (int)(pp1.y+dis/1.414);
                   }
                if ((pp2.x>pp1.x)&&(pp2.y<pp1.y))
                   {
                     pp2.x = (int)(pp1.x+dis/1.414);
                     pp2.y = (int)(pp1.y-dis/1.414);
                   }
                if ((pp2.x<pp1.x)&&(pp2.y>pp1.y))
                   {
                     pp2.x = (int)(pp1.x-dis/1.414);
                     pp2.y = (int)(pp1.y+dis/1.414);
                   }
                if ((pp2.x<pp1.x)&&(pp2.y<pp1.y))
                   {
                     pp2.x = (int)(pp1.x-dis/1.414);
                     pp2.y = (int)(pp1.y-dis/1.414);
                   }
              }


      if (i==m_inter_num)
      {
         m_interpoints.setElementAt(pp2, i-1);
      } else
      {
         m_interpoints.setElementAt(pp2, i);
      }
    }
    }

    public int findMaxX() {
        int x0=p0.x;
        int i,s=m_inter_num+1;
        Point p;
        for(i=1;i<s;++i) {
            p=getPoint(i);
            if(p.x>x0) x0=p.x;
        }
        return x0;
    }
    public int findMaxY() {
        int y0=p0.y;
        int i,s=m_inter_num+1;
        Point p;
        for(i=1;i<s;++i) {
            p=getPoint(i);
            if(p.y>y0) y0=p.y;
        }
        return y0;
    }
    public Vector GetAbsShape() {
        Vector v=new Vector();
        int i,s=m_inter_num+1;

        Point pa,pb;
        AbsLine al;
        pa=getPoint(0);

        for(i=0;i<s;++i) {
            pb=getPoint(i+1);
            al=new AbsLine(pa.x,pa.y,pb.x-pa.x,pb.y-pa.y);
            al.setColor(getColor());
            v.add(al);
            pa=pb;
        }
        return v;
    }


}
