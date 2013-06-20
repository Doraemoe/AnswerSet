package ASPFrame;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class DistributionFrame extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	HistorGramPane histPan;
	JPanel contropan;

	JTextField x_end, y_end;
	
	public DistributionFrame() {
		super("Test Statistics", true, true, true, true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		setBounds( 100, 100, screenSize.width-100, screenSize.height-200);

		init();
    }
    private void initControlPan(){

    	x_end=new JTextField(10);
    	y_end=new JTextField(10);
    	
    	
    	contropan.add(new JLabel("Max X"));
    	contropan.add(x_end);
    	contropan.add(new JLabel("Max Y"));
    	contropan.add(y_end);


    	JButton jb=new JButton("Update Range");
    	
		jb.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e){
						updateRange();
					}
				}
		);
		contropan.add(jb);
    }

    private void init()
	{
		JPanel panel=new JPanel();
		histPan=new HistorGramPane();
		
		panel.setLayout(new BorderLayout());
		panel.add(histPan,BorderLayout.CENTER);
		setContentPane(panel);
		
		contropan=new JPanel();
		panel.add(contropan,BorderLayout.SOUTH);
		initControlPan();
	
	}
	public void setDistData(int data[]){
		histPan.setData(data);
		
	}
	public void setXValueRange(double x0,double x1){
//		staticPan.setXValueRange(x0, x1,x0,x1);
//		setXRange(x0,x1);
		
	}
	void setXRange(int x2){
		x_end.setText(""+x2);
		
	}
	void setYRange(int y2){
		y_end.setText(""+y2);
		
	}
	void updateRange(){
		int x2, y2;
		x2=Integer.parseInt(x_end.getText());
		y2=Integer.parseInt(y_end.getText());
		histPan.setFrameXYRange(x2, y2);
	}
}