package netp.canvas;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Vector;

import javax.swing.JOptionPane;

public class NetpCanvasHandler implements MouseListener, 
    MouseMotionListener, KeyListener
{
    NetpCanvas m_par;
    private Point m_lastp;
    private boolean inDragMode=false;

    private boolean inSelectDragMode=false;
    private Point m_selectStartP=null;
    private Point m_selectStopP=null;

	private boolean bHandObjTip=false;

	public void setHandleObjTip(boolean b) {
		bHandObjTip=b;
	}
    public boolean inSelectDragMode() {
        return inSelectDragMode;
    }
    public Point getSelectStartP() {
        return m_selectStartP;
    }
    public Point getSelectStopP() {
        return m_selectStopP;
    }

    public Point getLastPoint()
    {
        return m_lastp;
    }

    public  NetpCanvasHandler(NetpCanvas par)
    {
        m_par=par;
    }
    public void setLastP(Point p) {
        m_lastp=p;
    }
    public void mouseMoved(MouseEvent e)
    {
		NetpCanvasObject p_obj;
		if(!bHandObjTip) {
			m_par.setTipObj(null);
			return;
		}
        m_lastp=e.getPoint();
		p_obj=m_par.findTipObject(m_lastp);
		m_par.setTipObj(p_obj);
    }

    public void mouseEntered(MouseEvent e)
    {    }
   
    public void mouseExited(MouseEvent e)
    {    }
   
    public void mouseDragged(MouseEvent e)
    {
        if(inDragMode)
        {
            Vector<NetpCanvasObject> v=m_par.findGroupSelectObject();
            if((v==null)||(v.size()==0)) {
                NetpCanvasObject p_ob;
                p_ob=m_par.findSelectObject();

                if(p_ob!=null)
                {
                    Point p=e.getPoint();
                    p_ob.dragTo(m_par.getOriPoint(m_lastp),m_par.getOriPoint(p));
                    m_lastp=p;
                    m_par.repaint();
                }
                m_par.mayChanged();
            }
            else {
                int s,i;    
                NetpCanvasObject p_ob;
                s=v.size();
                Point p=e.getPoint();
                for(i=0;i<s;++i) {
                    p_ob=(NetpCanvasObject) v.elementAt(i);
                    p_ob.dragTo(m_par.getOriPoint(m_lastp),m_par.getOriPoint(p));
                }
                m_lastp=p;
                m_par.mayChanged();
                m_par.repaint();
            }
        }
        if(inSelectDragMode) {
            m_selectStopP=e.getPoint();
            m_lastp=m_selectStopP;
            m_par.repaint();
        }

    }

    public void mouseClicked(MouseEvent e)
    {
        if((e.getModifiers() & MouseEvent.CTRL_MASK) == MouseEvent.CTRL_MASK) {
            m_lastp=e.getPoint();
            m_par.selectChangeDraw();
            return;
        }

        NetpCanvasObject p_obj;
        m_lastp=e.getPoint();


        if((e.getModifiers() & MouseEvent.BUTTON3_MASK) == MouseEvent.BUTTON3_MASK) 
        {

            Vector<NetpCanvasObject> v=m_par.findGroupSelectObject();
            NetpCanvasObject p_ob;

            if((v!=null)&&(v.size()>1)) {
                int s,i;
                s=v.size();
                for(i=0;i<s;++i) {
                    p_ob=(NetpCanvasObject) v.elementAt(i);
                    if(p_ob.toPointDistance(m_lastp)<NetpCanvasObject.MINDIS) {
                        m_par.showGroupMenu(m_lastp,v);
                        return;
                    }
                }
            }
        }

        p_obj=m_par.findClickedObject(m_lastp);
        m_par.selectChangeDraw();

        if((e.getModifiers() & MouseEvent.BUTTON3_MASK) == MouseEvent.BUTTON3_MASK) 
        {
            m_par.mayChanged();

            if(p_obj==null)
            {
                m_par.showMenu(m_lastp);
                return;
            }
            p_obj.showMenu(m_lastp);
        }
    } 

    public void mousePressed(MouseEvent e)
    {   
        m_lastp=e.getPoint();
        Vector<NetpCanvasObject> v=m_par.findGroupSelectObject();
        NetpCanvasObject p_ob;

        if((v!=null)&&(v.size()>1)) {
            int s,i;
            s=v.size();
            for(i=0;i<s;++i) {
                p_ob=(NetpCanvasObject) v.elementAt(i);
                if(p_ob.toPointDistance(m_lastp)<NetpCanvasObject.MINDIS) {
                    inDragMode = true;
                    m_par.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    return;
                }
            }
        }
        if((e.getModifiers() & MouseEvent.CTRL_MASK) == MouseEvent.CTRL_MASK) {
            if((v==null)||(v.size()==0)) { return; }
            if(v.size()>0) {
                inDragMode = true;
                m_par.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }
        }else {
            NetpCanvasObject p_obj;
            p_obj=m_par.findClickedObject(m_lastp);
            m_par.selectChangeDraw();
            if(p_obj!=null)
            {
                p_obj.startDrag(m_par.getOriPoint(m_lastp));
                m_par.setCursor(p_obj.getDragCursor());
                inDragMode = true;
                inSelectDragMode=false;
            }
            else {
                inDragMode = false;
                inSelectDragMode=true;
                m_selectStartP=m_lastp;
                m_selectStopP=m_lastp;
            }
        }
    }

    public void mouseReleased(MouseEvent e)
    {   
        if(inDragMode)
        {
            mouseDragged(e);

            NetpCanvasObject p_ob=m_par.findSelectObject();

            if(p_ob!=null)
            {
                p_ob.stopDrag(m_par.getOriPoint(e.getPoint()));
                m_par.repaint();
            }

            inDragMode = false;
            inSelectDragMode = false;
            m_par.setCursor(Cursor.getDefaultCursor());
            m_par.mayChanged();

        } 

        if(inSelectDragMode) {
            inSelectDragMode = false;
            int x,y,w,h;
            x=m_selectStartP.x; w=m_selectStopP.x-m_selectStartP.x;
            y=m_selectStartP.y; h=m_selectStopP.y-m_selectStartP.y;

            if(w<0) {x=m_selectStopP.x;w=-w; }
            if(h<0) {y=m_selectStopP.y;h=-h; }
            
            m_par.findRectSelectObject(x,y,w,h);
            m_par.repaint();
        }
    }
    public void keyPressed(KeyEvent e) {
        int c=e.getKeyCode();
        NetpCanvasObject p_ob;
        if(c==KeyEvent.VK_DELETE) {
            Vector<NetpCanvasObject> v=m_par.findGroupSelectObject();
            if((v==null)||(v.size()==0)) {
                p_ob=m_par.findSelectObject();
                if(p_ob!=null)
                    p_ob.tryDeleteMe();
                return;
            }
            else {
                int rec=JOptionPane.showConfirmDialog(m_par.getHost(),
                    "Are you show to delete these objects?","Delete",
                    JOptionPane.YES_NO_OPTION);
                if(rec!=JOptionPane.YES_OPTION) return;

                int s,i;    
                s=v.size();
                for(i=0;i<s;++i) {
                    p_ob=(NetpCanvasObject)v.elementAt(i);
                    p_ob.deleteMe();
                }
            }
        }

    }
    public void keyReleased(KeyEvent e) {
    }
    public void keyTyped(KeyEvent e) {
    }
}
