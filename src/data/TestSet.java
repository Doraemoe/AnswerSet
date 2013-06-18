package data;

import netp.xml.BuildNode;
import netp.xml.BuildTree;
import netp.xml.ParseNode;

public class TestSet extends AbsData{

//	BerpBehaviorTree m_bt;

//	BerpTreeEditCanvas m_cvs;
	
    public TestSet() {
    	super();
    }
    public String toString() {
    	return "";
    }
	public String getName() {
		
		return "Test Set";
	}
    public String getToolTips(){
    	return "Data Results";
    }
    
    void buildTree(BuildTree bt){
        BuildNode bn;
        int i,size;
        TestResult chd;

        size=getTestResultNum();
        bn=new BuildNode("TestSet","");
        bn.setAttribute("ResultNum", ""+size);
        bt.appendChild(bn);
        bt.downTree();
        for(i=0;i<size;++i){
        	chd=getResultData(i);
        	chd.buildTree(bt);
        }
        bt.upTree();
    }

    void parseTree(ParseNode pn){
    	
    }
	public String getDesc() {
		return "";
	}
	public int getTestResultNum(){
		return getChildrenNum();
	}
	
	public TestResult getResultData(int iIdx){
		return (TestResult)this.getChildAt(iIdx);
	}
	
	public int createNewTestResult(){
		int size,ser;
		size=getTestResultNum();
		TestResult dt;
		ser=size;
		dt=new TestResult();
		dt.setParent(this);
		addChild(dt);
		dt.setSer(ser);
		
		return ser;
		
	}
/*    
    public ParseNode loadFile(){
    		pn=pt.getRoot();

    		int i,len;
    		len=pn.getChildNum();
        	for(i=0;i<len;++i){
        		if(pn.getChild(i).getTag().equals("CanvasObject"))
        			return pn.getChild(i);
        	}
    		
    	}
		return null;
    }
*/
}