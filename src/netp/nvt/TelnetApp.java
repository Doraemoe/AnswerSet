package netp.nvt;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class TelnetApp {
	public static void main (String args[]) {
		PortTalk portTalk=new PortTalk(args);
		portTalk.start();
	}
}

class PortTalk extends Thread {
	Socket connection;
	OutputStream outStream;
	NVTInputStream inStream;
	NVTPrinter printer;
	public PortTalk(String args[]) {
		if(args.length!=2) error("Usage: java TelnetApp host port");
		String destination=args[0];
		int port=0;
		try {
			port=Integer.valueOf(args[1]).intValue();
		}
		catch (NumberFormatException ex) {error("Invalid port number"); }
		try {
			connection = new Socket(destination,port);
		}
		catch(UnknownHostException ex) {	error("Unknow host"); }
		catch (IOException ex) {error("IO error creating socket"); }
		try {
			outStream=connection.getOutputStream();
			inStream=new NVTInputStream(connection.getInputStream(),outStream);
		}
		catch (IOException ex) {error("IO error geting strems"); }
		System.out.println("Connected to "+ destination+" at port "+port+".");
	}
	public void run() {
		printer=new NVTPrinter(inStream);
		printer.start();
		yield();
		processUserInput();
		shutdown();
	}
	public void processUserInput() {
		try {
			String line;
			boolean finished=false;
			BufferedReader userInputStream=new BufferedReader (
				new InputStreamReader(System.in));
			do {
				line=userInputStream.readLine();
				if(line==null) finished=true;
				else {
					try {
						for(int i=0;i<line.length();++i)
							outStream.write(line.charAt(i));
							outStream.write('\n');
					}
					catch (IOException ex) {
					}
				}
			}while(!finished);
		}
		catch(IOException ex ) {
			error("Error reading user input");
		}
	}
	public void shutdown() {
		try {
			connection.close();
		}
		catch(IOException ex) {error("IO error closing socket"); }
	}
	public void error(String s) {
		System.out.println(s);
		System.exit(1);
	}
}
