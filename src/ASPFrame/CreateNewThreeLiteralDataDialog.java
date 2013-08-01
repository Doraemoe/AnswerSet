package ASPFrame;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CreateNewThreeLiteralDataDialog {

    JPanel jp;

    JTextField tf_atoms = new JTextField(5);  // n 
    JTextField tf_atomEnds = new JTextField(5);   // 
    
    JTextField tf_rules = new JTextField(5);   // l
    JTextField tf_ruleEnds = new JTextField(5);
    
    JTextField tf_constraints = new JTextField(5); //constraint
    JTextField tf_constraintEnds = new JTextField(5);
    
    JTextField tf_programs = new JTextField(5);   // number of programs

    JTextField tf_setNum = new JTextField(5);   // l
    
    
    
    int m_n, m_l, m_p, m_c;
    String m_mode;
    
    boolean b_batchMode;
    int m_setNum;
    int m_nend, m_lend, m_cend;
    
    double d_fixDensityRate;
    boolean b_linerMode;
        
    String p_title="Create New Test Data";
    private int m_original_n, m_original_l, m_original_c;
    
    public CreateNewThreeLiteralDataDialog()
    {
    	
    	m_original_n = 0;
    	m_original_l = 0;
    	m_original_c = 0;
    	
    	JPanel grid = new JPanel(new GridLayout(0, 2));
		
        grid.add(new JLabel("Number of atoms(n) begin:"));
        grid.add(tf_atoms);
        
        grid.add(new JLabel("Number of atoms(n) end:"));
        grid.add(tf_atomEnds);

        grid.add(new JLabel("Number of rules in one program begin:"));
        grid.add(tf_rules);
		
        grid.add(new JLabel("Number of rules in one program end:"));
        grid.add(tf_ruleEnds);
        
        grid.add(new JLabel("Number of constraints in one program begin:"));
        grid.add(tf_constraints);
        
        grid.add(new JLabel("Number of constraints in one program end:"));
        grid.add(tf_constraintEnds);

        grid.add(new JLabel("Number of programs"));
        grid.add(tf_programs);
        
        grid.add(new JLabel("Number of data set in the batch"));
        grid.add(tf_setNum);
        
        JPanel box = new JPanel();
        box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
        box.add(grid);
 
        jp = new JPanel(new BorderLayout());
        jp.add(box, BorderLayout.PAGE_START);
    }
    
    
    
    public void parseInput() {
    	m_n = Integer.parseInt(tf_atoms.getText());
    	m_l = Integer.parseInt(tf_rules.getText());
    	m_p = Integer.parseInt(tf_programs.getText());
    	m_c = Integer.parseInt(tf_constraints.getText());

    	b_batchMode = true;
    	
    	if(b_batchMode) {
            m_setNum = Integer.parseInt(tf_setNum.getText());
            m_nend = Integer.parseInt(tf_atomEnds.getText());
            m_lend = Integer.parseInt(tf_ruleEnds.getText());
            m_cend = Integer.parseInt(tf_constraintEnds.getText());
    	} 	
    }

    public boolean isBatchMode(){
    	return b_batchMode;
    }
    
    public void setBatchIdx(int idx){
		if(idx==0){
			m_original_n = m_n;
			m_original_l = m_l;
			m_original_c = m_c;
		}  		
		m_n = (m_nend * idx + m_original_n * (m_setNum - idx - 1)) / (m_setNum - 1);
		m_l = (m_lend * idx + m_original_l * (m_setNum - idx - 1)) / (m_setNum - 1);
		m_c = (m_cend * idx + m_original_c * (m_setNum - idx - 1)) / (m_setNum - 1);
		return;	
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
    
    public int getConstraintsNum() {
    	return m_c;
    }
}