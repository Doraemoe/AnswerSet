package data;

import java.io.PrintStream;

import netp.NetpFileLineReader;
import netp.xml.BuildNode;
import netp.xml.BuildTree;
import netp.xml.ParseNode;

public class BasicResultStatic {
	int p_totalProgram;
	int p_totalAnswers;
	int p_EmptyAns;
	int p_avgTime;
	int p_minTime;
	int p_maxTime;
	int p_totalTime;
	int p_errorNum;
	int p_firstError;
	String strSolver;
	public void setSolver(String s){
		strSolver=s;
	}
	public BasicResultStatic(){
		p_totalProgram=0;
		p_totalAnswers=0;
		p_avgTime=0;
		p_minTime=0;
		p_maxTime=0;
		p_totalTime=0;
		p_errorNum=0;
		p_firstError=0;
		p_EmptyAns=0;
	}
	void parseResult(String strResult){
		String strAnsNum="",strTimeUsage;
		boolean bHasEmpty=false,bHasAnswer=false;
		
		
		if(strSolver.equals("DLV")){
			if(strResult.indexOf("{}")>=0)
				bHasEmpty=true;
			else {
				strAnsNum=netp.NetpGeneral.findStringBetween(strResult,"Answer sets printed       :","Time");
				strAnsNum=strAnsNum.trim();
			}
			if(strResult.indexOf("{")>=0)
				bHasAnswer=true;
		}
		if(strSolver.equals("Smodels")){
			if(strResult.indexOf("True")>=0)
				bHasAnswer=true;
		}
		if((strSolver.equals("Clasp"))||(strSolver.equals("Clingo"))){
			bHasAnswer=true;
			if(strResult.indexOf("UNSATISFIABLE")>=0)
				bHasAnswer=false;
		}
		
		strTimeUsage=netp.NetpGeneral.findStringBetween(strResult,"\"UsedTime:","\"").trim();
		if(strTimeUsage.equals("")){
			p_errorNum++;
			if(p_errorNum==1) p_firstError=p_totalProgram; // first error
			return;
		}
		p_totalProgram++;// valid program
		if(bHasAnswer){
			p_totalAnswers++;
		}
		if(bHasEmpty) p_EmptyAns++;
		int timeUsage;
		timeUsage=Integer.parseInt(strTimeUsage);
		p_totalTime+=timeUsage;
		if(timeUsage>p_maxTime) p_maxTime=timeUsage;
		if(p_totalProgram==1)p_minTime=timeUsage;
		if(timeUsage<p_minTime) p_minTime=timeUsage;
	}
	
	
	
	void parseFinish(){
		if(p_totalProgram>0)
			p_avgTime=p_totalTime/p_totalProgram;
	}
	void parseFile(String strFile){
    	NetpFileLineReader fr=new NetpFileLineReader(strFile);
		if(fr.open()) { // file exist
    		StringBuffer bf=new StringBuffer();
    		String s;
    		while((s=fr.readNextLine())!=null){
    			bf.append(s);
    		}
    		fr.close();
    		parseResult(bf.toString());
		}
	}
	public String getStringResult(){
		String sRet= "Total Program:"+ p_totalProgram +
				"\n With Answerset:"+ p_totalAnswers +
				"\n Empty Answerset:"+ p_EmptyAns +
				"\n Average Time:"+ p_avgTime +
				"\n Min Time:"+ p_minTime +
				"\n Max Time:"+ p_maxTime;
		if(p_errorNum>0)
			sRet+="\nError Num: "+p_errorNum+"\nFirst Error:" +p_firstError;
		return sRet;
	}
	public int getTotalTime(){
		return p_totalTime;
	}
	
    void buildTree(BuildTree bt){
        BuildNode bn;
        
        bn=new BuildNode("BasicStatic","");
        bn.setAttribute("TotalProgram",""+p_totalProgram);
        bn.setAttribute("TotalAnswer",""+p_totalAnswers);
        bn.setAttribute("EmptyAnswers",""+p_EmptyAns);
        bn.setAttribute("AverageTime",""+p_avgTime);
        bn.setAttribute("Mintime",""+p_minTime);
        bn.setAttribute("MaxTime",""+p_maxTime);
        bn.setAttribute("TotalTime",""+p_totalTime);
        bn.setAttribute("ErrorNum",""+p_errorNum);
        bn.setAttribute("FirstError",""+p_firstError);
        
        bt.appendChild(bn);
        
    }
    public void parseTree(ParseNode pn){
    	ParseNode bsnd;
    	bsnd=pn.findFirstChild("BasicStatic");
    	if(bsnd==null)return; 
    	String str;
    	str=bsnd.getAttribute("TotalProgram");
    	
    	p_totalProgram=Integer.parseInt(str);
    	
    	str=bsnd.getAttribute("TotalAnswer");
    	p_totalAnswers=Integer.parseInt(str);
    	p_EmptyAns=Integer.parseInt(bsnd.getAttribute("EmptyAnswers"));
    	p_avgTime=Integer.parseInt(bsnd.getAttribute("AverageTime"));
    	p_minTime=Integer.parseInt(bsnd.getAttribute("Mintime"));
    	p_maxTime=Integer.parseInt(bsnd.getAttribute("MaxTime"));
    	p_totalTime=Integer.parseInt(bsnd.getAttribute("TotalTime"));
    	p_errorNum=Integer.parseInt(bsnd.getAttribute("ErrorNum"));
    	p_firstError=Integer.parseInt(bsnd.getAttribute("FirstError"));
    
    }
    double getSatRate(){
    	return (p_totalAnswers*100.0)/p_totalProgram;
    }
	public void export(PrintStream pr) {
		// TODO Auto-generated method stub
		pr.print(p_totalAnswers+","+p_avgTime+",");
		
	}
	
}
