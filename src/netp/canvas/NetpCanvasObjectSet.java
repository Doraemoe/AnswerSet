package netp.canvas;

import java.util.*;
import java.awt.*;

import netp.xmldocument.*;
import netp.xml.*;
 
public class NetpCanvasObjectSet
{
    private Hashtable<String, NetpCanvasObject> m_objs;
    private NetpCanvas m_par;
    
    private boolean selectChange=false;
    private Vector<NetpCanvasObject> groupSelected=new Vector<NetpCanvasObject>();

	private Vector<NetpCanvasObject> m_objlist; 
    public NetpCanvasObjectSet(NetpCanvas par) 
    {
        m_par=par;
        m_objs=new Hashtable<String, NetpCanvasObject>();
		m_objlist=new Vector<NetpCanvasObject>();
    }
    public Enumeration<String> keys() {
        return m_objs.keys();
    }
    public Enumeration<NetpCanvasObject> elements() {
        return m_objs.elements();
    }
    public void draw(Graphics g)
    {
        NetpCanvasObject p_obj;
/*
        for (Enumeration e = m_objs.elements() ; e.hasMoreElements() ;) 
        {
            p_obj = (NetpCanvasObject) e.nextElement();
            p_obj.draw(g);
        }
*/
		int i,s=m_objlist.size();
		for(i=0;i<s;++i) {
			p_obj = (NetpCanvasObject) m_objlist.elementAt(i);
			p_obj.draw(g);
		}

    }
    public void initSelectGroup() {
        groupSelected=new Vector<NetpCanvasObject>();
    }
    public void addGroupSelectObject(NetpCanvasObject o) {
        groupSelected.addElement(o);
        o.setSelect(true);
    }
    public Vector<NetpCanvasObject> findGroupClickedObject(Point p_point) {
        selectChange=false;

        groupSelected=new Vector<NetpCanvasObject>();
        NetpCanvasObject p_obj;

        for (Enumeration<NetpCanvasObject> e = m_objs.elements() ; e.hasMoreElements() ;) 
        {
            p_obj = (NetpCanvasObject) e.nextElement();
            if(p_obj.checkIfRawClicked(p_point))
            {
                if(p_obj.isSelectChanged()) selectChange=true;
            }
            if(p_obj.isSelected()) groupSelected.addElement(p_obj);
        }
        return groupSelected;
    }

	public NetpCanvasObject findTipObject(Point p_point) {
        NetpCanvasObject p_obj;

		int i,s=m_objlist.size();
		for(i=s-1;i>=0;--i) {
			p_obj=(NetpCanvasObject) m_objlist.elementAt(i);
			if(!p_obj.provideTip()) continue;
            if(p_obj.checkIfClose(p_point))
				return p_obj;
        }
        return null;

	}
    public NetpCanvasObject findClickedObject(Point p_point)
    {
        selectChange=false;
        groupSelected=null;
        NetpCanvasObject p_sObj=null;
        NetpCanvasObject p_obj;

//        for (Enumeration e = m_objs.elements() ; e.hasMoreElements() ;) 
//        {
		int i,s=m_objlist.size();
		for(i=s-1;i>=0;--i) {

//            p_obj = (NetpCanvasObject) e.nextElement();
			p_obj=(NetpCanvasObject) m_objlist.elementAt(i);
            if(p_obj.checkIfClicked(p_point,p_sObj))
            {
                p_sObj=p_obj;
            }
            if(p_obj.isSelectChanged()) selectChange=true;
        }
        return p_sObj;
    }

    public NetpCanvasObject findSelectObject()
    {
        NetpCanvasObject p_obj;
        for (Enumeration<NetpCanvasObject> e = m_objs.elements() ; e.hasMoreElements() ;) 
        {
            p_obj = (NetpCanvasObject) e.nextElement();
            if(p_obj.isSelected()) return p_obj;
        }
        return null;
    }

    public Vector<NetpCanvasObject> findGroupSelectObject() {
        return groupSelected;
    }
    public Vector<NetpCanvasObject> findRectSelectObject(int x,int y,int w,int h){
        groupSelected=new Vector<NetpCanvasObject>();
        NetpCanvasObject p_obj;

        for (Enumeration<NetpCanvasObject> e = m_objs.elements() ; e.hasMoreElements() ;) 
        {
            p_obj = (NetpCanvasObject) e.nextElement();
            if(p_obj.inRect(x,y,w,h))
            {
                groupSelected.addElement(p_obj);
            }
        }
        return groupSelected;
    }


    public NetpCanvasObject findObjectById(String p_id)
    {
        return (NetpCanvasObject) m_objs.get(p_id);
    }

    public NetpCanvasObject get(String p_id)
    {
        return (NetpCanvasObject) m_objs.get(p_id);
    }

    public boolean isSelectChanged()
    {
        boolean tmp=selectChange;
        selectChange=false;
        return tmp;
    }
    public void deleteObject(NetpCanvasObject ob)
    {
        m_objs.remove(ob.getId());
		m_objlist.remove(ob);
    }
    public void deleteAllObject(){
    	m_objs.clear();
    	m_objlist.removeAllElements();
    }
    public String findNewId(String type) {
        int st=0;
        String id;
        Object ret;
        while (true) {
            id=type+"_"+st;
            ret=m_objs.get(id);
            if(ret==null) return id;
            st++;
        }
    }
    public void addObject(NetpCanvasObject ob)
    {
		addObject(m_objlist.size(),ob);
    }

    public void addObject(int pos, NetpCanvasObject ob)
    {
        String id=ob.getId();
        ob.setParent(m_par);
        m_objs.put(id,ob);
		m_objlist.add(pos,ob);
        m_par.receiveObject(ob);
    }

    public void buildSaveDocument(NetpCmdRequest p_doc){
        String p_id,p_para;
        NetpCanvasObject p_obj;
//        for (Enumeration e = m_objs.elements() ; e.hasMoreElements() ;) 
//        {
		int i,s=m_objlist.size();
		for(i=0;i<s;++i) {
            p_obj = (NetpCanvasObject) m_objlist.elementAt(i);
            if(!p_obj.needSave()) continue;
            p_id=p_obj.getId();
            p_para=p_obj.toString();
            p_doc.setPara(p_id,p_para);
        }
    }

	public void saveDocument(BuildTree bt)
	{
        BuildNode p_node;

        String p_id,p_para;
        NetpCanvasObject p_obj;
		int i,s=m_objlist.size();
//        for (Enumeration e = m_objs.elements() ; e.hasMoreElements() ;) 
//        {
		for(i=0;i<s;++i) {
            p_obj = (NetpCanvasObject) m_objlist.elementAt(i);
            if(!p_obj.needSave()) continue;

            p_id=p_obj.getId();
            p_para=p_obj.toString();
            p_node=new BuildNode(p_id,p_para);
            bt.appendChild(p_node);
        }
    }

    public NetpCanvasSubObject findSocket(Point p,String p_socket_type)
    {
        NetpCanvasObject p_obj;
        NetpCanvasSubObject p_sub_obj;
        for (Enumeration<NetpCanvasObject> e = m_objs.elements() ; e.hasMoreElements() ;) 
        {
            p_obj = (NetpCanvasObject) e.nextElement();
            p_sub_obj=p_obj.findSocket(p,p_socket_type);
            if(p_sub_obj!=null) return p_sub_obj;
        }
        return null;
    }
    public int getObjNum(){
    	return m_objlist.size();
    }
    public NetpCanvasObject getCanvasObject(int i){
    	return (NetpCanvasObject) m_objlist.get(i);
    }
}
