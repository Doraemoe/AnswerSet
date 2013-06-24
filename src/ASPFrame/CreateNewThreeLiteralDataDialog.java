package ASPFrame;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CreateNewThreeLiteralDataDialog {

    JPanel jp;

    JTextField tf_atoms = new JTextField(5);  // n 
    JTextField tf_rules = new JTextField(5);   // l
    JTextField tf_programs = new JTextField(5);   // number of programs
    JTextField tf_fixDensityRate = new JTextField("0.0"); // the fixed density rate
    
    JComboBox<String> cb_mode = new JComboBox<String>();
    //JCheckBox ckb_linerMode = new JCheckBox(); // if using liner mode
    //JCheckBox ckb_squareMode = new JCheckBox(); // if using squrare mode
    //JCheckBox ckb_cubeMode = new JCheckBox(); // if using cube mode
    
    JCheckBox ckb_batchMode = new JCheckBox();
    
    JTextField tf_ruleEnds = new JTextField(5);   // l
    JTextField tf_setNum = new JTextField(5);   // l
    
    
    
    int m_n, m_l, m_p;
    String m_mode;
    
    boolean b_batchMode;
    int m_setNum;
    int m_lend,m_lstart;
    double d_fixDensityRate;
    boolean b_linerMode;
        
    String p_title="Create New Test Data";
    private int m_original_n;
    
    void initComboBoxes(){
    	int i;
    	
    	String strClasses[] = {"Liner Mode", "Square Mode", "Cube Mode"};
    	for(i = 0;i < 3; ++i)
    		cb_mode.addItem(strClasses[i]);
    	
    }
    
    public CreateNewThreeLiteralDataDialog()
    {
    	initComboBoxes();
    	
    	m_original_n=0;
        jp=new JPanel();
        jp.setLayout(new GridLayout(15, 2));


        jp.add(new JLabel("Fix Density Rate"));
		jp.add(tf_fixDensityRate);

		jp.add(new JLabel("Rules Mode"));
		jp.add(cb_mode);
		
		jp.add(new JLabel("Number of atoms(n):"));
        jp.add(tf_atoms);

		jp.add(new JLabel("Number of rules in one program(l)"));
		jp.add(tf_rules);

		jp.add(new JLabel("Number of programs(l)"));
		jp.add(tf_programs);

        jp.add(new JLabel("Batch data generator"));
        jp.add(ckb_batchMode);
         
        jp.add(new JLabel("Number of atoms(n) End"));
        jp.add(tf_ruleEnds);

        jp.add(new JLabel("Number of data set in the batch"));
        jp.add(tf_setNum);   
    }
    
    
    
    public void parseInput() {
    	m_n = Integer.parseInt(tf_atoms.getText());
    	m_l = Integer.parseInt(tf_rules.getText());
    	m_p = Integer.parseInt(tf_programs.getText());

    	d_fixDensityRate = Double.parseDouble(tf_fixDensityRate.getText());
    	m_mode = (String) cb_mode.getSelectedItem();
    	b_batchMode = ckb_batchMode.isSelected();
    	
    	if(b_batchMode) {
            m_setNum = Integer.parseInt(tf_setNum.getText());
            m_lend = Integer.parseInt(tf_ruleEnds.getText());
            m_lstart = m_l;
    	} 	
    }

    public boolean isBatchMode(){
    	return b_batchMode;
    }
    
    public void setBatchIdx(int idx){
    	if(m_mode.equals("Liner Mode")) {
    		if(idx==0){
    			m_original_n=m_n;
    		}  		
    		m_n = (m_lend * idx + m_original_n * (m_setNum - idx - 1)) / (m_setNum - 1);
    		m_l = (int) ((m_n - 1) * d_fixDensityRate);
    		return;
    	}
    	else if(m_mode.equals("Square Mode")) {
    		if(idx==0){
    			m_original_n=m_n;
    		}	
    		m_n = (m_lend * idx + m_original_n * (m_setNum - idx - 1)) / (m_setNum - 1);
    		m_l = (int) ((m_n * (m_n - 1)) * d_fixDensityRate + 0.5);
    	}
    	else {
    		if(idx==0){
    			m_original_n=m_n;
    		}
    		m_n = (m_lend*idx + m_original_n*(m_setNum-idx-1)) / (m_setNum-1);
    		m_l = (int) ((m_n * (m_n - 1) * (m_n - 2)) * d_fixDensityRate + 0.5);
    	}
    }
    
    public int getBatchSetNum(){
    	return m_setNum;
    }
    
    public int getAtomNum() {
    	return m_n;
    }
    
    public int getRulesNum(){
    	return m_l;
    }

    public int getProgramNum(){
    	return m_p;
    }
    
    public String getRuleMode(){
    	return m_mode;
    }

	public double getDensityRate(){
		return d_fixDensityRate;
	}
}