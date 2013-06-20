package ASPNode;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import netp.NetpFileLineReader;
import netp.NetpGeneral;
import netp.canvas.NetpCanvas;
import ASPFrame.ASPFrame;
import ASPFrame.DistributionFrame;
import ASPFrame.InterFrame;
import AnswerSet.ImageManager;
import data.ASPProject;
import data.TestData;

public class TestDataNode extends DocNode
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
    public TestDataNode(Object o) {
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
		JMenuItem mnShowDistribution;
		mnShowDistribution=new JMenuItem("Show degree distribution of literals");
		mn.add(mnShowDistribution);
		mnShowDistribution.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e){
						showDegreeDistribution();
					}
				}
		);

	}
    
	void addDistribution(int rData[],int iP){
    	TestData td;
    	String strFileName;
    	int iDataSer;
    	td=getTestData();
    	iDataSer=td.getSer();
    	ProjectNode pn;
    	pn=(ProjectNode) getParent().getParent();
    	ASPProject app;
    	app=pn.getProjectData();
    	
    	strFileName=app.getTestDataProgramFile(iDataSer, iP);

    	int n,l,k;
    	n=td.getAtomNum();
    	l=td.getRulesNum();
    	k=td.getLiteralNum();
    	int nDist[]=new int[n]; // this is not distribution. it is only the appearance times of a literal.
    	
    	
//    	strFileName="C:\\asptest\\test\\TestPowerLaw\\data_1\\data_0.txt";

    	NetpFileLineReader fr=new NetpFileLineReader(strFileName);
    	fr.open();
    	
    	String strRule,strHead,strBody;
    	int iLiteral;
    	int i,j;
    	
    	for(i=0;i<n;++i){
    		nDist[i]=0;
    	}
    	for(i=0;i<l;++i){
    		strRule=fr.readNextLine();
    		strHead=NetpGeneral.findStringBetween(strRule, "_", ":").trim();
    		iLiteral=Integer.parseInt(strHead);
    		nDist[iLiteral]++;
    		strBody=NetpGeneral.findStringAfter(strRule, ":-");
    		for(j=0;j<k-2;++j){
    			strHead=NetpGeneral.findStringBetween(strBody, "_", ",");
        		iLiteral=Integer.parseInt(strHead);
        		nDist[iLiteral]++;
    			strBody=NetpGeneral.findStringAfter(strBody, ",");
    		}
			strHead=NetpGeneral.findStringBetween(strBody, "_", ".");
    		iLiteral=Integer.parseInt(strHead);
    		nDist[iLiteral]++;
    	}
    	int r;
    	for(i=0;i<n;++i){
    		r=nDist[i];
    		if(r>=n)r=n-1; // incase not over flow
    		rData[r]++;
    	}
		
	}
	
	public void showDegreeDistribution(int iP){
    	TestData td;
    	int pNum;

    	td=getTestData();
    	pNum=td.getProgramNum();

    	int n;
    	n=td.getAtomNum();
    	int rDist[]=new int[n]; // this is the real distribution.
    	int i;
    	
    	for(i=0;i<n;++i){
    		rDist[i]=0;
    	}

    	if(iP>=0){
    		addDistribution(rDist,iP);
    	}
    	else {
    		for(i=0;i<pNum;++i)
        		addDistribution(rDist,i);
    	}
    		

    	
    	DistributionFrame sf;
		sf=new DistributionFrame();
		
		sf.setDistData(rDist);
		ASPFrame.addFrame(sf);
		ASPFrame.openFrame(sf);
    	
    	
    	
    }
    public void showDegreeDistribution(){
    	TestData td;
    	td=getTestData();
    	int pNum;
    	pNum=td.getProgramNum();

        String[] options = {
    		    "Ok",
    		    "Cancel"
    		};
    	
    	
    	JPanel jp;
        jp=new JPanel();
        jp.setLayout(new GridLayout(1,2));
        
        
        JTextField tf_program = new JTextField(10); 
        tf_program.setText("-1");

        jp.add(new JLabel("Program no (-1 -"+(pNum-1)+")"));
		jp.add(tf_program);

        int result = JOptionPane.showOptionDialog(
    		    null,                             // the parent that the dialog blocks
    		    jp,                                    // the dialog message array
    		    "Display the literal degree distribution of a selected program", // the title of the dialog window
    		    JOptionPane.DEFAULT_OPTION,                 // option type
    		    JOptionPane.INFORMATION_MESSAGE,            // message type
    		    null,                                       // optional icon, use null to use the default icon
    		    options,                                    // options string array, will be made into buttons
    		    options[0]                                  // option that should be made into a default button
    		);
        if(result==0) {
        	int m_p;
        	
        	m_p=Integer.parseInt(tf_program.getText());
            showDegreeDistribution(m_p);
        }
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
