package data;

import java.io.PrintStream;

import ASPFrame.CreateNewDataDialog;
import AnswerSet.CalculateM;
import netp.xml.BuildNode;
import netp.xml.BuildTree;
import netp.xml.ParseNode;

public class TestData extends AbsData {

    int m_n,m_k,m_l,m_p;
    boolean b_NoRepeatLiteral;
    boolean b_IndependentRepeatLiteral;
    int m_IndependentRepeatLiteralNum;
    boolean b_usePowerLaw;
    String m_class;
    
    
	int m_ser;
    
	static public String[]getDataClassList(){
		String ret[]={"Random","mR-","mR+"};
		return ret;
	}
	
	public TestData() {
    	super();
    }
    public String toString() {
    	return "n: "+m_n+", k: "+m_k+" l: "+m_l+" p: "+m_p;
    }
	public String getName() {
		
		return "TestData:" +m_ser;
	}
    public String getToolTips(){
    	return "Data Set";
    }
    void buildTree(BuildTree bt){
        BuildNode bn;
        
        bn=new BuildNode("TestData",""+m_ser);
        bt.appendChild(bn);
        bt.downTree();
        bn=new BuildNode("n",""+m_n);
        bt.appendChild(bn);
        bn=new BuildNode("k",""+m_k);
        bt.appendChild(bn);
        bn=new BuildNode("l",""+m_l);
        bt.appendChild(bn);
        bn=new BuildNode("p",""+m_p);
        bt.appendChild(bn);
        bn=new BuildNode("NoRepeatLiteral",""+b_NoRepeatLiteral);
        bt.appendChild(bn);
        bn=new BuildNode("IndependentRepeatLiteral",""+b_IndependentRepeatLiteral);
        bt.appendChild(bn);

        bn=new BuildNode("IndependentRepeatLiteralNum",""+m_IndependentRepeatLiteralNum);
        bt.appendChild(bn);
        
        
        bn=new BuildNode("UsePowerLaw",""+b_usePowerLaw);
        bt.appendChild(bn);
        
        bn=new BuildNode("class",m_class);
        bt.appendChild(bn);

        
        bt.upTree();
    }
    
    public int getAtomNum() {
    	return m_n;
    }
    public int getRulesNum(){
    	return m_l;
    }
    public int getLiteralNum(){
    	return m_k;
    }
    public int getProgramNum(){
    	return m_p;
    }
    public boolean isNoRepeatLiteral(){
    	return b_NoRepeatLiteral;
    }
    public boolean isIndependentRepeatLiteral(){
    	return b_IndependentRepeatLiteral;
    }
    public int getIndependentRepeatLiteral(){
    	return m_IndependentRepeatLiteralNum;
    }

    
    public boolean usePowerLaw(){
    	return b_usePowerLaw;
    }
    public int getSer(){
    	return m_ser;
    }
    
    public String getDataClass(){
    	return m_class;
    }
    
    public void setUsePowerLaw(boolean b){
    	b_usePowerLaw=b;
    }

    public void setAtomNum(int n) {
    	m_n=n;
    }
    public void setRulesNum(int l){
    	m_l=l;
    }
    public void setLiteralNum(int k){
    	m_k=k;
    }
    public void setProgramNum(int p){
    	m_p=p;
    }
    public void setNoRepeatLiteral(boolean b){
    	b_NoRepeatLiteral=b;
    }
    public void setIndependentRepeatLiteral(boolean b){
    	b_IndependentRepeatLiteral=b;
    }
    public void setIndependentRepeatLiteralNum(int num){
    	m_IndependentRepeatLiteralNum=num;
    }
    
    

    public void setClass(String sClass){
    	m_class=sClass;
    }
    public void setSer(int s){
    	m_ser=s;
    }
    public void parseTree(ParseNode pn){

		setAtomNum(Integer.parseInt(pn.findFirstChild("n").getValue()));
		setRulesNum(Integer.parseInt(pn.findFirstChild("l").getValue()));
		setLiteralNum(Integer.parseInt(pn.findFirstChild("k").getValue()));
		setProgramNum(Integer.parseInt(pn.findFirstChild("p").getValue()));
		if(pn.findFirstChild("NoRepeatLiteral").getValue().equals("true"))
			setNoRepeatLiteral(true);
		else 
			setNoRepeatLiteral(false);

		if(pn.findFirstChild("IndependentRepeatLiteral")!=null){
			if(pn.findFirstChild("IndependentRepeatLiteral").getValue().equals("true"))
				setIndependentRepeatLiteral(true);
			else 
				setIndependentRepeatLiteral(false);
			
			setIndependentRepeatLiteralNum(Integer.parseInt(pn.findFirstChild("IndependentRepeatLiteralNum").getValue()));
			
		}
		else
			setIndependentRepeatLiteral(false);
		
		
		

		if(pn.findFirstChild("UsePowerLaw")!=null){
			if(pn.findFirstChild("UsePowerLaw").getValue().equals("true"))
				setUsePowerLaw(true);
			else 
				setUsePowerLaw(false);
		}
		else
			setUsePowerLaw(false);
		
		setClass(pn.findFirstChild("class").getValue());
    	
    }
    
	public String getDesc() {
    	return "class: "+m_class+"\nNo repeat literal:"+b_NoRepeatLiteral+
    		"\nn: "+m_n+", k: "+m_k+" l: "+m_l+" p: "+m_p+
    		"\nPowerlaw distribution:"+b_usePowerLaw+
    		"\nIdpRepLit:"+b_IndependentRepeatLiteral+ " IdpRepNum: "+m_IndependentRepeatLiteralNum
    		
    		;
	}

	public void setDataInfo(CreateNewDataDialog dlg) {
		setAtomNum(dlg.getAtomNum());
		setLiteralNum(dlg.getLiteralNum());
		setRulesNum(dlg.getRulesNum());
		setProgramNum(dlg.getProgramNum());
		setNoRepeatLiteral(dlg.isNoRepeatLiteral());
		setClass(dlg.getDataClass());
		setUsePowerLaw(dlg.usePowerLaw());
		setIndependentRepeatLiteral(dlg.isRepeatLiteralIndep());
		setIndependentRepeatLiteralNum(dlg.getRepeatLiteralRuleNum());
	}

    public double getRuleDensity(){
    	return m_l*1.0/m_n;
    }

	public void export(PrintStream pr) {
		// TODO Auto-generated method stub
		double density, m0,m1;
		CalculateM cm=new CalculateM(m_n,m_l);

		m0=cm.calculateM0();
		m1=cm.calculateM1();
		
		
		density=100.00*m_l/m_n/(m_n-1);
		
		pr.print(""+m_ser+","+m_n+","+m_k+","+m_l+","+m_p+","+b_NoRepeatLiteral+","+b_usePowerLaw+","+m_class+","+density+","+m0+","+m1+",");
		
		
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
