package data;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;

import AnswerSet.ASP;
import AnswerSet.ASPGenerator;
import AnswerSet.Config;

import netp.NetpFileLineReader;
import netp.xml.BuildNode;
import netp.xml.BuildTree;
import netp.xml.ParseNode;
import netp.xml.ParseTree;

public class ASPProject extends AbsData{

	String m_sysName;
	String m_filePath;
	DataSet m_dataset;
	TestSet m_testset;
	
	ParseTree pt;
	ParseNode pn;
	
	
//	BerpBehaviorTree m_bt;

//	BerpTreeEditCanvas m_cvs;
	
    public ASPProject() {
    	super();
    	m_sysName="";
    	m_filePath="";
    	m_dataset=new DataSet();
    	addChild(m_dataset);
    	m_dataset.setParent(this);
    	m_testset=new TestSet();
    	addChild(m_testset);
    	m_testset.setParent(this);
//    	m_cvs=null;
    }
    public void setFilePath(String p){
    	m_filePath=p;
    }
    public void setProjectName(String n){
    	m_sysName=n;
    }
    public String toString() {
    	return "";
    }
    public String getDesc() {
    	return "File Name: "+getFileName();
    }
    String getFileName(){
    	return m_filePath+m_sysName+Config.getConfig("projectExt");
    }
    String getExportFileName(){
    	return m_filePath+m_sysName+".txt";
    }

    String getTestDataDir(int ser){
    	return m_filePath+m_sysName+"/data_"+ser+"/";
    	
    }
    String getTestResDir(int ser){ // result directory
    	return m_filePath+m_sysName+"/res_"+ser+"/";
    }
    
    public TestData getTestData(int ser){
    	return m_dataset.getTestData(ser);
    }
    public int getTestDataNum(){
    	return m_dataset.getTestDataNum();
    }
    public TestResult getTestResult(int ser){
    	return m_testset.getResultData(ser);
    }
    public int getTestResultNum(){
    	return m_testset.getTestResultNum();
    }
    
    public int getTestDataProgramNum(int ser){
    	return m_dataset.getTestData(ser).getProgramNum();
    }
    
    public String getTestDataProgramFile(int dataser,int programser){
    	return getTestDataDir(dataser)+"data_"+programser+".txt";
    }

    public String getTestResultFile(int tser,int programser){
    	return getTestResDir(tser)+"reult_"+programser+".txt";
    }
   
