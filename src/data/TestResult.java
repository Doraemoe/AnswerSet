package data;

import java.io.PrintStream;

import netp.xml.BuildNode;
import netp.xml.BuildTree;
import netp.xml.ParseNode;

public class TestResult extends AbsData {

    int m_data_ser;
    long m_timeusage;
	int m_ser;
    
	AnswerSetSizeDist m_assd;
	
	BasicResultStatic m_brs;
	String strSolver;
	
	public boolean isASSDCerated(){
		return m_assd.isCreated();
	}
	public AnswerSetSizeDist getASSD(){
		return m_assd;
	}
    public TestResult() {
    	super();
    	m_brs=new BasicResultStatic();
    	m_assd=new AnswerSetSizeDist();
    }
    public String toString() {
    	return "Date Set: "+m_data_ser+", Time Usage: "+m_timeusage;
    }
	public String getName() {
		
		return "TestResult:" +m_ser+" Data("+m_data_ser+")";
	}
    public String getToolTips(){
    	return "Data Set";
    }
    
    void buildTree(BuildTree bt){
        BuildNode bn;
        
        bn=new BuildNode("TestResult",""+m_ser);
        bt.appendChild(bn);
        bt.downTree();
        bn=new BuildNode("TestData",""+m_data_ser);
        bt.appendChild(bn);
        bn=new BuildNode("TimeUsage",""+m_timeusage);
        bt.appendChild(bn); 
        bn=new BuildNode("Solver",strSolver);
        bt.appendChild(bn); 
        m_brs.buildTree(bt);
        m_assd.buildTree(bt);
        
        bt.upTree();
    }
    
    
    public int getTestDataNum(){
    	return m_data_ser;
    }
    public void setTestDataNum(int s) {
    	m_data_ser=s;
    }
    
    public int getSer(){
    	return m_ser;
    }
    public void setSer(int s){
    	m_ser=s;
    }
    public long getTimeUsage(){
    	return m_timeusage;
    }
    public void setTimeUsage(long t){
    	m_timeusage=t;
    }
    public void setSolver(String s){
    	strSolver=s;
    }
    public String getSolver(){
    	return strSolver;
    }
    
    public void parseTree(ParseNode pn){
    	
    	ParseNode pd;
    	pd=pn.findFirstChild("Solver");
    	if(pd==null) setSolver("DLV");
    	else
    		setSolver(pd.getValue());
    	m_data_ser=Integer.parseInt(pn.findFirstChild("TestData").getValue());
    	m_timeusage=Integer.parseInt(pn.findFirstChild("TimeUsage").getValue());
    	m_brs.parseTree(pn);
    	m_assd.parseTree(pn);
    }
	public String getDesc() {
    	return "Date Set: "+m_data_ser+", Time Usage: "+m_timeusage+ "Solver: "+strSolver+"\n"+m_brs.getStringResult();
	}

	
    public void setBasicResultStatic(BasicResultStatic bsr){
    	m_brs=bsr;
    }
    public double getSatRate(){
    	return m_brs.getSatRate();
    }
    public double getTestTime(){
    	if(m_brs.p_totalProgram==0) return 0;
    	return m_brs.getTotalTime()/m_brs.p_totalProgram;
    }
	public void export(PrintStream pr) {

		pr.print(m_ser+","+m_timeusage+",");
		m_brs.export(pr);
		m_assd.export(pr);
		
		
	}
	
}
