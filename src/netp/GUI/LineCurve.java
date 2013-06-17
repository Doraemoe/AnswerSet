package netp.GUI;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

public class LineCurve {
	double dData[];
	double dAvg;
	double dMax;
	int iDataNum;
	int iMaxNum;
	int iOffset;
	Color col;
	boolean bDrawAvage;

	static float sk[]={1.0f,3.0f};
	public static BasicStroke dotBS=new BasicStroke(1.0f,BasicStroke.CAP_SQUARE,
		BasicStroke.JOIN_ROUND,	1.0f,sk,0f);

	public int getMaxX() {
		return iMaxNum;
	}
	public double getMaxY() {
		return dMax;
	}
	public void setColor(Color c){
		col=c;
	}
	public LineCurve(int maxnum,Color c){
		iMaxNum=maxnum;
		dData=new double[iMaxNum];
		clearData();
		col=c;
		bDrawAvage=false;
		iOffset=0;
	}
	public void clearData(){
		iDataNum=0;
		dMax=0.0;
		dAvg=0.0;
	}
	public void setData(double v[]){
		int i;
		for(i=0;i<iMaxNum;++i) {
			dData[i]=v[i];
		}
		iDataNum=iMaxNum;
		iOffset=0;
		calculate();
	}
	public void setData(double v){
		int i,tmp;
		if(iDataNum<iMaxNum) {
			dData[iDataNum++]=v;
		}
		else {
			tmp=iMaxNum-1;
			for(i=0;i<tmp;++i) {
				dData[i]=dData[i+1];
			}
			dData[iMaxNum-1]=v;
			iOffset++;
		}
		calculate();
	}
	public int getOffset(){
		return iOffset;
	}
	public void calculate(){
		if(iDataNum==0){
			dMax=0.0;
			dAvg=0.0;		
		}
		else {
			int i;
			double dSum=0;
			for(i=0;i<iDataNum;++i){
				if(dData[i]>dMax) dMax=dData[i];
				dSum+=dData[i];
			}
			dAvg=dSum/iDataNum;
		}
	}
	public void draw(Graphics g,int x0,int x1,int y0,int y1,int iMaxX,double dMinY,double dMaxY){
		if(iDataNum==0)return;
		g.setColor(col);
		int dx0,dy0,dx1=0,dy1=0;
		int i;
		for(i=0;i<iDataNum;++i){
			dx0=x0+(i)*(x1-x0)/(iMaxX-1);
			dy0=(int)(y1-(dData[i]-dMinY)*(y1-y0)/(dMaxY-dMinY));
			if(i>0){
				g.drawLine(dx0,dy0,dx1,dy1);
			}
			dx1=dx0;
			dy1=dy0;
		}
		if(bDrawAvage) {
			Graphics2D g2=(Graphics2D)g;
			dy1=(int)(y1-dAvg*(y1-y0)/dMaxY);
			
			Stroke oldbs = g2.getStroke();			
			g2.setStroke(dotBS);
			g.drawLine(x0,dy1,x1,dy1);

			g2.setStroke(oldbs);
		}	
	}
	public void copy(LineCurve wc){
		iDataNum=wc.iDataNum;
		iMaxNum=wc.iMaxNum;
		dData=new double[iMaxNum];

		int i;
		for(i=0;i<iDataNum;++i){
			dData[i]=wc.dData[i];
		}
		calculate();
	}
	public void setDrawAvage(boolean b) {
		bDrawAvage=b;
	}
}
