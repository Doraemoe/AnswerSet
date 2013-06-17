package data;

import netp.xml.BuildNode;
import netp.xml.BuildTree;
import netp.xml.ParseNode;

public class DataSet extends AbsData{

//	BerpBehaviorTree m_bt;

//	BerpTreeEditCanvas m_cvs;
	
    public DataSet() {
    	super();
    }
    public String toString() {
    	return "";
    }
	public String getName() {
		
		return "DataSet";
	}
    public String getToolTips(){
    	return "Data Set";
    }
    
    void buildTree(BuildTree bt){
        BuildNode bn;
        int i,size;
        TestData chd;

        size=getTestDataNum();
        bn=new BuildNode("DataSet","");
        bn.setAttribute("DataNum", ""+size);
        bt.appendChild(bn);
        bt.downTree();
        for(i=0;i<size;++i){
        	chd=getTestData(i);
        	chd.buildTree(bt);
        }
        bt.upTree();
    }

    void parseTree(ParseNode pn){
    	
    }
	public String getDesc() {
		return "";
	}
	public int getTestDataNum(){
		return getChildrenNum();
	}
	public TestData getTestData(int iIdx){
		return (TestData)this.getChildAt(iIdx);
	}
	public int createNewTestData(){
		int size,ser;
		size=getTestDataNum();
		TestData dt;
		ser=size;
		dt=new TestData();
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