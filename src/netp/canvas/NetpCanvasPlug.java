package netp.canvas;

import java.util.Hashtable;
public class NetpCanvasPlug extends NetpCanvasSocket
{
 
    public NetpCanvasPlug(String subid,NetpCanvasObject par)
    {
        super(subid,par);
        m_sub_type="netpplug";
        m_plug_type="netpsocket";
    }

    public void setSocketType(String tp)
    {
        m_plug_type=tp;
    }

    public String getSocketType()
    {
        return m_plug_type;
    }

    public boolean isPlug()
    {
        return true;
    }
    public boolean isSocket()
    {
        return false;
    }
    public void receiveCmd(String cmd)
    {
        if("forcemove".equals(cmd))
        {
            plugForceMove();
            return;
        }
        super.receiveCmd(cmd);
    }
    private void plugForceMove()
    {
        if(m_status!=NCS_OCCUPIED) return;
        NetpCanvasSubObject ob;
        ob=findSubObjectById(m_plug_obj_id,m_plug_obj_sub_id);
        if(ob==null) return;

        m_par.forceMove(m_sub_id,ob.getPosition());

    }
    public String getSocketId() {
        if(m_status!=NCS_OCCUPIED) return null;
        return m_plug_obj_id;
    }

    protected void disconnectMe()
    {
        Hashtable tb=new Hashtable();
        tb.put("obj_id",m_par.getId());
        tb.put("sub_obj_id",m_sub_id);

        m_par.sendSubObjCommand(m_plug_obj_id,m_plug_obj_sub_id,"breakconnection",tb);
        breakConnection();
    }
}