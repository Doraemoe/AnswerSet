package ASPNode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import netp.canvas.NetpCanvas;
import netp.xml.BuildTree;
import netp.xml.ParseNode;
import ASPFrame.ASPFrame;
import ASPFrame.InterFrame;
import ASPFrame.OpenProjectDlg;
import AnswerSet.ASP;
import AnswerSet.Config;
import AnswerSet.ImageManager;
import data.ASPProject;
import data.DataSet;
import data.SystemRoot;
import data.TestSet;

public class ProjectRoot extends DocNode
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    static OpenProjectDlg opProject;

	JMenuItem rename;
//	BerpExtraSysSetsNode extraSysNode;
	public ProjectRoot(Object o) {
		super(o);
		initMenu();
    }
//	public BerpExtraSysSetsNode getExtraSysSetNode(){
//		return extraSysNode;
//	}
//	public void createExtraSysNode(){
//		extraSysNode=new BerpExtraSysSetsNode(null);
//		this.addChildNode(extraSysNode);
//	}
    public boolean isLeaf() {
		return false;
    }
    
    public int getNodeImage(){
    	return ImageManager.PROJECTROOT;
    }
    
	protected void initMenu()
	{
		super.initMenu();
		JMenuItem mnNewSystem;
		mnNewSystem=new JMenuItem("New project...");
		mn.add(mnNewSystem);
		mnNewSystem.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e){
						tryCreateNewProject();
					}
				}
		);
		
		JMenuItem mnOpenSystem;
		mnOpenSystem=new JMenuItem("Open a project...");
		mn.add(mnOpenSystem);
		mnOpenSystem.addActionListener(
                new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        tryOpenProject();
                    }
                }
            );
		
		
	}
	public void tryCreateNewProject(){
		String sysName = JOptionPane.showInputDialog("Please input the name of the new project"); 

		if(sysName.length()==0) return;

		Vector<String> v=getEnvironmentProjectList();
		int i,len;
		String osysname;
		len=v.size();
		for(i=0;i<len;++i){
			osysname=(String)v.get(i);
			if(sysName.equals(osysname)){
				JOptionPane.showMessageDialog(ASP.getTopFrame(), 
						"There is a project called "+sysName+" in the environment already. You may select to open it or create a project with a different name.", "Informaiton", JOptionPane.INFORMATION_MESSAGE);
				return;
			}
		}

    	String rootPath;
    	rootPath=Config.getConfig("docRoot");
    	String sysPath=rootPath+"/"+sysName;
		
		
		new File(sysPath).mkdirs();
		
		ProjectNode nd=addProject(sysName);
		ASPProject dt=nd.getProjectData();
		dt.tryToSave();
//		dt.clearChanged();
		
	}
	ProjectNode addProject(String sName){
		SystemRoot rt=getSystemRoot();
		ASPProject s=rt.addNewProject();
		s.setProjectName(sName);
		s.setFilePath(Config.getConfig("docRoot")+"/");
//		BerpBehaviorTree bt;
//		bt=s.getBehaviorTree();
		
		ProjectNode nd=new ProjectNode(s);
//		nd.setBehaviorTree(bt);
		addChildNode(nd);
		
		DataSet ds=s.getDataSet();
		DataSetNode dsn=new DataSetNode(ds);
		nd.addChildNode(dsn);

		TestSet ts=s.getTestSet();
		TestSetNode tsn=new TestSetNode(ts);
		nd.addChildNode(tsn);
		
		return nd;
		
	}
	public SystemRoot getSystemRoot() {
		return (SystemRoot) getData();
	}
	public boolean tryOpenProject(String sName){
		Vector<String> v=getOpenProjectList();
		if(v.contains(sName)){
			JOptionPane.showMessageDialog(ASP.getTopFrame(), 
					"Project "+sName + " is already opened.","Informaiton",JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		
		ProjectNode nd=addProject(sName);
		ASPProject dt=nd.getProjectData();
		dt.loadFile();
		nd.restoreProject();
		return true;
	}
	public void tryOpenProject(){
		if(opProject==null) {
			opProject=new OpenProjectDlg(ASP.getTopFrame());
			opProject.setParent(this);
		}
		opProject.setProjectList(getEnvironmentProjectList());
		opProject.setVisible(true);
		
	}

	public void saveDocument(BuildTree bt)
	{
/*
		
		BuildNode p_node = new BuildNode ("Components");
        bt.appendChild(p_node);
        bt.downTree();
		nComs.saveDocument(bt);
		bt.upTree();

		p_node = new BuildNode("Trees");
		bt.appendChild(p_node);
		bt.downTree();
		nBhvs.saveDocument(bt);
		bt.upTree();			

        bt.appendChild(new BuildNode("FullTrees"));
		bt.downTree();
		nFBhvs.saveDocument(bt);
		bt.upTree();			
*/
/*
        p_node = new BuildNode("Dependence");
        bt.appendChild(p_node);
        bt.downTree();
        bt.upTree();
*/
	}
	public void actionPerformed(ActionEvent e)
	{
		Object ob=e.getSource();
		if(ob==rename){
			Object ret = JOptionPane.
				showInputDialog(ASPFrame.getProjectFrame(),"Please input the project's name",
					"Project Name",JOptionPane.INFORMATION_MESSAGE);
			if(ret==null) return;
		}
	}


	public void parseNode(ParseNode nd)
	{

	}
    public void close(){
    }

    public InterFrame getFrame(){ return null;}
    public NetpCanvas getCanvas(){ return null;}
    
    
    public Vector<String> getEnvironmentProjectList(){
    	Vector<String> v=new Vector<String>();
    	String rootPath;
    	rootPath=Config.getConfig("docRoot");
    	File rtf=new File(rootPath);
        File fs[];
        fs=rtf.listFiles();
        int num=fs.length;
        File f;
        int i,len;
        String sDir;

        for(i=0;i<num;++i) {
          f=fs[i];
          if(f.isDirectory()) continue;
          sDir=f.getName();
          len=sDir.length();
          if(len<5) continue; // a file name is like xxx.ber
          
          if(sDir.substring(len-4,len).equals(Config.getConfig("projectExt"))){
        	  v.add(sDir.substring(0,len-4));
          }
        }
        return v;
    }
    public Vector<String> getOpenProjectList(){
    	Vector<String> v=new Vector<String>();
    	SystemRoot o=getSystemRoot();
    	int i,s=o.getChildrenNum();
    	String sName;
    	ASPProject bs;
    	for(i=0;i<s;++i){
    		bs=(ASPProject)o.getChildAt(i);
			sName=bs.getName();
			v.add(sName);
    	}
    	return v;
    }
    ASPProject getProjectByName(String sysName){
    	SystemRoot o=getSystemRoot();
    	int i,s=o.getChildrenNum();
    	String sName;
    	ASPProject bs;
    	for(i=0;i<s;++i){
    		bs=(ASPProject)o.getChildAt(i);
    		sName=bs.getName();
    		if(sName.equals(sysName))
    			return bs;
    	}
    	return null;
    	
    }
//	public BerpTreeEditCanvas getTreeEditCanvasBySysName(String sysName) {
//		BerpSystem bs=getSystemByName(sysName);
//		if(bs==null) return null;
//		return bs.getCanvas();
//	}
 //   public BerpExtraRunTreeNode getExtraSystemById(String sysId){
//    	return extraSysNode.getExtraSystemById(sysId);
//    }
}
