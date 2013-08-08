package ASPFrame;

import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class CreateNewAnnotationDialog {
	JPanel jp;
	JTextArea annotationBox;
	String annotation;
	
	public CreateNewAnnotationDialog() {
		annotationBox = new JTextArea(10, 20);
		annotationBox.setLineWrap(true);
		annotationBox.setWrapStyleWord(true);
		
		jp = new JPanel();
		jp.setLayout(new GridLayout(0,1));
		jp.add(new JLabel("Annotation"));
		jp.add(new JScrollPane(annotationBox));
		
	}
	
	public void setAnnoation(String s) { // add_annotation
		annotation=s; // add_annotation
		annotationBox.setText(s); // add_annotation
	} // add_annotation
	
	public void parseInput() {
		annotation = annotationBox.getText();
	}
	
	public boolean start() {
		String[] options = {
			    "Ok",
			    "Cancel"
			};
		int result = JOptionPane.showOptionDialog(
			    null,                             // the parent that the dialog blocks
			    jp,                                    // the dialog message array
			    "Annotation", // the title of the dialog window
			    JOptionPane.DEFAULT_OPTION,                 // option type
			    JOptionPane.INFORMATION_MESSAGE,            // message type
			    null,                                       // optional icon, use null to use the default icon
			    options,                                    // options string array, will be made into buttons
			    options[0]                                  // option that should be made into a default button
			);
		
		if(result==0) {
			annotation = annotationBox.getText();
        	return true;
        }
        return false;
	}
	
	public String getAnnotation() {
		return annotation;
	}
}
