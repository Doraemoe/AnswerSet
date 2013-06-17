package netp.canvas;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import netp.GUI.*;
import netp.xmldocument.*;
import netp.xml.*;
import netp.absshape.*;
import netp.*;

public class NetpCanvas extends JPanel implements ActionListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected JMenuItem mchangesize;

	public NetpCanvasObject m_tipObj=null;
    public NetpCanvasHandler m_hd;
    private int m_zoom=100; // 100 is 1 means ori size 10 means 1/10

    protected NetpCanvasHost m_par;
    protected NetpCanvasObjectSet m_objs;

    protected JPopupMenu m_menu;
    protected JMenuItem m_mzoom;
    Graphics outg;
    Image outimg;

    protected Vector m_groupobj;
    protected JPopupMenu m_groupmenu;
    protected JMenuItem m_groupdelete;

	protected Dimension d=new Dimension(600,800);

    public Rectangle rectView=null;
	boolean sizechanged=true;
    protected int m_oriX=600,m_oriY=800;

	public void setHandleObjTip(boolean b) {
		m_hd.setHandleObjTip(b);
	}

	public void setTipObj(NetpCanvasObject o){
		m_tipObj=o;
	}
    public int getZoom() {
        return m_zoom;
    }
    public void setZoom (int z) {
        if(z<=0) z=1;
        m_zoom=z;
        d.width=m_oriX;
        d.height=m_oriY;
        setSize(d);
        repaint();
    }
    public Component getHost()
    {
        return (Component) m_par;
    }
    public void deleteGroupObject()
    {
        int i,s=m_groupobj.size();
        NetpCanvasObject p_ob;
        for(i=0;i<s;++i) {
            p_ob=(NetpCanvasObject) m_groupobj.elementAt(i);
            p_ob.deleteMe();
        }
        repaint();
    }
	public void actionPerformed(ActionEvent e){
		Object obj=e.getSource();
		if(obj instanceof AddCanvasObjectMenuItem){
			AddCanvasObjectMenuItem mt=(AddCanvasObjectMenuItem) obj;
			String cName=mt.getClassName();
			addNewObject(cName);
			return;
		}
        if(obj==m_groupdelete) {
            int res;
            res=JOptionPane.showConfirmDialog(null,
                "Are you sure to delete the selected objects ?","Delete",
                JOptionPane.YES_NO_OPTION);
            if(res==JOptionPane.YES_OPTION) {
                deleteGroupObject();
            }
        }
	}
	public Dimension getPreferredSize()
	{
		return d;
	}

	public void setSize(Dimension d)
	{
		m_oriX=d.width;
		m_oriY=d.height;

//        d.width=getDrawD(m_oriX);
//        d.height=getDrawD(m_oriY);
		this.d=d;
		setSize(d.width,d.height);
		setPreferredSize(d);
		sizechanged=true;

	}
	public void addNewObjectMenuItem(String lb,String cName)
	{
		JMenuItem mi=new AddCanvasObjectMenuItem(lb,cName);
		m_menu.add(mi);
		mi.addActionListener(this);
	}
    protected void init_menu()
    {

      addNewObjectMenuItem("Add Pixel", getClassName("pixel"));
      addNewObjectMenuItem("Add Line", getClassName("line"));
      addNewObjectMenuItem("Add Rectangle", getClassName("rect"));
      addNewObjectMenuItem("Add PolyLine",getClassName("polyline"));
      addNewObjectMenuItem("Add Label", getClassName("label"));
      m_menu.add(m_mzoom);
    }

    protected void init ()
    {
        m_hd = new NetpCanvasHandler(this);
        addKeyListener(m_hd);
        addMouseListener(m_hd);
        addMouseMotionListener(m_hd);
        m_objs=new NetpCanvasObjectSet(this);
		m_menu=new NetpPopupMenu();

        m_groupmenu=new NetpPopupMenu();
        m_mzoom=new JMenuItem("Zoom ...");
        m_mzoom.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e) {
                    openChangeZoomBox();
                }
            }
        );

		mchangesize=new JMenuItem("Change Size");
		mchangesize.addActionListener(
			new ActionListener (){
				public void actionPerformed(ActionEvent e){
					changeSize();
		}});


    }
    protected void init_group_menu() {

        m_groupdelete = new JMenuItem("Delete");
        m_groupdelete.addActionListener(this);
        m_groupmenu.add(m_groupdelete);
    }
    public NetpCanvas(NetpCanvasHost par)
    {
        super();
        m_par = par;
        init();
        init_menu();
        init_group_menu();
    }
    protected NetpCanvasObject addNewObject(String t_classname)
    {
        NetpCanvasObject p_obj=createNewObject(t_classname);
        if(p_obj==null) return null;
        String p_id=m_objs.findNewId(p_obj.getType());
        p_obj.setId(p_id);
        m_objs.addObject(p_obj);
        p_obj.setInitPoint(m_hd.getLastPoint());
        repaint();
		return p_obj;
    }

    protected NetpCanvasObject createNewObject(String t_classname)
    {
        try
        {
            Class p_class=Class.forName(t_classname);
            NetpCanvasObject p_obj=(NetpCanvasObject)p_class.newInstance();
            return p_obj;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }

    public void update(Graphics g)
    {
        paint(g);
    }
    public int getTotalHeight()
    {
        return d.height;
    }

    public int getTotalWidth()
    {
        return d.width;
    }


    protected void canvasDraw(Graphics g)
    {
        drawSelectedFrame(g);
    }
    protected void afterCanvasDraw(Graphics g) {
        return;
    }
    protected void drawSelectedFrame(Graphics g) {
        if(!m_hd.inSelectDragMode()) return;
        Point p1,p2;
        p1=m_hd.getSelectStartP();
        p2=m_hd.getSelectStopP();
        int x,y,w,h;
        x=p1.x; w=p2.x-p1.x;
        y=p1.y; h=p2.y-p1.y;

        if(w<0) {x=p2.x;w=-w; }
        if(h<0) {y=p2.y;h=-h; }
        Graphics2D g2=(Graphics2D) g;
        BasicStroke  bs=NetpGUIProvider.getDashLine();
        Stroke oldbs = g2.getStroke();
        g2.setStroke(bs);
        g2.setColor(Color.gray);
        g2.drawRect(x-rectView.x,y-rectView.y,w,h);
        g2.setStroke(oldbs);

    }
    protected void clearBackground(Graphics g)
    {
        g.setColor(Color.white);
//        g.fillRect(0,0,d.width,d.height);
        g.fillRect(0,0,rectView.width,rectView.height);
    }


	protected boolean sizeChanged()
	{
		return sizechanged;
	}
	protected boolean drawAble()
	{
		if ((getWidth()>0)&&(getHeight()>0))
			return true;
		return false;
	}
    public void paint ( Graphics g )
    {
        Rectangle rc;
/*
        if(sizeChanged())
        {
			sizechanged=false;
            outimg=createImage(d.width,d.height);
            outg=outimg.getGraphics();
        }
*/

        rc=m_par.getViewRect();
        if((rc.width<=0)||(rc.height<=0)) return;
        if((rectView==null)||(rectView.width!=rc.width)||
            (rectView.height!=rc.height)) {
            rectView=rc;
            outimg=createImage(rectView.width,rectView.height);
            outg=outimg.getGraphics();
        }
        if(!rectView.equals(rc))
            rectView=rc;
//      if(drawAble())
//      {
            clearBackground( outg );
            canvasDraw( outg);
            m_objs.draw(outg);
            afterCanvasDraw(outg);
			if(m_tipObj!=null) m_tipObj.drawToolTip(g);

            g.drawImage(outimg,rectView.x,rectView.y,this);
//      }
//		g.drawString("ok",10,10);
    }

    public void showMenu(Point p)
    {
        m_menu.show(this,p.x,p.y);
    }
    public Vector findRectSelectObject(int x,int y,int w,int h){
        return m_objs.findRectSelectObject(x,y,w,h);
    }
	public NetpCanvasObject findTipObject(Point p_point) {
		return m_objs.findTipObject(p_point);
	}
    public NetpCanvasObject findClickedObject(Point p_point)
    {
        return m_objs.findClickedObject( p_point);
    }
    public Vector findGroupClickedObject(Point p_point) {
        return m_objs.findGroupClickedObject( p_point);
    }
    public Vector findGroupSelectObject() {
        return m_objs.findGroupSelectObject();
    }

    public void deleteObject(NetpCanvasObject ob)
    {
        m_objs.deleteObject(ob);
        repaint();
    }
    public void deleteAllObject(){
    	m_objs.deleteAllObject();
    }

    public void selectChangeDraw()
    {
        if(m_objs.isSelectChanged())
           repaint();
    }

    public int getObjNum(){
    	return m_objs.getObjNum();
    }
    public NetpCanvasObject getCanvasObject(int i){
    	return m_objs.getCanvasObject(i);
    }
    
    
    public NetpCanvasObject findSelectObject()
    {
        return m_objs.findSelectObject();
    }
    public void buildSaveDocument(NetpCmdRequest p_doc)
    {
        m_objs.buildSaveDocument(p_doc);
    }
    public String getClassName(String p_type)
    {
        if("pixel".equals(p_type)) return "netp.canvas.NetpPixel";
        if("line".equals(p_type)) return "netp.canvas.NetpLine";
        if("polyline".equals(p_type)) return "netp.canvas.NetpPolyLine";
        if("rect".equals(p_type)) return "netp.canvas.NetpRect";
        if("label".equals(p_type)) return "netp.canvas.NetpLabel";
        return "";
    }

    public void receiveObjects(NetpMapParser mapParser)
    {
        NetpCanvasObject p_obj;
        String o_id,p_para;
        NetpCanvasObjectParser p_objp;
        String p_type,p_classname;
        for (Enumeration e = mapParser.getMapList() ; e.hasMoreElements() ;)
        {
            o_id=(String) e.nextElement();

            if("is_templet".equals(o_id)) continue;
            if("id".equals(o_id)) continue;

            p_para=mapParser.getMap(o_id);
            p_objp= new NetpCanvasObjectParser(p_para);

            p_type=p_objp.getStringValue("type");
            p_classname=getClassName(p_type);

            p_obj=createNewObject(p_classname);

            if(p_obj==null) continue;
            p_obj.setId(o_id);
            m_objs.addObject(p_obj);
            p_obj.setInit(p_objp);
        }
        repaint();
    }
    public NetpCanvasObject findObjectById(String p_id)
    {
        return m_objs.findObjectById(p_id);
    }

    public void sendSubObjCommand(String p_id,String p_sub_id,String p_cmd)
    {
        NetpCanvasObject ob=findObjectById(p_id);
        if(ob!=null)
            ob.sendSubObjCommand(p_sub_id,p_cmd);
    }

    public void sendSubObjCommand(String p_id,String p_sub_id,String p_cmd,Hashtable p_paras)
    {
        NetpCanvasObject ob=findObjectById(p_id);
        if(ob!=null)
            ob.sendSubObjCommand(p_sub_id,p_cmd,p_paras);
    }

    public void plugIn(String id1,String sub_id1,String id2,String sub_id2)
    {
        Hashtable ht;

        ht=new Hashtable();
        ht.put("obj_id",id2);
        ht.put("sub_obj_id",sub_id2);
        sendSubObjCommand(id1,sub_id1,"plugin",ht);

        ht=new Hashtable();
        ht.put("obj_id",id1);
        ht.put("sub_obj_id",sub_id1);
        sendSubObjCommand(id2,sub_id2,"plugin",ht);
    }
    public NetpCanvasSubObject findSocket(Point p,String p_socket_type)
    {
        return m_objs.findSocket(p,p_socket_type);
    }
    public NetpCanvasSubObject findSubObjectById(String p_id,String p_sub_id)
    {
        NetpCanvasObject p_ob;
        p_ob=findObjectById(p_id);
        if(p_ob==null) return null;
        return p_ob.findSubObjectById(p_sub_id);
    }
    public  void setAbsArrow(Vector v, Point pa, Point pb, int len,int type) {
        setAbsArrow(v,pa,pb,len,type,Color.black);
    }

    public  void setAbsArrow(Vector v, Point pa, Point pb, int len,int type,Color col)
    {
        AbsLine al;
        double sin15=0.2588,cos15=0.9659;
        double sin7 = 0.12189, cos7=0.99255;
        if(type==0) {
            double x_1,y_1,x_2,y_2;
            double x_m1,y_m1;

            x_m1=NetpCanvasGeom.findUniformPointX(pa,pb,len);
            y_m1=NetpCanvasGeom.findUniformPointY(pa,pb,len);

            x_1=((x_m1-pb.x)*cos15 + (y_m1-pb.y)*0.2588 + pb.x)+.5;
            y_1=((x_m1-pb.x)*(-0.2588) + (y_m1-pb.y)*cos15 + pb.y)+.5;
            al=new AbsLine();
            al.setPosition(pb.x,pb.y);
            al.setSize((int)(x_1-pb.x),(int)(y_1-pb.y));
            al.setColor(col);
            v.add(al);
            x_2=((x_m1-pb.x)*cos15 - (y_m1-pb.y)*0.2588 + pb.x)+.5;
            y_2=((x_m1-pb.x)*0.2588 + (y_m1-pb.y)*cos15 + pb.y)+.5;
            al=new AbsLine();
            al.setPosition(pb.x,pb.y);
            al.setSize((int)(x_2-pb.x),(int)(y_2-pb.y));
            al.setColor(col);
            v.add(al);

            return;
        }
        if(type==2) // two solid arrow
        {
            double x_1,y_1,x_2,y_2,x_3,y_3,x_4,y_4;
            double x_m1,y_m1,x_m2,y_m2;
            x_m1=NetpCanvasGeom.findUniformPointX(pa,pb,len);
            y_m1=NetpCanvasGeom.findUniformPointY(pa,pb,len);

            x_m2=NetpCanvasGeom.findUniformPointX(pa,pb,len*2);
            y_m2=NetpCanvasGeom.findUniformPointY(pa,pb,len*2);

            x_1=(((double)(x_m1-pb.x))*cos15 + ((double)(y_m1-pb.y))*sin15 + pb.x)+.5;
            y_1=(((double)(x_m1-pb.x))*(-sin15) + ((double)(y_m1-pb.y))*cos15 + pb.y)+.5;

            x_2=(((double)(x_m1-pb.x))*cos15 - ((double)(y_m1-pb.y))*sin15 + pb.x)+.5;
            y_2=(((double)(x_m1-pb.x))*sin15 + ((double)(y_m1-pb.y))*cos15 + pb.y)+.5;

            x_3=(((double)(x_m2-pb.x))*cos7 + ((double)(y_m2-pb.y))*sin7 + pb.x)+.5;
            y_3=(((double)(x_m2-pb.x))*(-sin7) + ((double)(y_m2-pb.y))*cos7 + pb.y)+.5;

            x_4=(((double)(x_m2-pb.x))*cos7 - ((double)(y_m2-pb.y))*sin7 + pb.x)+.5;
            y_4=(((double)(x_m2-pb.x))*sin7 + ((double)(y_m2-pb.y))*cos7 + pb.y)+.5;
            x_m1+=.5; y_m1+=.5;
            AbsPolygon pg=new AbsPolygon();
            pg.addPoint(pb.x,pb.y);
            pg.addPoint((int)x_2,(int)y_2);
            pg.addPoint((int) x_m1,(int) y_m1);
            pg.addPoint((int)x_4,(int)y_4);
            pg.addPoint((int)x_3,(int)y_3);
            pg.addPoint((int)x_m1,(int) y_m1);
            pg.addPoint((int)x_1,(int)y_1);
            pg.setColor(col);
            v.add(pg);
            return;

        }
    }
    public  void drawArrow( Graphics g, Point pa, Point pb, int len,int type)
    {
        double sin15=0.2588,cos15=0.9659;
        double sin7 = 0.12189, cos7=0.99255;
        if(type==0) {

            double x_1,y_1,x_2,y_2;
            double x_m1,y_m1;

            x_m1=NetpCanvasGeom.findUniformPointX(pa,pb,len);
            y_m1=NetpCanvasGeom.findUniformPointY(pa,pb,len);

            x_1=((x_m1-pb.x)*cos15 + (y_m1-pb.y)*0.2588 + pb.x)+.5;
            y_1=((x_m1-pb.x)*(-0.2588) + (y_m1-pb.y)*cos15 + pb.y)+.5;
            g.drawLine(getDrawX((int) x_1), getDrawY((int) y_1),
                getDrawX(pb.x), getDrawY(pb.y));

            x_2=((x_m1-pb.x)*cos15 - (y_m1-pb.y)*0.2588 + pb.x)+.5;
            y_2=((x_m1-pb.x)*0.2588 + (y_m1-pb.y)*cos15 + pb.y)+.5;
            g.drawLine(getDrawX((int) x_2), getDrawY((int) y_2),
             getDrawX(pb.x), getDrawY(pb.y));
            return;
        }
        if(type==2) // two solid arrow
        {

            double x_1,y_1,x_2,y_2,x_3,y_3,x_4,y_4;
            double x_m1,y_m1,x_m2,y_m2;

            x_m1=NetpCanvasGeom.findUniformPointX(pa,pb,len);
            y_m1=NetpCanvasGeom.findUniformPointY(pa,pb,len);

            x_m2=NetpCanvasGeom.findUniformPointX(pa,pb,len*2);
            y_m2=NetpCanvasGeom.findUniformPointY(pa,pb,len*2);

            x_1=(((double)(x_m1-pb.x))*cos15 + ((double)(y_m1-pb.y))*sin15 + pb.x)+.5;
            y_1=(((double)(x_m1-pb.x))*(-sin15) + ((double)(y_m1-pb.y))*cos15 + pb.y)+.5;

            x_2=(((double)(x_m1-pb.x))*cos15 - ((double)(y_m1-pb.y))*sin15 + pb.x)+.5;
            y_2=(((double)(x_m1-pb.x))*sin15 + ((double)(y_m1-pb.y))*cos15 + pb.y)+.5;

            x_3=(((double)(x_m2-pb.x))*cos7 + ((double)(y_m2-pb.y))*sin7 + pb.x)+.5;
            y_3=(((double)(x_m2-pb.x))*(-sin7) + ((double)(y_m2-pb.y))*cos7 + pb.y)+.5;

            x_4=(((double)(x_m2-pb.x))*cos7 - ((double)(y_m2-pb.y))*sin7 + pb.x)+.5;
            y_4=(((double)(x_m2-pb.x))*sin7 + ((double)(y_m2-pb.y))*cos7 + pb.y)+.5;
            x_m1+=.5; y_m1+=.5;
            Polygon pg=new Polygon();
            pg.addPoint(getDrawX(pb.x),getDrawY(pb.y));
            pg.addPoint(getDrawX((int)x_2),getDrawY((int)y_2));
            pg.addPoint(getDrawX((int) x_m1),getDrawY((int) y_m1));
            pg.addPoint(getDrawX((int)x_4),getDrawY((int)y_4));
            pg.addPoint(getDrawX((int)x_3),getDrawY((int)y_3));
            pg.addPoint(getDrawX((int)x_m1),getDrawY((int) y_m1));
            pg.addPoint(getDrawX((int)x_1),getDrawY((int)y_1));
            g.fillPolygon(pg);
            return;

        }
    }
    public void mayChanged()
    {
    }

    public void initSelectGroup() {
        m_objs.initSelectGroup();
    }
    public void addGroupSelectObject(NetpCanvasObject o) {
        m_objs.addGroupSelectObject(o);
    }

    protected void receiveObject(NetpCanvasObject ob){
    }

    protected void showGroupMenu(Point p,Vector v){
        m_groupobj=v;
        m_groupmenu.show(this,p.x,p.y);
    }
    public void clearAll() {
        m_objs=new NetpCanvasObjectSet(this);
        repaint();
    }
    public int findMaxX() {
        int x0=0,x;
        NetpCanvasObject p_obj;
        for (Enumeration e = m_objs.elements() ; e.hasMoreElements() ;)
        {
            p_obj=(NetpCanvasObject) e.nextElement();
            x=p_obj.findMaxX();
            if(x>x0) x0=x;
        }
        return x0;
    }
    public int findMaxY() {
        int x0=0,x;
        NetpCanvasObject p_obj;
        for (Enumeration e = m_objs.elements() ; e.hasMoreElements() ;)
        {
            p_obj=(NetpCanvasObject) e.nextElement();
            x=p_obj.findMaxY();
            if(x>x0) x0=x;
        }
        return x0;
    }
    public void autoSize(int x0, int y0) {
        int x,y;
        x=findMaxX()+10;
        y=findMaxY()+10; // 10 is margine
        if(x<x0) x=x0;
        if(y<y0) y=y0; // x0 y0 the minimum size
//        m_oriX=x; m_oriY=y;
        setSize(new Dimension(x,y));
    }
    public Vector GetAbsShape() {
        NetpCanvasObject p_obj;
        Vector v = new Vector();
        Vector v2;
        for (Enumeration e = m_objs.elements() ; e.hasMoreElements() ;)
        {
            p_obj=(NetpCanvasObject) e.nextElement();
            v2=p_obj.GetAbsShape();
            if(v2==null) continue;
            if(p_obj.preferDrawFirst())
                v.addAll(0,v2);
            else
                v.addAll(v2);
        }
        return v;
    }
    public void saveDocument(BuildTree bt)
    {
        BuildNode p_node;
	p_node=new BuildNode("cvs_wd",""+getTotalWidth());
	bt.appendChild(p_node);
	p_node=new BuildNode("cvs_ht",""+getTotalHeight());
	bt.appendChild(p_node);

        m_objs.saveDocument(bt);
    }

    public void parseNode(ParseNode nd)
    {
        int i,s;
        s=nd.getTotalChild();
        ParseNode pd;
        String o_id,p_para,p_type,p_classname;
        NetpCanvasObjectParser p_objp;
        NetpCanvasObject p_obj;

	int w = NetpGeneral.stringToInt(nd.getChildValue("cvs_wd"));
	int h = NetpGeneral.stringToInt(nd.getChildValue("cvs_ht"));
        if((w>0)&&(h>0)){
//            m_oriX=w;
//            m_oriY=h;
            setSize(new Dimension(w,h));
        }

        for(i=0;i<s;++i)
        {
            pd=nd.getChild(i);
            o_id=pd.getTag();
            p_para=pd.getValue();
            p_objp= new NetpCanvasObjectParser(p_para);

            p_type=p_objp.getStringValue("type");
            if((p_type==null)||(p_type.equals(""))) continue;
            p_classname=getClassName(p_type);
            if((p_classname==null)||(p_classname.equals(""))) continue;

            p_obj=createNewObject(p_classname);
            if(p_obj==null) continue;
            p_obj.setId(o_id);
            p_obj.setInit(p_objp);

            m_objs.addObject(p_obj);
        }
    }

    public void receiveCmd(String cmd,Hashtable tb)
    {
    }

    public void receiveCmd(String cmd) {
    }


    public void updateData() {
        repaint();
    }
    public void openChangeZoomBox() {
        String[] zoms={"100","95","90","85","80","75","70","65","60","55",
        		"50","45","40","35","30","25","20","15","10","5","4","3","2","1"};
        Object res=JOptionPane.showInputDialog(
            (Component) m_par,
            "Zoom","Zoom",JOptionPane.QUESTION_MESSAGE,
            null,zoms,""+getZoom());
        if(res==null) return;
        String ret=(String)res;
        setZoom(NetpGeneral.stringToInt(ret));
    }
    public int getZoomValue(int inp){
    	return inp*m_zoom/100;
    }
    public int getZoomRevValue(int inp){
    	return inp*100/m_zoom;
    }
    public int getDrawX(int x) {
    	return getZoomValue(x)-rectView.x;
//        return x/m_zoom-rectView.x;
    }
    public int getDrawY(int y) {
    	return getZoomValue(y)-rectView.y;
//        return y/m_zoom-rectView.y;
    }
    public int getDrawD(int d) {
    	return getZoomValue(d);
//        return d/m_zoom;
    }

    public Point getOriPoint(Point p) {
        Point pp=new Point();
        pp.x=this.getZoomRevValue(p.x);
        pp.y=this.getZoomRevValue(p.y);
//        pp.x=p.x*m_zoom;
//        pp.y=p.y*m_zoom;
        return pp;
    }
    public int getOriD(int x) {
    	return this.getZoomRevValue(x);
//        return x*m_zoom;
    }
    public void changeSize(){
        Object[] message = new Object[5];
		JTextField wd=new JTextField(""+getTotalWidth());
		JTextField ht=new JTextField(""+getTotalHeight());

        JCheckBox jb=new JCheckBox("Auto Size",false);
		message[0] = "Canvas Width:";
        message[1] = wd;
		message[2] = "Canvas Height:";
		message[3] = ht;
		message[4] = jb;

        String[] options = {
		    "Ok",
		    "Cancel"
		};
        int result = JOptionPane.showOptionDialog(
		    (Component)m_par,                             // the parent that the dialog blocks
		    message,                                    // the dialog message array
		    "Change Canvas Size", // the title of the dialog window
		    JOptionPane.DEFAULT_OPTION,                 // option type
		    JOptionPane.INFORMATION_MESSAGE,            // message type
		    null,                                       // optional icon, use null to use the default icon
		    options,                                    // options string array, will be made into buttons
		    options[0]                                  // option that should be made into a default button
		);
		if(result==0) {
            if(jb.isSelected()){
                autoSize(400,600);
                return;
            }

             int h,w;
             h=NetpGeneral.stringToInt(ht.getText());
             w=NetpGeneral.stringToInt(wd.getText());
			 if((h>0)&&(w>0)) {
//                 m_oriX=w;
//                 m_oriY=h;
                setSize(new Dimension(w,h));
				repaint();
			 }
		}
    }

}
class AddCanvasObjectMenuItem extends JMenuItem
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String cName;
	public AddCanvasObjectMenuItem(String lb,String classname)
	{
		super(lb);
		cName=classname;
	}
	public String getClassName()
	{
		return cName;
	}
}