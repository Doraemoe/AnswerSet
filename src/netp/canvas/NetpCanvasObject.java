package netp.canvas;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import netp.NetpGeneral;
import netp.GUI.ColorChangable;
import netp.GUI.ColorDialog;
abstract public class NetpCanvasObject implements ActionListener, ColorChangable
{
    protected Point p0;
    protected String type;
    private String id;
    protected NetpCanvas m_par;
    protected boolean isSelected=false;
    private boolean selectChange=false;
    protected Color m_color=Color.black;

    protected JPopupMenu m_menu=null;
    protected JMenuItem m_del,m_chgcol;

    final static public int MINDIS=4;


    protected Vector<String> m_sub_obj_list=null;

    public void setDeleable(boolean b) {
        if(m_menu==null) {
            m_menu = new NetpPopupMenu();
            init_menu();
        }
        if(m_del!=null)
            m_del.setEnabled(b);
    }
    /* if this function return true, it should be draw before other objs*/
    public boolean preferDrawFirst() {
        return false;
    }
    public void setParent(NetpCanvas par)
    {
        m_par=par;
    }

    public void setInitPoint(Point p)
    {
        if(p==null) p0 = new Point (0,0);
        else p0=new Point(p.x,p.y);
        boundryCheck();
    }

    public boolean isSelected()
    {
        return isSelected;
    }

    public void setSelect(boolean s)
    {
        if(s!=isSelected) selectChange=true;
        isSelected=s;
    }

    public String getId()
    {
        return id;
    }

    public void setId(String p_id)
    {
        id=p_id;
    }

    public String getType()
    {
        return type;
    }

    public void setColor(Color c)
    {
       m_color=c;
       m_par.repaint();
    }


    protected void tryChangeColor()
    {

       Frame fm = new Frame();
       ColorDialog cd=new ColorDialog(fm,getColor(), this);
       fm.setVisible(true);
       cd.setVisible(true);
    }

    public void tryDeleteMe()
    {
        int rec=JOptionPane.showConfirmDialog(m_par.getHost(),
            "Are you show to delete this object?","Delete",
            JOptionPane.YES_NO_OPTION);
        if(rec==JOptionPane.YES_OPTION)
            deleteMe();
    }
    public void deleteMe()
    {
        String res=canDeleteMe();
        if(res!=null) {
            JOptionPane.showMessageDialog(m_par.getHost(),
                res,"Can't delete",JOptionPane.ERROR_MESSAGE);
            return;
        }
        beforeDeleteMe();
        m_par.deleteObject(this);
        afterDeleteMe();
    }
	public boolean provideTip() {
		return false;
	}
	public void drawToolTip(Graphics g) {
		return;
	}
    public void actionPerformed(ActionEvent e)
    {
        Object o=e.getSource();
        if(o==m_del) {
            tryDeleteMe();
            return;
        }
        if(o==m_chgcol) {
            tryChangeColor();
            return;
        }
    }
    protected void init_menu()
    {
        m_chgcol=new JMenuItem("Change Color");
        m_del=new JMenuItem("Delete");
        m_chgcol.addActionListener(this);
        m_del.addActionListener(this);
        m_menu.add(m_chgcol);
        m_menu.add(m_del);
    }

    public NetpCanvasObject()
    {
//        m_menu = new NetpPopupMenu();
//        init_menu();
        m_color = Color.black;
    }

    abstract public void draw(Graphics g);

    public boolean checkIfRawClicked(Point p) {
        if(toPointDistance(p)<MINDIS) {
            if(isSelected()) {
                setSelect(false);
                return false;
            }
            setSelect(true);
            return true;
        }
        return false;
    }

	public boolean checkIfClose(Point p_point){
        if(toPointDistance(p_point)<MINDIS)
            return true;
        return false;

	}
    public boolean checkIfClicked(Point p_point,NetpCanvasObject p_sObj)
    {
        if(p_sObj!=null)
        {
            setSelect(false);
            return false;
        }

        if(toPointDistance(p_point)<MINDIS)
        {
            setSelect(true);
            return true;
        }

        setSelect(false);
        return false;
    }

    abstract public int toPointDistance(Point p);
    public boolean isSelectChanged()
    {
        boolean tmp=selectChange;
        selectChange=false;
        return tmp;
    }