    public BasicResultStatic performTest(int dser,int tser, boolean bBatchMode,String strSolver,boolean allModel){// data ser and test(result) ser
    	String path;
    	path=getTestResDir(tser);
		new File(path).mkdirs();

		int programnum=getTestDataProgramNum(dser);
		int i;

		String proFile;
		String resFile;
		String strCommand;
		
		
		strCommand=Config.getConfig("dlvLoc");
		String cmd[] = {"cmd","/C",strCommand,"-silent","-stats","","","","", ">","","","2>&1"};
	

		if(strSolver.equals("Smodels")){
			strCommand=Config.getConfig("lparseLoc");
			cmd[2]=strCommand;
			cmd[3]="";
			cmd[4]="";
			cmd[5]="";
			cmd[6]="|";
			strCommand=Config.getConfig("smodelsLoc");
			cmd[7]=strCommand;
		}

		if(strSolver.equals("Clasp")){
			strCommand=Config.getConfig("lparseLoc");
			cmd[2]=strCommand;
			cmd[3]="";
			cmd[4]="";
			cmd[5]="";
			cmd[6]="|";
			strCommand=Config.getConfig("claspLoc");
			
			
			cmd[7]=strCommand;

			if(allModel) cmd[8]= " 0 "; 
		
		}
		
		
		String cmda[] = {"cmd","/C","echo","info",">>","file"};
		Runtime r=Runtime.getRuntime();
		Process p;
		
		long tmStarta,tmEnda,tmUsagea;
		System.currentTimeMillis();
		for(i=0;i<programnum;++i){
			proFile=getTestDataProgramFile(dser,i);
			resFile=getTestResultFile(tser,i);
			cmd[5]=proFile;
			cmd[10]=resFile;
			try {
				tmStarta=System.currentTimeMillis();
				p=r.exec(cmd);
			    p.waitFor();
				tmEnda=System.currentTimeMillis();
			    tmUsagea=tmEnda-tmStarta;
			    cmda[3]="UsedTime: "+tmUsagea;
			    cmda[5]=resFile;
				p=r.exec(cmda);
			    p.waitFor();
			    System.out.println("finished "+i+" out of "+programnum);
			    
			} catch (Exception e) {
			      e.printStackTrace();
			}
		}
		
		System.currentTimeMillis();
		// perform the statistic analysis
		BasicResultStatic brs=new BasicResultStatic();
		brs.setSolver(strSolver);
		for(i=0;i<programnum;++i){
			resFile=getTestResultFile(tser,i);
			brs.parseFile(resFile);
		}
		brs.parseFinish();
		
		if(!bBatchMode)
			ASP.showMessageBox(brs.getStringResult());
		return brs;
		
    }
    public int createTestData(int ser, boolean bMode){
    	String path;
    	path=getTestDataDir(ser);
		new File(path).mkdirs();
		TestData td=getTestData(ser);
		int programnum=getTestDataProgramNum(ser);
		int i;
		String strPro;
		ASPGenerator gen;
		gen=new ASPGenerator();
		String proFile;
		FileOutputStream f;
		PrintStream pr;

		
		long tmStart,tmEnd,tmUsage;
		tmStart=System.currentTimeMillis();
		for(i=0;i<programnum;++i){
			strPro=gen.RandomGenerator(td);
			proFile=getTestDataProgramFile(ser,i);
			try {
				f = new FileOutputStream(proFile);

				pr=new PrintStream(f);
				pr.print(strPro);
				pr.flush();
				pr.close();
				f.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		tmEnd=System.currentTimeMillis();
		tmUsage=tmEnd-tmStart;
		if(!bMode)
			ASP.showMessageBox("There are "+programnum+" programs generated in "+tmUsage+" milliseconds");
		return (int)tmUsage;
		
    }
	public String getName() {
		
		return m_sysName;
	}
    public String getToolTips(){
    	return "Project of "+getName();
    }
    private void parseASSDInfo(int test_id){
    	int i, dataser, p_n, p_pronum;
    	
    	TestResult tr;
    	TestData td;
    	AnswerSetSizeDist p_assd;

    	tr=m_testset.getResultData(test_id);
   		dataser= tr.getTestDataNum();
   		td=m_dataset.getTestData(dataser);
   		p_n=td.getAtomNum();
   		p_assd=tr.getASSD();
   		p_assd.create(p_n);

   		p_pronum=td.getProgramNum();
   		String testResultFile;
   		
   		for(i=0;i<p_pronum;++i){
   			testResultFile=this.getTestResultFile(test_id, i);
   			p_assd.parseFile(testResultFile);
   		}
    }
    void checkASSDBeforeSave(){
    	int testNum, i;
    	testNum=m_testset.getTestResultNum();
    	
    	TestResult tr;
    	for(i=0;i<testNum;++i){
    		tr=m_testset.getResultData(i);
    		if(tr.isASSDCerated())continue;
    		
    		parseASSDInfo(i);
    	}
    }

    void exportHead(PrintStream pr){
    	pr.println("data ser,n,k,l,p,norepeat,powerlaw,class,bdensity, m0,m1,test ser, timeusage, answersets, avgtime,avgsize, s1,n1,s2,n2,s3,n3,s4,n4,s5,n5,s6,n6,s7,n7,s8,n8");
		pr.flush();
    }
    public void tryToExport(){
    	String filename=getExportFileName();

		FileOutputStream f;
		try {
			f = new FileOutputStream(filename);

			PrintStream pr=new PrintStream(f);
			exportHead(pr);
			
			int num=m_testset.getTestResultNum();
			TestResult tr;
			TestData td;
			int dataser;
			for(int i=0;i<num;++i){
				tr=m_testset.getResultData(i);
				dataser=tr.getTestDataNum();
				td=m_dataset.getTestData(dataser);
				td.export(pr);
				tr.export(pr);
				pr.println();
			}
			pr.flush();
			pr.close();
			
			
			f.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		clearChanged();
    	
    }
    public void tryToSave(){
//    	if(m_cvs==null) return;
    	
    	checkASSDBeforeSave();
    	String filename = getFileName();

		FileOutputStream f;
		try {
			f = new FileOutputStream(filename);

			PrintStream pr=new PrintStream(f);

			BuildTree bt=new BuildTree("ASPProject");
            bt.getRoot().setAttribute("Version","1.00");

            BuildNode bn;
            
            bn=new BuildNode("Sysname",m_sysName);
            bt.appendChild(bn);
            m_dataset.buildTree(bt);
            m_testset.buildTree(bt);
            
            
			pr.print(bt.toString());
			pr.flush();
			pr.close();
			
			
			f.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		clearChanged();
//		m_bt.clearChanged();
    }
    public boolean loadFile(){
    	pt=new ParseTree();
   // 	ParseNode pn;
    	
    	String filename=getFileName();
    	NetpFileLineReader fr=new NetpFileLineReader(filename);
		if(fr.open()) { // file exist
    		StringBuffer bf=new StringBuffer();
    		String s;
    		while((s=fr.readNextLine())!=null){
    			bf.append(s);
    		}
    		fr.close();

    		pt.parse(bf.toString());

   // 		pn=pt.getRoot();
    		return true;
		}
		return false;
    
    }
    
    public ParseNode getDataSetParseNode(){

    	ParseNode pn;
    	pn=pt.getRoot();
    	int i,len;
		len=pn.getChildNum();
    	for(i=0;i<len;++i){
    		if(pn.getChild(i).getTag().equals("DataSet"))
    			return pn.getChild(i);
    	}
		return null;
    }
    
    public ParseNode getTestSetParseNode(){

    	ParseNode pn;
    	pn=pt.getRoot();
    	int i,len;
		len=pn.getChildNum();
    	for(i=0;i<len;++i){
    		if(pn.getChild(i).getTag().equals("TestSet"))
    			return pn.getChild(i);
    	}
		return null;
    }

    
    
    public DataSet getDataSet(){
    	return m_dataset;
    }
    public TestSet getTestSet(){
    	return m_testset;
    }
//    public void setCanvas(BerpTreeEditCanvas c){
//    	m_cvs=c;
//    }
//    public BerpTreeEditCanvas getCanvas(){
//    	return m_cvs;
//    }

}