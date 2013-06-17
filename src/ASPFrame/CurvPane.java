package ASPFrame;
import javax.swing.JPanel;
import netp.GUI.LineCurve;
import java.awt.*;
import java.awt.event.*;
//import javax.swing.*;
/*
 * Created on 2003/9/4
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
import java.text.DecimalFormat;

/**
 * @author larry
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

public class CurvPane extends JPanel implements MouseMotionListener, MouseListener{
	LineCurve cv;
	LineCurve cv2;
	
	boolean bShowCurve1,bShowCurve2;
	
	public static Font smallFont=new Font("Courier New",Font.PLAIN,12);
	
	public static DecimalFormat df = new DecimalFormat("#.##");
	public static DecimalFormat getDecimalFormat(){
		return df;
	}
	
	
	
	int mx0,mx1,my0,my1;
	int maxx,offset;
	int mx,my; // the position of mouse
	static Color gridCol=new Color(0,127,0);

	int ty0,by0; // the head and tail. pixel
	
	double mx_start_val,mx_end_val;// real value range of the curve
	double mx_curve_range_beg,mx_curve_range_end;// the frame range. 
	
	double my_curve_range_beg,my_curve_range_end; // the frame range
	double my_curve_range_beg2,my_curve_range_end2; // the frame range for second curve
	
	String lb_BottomLeft,lb_BottomRight;
	String lb_LeftTop,lb_LeftBottom;
	String lb_RightTop,lb_RightBottom;

	public void setShowCurve1(boolean b){
		bShowCurve1=b;
		invalidate();
		repaint();
	}
	public void setShowCurve2(boolean b){
		bShowCurve2=b;
		invalidate();
		repaint();
	}

	public CurvPane(){
		super();
		cv=null;
		cv2=null;
		mx=-1;my=-1;
		addMouseListener(this);
		addMouseMotionListener(this);
		ty0=16;
		by0=6;
		mx_start_val=0;
		mx_end_val=100;
		mx_curve_range_beg=0;
		mx_curve_range_end=0;
		lb_BottomLeft="";
		lb_BottomRight="";
		lb_LeftTop="";
		lb_LeftBottom="";
		setFrameYRange(0,100);
		setFrameYRange2(0,100);
		
		bShowCurve1=true;
		bShowCurve2=true;
		
	}
	public void setYTB(int t,int b){
		ty0=t; // head of the grid.
		by0=b;
	}
	public void setXValueRange(double x0,double x1,double cur_beg,double cur_end){
		mx_start_val=x0;
		mx_end_val=x1;
		
		setFrameXRange(cur_beg,cur_end);
		
	}
	public void setFrameXRange(double x1,double x2){
		mx_curve_range_beg=x1;
		mx_curve_range_end=x2;

		lb_BottomLeft=getDecimalFormat().format(mx_curve_range_beg);
		lb_BottomRight=getDecimalFormat().format(mx_curve_range_end);

		
		this.invalidate();
		this.repaint();
		
	}

	public void setFrameYRange(double y1,double y2){
		my_curve_range_beg=y1;
		my_curve_range_end=y2;

		lb_LeftBottom=getDecimalFormat().format(my_curve_range_beg);
		lb_LeftTop=getDecimalFormat().format(my_curve_range_end);

		
		this.invalidate();
		this.repaint();
		
	}
	
	public void setFrameYRange2(double y1,double y2){
		my_curve_range_beg2=y1;
		my_curve_range_end2=y2;

		lb_RightBottom=getDecimalFormat().format(my_curve_range_beg2);
		lb_RightTop=getDecimalFormat().format(my_curve_range_end2);

		
		this.invalidate();
		this.repaint();
		
	}

	
	
	public void setCurv(LineCurve c){
		cv=c;
	}
	public void setCurv2(LineCurve c){
		cv2=c;
	}
	void drawGrid(Graphics g){
		int i;
		int x0,y0,x1,y1;
		int ioff;
		g.setColor(gridCol);
		x0=mx0;x1=mx1;
		for(i=0;i<=11;++i){
			y0=my0+i*(my1-my0)/10;
			g.drawLine(x0,y0,x1,y0);
		}
		
		y0=my0;y1=my1;
		ioff=offset*(mx1-mx0)/maxx;
		ioff=ioff%((mx1-mx0)/10);
		for(i=0;i<=11;++i){
			x0=mx0+i*(mx1-mx0)/10-ioff;
			if(x0<mx0)continue;
			if(x0>mx1)continue;
			g.drawLine(x0,y0,x0,y1);
		}
	}
	void drawBack(Graphics g){
		g.setColor(Color.BLACK);
		g.fillRect(6,16,getWidth()-12,getHeight()-21);
	}
	public double getMaxY(){
		return my_curve_range_end;
	}
	public double getMinY(){
		return my_curve_range_beg;
	}

	public double getMaxY2(){
		return my_curve_range_end2;
	}
	public double getMinY2(){
		return my_curve_range_beg2;
	}
	
	
	public void paint(Graphics g){
		super.paint(g);
		
		FontMetrics	 fm=getFontMetrics(smallFont);
		int wd=fm.stringWidth(lb_LeftTop);
		if(wd<fm.stringWidth(lb_LeftBottom))wd=fm.stringWidth(lb_LeftBottom);
		
		mx0=wd+15;
		
		mx1=getWidth()-10;

		if(cv2!=null){
			wd=fm.stringWidth(lb_RightTop);
			if(wd<fm.stringWidth(lb_RightBottom))wd=fm.stringWidth(lb_RightBottom);
			mx1=getWidth()-wd-10;
		}

		my0=ty0;
		my1=getHeight()-by0;

		drawBack(g);
		if(cv==null) return;
		maxx=cv.getMaxX();
		offset=cv.getOffset();
		drawGrid(g);
		
		double cuv_x_beg, cur_x_end;
		
		cuv_x_beg=mx0+(mx_start_val-mx_curve_range_beg)*(mx1-mx0)/(mx_curve_range_end-mx_curve_range_beg);
		cur_x_end=mx0+(mx_end_val-mx_curve_range_beg)*(mx1-mx0)/(mx_curve_range_end-mx_curve_range_beg);
		
		
		if(bShowCurve1)
			cv.draw(g,(int)cuv_x_beg,(int)cur_x_end,my0,my1,maxx,getMinY(),getMaxY());		

		if(cv2!=null) {
			if(bShowCurve2)
			cv2.draw(g,(int)cuv_x_beg,(int)cur_x_end,my0,my1,maxx,getMinY2(),getMaxY2());		
		}
		drawLabel(g);
	}
	void drawLabel(Graphics g){
		g.setColor(Color.green);
		FontMetrics	 fm=getFontMetrics(smallFont);
		
		g.drawString(lb_BottomLeft, mx0, my1+15);

		int wd=fm.stringWidth(lb_BottomRight);
		g.drawString(lb_BottomRight, mx1-wd, my1+15);
		

		wd=fm.stringWidth(lb_LeftTop);
		g.drawString(lb_LeftTop, mx0-wd-5, my0+10);
		
		wd=fm.stringWidth(lb_LeftBottom);
		g.drawString(lb_LeftBottom, mx0-wd-5, my1-5);
		

		if(cv2!=null){
			g.drawString(lb_RightTop, mx1+3, my0+10);
			g.drawString(lb_RightBottom, mx1+3, my1-5);
		}
		
		if((mx<mx0)||(mx>mx1)||(my<my0)||(my>my1)) return;
		double lv=my_curve_range_beg+(my_curve_range_end-my_curve_range_beg)*(my1-my)/(my1-my0);
		
			
			
		double mx_value;
		mx_value=(mx_curve_range_beg*(mx1-mx)+mx_curve_range_end*(mx-mx0))/(mx1-mx0);
		
		String lb=getDecimalFormat().format(mx_value)+"/"+getDecimalFormat().format(lv);

		if(cv2!=null){
			lv=my_curve_range_beg2+(my_curve_range_end2-my_curve_range_beg2)*(my1-my)/(my1-my0);
			lb+=" , "+getDecimalFormat().format(lv);
		}
		
		g.setFont(smallFont);
		wd=fm.stringWidth(lb);
//		if(mx>(mx0+mx1)/2){
//			g.drawString(lb,mx0+3,25);
//		}	
//		else 		
			g.drawString(lb,mx1-wd-3,25);
		
		
	}
	static public double calculateMaxY(double ix){
		if(ix>10)return 10*calculateMaxY(ix/10);
		if((ix>0)&&(ix<1)) return 0.1*calculateMaxY(ix*10);
		if(ix<0)return 0;
		if(ix>5) return 10;
		if(ix>2)return 5;
		if(ix>1)return 2;
		return 1;
	}	
	
	public void mouseMoved(MouseEvent e){
		mx=e.getX();
		my=e.getY();
		if((mx<mx0)||(mx>mx1)||(my<my0)||(my>my1)){
			mx=-1; my=-1;
		}
		repaint();
	}
	public void mouseDragged(MouseEvent e){
		
	}

	public void mouseClicked(MouseEvent e){
	}
	public void mouseEntered(MouseEvent e){mouseMoved(e);} 
	public void mouseExited(MouseEvent e) {			
		mx=-1; my=-1;
		repaint();
	} 
	public void mousePressed(MouseEvent e) {}
	public void mouseReleased(MouseEvent e) {}
	
}
