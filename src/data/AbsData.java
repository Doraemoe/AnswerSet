package data;

import java.util.Vector;

abstract public class AbsData  {
	AbsData m_parent;
	Vector<AbsData> m_children;
	boolean m_changed;
	
	public void clear() {
    	
    }
	public void setParent(AbsData p){
		m_parent=p;
	}
	public void addChild(AbsData c){
		m_children.add(c);
	}
	public void clearChild(){
		m_children.removeAllElements();
	}
	public AbsData getParent(){
		return m_parent;
	}
	public int getChildrenNum(){
		return m_children.size();
	}
	public AbsData getChildAt(int idx){
		return (AbsData) m_children.get(idx);
	}
	
	public AbsData(){
		m_children=new Vector<AbsData>();
		m_parent=null;
		m_changed=false;
	}
	
	public void setChanged(){
		m_changed=true;
		if(m_parent!=null)
			m_parent.setChanged();
	}
	
	public void clearChanged(){
		m_changed=false;
	}
	
	public boolean isChanged(){
		return m_changed;
	}
	
    abstract public String getToolTips();
 	abstract public String getDesc() ;
    abstract public String getName();
    
    public void tryToSave(){
    	
    }
}
