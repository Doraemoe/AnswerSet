package ASPFrame;

import java.awt.GridLayout;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ShowStaticDialog 
{

    JPanel jp;
    JComboBox<String> cb_testser = new JComboBox<String>();  // k
    JComboBox<String> cb_testser2 = new JComboBox<String>();  // kend

    
    int m_testser;

    int m_testserend;
    boolean b_batchMode;
    
    
    String p_title="Show Test Statics";

    void setMaxTestSer(int maxser){
    	int i;
    	for(i=0;i<maxser;++i) {
    		cb_testser.addItem(""+i);
    		cb_testser2.addItem(""+i);
    	}
    }
    public ShowStaticDialog(int maxser)
    {
    	setMaxTestSer(maxser);

        jp=new JPanel();
        jp.setLayout(new GridLayout(3,2));

        jp.add(new JLabel("From Test:"));
        jp.add(cb_testser);


        jp.add(new JLabel("To Test:"));
        jp.add(cb_testser2);
        
        
    }
    public boolean start() 
    {
       String[] options = {
		    "Ok",
		    "Cancel"
		};
        int result = JOptionPane.showOptionDialog(
		    null,                             // the parent that the dialog blocks
		    jp,                                    // the dialog message array
		    p_title, // the title of the dialog window
		    JOptionPane.DEFAULT_OPTION,                 // option type
		    JOptionPane.INFORMATION_MESSAGE,            // message type
		    null,                                       // optional icon, use null to use the default icon
		    options,                                    // options string array, will be made into buttons
		    options[0]                                  // option that should be made into a default button
		);
        if(result==0) {
        	
        	m_testser=Integer.parseInt((String)cb_testser.getSelectedItem());
        	m_testserend=Integer.parseInt((String)cb_testser2.getSelectedItem());

        	return true;
        }
        return false;
    }
    public int getTestSer() {
    	return m_testser;
    }
    public boolean isBatchMode(){
    	return b_batchMode;
    }
    public int getTestSerEnd(){
    	return m_testserend;
    }

}