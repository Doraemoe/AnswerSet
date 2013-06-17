package ASPFrame;

import java.awt.Rectangle;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.UIManager;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import netp.canvas.NetpCanvas;
import netp.canvas.NetpCanvasHost;
import netp.xml.BuildTree;
import netp.xml.ParseNode;

public class InterFrame extends JInternalFrame implements NetpCanvasHost, InternalFrameListener
{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static int xStar=210;
    static int yStar=35;
    JViewport vp;
    NetpCanvas cvs;
    JScrollPane pl;
    public InterFrame(Object o) {
		super(o.toString(), true, true, true, true);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setFrameIcon( (Icon)UIManager.get("Tree.openIcon")); // PENDING(steve) need more general palce to get this icon
		setBounds( xStar, yStar, 400, 400);
        xStar+=20;
        yStar+=20;
        if(yStar>150) {
            xStar=210;
            yStar=35;
        }
        initConPanel();
    }
    void initConPanel(){
    	pl = new JScrollPane();
    	setContentPane(pl);
    	vp = pl.getViewport();
        addInternalFrameListener(this);
    }
    public void setCanvas(NetpCanvas vs) {
	vs.setBounds(0,0,600,800);
	vp.add(vs);
        pl.addKeyListener(vs.m_hd);
        cvs=vs;
    }
    public void saveDocument(BuildTree bt)
    {
        cvs.saveDocument(bt);
    }

    public void parseNode(ParseNode nd)
    {
       cvs.parseNode(nd);
     }
    public NetpCanvas getCanvas() {
        return cvs;
    }
    public Rectangle getViewRect(){
        return vp.getViewRect();
    }
    public void internalFrameActivated(InternalFrameEvent e){
      ASPFrame.setActiveCanvas(cvs);

    }
    public void internalFrameDeactivated(InternalFrameEvent e){
      ASPFrame.setActiveCanvas(null);
    }
    public void internalFrameClosed(InternalFrameEvent e){

    }
    public void internalFrameClosing(InternalFrameEvent e){

    }
    public void internalFrameDeiconified(InternalFrameEvent e){

    }
    public void internalFrameIconified(InternalFrameEvent e){

    }
    public void internalFrameOpened(InternalFrameEvent e){

    }
}
