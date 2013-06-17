package netp.canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
public class NetpCanvasMultiSocket extends NetpCanvasSocket
{

    final static public int NCS_HALFOCCUPIED=10;

    protected int m_max_plug_num=-1; // -1 means no limit
    protected int m_plug_num=0; // how many plugs have pluged in this socket

    protected HashSet m_plugs;


    public NetpCanvasMultiSocket(String subid,NetpCanvasObject par)
    {
        super(subid,par);
        m_plugs=new HashSet();
    }


    public void receiveCmd(String cmd,Hashtable tb)
    {
        if("plugin".equals(cmd)) 
        {
            String p_id,p_sub_id;
            p_id=(String) tb.get("obj_id");
            p_sub_id=(String) tb.get("sub_obj_id");
            plugIn(p_id,p_sub_id);
            return;
        }
        if("disconnect".equals(cmd)) 
        {
            String p_id,p_sub_id;
            p_id=(String) tb.get("obj_id");
            p_sub_id=(String) tb.get("sub_obj_id");
            disconnectMe(p_id,p_sub_id,true);
            return;
        }
        if("breakconnection".equals(cmd)) 
        {
            String p_id,p_sub_id;
            p_id=(String) tb.get("obj_id");
            p_sub_id=(String) tb.get("sub_obj_id");
            disconnectMe(p_id,p_sub_id,false);
            return;
        }


    }

    public void receiveCmd(String cmd)
    {
        if("disconnect".equals(cmd)) 
             disconnectMe();
        if("breakconnection".equals(cmd))
            disconnectMe();

        if("forcemove".equals(cmd))
        {
            if(m_status!=NCS_EMPTY)
                forcemoveOthers();
        }

        return;
    }
    protected void forcemoveOthers() {
        plug p;
        for(Iterator e=m_plugs.iterator(); e.hasNext();)
        {
            p=(plug) e.next();
            m_par.sendSubObjCommand(p.m_id,p.m_sub_id,"forcemove");
        }
    }
    protected void disconnectMe(String id,String sub_id,boolean sendback) {
        plug p=new plug(id,sub_id);
        m_plugs.remove(p);
        if(sendback)
            m_par.sendSubObjCommand(id,sub_id,"breakconnection");
        m_plug_num=m_plugs.size();
        m_status=NCS_HALFOCCUPIED;
        if(m_plug_num==0) breakConnection();
    }
    protected void disconnectMe()
    {
        plug p;
        for(Iterator e=m_plugs.iterator(); e.hasNext();)
        {
            p=(plug) e.next();
            m_par.sendSubObjCommand(p.m_id,p.m_sub_id,"breakconnection");
        }
        breakConnection();
    }
    public void breakConnection()
    {
        m_status=NCS_EMPTY;
        m_plug_num=0;
        m_plugs=new HashSet();
    }


    public void draw(Graphics g)
    {
        if(m_status!=NCS_ACTIVE) return ;
        Color c=m_par.getColor();
        Point p=getPosition();
        g.setColor(c);
        g.fillOval(p.x-3,p.y-3,7,7);
    }

    protected void plugIn(String p_id,String p_sub_id)
    {
        plug o=new plug(p_id,p_sub_id);
        m_plugs.add(o);
        m_plug_num=m_plugs.size();
        if((m_max_plug_num>=0)&&(m_plug_num>=m_max_plug_num))
        {
            m_status=NCS_OCCUPIED;
        }
        else
            m_status=NCS_HALFOCCUPIED;
    }
    public String toString(){
        String s;
        s=super.toString();
        s=s+m_par.intToString(m_sub_id+"_num",m_plug_num);

        plug p;
        int i=0;
        for(Iterator e=m_plugs.iterator(); e.hasNext();i++)
        {
            p=(plug) e.next();
            s=s+m_par.stringToString(m_sub_id+"_opp_id_"+i,p.m_id);
            s=s+m_par.stringToString(m_sub_id+"_opp_sub_id_"+i,p.m_sub_id);

        }

        return s;
    }
    public void setInit(NetpCanvasObjectParser ps)
    {
        super.setInit(ps);
        m_plug_num=ps.getIntValue(m_sub_id+"_num");
        int i;
        String id,sub_id;
        plug p;
        for(i=0;i<m_plug_num;++i) {
            id=ps.getStringValue(m_sub_id+"_opp_id_"+i);
            sub_id=ps.getStringValue(m_sub_id+"_opp_sub_id_"+i);
            p=new plug(id,sub_id);
            m_plugs.add(p);
        }
    }
    public Vector getPlugIds() {
        Vector ret=new Vector();
        plug p;
        for(Iterator e=m_plugs.iterator(); e.hasNext();)
        {
            p=(plug) e.next();
            ret.addElement(p.m_id);
        }
        return ret;
    }

} 
class plug
{
    String m_id;
    String m_sub_id;
    public plug(String id, String sub_id)
    {
        m_id=id;
        m_sub_id=sub_id;
    }
    public boolean equals(Object o){
        if((o!=null)&&(o instanceof plug))
        {
            plug oo = (plug) o;
            if((m_id.equals(oo.m_id))&&(m_sub_id.equals(oo.m_sub_id)))
                return true;
        }
        return false;
    }
    public int hashCode() {
        return m_id.hashCode()+m_sub_id.hashCode();
    }
}