    public Point getLocationOnScreen()
    {
        return m_par.getLocationOnScreen();
    }

    public Toolkit getToolkit()
    {
        return m_par.getToolkit();
    }

    protected Vector<String> getSubObjectList()
    {
        return m_sub_obj_list;
    }


    public NetpCanvasSubObject findSocket(Point p,String p_socket_type)
    {
        NetpCanvasSubObject p_sub_obj;
        p_sub_obj=findSubObjectByPosition(p);
        if(p_sub_obj==null) return null;

        if(p_sub_obj.getSubObjType().equals(p_socket_type))
            return p_sub_obj;
        return null;
    }
    public void receiveCmd(String cmd,Hashtable tb)
    {
    }

    public void receiveCmd(String cmd) {
    }

    public void sendSubObjCommand(String cmd)
    {
        Vector<String> obs=getSubObjectList();
        if(obs==null) return ;
        int num=obs.size();
        int i;
        for(i=0;i<num;++i)
        {
            String p_sub_id=(String) obs.elementAt(i);
            sendSubObjCommand(id,p_sub_id,cmd);

        }
        return;
    }
    protected NetpCanvasSubObject findSubObjectByPosition(Point p)
    {
        Vector<String> obs=getSubObjectList();
        if(obs==null) return null;
        int num=obs.size();
        int i;
        Point pp;
        for(i=0;i<num;++i)
        {
            String p_id = obs.elementAt(i);
            pp = getSubObjPosition(p_id);
            if(NetpCanvasGeom.ptToPt(p,pp)<MINDIS) {

                return findSubObjectById(p_id);
            }
        }
        return null;
    }
    protected String subObjectToString()
    {
        StringBuffer bf=new StringBuffer();
        Vector<String> obs=getSubObjectList();
        int num=obs.size();
        int i;
        NetpCanvasSubObject ob;
        for(i=0;i<num;++i)
        {
            String p_id = obs.elementAt(i);

            ob=findSubObjectById(p_id);
            if(ob!=null) bf.append(ob.toString());
        }
        return bf.toString();
    }

    protected void subObjectSetInit(NetpCanvasObjectParser ps)
    {
        Vector<String> obs=getSubObjectList();
        int num=obs.size();
        int i;
        NetpCanvasSubObject ob;
        for(i=0;i<num;++i)
        {
            String p_id = obs.elementAt(i);

            ob=findSubObjectById(p_id);
            if(ob!=null) ob.setInit(ps);
        }
    }

    protected void drawSubObject(Graphics g)
    {
        Vector<String> obs=getSubObjectList();
        int num=obs.size();
        int i;
        NetpCanvasSubObject ob;
        for(i=0;i<num;++i)
        {
            String p_id = obs.elementAt(i);

            ob=findSubObjectById(p_id);
            if(ob!=null) ob.draw(g);
        }

    }
    public void showMenu(Point p)
    {
/*
        NetpCanvasSubObject ob;
        ob=findSubObjectByPosition(p);
        if(ob==null)
            m_menu.show(p);
        else
            ob.showMenu(p);
*/
        if(m_menu==null) {
            m_menu = new NetpPopupMenu();
            init_menu();
        }
        m_menu.show(m_par,p.x,p.y);
    }


    public void startDrag(Point p)
    {
    }

    public void stopDrag(Point p)
    {
    }

    public Cursor getDragCursor()
    {
        return Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
    }

    protected void shiftDrag(Point p1,Point p2)
    {
        p0.x+=(p2.x-p1.x);
        p0.y+=(p2.y-p1.y);
    }

    public void dragTo(Point p1,Point p2)
    {
        shiftDrag(p1,p2);
        boundryCheck();
    }

    protected void defBoundryCheck()
    {
        int w=m_par.getTotalWidth();
        int h=m_par.getTotalHeight();
        if(p0.x<=0) p0.x=1;
        if(p0.y<=0) p0.y=1;
        if(getDrawD(p0.x)>=(w-1)) p0.x=m_par.getOriD(w)-2;
        if(getDrawD(p0.y)>=(h-1)) p0.y=m_par.getOriD(h)-2;

    }

