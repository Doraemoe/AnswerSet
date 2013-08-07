package data;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.CountDownLatch;

public class ComputeResult implements Runnable {
	private final CountDownLatch downLatch;
	CommandSet command;
	String proFile;
	String resFile;
	int i;
	String cmd[];
	String cmda[];
	long tmStarta,tmEnda,tmUsagea;
	
	
	public ComputeResult(CountDownLatch downLatch, CommandSet cmds) {
		this.downLatch = downLatch;
		command = cmds;
		cmd = command.cmd;
		cmda = command.cmda;
	}
	
	public void run() {  
		i = command.geti();
		Runtime r=Runtime.getRuntime();
		Process p;
		while(i < command.programnum) {
			proFile = getTestDataProgramFile(command.dser,i);
			resFile=getTestResultFile(command.tser,i);
			
			File abstractFile = new File(resFile);
			if(abstractFile.exists()) {
				continue;
			}
			cmd[5]=proFile;
			cmd[10]=resFile;
			System.out.println("0000000000000");
			System.out.println(cmd[0]);
			System.out.println("0000000000000");
			System.out.println(cmd[1]);
			System.out.println("0000000000000");
			System.out.println(cmd[2]);
			System.out.println("0000000000000");
			System.out.println(cmd[3]);
			System.out.println("0000000000000");
			System.out.println(cmd[4]);
			System.out.println("0000000000000");
			System.out.println(cmd[5]);
			System.out.println("0000000000000");
			System.out.println(cmd[6]);
			System.out.println("0000000000000");
			try {
				tmStarta=System.currentTimeMillis();
				p=r.exec(cmd);
			    p.waitFor();
			    
			    
			    BufferedReader  br =  new  BufferedReader (new  InputStreamReader(p.getErrorStream()));  
			    String msg = null;
			    while  ((msg =  br .readLine())  !=  null)  {  
			         System.out.println(msg );  
			    }  
			    
				tmEnda=System.currentTimeMillis();
			    tmUsagea=tmEnda-tmStarta;
			    cmda[3]="UsedTime: "+tmUsagea;
			    cmda[5]=resFile;
				p=r.exec(cmda);
			    p.waitFor();
			    System.out.println("finished "+i+" out of "+ command.programnum);
			} catch (Exception e) {
			      e.printStackTrace();
			}
			i = command.geti();
		}
		
		downLatch.countDown();
    }  

	public String getTestDataProgramFile(int dataser,int programser){
    	return command.m_filePath + command.m_sysName + "/data_" + dataser + "/" + "data_" + programser + ".txt";
    }

    public String getTestResultFile(int tser,int programser){
    	return command.m_filePath + command.m_sysName + "/res_" + tser + "/" + "reult_" + programser + ".txt";
    }
}
