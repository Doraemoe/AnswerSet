package netp;

import java.io.FileInputStream;

/**
 * @author larry
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class NetpFileLineReader {
	String sFileName;
	FileInputStream strm;
	public NetpFileLineReader(String fName){
		sFileName=fName;		
	}
	public boolean open(){
		try{
			strm = new FileInputStream(sFileName);
		}
		catch (Exception e){
			return false;
		}
		return true;
	}
	public String readNextLine(){
		StringBuffer sb=new StringBuffer();
		int len=0;
		int b;
		try{
			while(true){
				b=strm.read();
				if((b==13)||(b==10)||(b<0)){
					if(len>0) return sb.toString();					
					if(b<0) return null;
				}
				else {
					sb.append((char)b);
					len++;					
				}
			}
		}
		catch (Exception e){
			close();
			return null;
		}
	}
	public void close(){
		try{
			strm.close();		
		}
		catch(Exception e){
			
		}
	}
}
