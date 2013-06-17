package data;

import java.io.PrintStream;
import java.util.Vector;

import netp.NetpFileLineReader;
import netp.NetpGeneral;
import netp.xml.BuildNode;
import netp.xml.BuildTree;
import netp.xml.ParseNode;

public class AnswerSetSizeDist {
	int m_arDist[];
	int m_n;
	private boolean m_bCreated;
	public AnswerSetSizeDist(){
		m_bCreated=false;
	}
	public boolean isCreated(){
		return m_bCreated;
	}
	public void create(int n){
		m_arDist=new int[n+1];
		m_n=n;
		initDist();
		
	}
	private void initDist(){
		for(int i=0;i<=m_n;++i){
			m_arDist[i]=0;
		}
	}
	private void parseAnswerString(String strInp){
		if(strInp.length()==0) {
			m_arDist[0]++;
			return;
		}

		int iLen=strInp.length();
		char c;
		int idx=1;
		for(int i=0;i<iLen;++i){
			c=strInp.charAt(i);
			if(c==',') idx++;
		}
		m_arDist[idx]++;
	}
	private void parseClaspString(String strInp){
		String strAnsStr;
		String strModelNum;
		int modNum;
		if(strInp.indexOf("UNSATISFIABLE")>=0) return; // no answer set
		strAnsStr=NetpGeneral.findStringBetween(strInp,"Solving...", "SATISFIABLE");
		strModelNum=NetpGeneral.findStringBetween(strInp, "Models      :","Time");
		strModelNum=strModelNum.trim();
		modNum=Integer.parseInt(strModelNum);
		
		int i,rmodNum=0;

		int iLen=strAnsStr.length();
		char c;
		int idx=0;
		for(i=0;i<iLen;++i){
			c=strAnsStr.charAt(i);
			if(c=='_') {
				idx++;
			}
			if(c=='A'){// find new answer
				if(i>0){
					m_arDist[idx]++;
					rmodNum++;
				}
				idx=0;
			}
		}
		if(idx>0) {
			m_arDist[idx]++;
			rmodNum++;
		}
		if(rmodNum!=modNum){
			System.out.println("ERROR rmodNum= "+rmodNum+" modNum= "+modNum);
		}
		
		
		
	}
	private void parseString(String strInp){
		String strAnsSet;
		String strRest;
		if(strInp.substring(0,5).equals("clasp"))
		{
			parseClaspString(strInp);
			return;
		}
		
		if(strInp.indexOf("{")<0) return; 
		strAnsSet=NetpGeneral.findStringBetween(strInp, "{", "}");
		parseAnswerString(strAnsSet);
		strRest=NetpGeneral.findStringAfter(strInp, "}");
		parseString(strRest);
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
    		parseString(bf.toString());
		}
		m_bCreated=true;
	}
	
    void buildTree(BuildTree bt){
    	if(!m_bCreated)return;
    	BuildNode bn,bc;
        bn=new BuildNode("AnswerSetDist","");
        bn.setAttribute("n", ""+m_n);
        int i;
        for(i=0;i<=m_n;++i){
        	if(m_arDist[i]>0){
        		bc=new BuildNode("val");
        		bc.setAttribute("size", ""+i);
        		bc.setAttribute("num", ""+m_arDist[i]);
        		bn.addChild(bc);
        	}
        }
        bt.appendChild(bn);
    }

    
    public void parseTree(ParseNode pn){
    	ParseNode bsnd,cnd;
    	bsnd=pn.findFirstChild("AnswerSetDist");
    	m_bCreated=false;
    	if(bsnd==null)return; 
    	int p_n;
    	p_n=Integer.parseInt(bsnd.getAttribute("n"));
    	create(p_n);

    	Vector<ParseNode> vNodes;
    	vNodes= bsnd.findChild("val");
    	
    	int i,iLen;
    	iLen=vNodes.size();
    	int size,num;
    	for(i=0;i<iLen;++i){
    		cnd=vNodes.elementAt(i);
    		size=Integer.parseInt(cnd.getAttribute("size"));
    		num=Integer.parseInt(cnd.getAttribute("num"));
    		m_arDist[size]=num;
    	}
    	m_bCreated=true;
    }
	public void export(PrintStream pr) {
		int i,totalsize,totalnum;
		double avgsize;
		totalsize=0;
		totalnum=0;
		
		for(i=0;i<=m_n;++i){
        	if(m_arDist[i]>0){
        		totalsize+=m_arDist[i]*i;
        		totalnum+=m_arDist[i];
        	}
        }
		if(totalnum>0)avgsize=1.0*totalsize/totalnum;
		else avgsize=0;
		pr.print(avgsize+",");
		pr.println();
		pr.println("dis");
		pr.println();
	
		for(i=0;i<=m_n;++i){
        	if(m_arDist[i]>0){
        		pr.println(i+","+m_arDist[i]);

        	}
        }
	}
}
