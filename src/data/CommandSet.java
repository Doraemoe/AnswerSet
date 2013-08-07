package data;

public class CommandSet {
	String cmd[];
	String cmda[];
	int i;
	final int programnum;
	final int dser;
	final int tser;
	final String m_sysName;
	final String m_filePath;
	
	public CommandSet(String cmd[], String cmda[], String sn, String fp, int ds, int ts, int pn) {
		this.cmd = cmd;
		this.cmda = cmda;
		m_sysName = sn;
		m_filePath = fp;
		dser = ds;
		tser = ts;
		programnum = pn;
		i = 0;
	}
	
	public synchronized int geti() {
		increasei();
		return i - 1;
	}
	
	public synchronized void increasei() {
		++i;
	}
}
