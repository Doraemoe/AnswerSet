package ASPFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import AnswerSet.Config;


public class ProjectRootDirBox extends JDialog implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JTextField textDir;
    JButton btBrowse,btOk,btCancel;

    static final int PROJECT_ROOT=0, DLV_LOC=1,SMODELS_LOC=2,CLASP_LOC=3,LPARSE_LOC=4, CLINGO_LOC = 5;
    int m_type;
    public ProjectRootDirBox(JFrame f,int type) {
		super(f, "Set system directories", true);

    	String sDir=Config.getConfig("docRoot");
        JPanel container = new JPanel();
		container.setLayout( new BorderLayout() );
		JLabel lbUp=new JLabel("Directory of systems:");

		m_type=type;
		switch(m_type){
		case PROJECT_ROOT:
			break;
		case DLV_LOC:
    		this.setTitle("Set DLV Location");
        	sDir=Config.getConfig("dlvLoc");
        	lbUp.setText("DLV Location");
    		break;
		case SMODELS_LOC:
    		this.setTitle("Set SModels Location");
        	sDir=Config.getConfig("smodelsLoc");
        	lbUp.setText("SModels Location");
    		break;
		case CLASP_LOC:
        	sDir=Config.getConfig("claspLoc");
    		this.setTitle("Set Clasp Location");
        	lbUp.setText("Clasp Location");
    		break;
		case LPARSE_LOC:
        	sDir=Config.getConfig("lparseLoc");
    		this.setTitle("Set lparse Location");
        	lbUp.setText("Lparse Location");
    		break;
		case CLINGO_LOC:
			sDir=Config.getConfig("clingoLoc");
			this.setTitle("Set Clingo Location");
			lbUp.setText("Clingo Location");
    		break;
		}
		container.add(lbUp,BorderLayout.NORTH);

		textDir=new JTextField(sDir);
		container.add(textDir,BorderLayout.CENTER);
		textDir.setText(sDir);
		btBrowse=new JButton("Browse...");
		btOk=new JButton("Ok");
		btCancel=new JButton("Cancel");

		btBrowse.addActionListener(this);
		btOk.addActionListener(this);
		btCancel.addActionListener(this);
		
		JPanel butP=new JPanel();
		container.add(butP,BorderLayout.SOUTH);
		butP.add(btBrowse);
		butP.add(btOk);
		butP.add(btCancel);
		getContentPane().add(container);
		pack();
		centerDialog();
    }


    protected void centerDialog() {
        Dimension screenSize = this.getToolkit().getScreenSize();
		Dimension size = this.getSize();
		screenSize.height = screenSize.height/2;
		screenSize.width = screenSize.width/2;
		size.height = size.height/2;
		size.width = size.width/2;
		int y = screenSize.height - size.height;
		int x = screenSize.width - size.width;
		this.setLocation(x,y);
    }


	public void actionPerformed(ActionEvent arg0) {
		Object o=arg0.getSource();
		if(o==btCancel) {
			this.dispose();
			return;
		}
		if(o==btBrowse){
			String sDir=textDir.getText();
		    JFileChooser jfc=new JFileChooser(sDir);
		    if(m_type==PROJECT_ROOT)
		    	jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		    int val=jfc.showOpenDialog(this);
		    if(val==JFileChooser.APPROVE_OPTION){
		      String fpath=jfc.getSelectedFile().getAbsolutePath();
		      textDir.setText(fpath);
		    }

			
			return;
		}
		if(o==btOk){
			String sDir=textDir.getText();
			
			switch(m_type){
			case PROJECT_ROOT:
				Config.SetConfig("docRoot",sDir);
				break;
			case DLV_LOC:
				Config.SetConfig("dlvLoc",sDir);
	    		break;
			case SMODELS_LOC:
				Config.SetConfig("smodelsLoc",sDir);
	    		break;
			case CLASP_LOC:
				Config.SetConfig("claspLoc",sDir);
	    		break;
			case LPARSE_LOC:
				Config.SetConfig("lparseLoc",sDir);
	    		break;
			case CLINGO_LOC:
				Config.SetConfig("clingoLoc", sDir);
				break;
			}
			this.dispose();
			return;
			
		}
	}
}

