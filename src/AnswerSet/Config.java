package AnswerSet;

import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Vector;

import netp.NetpFileLineReader;
import netp.xml.BuildNode;
import netp.xml.BuildTree;
import netp.xml.ParseNode;
import netp.xml.ParseTree;

public class Config
{
	static public final String confFile="./berp.conf";

	static public boolean bIsEduVersion=true; // set this to try to disable many advanced features.
	static public String defaultDocRoot="./workspace";
	static public final String projectExt=".asp";

	static Vector<ConfigData> data=new Vector<ConfigData>();
	
    static public void init() 
    {
    	registConfig("docRoot",ConfigData.TYPESTRING,defaultDocRoot);
    	registConfig("dlvLoc",ConfigData.TYPESTRING,defaultDocRoot);
    	registConfig("claspLoc",ConfigData.TYPESTRING,defaultDocRoot);
    	registConfig("smodelsLoc",ConfigData.TYPESTRING,defaultDocRoot);
    	registConfig("lparseLoc",ConfigData.TYPESTRING,defaultDocRoot);
    	
    	registConfig("Version",ConfigData.TYPESTRING,"1.00");
    	registConfig("defNodeWidth",ConfigData.TYPEINTEGER, "150");
    	registConfig("defNodeHeight",ConfigData.TYPEINTEGER, "45");
    	registConfig("projectExt",ConfigData.TYPEINTEGER, projectExt);
    	readConfig();
    }
    
    static void registConfig(String name,int type,String defval){
    	ConfigData dt=new ConfigData(name,type,defval);
    	data.add(dt);
    }
    
    static public int getIntConfig(String s){
    	String sRet=getConfig(s);
    	int iret=Integer.parseInt(sRet);
    	return iret;
    }
    
    static public String getConfig(String s){
    	int i,len;
    	len=data.size();
    	ConfigData d;
    	for(i=0;i<len;++i){
    		d=(ConfigData)data.get(i);
    		if(s.equals(d.getName())){
    			return d.getValue();
    		}
    	}
    	return null;
    }
    
    static public void SetConfig(String s,String v){
    	int i,len;
    	len=data.size();
    	ConfigData d;
    	for(i=0;i<len;++i){
    		d=(ConfigData)data.get(i);
    		if(s.equals(d.getName())){
    			d.setValue(v);
    		}
    	}
    	saveConfig();
    }
    
    static private void readConfig(){
    	
    	NetpFileLineReader fr=new NetpFileLineReader(confFile);
    	if(fr.open()) { // file exist
    		StringBuffer bf=new StringBuffer();
    		String s;
    		while((s=fr.readNextLine())!=null){
    			bf.append(s);
    		}
    		fr.close();
    		ParseTree pt=new ParseTree();
    		pt.parse(bf.toString());
    		ParseNode pn=pt.getRoot();
    		
    		int i,len;
        	String name,value;
        	len=data.size();
        	ConfigData d;
        	for(i=0;i<len;++i){
        		d=(ConfigData)data.get(i);
        		name=d.getName();
        		value=pn.getChildValue(name);
        		d.setValue(value);	
        	}
    		
    	}
    	else { // create the new file
    		saveConfig();
    	}
    	
    }
    static private void saveConfig(){
		FileOutputStream f;
		try {
			f = new FileOutputStream(confFile);

			PrintStream pr=new PrintStream(f);

			BuildTree bt=new BuildTree("ASPPCONFIG");
            bt.getRoot().setAttribute("Version","1.00");

            BuildNode bn;
            
    		int i,len;
        	String name,value;
        	len=data.size();
        	ConfigData d;
        	for(i=0;i<len;++i){
        		d=(ConfigData)data.get(i);
        		name=d.getName();
        		value=d.getValue();
                bn=new BuildNode(name,value);
                bt.appendChild(bn);
        	}
  //          fdFrame.saveDocument(bt);
			pr.print(bt.toString());
			pr.flush();
			pr.close();
			
			
			f.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}

class ConfigData
{
	static public final int TYPESTRING=1;
	static public final int TYPEINTEGER=2;
	static public final int TYPEPOINT=3;
	static public final int TYPEINTEGERARRAY=4;

	String m_Name;
	int m_Type; 
	String m_Defvalue;  // default value in string format
	String m_Value; // Current value in string format

	public ConfigData(String name,int type,String defvalue,String val){
		m_Name=name;
		m_Type=type;
		m_Defvalue=defvalue;
		m_Value=val;
	}
	public String getName(){
		return m_Name;
	}
	public ConfigData(String name,int type,String defvalue){
		this(name,type,defvalue,null);
	}
	
	public ConfigData(String name,int type){
		this(name,type,"",null);
	}
	public ConfigData(String name){
		this(name,TYPESTRING,"",null);
	}
	public String getValue(){
		if(m_Value==null) return m_Defvalue;
		if(m_Value.equals("")) return m_Defvalue;
		return m_Value;
	}
	public void setValue(String v){
		m_Value=v;
	}

	//  NEED MORE TO DEAL WITH INT VALUE AND OTHER TYPE OF VALUES
	
}