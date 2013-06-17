package ASPNode;

import netp.canvas.NetpCanvas;
import ASPFrame.InterFrame;
import AnswerSet.ImageManager;

public class TestSetNode extends DocNode
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
//	BerpTreeEditCanvas cvs;
//	BerpBehaviorTree m_tree;
	
    public TestSetNode(Object o) {
		super(o);
    }
//    public void setBehaviorTree(BerpBehaviorTree t){
//    	m_tree=t;
//    }
    public boolean isLeaf() {
		return false;
    }
    
    public int getNodeImage(){
    	return ImageManager.TREENODE;
    }
	public NetpCanvas getCanvas() {
		// TODO Auto-generated method stub
		return null;
	}
	public InterFrame getFrame() {
		// TODO Auto-generated method stub
		return null;
	}

}
