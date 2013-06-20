package netp.canvas;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Hashtable;
public class NetpCanvasSocket extends NetpCanvasSubObject
{
 
    final static public int NCS_OCCUPIED=2,NCS_ACTIVE=1,NCS_EMPTY=0;

    protected String m_plug_obj_id;
    protected String m_plug_obj_sub_id;

    protected String m_plug_type;

    protected void init_menu()
    {
    }

    public NetpCanvasSocket(String subid,NetpCanvasObject par)
    {
        super(subid,"netpsocket",par);
        m_status=NCS_EMPTY;
        m_plug_obj_id=null;
        m_plug_obj_sub_id=null;
        m_plug_type="netpplug";
    }

    public void setPlugType(String tp)
    {
        m_plug_type=tp;
    }
    public String getPlugType()
    {
        return m_plug_type;
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
        if("disconnect".equals(cmd)) {
            receiveCmd(cmd);
        }
        if("breakconnection".equals(cmd))
            receiveCmd(cmd);

    }
    public void receiveCmd(String cmd)
    {
        if("disconnect".equals(cmd)) 
        {
            if(m_status==NCS_OCCUPIED)
                disconnectMe();
        }
        if("breakconnection".equals(cmd))
            breakConnection();

        if("forcemove".equals(cmd))
        {
            if(m_status==NCS_OCCUPIED)
                m_par.sendSubObjCommand(m_plug_obj_id,m_plug_obj_sub_id,"forcemove");
        }

        return;
    }

    protected void disconnectMe()
    {
        m_par.sendSubObjCommand(m_plug_obj_id,m_plug_obj_sub_id,"breakconnection");
        breakConnection();
    }
    public void breakConnection()
    {
        m_status=NCS_EMPTY;
        m_plug_obj_id=null;
        m_plug_obj_sub_id=null;
    }


    public void draw(Graphics g)
    {
        if(m_status!=NCS_ACTIVE) return ;
        Color c=m_par.getColor();
        Point p=getPosition();
        g.setColor(c);
        g.fillOval(p.x-3,p.y-3,7,7);
    }
    public boolean isPlug()
    {
        return false;
    }
    public boolean isSocket()
    {
        return true;
    }

    protected void plugIn(String p_id,String p_sub_id)
    {
        m_status=NCS_OCCUPIED;
        m_plug_obj_id=p_id;
        m_plug_obj_sub_id=p_sub_id;
    }
    public String toString(){
        String s;
        s=super.toString();
        s=s+NetpCanvasObject.stringToString(m_sub_id+"_opp_id",m_plug_obj_id);
        s=s+NetpCanvasObject.stringToString(m_sub_id+"_opp_sub_id",m_plug_obj_sub_id);
        return s;
    }
    public void setInit(NetpCanvasObjectParser ps)
    {
        super.setInit(ps);
        m_plug_obj_id=ps.getStringValue(m_sub_id+"_opp_id");
        m_plug_obj_sub_id=ps.getStringValue(m_sub_id+"_opp_sub_id");
    }
    public String getPlugId() {
        if(m_status!=NCS_OCCUPIED) return null;
        return m_plug_obj_id;
    }
    public boolean isConnected() {
        return (m_status==NCS_OCCUPIED);
    }
}
