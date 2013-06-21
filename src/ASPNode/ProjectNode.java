package ASPNode;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.tree.TreeNode;

import netp.GUI.LineCurve;
import netp.canvas.NetpCanvas;
import netp.xml.ParseNode;
import ASPFrame.ASPFrame;
import ASPFrame.CreateNewDataDialog;
import ASPFrame.CreateNewDataDialogTab;
import ASPFrame.CreateNewTestDialog;
import ASPFrame.CreateNewThreeLiteralDataDialog;
import ASPFrame.InterFrame;
import ASPFrame.ShowStaticDialog;
import ASPFrame.StaticsFrame;
import AnswerSet.ASP;
import AnswerSet.ImageManager;
import data.ASPProject;
import data.BasicResultStatic;
import data.DataSet;
import data.TestData;
import data.TestResult;
import data.TestSet;

public class ProjectNode extends DocNode
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JMenuItem rename;
	InterFrame bhvFrame;
//	BerpTreeEditCanvas cvs;
//	BerpBehaviorTree m_tree;
	
    public ProjectNode(Object o) {
		super(o);
		initMenu();
		bhvFrame=null;
		
		
		//		runFrame=null;
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
    
	protected void initMenu()
	{
		super.initMenu();

		JMenuItem mnCreateNewData;
		mnCreateNewData=new JMenuItem("Create New Test Data...");
		mn.add(mnCreateNewData);
		mnCreateNewData.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e){
						createNewTextData();
					}
				}
		);
		
		JMenuItem mnOpenFrame;
		mnOpenFrame=new JMenuItem("Test...");
		mn.add(mnOpenFrame);
		mnOpenFrame.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e){
						tryTest();
					}
				}
		);
		JMenuItem mnShowStatics;
		
		mnShowStatics=new JMenuItem("Show Statics...");
		mn.add(mnShowStatics);
		mnShowStatics.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        tryShowStatics();
                    }
                }
            );
		
		JMenuItem mnSave;
		
		mnSave=new JMenuItem("Save Project");
		mn.add(mnSave);
		mnSave.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        tryToSave();
                    }
                }
            );


		JMenuItem mnExport;
		mnExport=new JMenuItem("Export Project");
		mn.add(mnExport);
		mnExport.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        tryToExport();
                    }
                }
            );
		
		
	}

	void tryToExport(){
		ASPProject data;
		data=this.getProjectData();
		data.tryToExport();
		
	}
	void tryToSave(){
		ASPProject data;
		data=this.getProjectData();
		data.tryToSave();
	}
	void tryTest(int p_data_ser,boolean bBatchMode,String strSolver,boolean allModel){
		ASPProject data;
		data=this.getProjectData();

		long p_timeusage;
		int m_testnum;
		m_testnum=data.getTestResultNum();
		BasicResultStatic brs;
		brs=data.performTest(p_data_ser, m_testnum,bBatchMode,strSolver, allModel);
		p_timeusage=brs.getTotalTime();

		TestSet ts=getTestSet();
		int ser;
		ser=ts.createNewTestResult();
		TestResult tr;
		tr=ts.getResultData(ser);
		tr.setTestDataNum(p_data_ser);
		tr.setTimeUsage(p_timeusage);
		tr.setBasicResultStatic(brs);
		tr.setSolver(strSolver);
		
		TestResultNode trn=new TestResultNode(tr);
//		nd.setBehaviorTree(bt);
		TestSetNode tsn=getTestSetNode();
		tsn.addChildNode(trn);
		save();
		
	}
	void tryTest(){
		ASPProject data;
		data=this.getProjectData();
		
		int maxdataser;
		maxdataser=data.getTestDataNum();

		
		CreateNewTestDialog dlg=new CreateNewTestDialog(maxdataser);
		int p_data_ser;
		if(!dlg.start())
			return;
		
		p_data_ser=dlg.getTestSer();

		String strSolver;
		strSolver=dlg.getSolver();
		boolean allModel;
		allModel=dlg.getAllModel();
		if(dlg.isBatchMode()){
			int p_data_end=dlg.getTestSerEnd();
			for(int i=p_data_ser;i<=p_data_end;++i)
				tryTest(i,true,strSolver,allModel);
		}
		else 
			tryTest(p_data_ser,false,strSolver,allModel);
	}
	
	public void tryShowStatics(){

		ASPProject data;
		data=this.getProjectData();
		
		int maxtestser;
		maxtestser=data.getTestResultNum();

		
		ShowStaticDialog dlg=new ShowStaticDialog(maxtestser);

		int p_test_beg,p_test_end;
		if(!dlg.start())
			return;
		
		p_test_beg=dlg.getTestSer();

		p_test_end=dlg.getTestSerEnd();
		TestResult tr;
		TestData td;
		double p_density_beg=0,p_density_end=0;
		int iTestDataNum;
		LineCurve lc=new LineCurve(p_test_end-p_test_beg+1,Color.WHITE);
		LineCurve lc2=new LineCurve(p_test_end-p_test_beg+1,Color.YELLOW);
		
		for(int i=p_test_beg;i<=p_test_end;++i){
			tr=data.getTestResult(i);
			if(i==p_test_beg) {
				iTestDataNum=tr.getTestDataNum();
				td=data.getTestData(iTestDataNum);
				p_density_beg=td.getRuleDensity();
			}
			if(i==p_test_end) {
				iTestDataNum=tr.getTestDataNum();
				td=data.getTestData(iTestDataNum);
				p_density_end=td.getRuleDensity();
			}
			
			
			lc.setData(tr.getSatRate());
			lc2.setData(tr.getTestTime());
		}
		
		StaticsFrame sf;
		sf=new StaticsFrame();
		
		sf.setSatRateCurve(lc);
		sf.setXValueRange(p_density_beg, p_density_end);
		sf.setTimeCurve(lc2);
		ASPFrame.addFrame(sf);
		ASPFrame.openFrame(sf);
	}
	
	/*
	public void openBehaviorTreeRunFrame(){
		BerpRunFrame runFrame;
		BerpTreeRunCanvas runCvs;
		
		if(bhvFrame==null) {
			openBehaviorTreeFrame();
		}
		bhvFrame.setVisible(false);
		BerpBehaviorTreeNode bn=cvs.getTreeNodeRoot();
    	if(bn==null) return; // can't proceed
    	
    	BerpBehaviorTree selftree=(BerpBehaviorTree) m_tree;
    	BerpBehaviorTree mtree=BerpFrame.CloneRuntimeBehaviorTree(selftree.getSystemName(),"");
    	if(mtree==null)return;
    	
//    	BerpBehaviorTree mtree=new BerpBehaviorTree(selftree.getSystemName());
    	
//    	mtree.setRootNode(bn);
		mtree.getRoot().setRunTimeNodeId("0",true); // true means set the runtime id for whole tree
    	mtree.prepareToRun();
    	if(!mtree.tryToBuildComponentTrees()) return; 

		runFrame=new BerpRunFrame(mtree);
		runFrame.setIsSystem();
		mtree.setRunName(runFrame.getTitle());
		runFrame.setTitle("R::"+runFrame.getTitle());
		BerpFrame.addFrame(runFrame);
		runCvs=new BerpTreeRunCanvas(runFrame);
		runCvs.setData(mtree);
		runCvs.setTreeNode(this);
		runFrame.setCanvas(runCvs);

		BerpRunTreeNode rnd=new BerpRunTreeNode(mtree);
		rnd.setFrame(runFrame);
		rnd.setBehaviorTree(mtree);
		addChildNode(rnd);
		rnd.setIsSystem();

		runFrame.initRunPane();
    	runCvs.initTreeNodes();
    	runFrame.setVisible(true);
    	createChildComponentRunFrame(rnd, runFrame,mtree);
		mtree.clearChanged();
    	runCvs.autoSize(10, 10);
	}
*/
/*
	static void createChildComponentRunFrame(BerpDocNode parNode, BerpRunFrame parFrame, BerpBehaviorTree parTree){
		int s;
		s=parTree.getCompTreeNum();
		if(s==0) return;
		Enumeration e=parTree.getCompTreeList();
		BerpBehaviorTree chbt;
		String chbtName;
		BerpRunFrame chRunFrame;
		BerpTreeRunCanvas chRunCvs;
		
		BerpSimpleSystemRun pRun,cRun;
		while(e.hasMoreElements()){
			chbtName=(String)e.nextElement();
			chbt=parTree.getCompTreebyName(chbtName);

			chRunFrame=new BerpRunFrame(chbt);
			parFrame.addChildFrame(chRunFrame);
			BerpFrame.addFrame(chRunFrame);
			chRunCvs=new BerpTreeRunCanvas(chRunFrame);
			chRunCvs.setData(chbt);
			chRunFrame.setCanvas(chRunCvs);

			chRunFrame.initRunPane();
			chRunCvs.initTreeNodes();
	    	chRunFrame.setVisible(true);

	    	chbt.setRunName(chRunFrame.getTitle());
	    	chRunFrame.setTitle(parFrame.getTitle()+"->"+chRunFrame.getTitle());

	    	pRun=parFrame.getRun();
	    	cRun=chRunFrame.getRun();
	    	cRun.setParentRun(pRun);
	    	pRun.addChildRun(cRun);
	    	
			BerpRunTreeNode rnd=new BerpRunTreeNode(chbt);
			rnd.setFrame(chRunFrame);
			rnd.setBehaviorTree(chbt);
			parNode.addChildNode(rnd);
	    	
	    	createChildComponentRunFrame(rnd, chRunFrame,chbt);
	    	chbt.clearChanged();
	    	chRunCvs.autoSize(10, 10);
	    	chRunFrame.setVisible(false);

		}
	}
*/
/*
	public void openBehaviorTreeFrame(){

		
		if(bhvFrame==null) {
			bhvFrame=new BerpInterFrame(m_tree);
			BerpFrame.addFrame(bhvFrame);
			cvs=new BerpTreeEditCanvas(bhvFrame);
			cvs.setData(m_tree);
			cvs.setTreeNode(this);
			bhvFrame.setCanvas(cvs);

			BerpSystem d=(BerpSystem)getBerpData();
			ParseNode pn=d.loadFile();
			if(pn!=null)
				cvs.parseNode(pn);
			getBerpSystem().setCanvas(cvs);
		}
		bhvFrame.setVisible(true);
	}
*/
	public void save(){
		getProjectData().tryToSave();
	}
    public void close(){
//		nComs.close();
//		nBhvs.close();
 //       nFBhvs.close();
    }

    public InterFrame getFrame(){ return bhvFrame;}
    public NetpCanvas getCanvas(){ return null;}

    ASPProject getProjectData(){
    	return (ASPProject)getData();
    }
    
	public void doubleClick(){
//		openBehaviorTreeFrame();
	}
	public DataSet getDataSet(){
		return getProjectData().getDataSet();
	}
	public TestSet getTestSet(){
		return getProjectData().getTestSet();
	}
	public TestSetNode getTestSetNode(){
		int i,size;
		size=getChildCount();
		TreeNode tn;
		for(i=0;i<size;++i){
			tn=getChildAt(i);
			if(tn instanceof TestSetNode)
				return (TestSetNode)tn;
		}
		return null;
		
	}
	
	public DataSetNode getDataSetNode(){
		int i,size;
		size=getChildCount();
		TreeNode tn;
		for(i=0;i<size;++i){
			tn=getChildAt(i);
			if(tn instanceof DataSetNode)
				return (DataSetNode)tn;
		}
		return null;
	}
	
	int createNewTextData(CreateNewDataDialog dlg, boolean batchMode){
		DataSet ds=getDataSet();

		int ser;
		ser=ds.createNewTestData();
		TestData dt;
		dt=ds.getTestData(ser);
		dt.setDataInfo(dlg);
		
		
		TestDataNode nd=new TestDataNode(dt);
//		nd.setBehaviorTree(bt);
		DataSetNode dsn=getDataSetNode();
		dsn.addChildNode(nd);
		save();
		return getProjectData().createTestData(ser,batchMode);
		
	}
	
	void createNewTextData(){
		CreateNewDataDialogTab dlg=new CreateNewDataDialogTab();
		CreateNewDataDialog twoLitDlg;
		CreateNewThreeLiteralDataDialog threeLitDlg;
		if(!dlg.createAndShowGUI()){
			return;
		}
		if(dlg.getSelectedTabNumber() == 0) {
			twoLitDlg = dlg.getTwoLiteralDialog();
			if(twoLitDlg.isBatchMode()){
				int iSetNum=twoLitDlg.getBatchSetNum();
				int iTotalTm=0;
				for(int i=0;i<iSetNum;++i){
					twoLitDlg.setBatchIdx(i);
					iTotalTm+=createNewTextData(twoLitDlg, true);
				}	
				ASP.showMessageBox("There are "+iSetNum+" sets of data generated in "+iTotalTm+" milliseconds");
			}
			else 
				createNewTextData(twoLitDlg,false);
		}
		else if(dlg.getSelectedTabNumber() == 1) {
			threeLitDlg = dlg.getThreeLiteralDialog();
			System.out.println(threeLitDlg.getAtomNum());
			System.out.println(threeLitDlg.getBatchSetNum());
			System.out.println(threeLitDlg.getDensityRate());
			System.out.println(threeLitDlg.getProgramNum());
			System.out.println(threeLitDlg.getRuleMode());
			System.out.println(threeLitDlg.getRulesNum());
		}
		
	}
	public void restoreProject() {
		ParseNode pn,pndata;
		pn=getProjectData().getDataSetParseNode();
		int i, testnum;
		testnum=Integer.parseInt(pn.getAttribute("DataNum"));
		DataSet ds=getDataSet();
		TestData dt;
		
		Vector<?> vnodes;
		vnodes=pn.findChild("TestData");
		testnum=vnodes.size();
		
		for(i=0;i<testnum;++i){
			pndata=(ParseNode)vnodes.get(i);
			ds.createNewTestData();
			dt=ds.getTestData(i);

			dt.parseTree(pndata);

			TestDataNode nd=new TestDataNode(dt);
//			nd.setBehaviorTree(bt);
			DataSetNode dsn=getDataSetNode();
			dsn.addChildNode(nd);
		}
		TestSet ts=getTestSet();
		TestResult tr;
		pn=getProjectData().getTestSetParseNode();
		
		vnodes=pn.findChild("TestResult");
		int resultnum;
		resultnum=vnodes.size();
		for(i=0;i<resultnum;++i){
			pndata=(ParseNode)vnodes.get(i);
			
			ts.createNewTestResult();
			tr=ts.getResultData(i);
			tr.parseTree(pndata);
			

			TestResultNode trn=new TestResultNode(tr);
			TestSetNode tsn=getTestSetNode();
			tsn.addChildNode(trn);
		}
	}
}
