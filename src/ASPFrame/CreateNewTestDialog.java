package ASPFrame;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class CreateNewTestDialog 
{

    JPanel jp;

    JComboBox<String> cb_testser = new JComboBox<String>();  // k

    
    JCheckBox ckb_batchMode = new JCheckBox();
    JCheckBox ckb_allModel=new JCheckBox(); // find all models or not
    
    JComboBox<String> cb_testser2 = new JComboBox<String>();  // kend

    JComboBox<String> cb_solver = new JComboBox<String>();  // solver
    
    int m_testser;

    int m_testserend;
    boolean b_batchMode, b_allModel;
    
    String m_solver;
    String p_title="Do a New Test";

    void setMaxTestSer(int maxser){
    	int i;
    	for(i=0;i<maxser;++i) {
    		cb_testser.addItem(""+i);
    		cb_testser2.addItem(""+i);
    	}
    }
    public CreateNewTestDialog(int maxser)
    {
    	setMaxTestSer(maxser);

        jp=new JPanel();
        jp.setLayout(new GridLayout(6,2));

        jp.add(new JLabel("Which Data:"));
        jp.add(cb_testser);

        jp.add(new JLabel("Batch Mode"));
        jp.add(ckb_batchMode);

        jp.add(new JLabel("Which Data:"));
        jp.add(cb_testser2);
        
        cb_solver.addItem("DLV");
        cb_solver.addItem("Smodels");
        cb_solver.addItem("Clasp");
        
        jp.add(new JLabel("Select Solver:"));
        jp.add(cb_solver);

        jp.add(new JLabel("Find All Models:"));
        jp.add(ckb_allModel);
        
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
        	b_batchMode=ckb_batchMode.isSelected();
        	m_testserend=Integer.parseInt((String)cb_testser2.getSelectedItem());
        	m_solver=(String)cb_solver.getSelectedItem();
        	b_allModel=ckb_allModel.isSelected();
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
    public String getSolver(){
    	return m_solver;
    }
	public boolean getAllModel() {
		// TODO Auto-generated method stub
		return b_allModel;
	}
}