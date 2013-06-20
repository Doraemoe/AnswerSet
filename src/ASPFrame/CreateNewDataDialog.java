package ASPFrame;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import data.TestData;

public class CreateNewDataDialog 
{

    JPanel jp;

    JComboBox<String> cb_class = new JComboBox<String>();  // k
    JTextField tf_atoms = new JTextField(5);  // n
    JComboBox<String> cb_literals = new JComboBox<String>();  // k

    
    
    JTextField tf_rules = new JTextField(5);   // l
    JTextField tf_programs = new JTextField(5);   // number of programs
    JTextField tf_fixDensityRate = new JTextField("0.0"); // the fixed density rate
    JCheckBox ckb_linerMode = new JCheckBox(); // if using liner mode
    

    
    
    JCheckBox ckb_batchMode = new JCheckBox();

    JCheckBox ckb_noRepeatLiteral = new JCheckBox();
    JCheckBox ckb_batchOnRepeatLiteral = new JCheckBox();
    JTextField tf_repeatLiteralRateStart = new JTextField("0.00"); // the fixed density rate
    JTextField tf_repeatLiteralRateEnd = new JTextField("0.50"); // the fixed density rate

    
    JCheckBox ckb_usePowerLaw = new JCheckBox();

    
    JTextField tf_ruleEnds = new JTextField(5);   // l
    JTextField tf_setNum = new JTextField(5);   // l
    
    
    
    int m_n,m_k,m_l,m_p;
    boolean b_noRepeatLiteral;
    boolean b_usePowerLaw;
    String m_class;
    
    boolean b_batchMode;
    boolean b_repeatLiterateBatch;
    int m_setNum;
    int m_lend,m_lstart;
    double d_fixDensityRate;
    boolean b_linerMode;
    
    double d_repeatRateStart=0,d_repeatRateEnd=0;
    double d_repeatLiteralRate=0;
    int m_repeatLiteralRuleNum=0;
    
    String p_title="Create New Test Data";
    private int m_original_n;
    
    void initComboBoxes(){
    	int i;
    	for(i=2;i<30;++i)
    		cb_literals.addItem(""+i);

    	String strClasses[];
    	strClasses=TestData.getDataClassList();
    	int len=strClasses.length;
    	for(i=0;i<len;++i)
    		cb_class.addItem(strClasses[i]);
    	
    }
    public CreateNewDataDialog()
    {
    	initComboBoxes();
    	m_original_n=0;
        jp=new JPanel();
        jp.setLayout(new GridLayout(15,2));

        jp.add(new JLabel("Test Data Class"));
		jp.add(cb_class);


        jp.add(new JLabel("Fix Density Rate"));
		jp.add(tf_fixDensityRate);

        jp.add(new JLabel("User Liner Mode"));
		jp.add(ckb_linerMode);
		
		
		
		jp.add(new JLabel("Number of atoms(n):"));
        jp.add(tf_atoms);

        jp.add(new JLabel("Number of literals in one rule(k)"));
		jp.add(cb_literals);
		cb_literals.setEditable(true);

		jp.add(new JLabel("Number of rules in one program(l)"));
		jp.add(tf_rules);

		jp.add(new JLabel("Number of programs(l)"));
		jp.add(tf_programs);

        jp.add(new JLabel("No Repeat Literals in a rule:"));
        jp.add(ckb_noRepeatLiteral);

        jp.add(new JLabel("Batch based on Repeat Literals:"));
        jp.add(ckb_batchOnRepeatLiteral);

        jp.add(new JLabel("Repeat Literals Rate Starts:"));
        jp.add(tf_repeatLiteralRateStart);
        

        jp.add(new JLabel("Repeat Literals Rate Ends:"));
        jp.add(tf_repeatLiteralRateEnd);
        
        
        jp.add(new JLabel("Use Powerlaw Distribution:"));
        jp.add(ckb_usePowerLaw);


        jp.add(new JLabel("Batch data generator"));
        jp.add(ckb_batchMode);
       

        
        jp.add(new JLabel("Number of rules (l) End. Or (n) if fixed density rate"));
        jp.add(tf_ruleEnds);

        jp.add(new JLabel("Number of data set in the batch"));
        jp.add(tf_setNum);
        
        
        
    }
    public boolean start() 
    {
       String[] options = {
		    "OK",
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
        	
        	m_n=Integer.parseInt(tf_atoms.getText());
        	m_l=Integer.parseInt(tf_rules.getText());
        	m_p=Integer.parseInt(tf_programs.getText());
        	m_k=Integer.parseInt((String)cb_literals.getSelectedItem());

        	d_fixDensityRate=Double.parseDouble(tf_fixDensityRate.getText());
        	
        	b_linerMode=ckb_linerMode.isSelected();
        	b_noRepeatLiteral=ckb_noRepeatLiteral.isSelected();

        	b_usePowerLaw=ckb_usePowerLaw.isSelected();
        	
        	m_class=(String)cb_class.getSelectedItem();

        	
        	b_batchMode=ckb_batchMode.isSelected();
        	if(b_batchMode) {
	            m_setNum=Integer.parseInt(tf_setNum.getText());
	            m_lend=Integer.parseInt(tf_ruleEnds.getText());
	            m_lstart=m_l;

	            if(!b_noRepeatLiteral){ // allow repeat literal
	            	b_repeatLiterateBatch=ckb_batchOnRepeatLiteral.isSelected();
	            	if(b_repeatLiterateBatch){
	                    d_repeatRateStart=Double.parseDouble(tf_repeatLiteralRateStart.getText());
	                    d_repeatRateEnd=Double.parseDouble(tf_repeatLiteralRateEnd.getText());
	            	}
	            }
        	}
        	return true;
        }
        return false;
    }

