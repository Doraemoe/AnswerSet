package ASPFrame;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import netp.GUI.LineCurve;

public class StaticsFrame extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	CurvPane staticPan;
	JPanel contropan;

	JTextField x_start,x_end,y_start,y_end, y_start2,y_end2;
	JCheckBox disCur1,disCur2;
	
	public StaticsFrame() {
		super("Test Statistics", true, true, true, true);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		setBounds( 100, 100, screenSize.width-100, screenSize.height-200);

		init();
    }
    private void initControlPan(){
    	
    	x_start=new JTextField(10);
    	x_end=new JTextField(10);
    	y_start=new JTextField(10);
    	y_end=new JTextField(10);
    	y_start2=new JTextField(10);
    	y_end2=new JTextField(10);
    	
    	disCur1=new JCheckBox("Display curv 1");
    	disCur2=new JCheckBox("Display curv 2");
    	disCur1.setSelected(true);
    	disCur2.setSelected(true);
    	
    	contropan.add(disCur1);
    	contropan.add(disCur2);
    	
    	contropan.add(new JLabel("X Range from: "));
    	contropan.add(x_start);
    	contropan.add(new JLabel("to: "));
    	contropan.add(x_end);

    	contropan.add(new JLabel("Y Range from: "));
    	contropan.add(y_start);
    	contropan.add(new JLabel("to: "));
    	contropan.add(y_end);
    	

    	contropan.add(new JLabel("Right-Y Range from: "));
    	contropan.add(y_start2);
    	contropan.add(new JLabel("to: "));
    	contropan.add(y_end2);

    	
    	JButton jb=new JButton("Update Range");
    	
		jb.addActionListener(
				new ActionListener() {
					public void actionPerformed(ActionEvent e){
						updateRange();
					}
				}
		);
		contropan.add(jb);
    	
    	
		disCur1.addChangeListener(
			new ChangeListener(){

				public void stateChanged(ChangeEvent e) {
					updateDisplay();
				}
			}
		);

		disCur2.addChangeListener(
				new ChangeListener(){

					public void stateChanged(ChangeEvent e) {
						updateDisplay();
					}
				}
			);
		
		setYRange(0,100);
    	
    }

    void updateDisplay(){
    	staticPan.setShowCurve1(disCur1.isSelected());
    	staticPan.setShowCurve2(disCur2.isSelected());
    }
    private void init()
	{
		JPanel panel=new JPanel();
		staticPan=new CurvPane();
		
		panel.setLayout(new BorderLayout());
		panel.add(staticPan,BorderLayout.CENTER);
		setContentPane(panel);
		staticPan.setYTB(30, 30);
		
		contropan=new JPanel();
		panel.add(contropan,BorderLayout.SOUTH);
		initControlPan();
	
	}
	public void setSatRateCurve(LineCurve lv){
		staticPan.setCurv(lv);
		
	}
	public void setTimeCurve(LineCurve lv){
		double maxY;
		maxY=lv.getMaxY();
		staticPan.setFrameYRange2(0,maxY);
		staticPan.setCurv2(lv);
    	setYRange2(0,maxY);
		
	}
	public void setXValueRange(double x0,double x1){
		staticPan.setXValueRange(x0, x1,x0,x1);
		setXRange(x0,x1);
		
	}
	void setXRange(double x1,double x2){
		x_start.setText(""+x1);
		x_end.setText(""+x2);
		
	}
	void setYRange(double y1,double y2){
		y_start.setText(""+y1);
		y_end.setText(""+y2);
		
	}
	void setYRange2(double y1,double y2){
		y_start2.setText(""+y1);
		y_end2.setText(""+y2);
		
	}
	void updateRange(){
		double x1,x2, y1, y2;
		x1=Double.parseDouble(x_start.getText());
		x2=Double.parseDouble(x_end.getText());
		staticPan.setFrameXRange(x1, x2);

		y1=Double.parseDouble(y_start.getText());
		y2=Double.parseDouble(y_end.getText());
		staticPan.setFrameYRange(y1, y2);

		y1=Double.parseDouble(y_start2.getText());
		y2=Double.parseDouble(y_end2.getText());
		staticPan.setFrameYRange2(y1, y2);
	}
}