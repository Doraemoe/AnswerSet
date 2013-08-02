package ASPFrame;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ASPNode.ProjectRoot;

public class OpenProjectDlg extends JDialog implements ListSelectionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	ProjectRoot parent;
	JList<String> lstProjects;
	DefaultListModel<String> listModel;
    JButton btOpen,btClose;

    
    public void setParent(ProjectRoot p){
    	parent=p;
    }
    public OpenProjectDlg(JFrame f) {
        super(f, "Open an Existing Project", true);
		JPanel container = new JPanel();
		container.setLayout( new BorderLayout() );
		add(container);
		JPanel plBottom,plUp;
		plBottom=new JPanel();
		plUp=new JPanel();
		container.add(plBottom, BorderLayout.SOUTH);
		container.add(plUp,BorderLayout.CENTER);
		plBottom.setLayout(new FlowLayout(FlowLayout.CENTER));


		btOpen=new JButton("Open");
		btOpen.addActionListener(new ActionListener (){
				public void actionPerformed(ActionEvent arg0) {
					tryOpenProject();
					
				}
			}
		);
		btClose=new JButton("Close");
		btClose.addActionListener(new ActionListener (){
			public void actionPerformed(ActionEvent arg0) {
				setVisible(false);
				
			}
		});
		
		plBottom.add(btOpen);
		plBottom.add(btClose);
		getRootPane().setDefaultButton(btClose);
		
		
		
		
		plUp.setBorder(new TitledBorder("Select a project"));
		plUp.setLayout(new BorderLayout());
		
		
        listModel = new DefaultListModel<String>();
        //Create the list and put it in a scroll pane.
        lstProjects = new JList<String>(listModel);
        lstProjects.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        lstProjects.setSelectedIndex(0);
        lstProjects.addListSelectionListener(this);
        lstProjects.setVisibleRowCount(10);
        JScrollPane listScrollPane = new JScrollPane(lstProjects);

		
		plUp.add(listScrollPane);

		this.setMinimumSize(new Dimension(200,350));
		pack();
		centerDialog();
    }
    void tryOpenProject(){

        int index = lstProjects.getSelectedIndex();
        String sName=(String)listModel.getElementAt(index);
        if(parent.tryOpenProject(sName)) setVisible(false);
        
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

    public void CancelPressed() {
        this.setVisible(false);
    }


    public void setProjectList(Vector<String> v) {
    	listModel.removeAllElements();
    	int i,len=v.size();
    	String sName;
    	for(i=0;i<len;++i){
    		sName=v.get(i);
    		listModel.addElement(sName);
    	}
    }
	public void valueChanged(ListSelectionEvent e) {
        if (e.getValueIsAdjusting() == false) {

            if (lstProjects.getSelectedIndex() == -1) {
            //No selection, disable fire button.
                btOpen.setEnabled(false);

            } else {
            //Selection, enable the fire button.
                btOpen.setEnabled(true);
            }
        }
		
	}

}

