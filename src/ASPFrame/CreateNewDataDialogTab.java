package ASPFrame;

import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JComponent;
import java.awt.event.KeyEvent;

public class CreateNewDataDialogTab extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6983081929454082032L;
	
	JTabbedPane tabbedPane;
	
	CreateNewDataDialog twoLiteralDlg;
	CreateNewThreeLiteralDataDialog threeLiteralDlg;
	
	public CreateNewDataDialogTab() {	
		
		tabbedPane = new JTabbedPane();	
		twoLiteralDlg=new CreateNewDataDialog();
		threeLiteralDlg = new CreateNewThreeLiteralDataDialog();	
		
		JComponent panel1 = twoLiteralDlg.jp;		
		tabbedPane.addTab("Tab 1", null, panel1, "2 literal generator");
		tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);
		
		JComponent panel2 = threeLiteralDlg.jp;
        tabbedPane.addTab("Tab 2", null, panel2, "3 literal generator");
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);
        
        add(tabbedPane);
        
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

	}
	
    public boolean createAndShowGUI() {
        //Create and set up the window.
    	
    	String[] options = {"OK", "Cancel"};
    	
        int result = JOptionPane.showOptionDialog(
		    null,                             // the parent that the dialog blocks
		    tabbedPane,                                    // the dialog message array
		    "Title", // the title of the dialog window
		    JOptionPane.DEFAULT_OPTION,                 // option type
		    JOptionPane.INFORMATION_MESSAGE,            // message type
		    null,                                       // optional icon, use null to use the default icon
		    options,                                    // options string array, will be made into buttons
		    options[0]                                  // option that should be made into a default button
		);

        if(result==0) {
        	if(getSelectedTabNumber() == 0) {
        		twoLiteralDlg.parseInput();
        	}
        	else if(getSelectedTabNumber() == 1){
        		threeLiteralDlg.parseInput();
        	}

        	return true;
        }
        else {
            return false;
        }
    }
    
    public int getSelectedTabNumber() {
    	return tabbedPane.getSelectedIndex();
    }
    
    public CreateNewDataDialog getTwoLiteralDialog() {
    	return twoLiteralDlg;
    }
    
    public CreateNewThreeLiteralDataDialog getThreeLiteralDialog() {
    	return threeLiteralDlg;
    }
}
