package netp.GUI;

import java.awt.*;

public class StLabel extends Label {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int row;
	public void setrow(int i) {
		row=i;
	}
	public int getrow() {
		return row;
	}
	public StLabel() {
		super();
		init();
	}
	public void init() {
		setBackground(new Color(204,204,204));
		setAlignment(Label.RIGHT);
		row=0;
	}
    public StLabel(String text) {
		super(text) ;
		init();
	}
    public StLabel(String text, int alignment) {
		super(text,alignment);
		init();
	}
	public void setText(String s) {
		int l=s.length();
		char b;
		if(l>0) {
			b=s.charAt(l-1);
			if(b!=' ') {
				s=s+" ";
			}
		}
		super.setText(s);
	}
	public void setDay(int d) {
	}
	public int getDay() {
		return 0;
	}
}
