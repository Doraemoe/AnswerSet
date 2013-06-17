package ASPNode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import netp.canvas.NetpCanvas;
import ASPFrame.InterFrame;
import AnswerSet.ImageManager;
import data.TestData;

public class TestResultNode extends DocNode
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    public TestResultNode(Object o) {
		super(o);
		initMenu();
		
		
    }
    public boolean isLeaf() {
		return true;
    }
    
    public int getNodeImage(){
    	return ImageManager.TREENODE;
    }
    
	protected void initMenu()
	{
		super.initMenu();
		JMenuItem mnStat;
		mnStat=new JMenuItem("Simple Statistics");
		mn.add(mnStat);
		mnStat.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e){
						showSimpleStatistic();
					}
				}
		);

	}
	void showSimpleStatistic(){
		
	}
    public void close(){
    }

    public InterFrame getFrame(){ return null;}
    public NetpCanvas getCanvas(){ return null;}

    TestData getTestData(){
    	return (TestData)getData();
    }
    
	public void doubleClick(){
//		openBehaviorTreeFrame();
	}

}