    public boolean isBatchMode(){
    	return b_batchMode;
    }
    
    public void setBatchIdx(int idx){
    	
    	if(b_linerMode){
    		if(idx==0){
    			m_original_n=m_n;
    		}
    		
    		m_n=(m_lend*idx + m_original_n*(m_setNum-idx-1))/(m_setNum-1);
    		m_l= (int) ((m_n-1 ) * d_fixDensityRate);

        	if(b_repeatLiterateBatch){
        		d_repeatLiteralRate=(d_repeatRateStart*(m_setNum-idx-1)+d_repeatRateEnd*idx)/(m_setNum-1);
        		m_repeatLiteralRuleNum=(int)(d_repeatLiteralRate+0.5);
        		
        	}
        	m_l+=m_repeatLiteralRuleNum;
    		return;
    		
    	}
    	
    	if(b_repeatLiterateBatch){
    		d_repeatLiteralRate=(d_repeatRateStart*(m_setNum-idx-1)+d_repeatRateEnd*idx)/(m_setNum-1);
    		m_l=(int) ((m_n * (m_n-1)) * d_fixDensityRate+ m_n*d_repeatLiteralRate+0.5);
    		
    		m_repeatLiteralRuleNum=(int)(m_n*d_repeatLiteralRate+0.5);
    		return;
    	}
    	if(d_fixDensityRate>0.00001){
    		if(idx==0){
    			m_original_n=m_n;
    		}
    		
    		m_n=(m_lend*idx + m_original_n*(m_setNum-idx-1))/(m_setNum-1);
    		m_l= (int) ((m_n * (m_n-1)) * d_fixDensityRate+0.5);
    		
    		return;
    	}
    	
    	m_l=(m_lend*idx+m_lstart*(m_setNum-idx-1))/(m_setNum-1);
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
    public int getLiteralNum(){
    	return m_k;
    }
    public boolean isLinerMode(){
    	return b_linerMode;
    }
    public int getProgramNum(){
    	return m_p;
    }
    public boolean isNoRepeatLiteral(){
    	return b_noRepeatLiteral;
    }
    public String getDataClass(){
    	return m_class;
    }
	public boolean usePowerLaw() {
		return b_usePowerLaw;
	}
	public double getDensityRate(){
		return d_fixDensityRate;
	}
	public boolean isRepeatLiteralIndep(){
		return b_repeatLiterateBatch;
	}
	public int getRepeatLiteralRuleNum(){
		return m_repeatLiteralRuleNum;
	}
}