    protected void boundryCheck()
    {
        defBoundryCheck();
    }

    public Color getColor()
    {
        return m_color;
    }

    public String toString()
    {
        StringBuffer sb=new StringBuffer();
        sb.append(stringToString("type",type));
        sb.append(pointToString("p0",p0));
        if(!m_color.equals(Color.black))
            sb.append(colorToString("c0",m_color));
        return sb.toString();
    }

    public void setInit(NetpCanvasObjectParser ps)
    {
        p0=ps.getPointValue("p0");
        m_color=ps.getColorValue("c0",Color.black);
    }


    static public  String intToString(String p_name,int v)
    {
        return p_name+":"+NetpGeneral.intToString(v)+";";
    }

    static public String booleanToString(String p_name,boolean v)
    {
        return p_name+":"+NetpGeneral.booleanToString(v)+";";
    }

    static public String pointToString(String p_name,Point p)
    {
        return p_name+":"+NetpGeneral.ptToString(p)+";";
    }

    protected String colorToString(String p_name,Color c)
    {
        return p_name+":"+NetpGeneral.colorToString(c)+";";
    }

    static public String stringToString(String p_name,String s)
    {
        if(s==null) s=" ";
        return p_name+":"+s+";";
    }
    public Point getSubObjPosition(String sub_id)
    {
        return null;
    }


    public void sendSubObjCommand(String p_id,String p_sub_id,String p_cmd)
    {
        m_par.sendSubObjCommand(p_id,p_sub_id,p_cmd);
    }

    public void sendSubObjCommand(String p_id,String p_sub_id,String p_cmd,Hashtable<String, String> p_paras)
    {
        m_par.sendSubObjCommand(p_id,p_sub_id,p_cmd,p_paras);
    }

    public void sendSubObjCommand(String p_sub_id,String p_cmd)
    {
        NetpCanvasSubObject ob=findSubObjectById(p_sub_id);
        if(ob!=null)
            ob.receiveCmd(p_cmd);
    }

    public void sendSubObjCommand(String p_sub_id,String p_cmd,Hashtable p_paras)
    {
        NetpCanvasSubObject ob=findSubObjectById(p_sub_id);
        if(ob!=null)
            ob.receiveCmd(p_cmd, p_paras);
    }
    public NetpCanvasSubObject findSubObjectById(String p_sub_id)
    {
        return null;
    }
    public NetpCanvasSubObject findSubObjectById(String p_id,String p_sub_id)
    {
        return m_par.findSubObjectById(p_id,p_sub_id);
    }
    public void forceMove(String p_sub_id,Point p)
    {
    }

    public void drawSelectRect(int x,int y, Graphics g)
    {
        g.drawRect(x-2,y-2,5,5);
    }
    public void releaseColorHold() // what does this mean ?
    {
    }
    protected String canDeleteMe() {
        return null;
    }
    protected void beforeDeleteMe()
    {
    }
    protected void afterDeleteMe() {
    }
    public boolean inRect(int x,int y,int w,int h){
        if((NetpCanvasGeom.ptToFilledRect(p0.x,p0.y, x,y,x+w,y+h))<=1){
            setSelect(true);
            return true;
        }
        setSelect(false);
        return false;
    }
    public int findMaxX() {
        return p0.x;
    }
    public int findMaxY() {
        return p0.y;
    }
    public Vector GetAbsShape() {
        return null;
    }
    public Point getOriPoint(Point p) {
        Point pp=new Point();
        pp.x=m_par.getZoomRevValue(p.x);
        pp.y=m_par.getZoomRevValue(p.y);
//        pp.x=p.x*m_par.m_zoom;
//       pp.y=p.y*m_par.m_zoom;
        return pp;
    }
    public int getDrawX(int x) {
    	return m_par.getZoomValue(x)-m_par.rectView.x;
//        return x/m_par.m_zoom-m_par.rectView.x;
    }
    public int getDrawY(int y) {
    	return m_par.getZoomValue(y)-m_par.rectView.y;
//        return y/m_par.m_zoom-m_par.rectView.y;
    }
    public int getDrawD(int d) {
    	return m_par.getZoomValue(d);
//        return d/m_par.m_zoom;
    }
    public boolean needSave() {
        return true;
    }
}
