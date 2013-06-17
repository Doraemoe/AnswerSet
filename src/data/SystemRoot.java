package data;

public class SystemRoot extends AbsData{

    public void clear() {
    }
    
    
    public SystemRoot() {
    	super();
    }
    
    public String toString() {
    	return "";
//        return m_generalInfo.m_projName;
    }
    public String getDesc() {
        return "The root for all opened projects";
    }

    public String getToolTips(){
    	return "Root node for all projects. Right click to start";
    }

	public String getName() {
		
		return "ASP Projects";
	}

	public ASPProject addNewProject(){
		ASPProject s=new ASPProject();
		s.setParent(this);
		this.addChild(s);
		return s;
	}
	public void tryToSave(){
		
	}

}