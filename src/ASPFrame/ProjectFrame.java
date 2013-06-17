package ASPFrame;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import netp.xml.BuildNode;
import netp.xml.BuildTree;
import netp.xml.ParseTree;

//import canvas.BerpTreeEditCanvas;
import ASPNode.DocNode;
//import node.BerpExtraRunTreeNode;
import ASPNode.ProjectRoot;

public class ProjectFrame extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public DefaultTreeModel treeModel;
	JTree tree;
    JTextArea fid;
	static ProjectRoot root;

    public ProjectFrame() {
		super("Project Explorer", true, true, true, true);
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setFrameIcon( (Icon)UIManager.get("Tree.openIcon")); // PENDING(steve) need more general palce to get this icon

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		setBounds( 0, 0, 200, screenSize.height-50);
		root=new ProjectRoot(ASPFrame.getData());

		init();
    }
    static public ProjectRoot getRootNode() {
        return root;
    }
//    public void initExtraSysNodeSet(){
//    	root.createExtraSysNode();
//    }
	private void init()
	{
		JPanel panel=new JPanel();

		panel.setLayout(new BorderLayout());

		JScrollPane sp = new JScrollPane();
		sp.setPreferredSize(new Dimension(300, 300));
		panel.add("Center",sp);


		setContentPane(panel);
		treeModel = new DefaultTreeModel(root);
		tree=new JTree(treeModel);

		sp.getViewport().add(tree);
		ToolTipManager.sharedInstance().registerComponent(tree);

        fid=new JTextArea(5,20);
        fid.setWrapStyleWord(true);
        fid.setLineWrap(true);
        JScrollPane sp2 = new JScrollPane();
        panel.add("South",sp2);
        sp2.getViewport().add(fid);

		MouseListener ml = new MouseAdapter() {
			 public void mouseClicked(MouseEvent e) {
				 int selRow = tree.getRowForLocation(e.getX(), e.getY());
				 if(selRow != -1) {
					 TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
					 DocNode nd=(DocNode) selPath.getLastPathComponent();

				    fid.setText(nd.getDesc());
                	if((e.getModifiers() & MouseEvent.BUTTON3_MASK) == MouseEvent.BUTTON3_MASK) {
						nd.showMenu(e);
					}

					 if(e.getClickCount() == 2) {
						nd.doubleClick();
					 }
				 }
			 }
		 };
		 tree.addMouseListener(ml);
		 tree.putClientProperty("JTree.lineStyle","Angled");
		 tree.setCellRenderer(new CellRenderer());
	}
	public void saveDocument(BuildTree bt)
	{
        BuildNode p_node = new BuildNode ("View");
        bt.appendChild(p_node);
        bt.downTree();
    	root.saveDocument(bt);
		bt.upTree();

	}
	public boolean parseTree(ParseTree pt)
	{
		root.parseNode(pt.getRoot().findFirstChild("View"));
		repaint();
		return true;
	}

    public void close() {
		root.close();
        init();
    }
//	public BerpTreeEditCanvas getTreeEditCanvasBySysName(String sysName) {
//		return root.getTreeEditCanvasBySysName(sysName);
//	}
	
//    public BerpExtraRunTreeNode getExtraSystemById(String sysId){
//    	return root.getExtraSystemById(sysId);
//    }

}