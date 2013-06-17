package ASPNode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

import netp.canvas.NetpCanvas;
import netp.xml.BuildTree;
import netp.xml.ParseNode;
import data.AbsData;
import ASPFrame.ASPFrame;
import ASPFrame.InterFrame;


abstract public class DocNode extends DefaultMutableTreeNode implements ActionListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JMenuItem export,showframe;
	JPopupMenu mn;
    
    protected void initMenu() {

		mn=new JPopupMenu();
    	export=new JMenuItem("Export To Pdf");
        export.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    exportSinglePage();
                }
            }
        );
        showframe=new JMenuItem("Show Frame");
        showframe.addActionListener(
            new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    showFrame();
                }
            }
        );
    }
    public void exportSinglePage() {
//        GseInterFrame gf=getGseFrame();
//        if(gf!=null) BerpFrame.exportSinglePage(gf);
    }
    public void showFrame() {
        InterFrame gf=getFrame();
		try { 
			gf.setVisible(true);
			gf.setSelected(true); 
		} catch (java.beans.PropertyVetoException e2) {
            e2.printStackTrace();
        }
        
    }
    public DocNode() {
        super();
    }
    public DocNode(Object o) {
        super(o);
    }

	public void showMenu(MouseEvent e)
	{
		if(mn!=null)
		mn.show(e.getComponent(),e.getX(),e.getY());
	}
	public void actionPerformed(ActionEvent e)
	{
	}
	public void doubleClick(){
	}
	public void saveDocument(BuildTree bt)
	{
	}
	public void parseNode(ParseNode nd) {
	}

	/*
	 * This function need to be reviewed
	 */
	public boolean matchStringId(String id) {
        AbsData gd=getData();
        if(gd.toString().equals(id)) return true;
        return false;
    }
    public DocNode searchNode(String PathId) {
        if((PathId==null)||(PathId.length()==0)) return this;
        String FirstId=getFirstId(PathId);
        String RestId=getRestId(PathId);

        for (Enumeration<?> e = children() ; e.hasMoreElements() ;) {
            DocNode gn=(DocNode) e.nextElement();
            if(gn.matchStringId(FirstId))
                return gn.searchNode(RestId);
        }
        return null;
    }
    static public String getFirstId(String Id) {
        int idx=Id.indexOf('.');
        if(idx==-1) return Id;
        return Id.substring(0,idx);
    }
    static public String getRestId(String Id) {
        int idx=Id.indexOf('.');
        if(idx==-1) return "";
        return Id.substring(idx+1);
    }
    public AbsData getData(){
        return (AbsData) getUserObject();
    }
    public String getId() {
        return toString();
    }
    public String toString(){
    	AbsData o=getData();
    	if(o==null) return "No name";
    	if(o.isChanged()) return "*"+o.getName();
    	return o.getName();
    }
    public void addChildNode(DocNode cn){
	    ASPFrame.getProjectFrame().treeModel.insertNodeInto(cn,
			     this, getChildCount());
    }
    public void removeNode(){
	    ASPFrame.getProjectFrame().treeModel.removeNodeFromParent(this);
    }
    abstract public InterFrame getFrame() ;
    abstract public NetpCanvas getCanvas();
    abstract public int getNodeImage();
    public String getToolTips(){
    	AbsData d=getData();
    	if(d==null) return "";
    	return d.getToolTips();
    }
    public String getDesc(){
    	AbsData d=getData();
    	if(d==null) return "";
    	return d.getDesc();
    }
    
    
}
