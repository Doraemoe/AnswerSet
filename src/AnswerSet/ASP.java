package AnswerSet;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;


import ASPFrame.ASPFrame;
public class ASP {
	static ASPFrame frame;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
	
		try {
			ImageManager.init();
			Config.init();
			javax.swing.plaf.metal.MetalLookAndFeel.setCurrentTheme( new javax.swing.plaf.metal.DefaultMetalTheme());
			UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            
            System.setErr(System.out);
		}  
		catch ( UnsupportedLookAndFeelException e ) {
			System.out.println ("Metal Look & Feel not supported on this platform. \nProgram Terminated");
			System.exit(0);
		}
		catch ( IllegalAccessException e ) {
			System.out.println ("Metal Look & Feel could not be accessed. \nProgram Terminated");
			System.exit(0);
		}
		catch ( ClassNotFoundException e ) {
			System.out.println ("Metal Look & Feel could not found. \nProgram Terminated");
			System.exit(0);
		}   
		catch ( InstantiationException e ) {
			System.out.println ("Metal Look & Feel could not be instantiated. \nProgram Terminated");
			System.exit(0);
		}
		catch ( Exception e ) {
			System.out.println ("Unexpected error. \nProgram Terminated");
			e.printStackTrace();
			System.exit(0);
		}
	
		frame = new ASPFrame();
        ASPFrame.constructTitle();
		frame.setVisible(true);
	}
    static public ASPFrame getTopFrame() {
        return frame;
    }
		
    static public void showMessageBox(String strMsg){
		JOptionPane.showMessageDialog(getTopFrame(), strMsg, "Informaiton", JOptionPane.INFORMATION_MESSAGE);
    }
}