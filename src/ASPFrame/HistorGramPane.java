package ASPFrame;

import javax.swing.JPanel;
import java.awt.*;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class HistorGramPane extends JPanel {
  private int iRange,iSet; // iRange is the x axis, iSet how many sample merge together, iMaxX the max value
  private double iData[];
  private int iDataNum,iMaxY;
  Color cBarColor;
  int mHeight,mWidth;
  int ix0,iy0,ix1,iy1;
  ClassPowerLawEst cple;
  boolean bCalCPLE;
  boolean bUsePoisson;
  double dPoissonlmdt,dPoissonWeight;
  
  boolean bUseBinomial;
  double dBinormialP;
  int iBinomialN;
  double iBinomialA;  
  static BasicStroke bsPoissonStroke=null;
  int iOff;
  boolean bHasData=false;
  int iDrawType;
  
  boolean bLog;
  static final public int GBAR=0, GCIRCLE=1;
  double dTaillongRate,dTailNumRate,dWeightTailRate; // calculate the tail and head rate
  double dP, dStd, dStdPRate; // calcualte std over probility
  double dTotalNode=0,dTotalLink=0,dMean=0;
  
  void setLogarithm(boolean b){
  	bLog=b;
  }
  BasicStroke getWideStroke(){
  	if(bsPoissonStroke==null)
  		bsPoissonStroke=new BasicStroke(3.0f);
  	return bsPoissonStroke;
  }
  public HistorGramPane() {
    super();
    iDataNum=0;
    iRange=50;
    iMaxY=0;
    iSet=1;
    setBarColor(Color.red);
    bCalCPLE=false;
    bUsePoisson=false;
	bUseBinomial=false;
    iDrawType=GBAR;
    setBackground(Color.white);
  }
  void retriveIMaxY() {
    iMaxY=0;
    int i;
    for(i=0;i<iDataNum;++i){
      if(iData[i]>iMaxY) iMaxY=(int)iData[i];
    }
  }
  void retrieveIRange(){
	    iRange=1;
	    int i;
	    for(i=0;i<iDataNum;++i){
	      if(iData[i]>0) iRange=i;
	    }
	  
  }
  public void setFrameXYRange(int x,int y){
	  iRange=x;
	  iMaxY=y;
	  invalidate();
	  repaint();
  }
  public void setBarColor(Color c) {
    cBarColor=c;
  }
	public void setData(int dData[]){
		setData(dData,true);
	}
  public void setData(double aData[]){
	setData(aData,true);
  }
  public void setData(int aData[],boolean bCPLE){
  	double d[];
  	int i,l;
  	l=aData.length;
  	d=new double[l];
  	for(i=0;i<l;++i){
  		d[i]=aData[i];
  	}
  	setData(d,bCPLE);
  }
  public void setData(double aData[],boolean bCPLE){
	iData=aData;
 	bCalCPLE=bCPLE;
	iDataNum=iData.length;
	calculateCPLE();
	retriveIMaxY();
	retrieveIRange();	
	repaint();
	bHasData=true;
	bUsePoisson=false;
	calculateDStdpRate();
	calculateTail();
	
  }
  public void calculatePoisson(){
  	bUsePoisson=true;
	int i,s=iData.length;
	double mean=0,num=0;
	for(i=0;i<s;++i){
		num+=iData[i];
		mean+=iData[i]*i;
	}
	dPoissonWeight=num;
	dPoissonlmdt=mean/num;
  }
  public void calculateBinomial(){
	bUseBinomial=true;
	int i,s=iData.length;
	double mean=0,num=0;
	for(i=0;i<s;++i){
		num+=iData[i];
		mean+=iData[i]*i;
		if(iData[i]>0)iBinomialN=i;
	}
	mean/=num;
	dBinormialP=((double)mean)/iBinomialN;
	iBinomialA=num;
  }
  
  public void calculateDStdpRate(){
	dP=0;
	dStd=0;
	dStdPRate=0; // calcualte std over probility

	dTotalNode=0;
	dTotalLink=0;
	dMean=0;
	int i,iNum;
	iNum=iData.length;
	for(i=0;i<iNum;++i){
		dTotalNode+=iData[i];
		dTotalLink+=i*iData[i];
	}
	dMean=dTotalLink/dTotalNode;
	dTotalLink/=2.0; // not double calcualte
	dP=dMean/(dTotalNode-1); // the possibility for a node to have a link with another node
	double dDiff=0;
	for(i=0;i<iNum;++i){
		dDiff=(dMean-i)*(dMean-i)*iData[i];
	}
	dDiff/=dTotalNode;
	dStd=Math.sqrt(dDiff);
	dStdPRate=dStd/dP;
	System.out.println("TotalNode:"+dTotalNode+"\nTotalLink:"+dTotalLink+"\nMean:"+dMean);
	System.out.println("Probability:"+dP+"\nSTD:"+dStd+"\nStdPRate:"+dStdPRate);

  }

  
  public void calculateTail(){
	dWeightTailRate=0;
	dTaillongRate=0;
	dTailNumRate=0;

	int i, iNum;
	int iHead,iMiddle,iTail;
	double dHeadpart,dTailpart;
	double dHeadNum,dTailNum;
	double dMax;
	iNum=iData.length;
	dMax=0;
	iMiddle=0;
	iTail=0;
	iHead=-1;
	for(i=0;i<iNum;++i){
		if((iHead==-1)&&(iData[i]>0.000000000001)){
			iHead=i;
		}
		if(iData[i]>dMax){
			dMax=iData[i];
			iMiddle=i;
		}
		if(iData[i]>0.000000000001){
			iTail=i;
		}
	}
	dHeadpart=0.0;
	dTailpart=0.0;
	dHeadNum=0.0;
	dTailNum=0.0;
	for(i=iHead;i<iMiddle;++i){
		dHeadpart=dHeadpart+iData[i]*(iMiddle-i);
		dHeadNum=dHeadNum+iData[i];
	}
	for(i=iMiddle+1;i<=iTail;++i){
		dTailpart=dTailpart+iData[i]*(i-iMiddle);
		dTailNum=dTailNum+iData[i];
	}
	dTaillongRate=((double)(iTail-iMiddle))/((double)(iMiddle-iHead));
	dTailNumRate=dTailNum/dHeadNum;
	dWeightTailRate=dTailpart/dHeadpart;
	
	System.out.println("TailLongRate:"+dTaillongRate+"\nTailNumRate:"+dTailNumRate+"\nWeightRate:"+dWeightTailRate);
  }
 
  public void calculateCPLE(){
  	int i,num,imax=0;
	if(!bCalCPLE) return;
/*
  	iOff=0;
  	num=iData.length;
  	for(i=0;i<num-1;++i){
  		if((iData[i]>iData[i+1])&&(i>1)&&(iData[i]>imax)){
  			iOff=i;
			imax=(int)iData[i];
  		}
  	}
  	num-=iOff;
  	double dt[];
  	dt=new double[num];
  	for(i=0;i<num;++i){
  		dt[i]=iData[i+iOff];
  	}
*/
   	cple=new ClassPowerLawEst(iData);
  	cple.calculate();
  }
  public void paint(Graphics g) {
    super.paint(g);
    mWidth=getBounds().width;
    mHeight=getBounds().height;
    if(mWidth<10) return;
    if(mHeight<10) return;
    ix0=mWidth/10;
    if(ix0>30) ix0=30;
    ix1=9*mWidth/10;
    if((mWidth-ix1)>20)ix1=mWidth-20;

    iy0=mHeight/10;
    if(iy0>10) iy0=10;
    iy1=(mHeight*9)/10;
    if((mHeight-iy1)>15) iy1=mHeight-15;
    drawAxis(g);
    drawCPLE(g);
    drawPoisson(g);
	drawBinomial(g);
  }
  void drawCPLE(Graphics g){
  	if(!bHasData) return;
	if(!bCalCPLE) return;

  	int x,y,x0=0,y0=0;
	int xx0=0,xx;
  	double dx,dy;
  	boolean bstartdraw=false;
  	g.setColor(Color.BLACK);
  	for(x=ix0;x<ix1;++x){
  		dx=pixelToX(x);
  		if(dx<iOff) continue;
  		dy=cple.getY(dx);
  		y=yToPixel(dy);
		if(bstartdraw)
			xx0=xToPixel(x0);
			xx=xToPixel(x);
			if((xx0>ix0)&&(xx>ix0))
				g.drawLine(xx0,y0,xx,y);

  		if(dy!=0){
  			bstartdraw=true;
  		}
		x0=x;
		y0=y;
  	}
  }
  void drawPoisson(Graphics g){
	if(!bUsePoisson)return;
	g.setColor(Color.black);
	Graphics2D g2d = (Graphics2D)g;
	Stroke oldStk;
	oldStk=g2d.getStroke();
	g2d.setStroke(getWideStroke());
	int i,idx,j,iVal;
	int iStep=iRange;
	int iFactorial=1;
	double dVal;
	idx=0;
	int ixb,ixe,iye;
	ixb=ix0+1;
	for(i=0;i<iStep;++i) {
	  dVal=(dPoissonWeight*Math.exp(-dPoissonlmdt)*Math.pow(dPoissonlmdt,i)/iFactorial);
	  iFactorial*=(i+1);
	  ixe=(ix1-ix0)*(i+1)/iStep+ix0;
	  iye=(int)(iy1-(dVal*(iy1-iy0)/iMaxY));
		g.drawLine(ixb,iye,ixe,iye);
	  ixb=ixe;
	}
  	g2d.setStroke(oldStk);
  }
  int getFactorial(int i){
  	if(i<=1)return 1;
  	return i*getFactorial(i-1);
  }
  int getC(int n,int m){
  	return getFactorial(n)/getFactorial(m)/getFactorial(n-m);
  }
  void drawBinomial(Graphics g){
	if(!bUseBinomial)return;
	g.setColor(Color.red);
	Graphics2D g2d = (Graphics2D)g;
	Stroke oldStk;
	oldStk=g2d.getStroke();
	g2d.setStroke(getWideStroke());
	int i,idx,j,iVal;
	int iStep=iRange;
	

	double dVal;
	idx=0;
	int ixb,ixe,iye;
	int c;
	ixb=ix0+1;
	for(i=0;i<=iBinomialN;++i) {
		c=getC(iBinomialN,i);
	  dVal=iBinomialA*c*Math.pow(dBinormialP,i)*Math.pow(1-dBinormialP,(iBinomialN-i));
	  ixe=(ix1-ix0)*(i+1)/iStep+ix0;
	  iye=(int)(iy1-(dVal*(iy1-iy0)/iMaxY));
		g.drawLine(ixb,iye,ixe,iye);
	  ixb=ixe;
	}
	g2d.setStroke(oldStk);
  	
  }
  void drawAxis(Graphics g){
    g.setColor(Color.black);
    g.drawLine(ix0,iy0,ix0,iy1);
    g.drawLine(ix0,iy1,ix1,iy1);
    int i,ixm,iym;
    for(i=1;i<=9;++i) {
      ixm=ix0+(ix1-ix0)*i/10;
      g.drawLine(ixm,iy1,ixm,iy1+3);
      iym=iy0+(iy1-iy0)*i/10;
      g.drawLine(ix0,iym,ix0-3,iym);
    }
    
    g.drawString("0",ix0-3,iy1+12);
    String sEndX=String.valueOf(iRange);
    String sEndY=String.valueOf(iMaxY);
    g.drawString(sEndX,ix1-sEndX.length()*3,iy1+12);
    g.drawString(sEndY,ix0-sEndY.length()*7,iy0+6);
	drawBars(g);
  }
  double pixelToX(int x){
  	double dx;
  	dx=x-ix0-1.0;
  	dx*=iRange;
  	dx/=(ix1-ix0);
  	return dx;
  }
  int yToPixel(double dy){
  	if(!bLog)
	return (int)(iy1-(dy*(iy1-iy0)/iMaxY));

	int yOff=0;
	double dMax=Math.log(iMaxY)+yOff;
	if(dy<=0) return iy1;
	double dVal=Math.log(dy)+yOff;
	if(dVal<0)dVal=0;

	return (int)(iy1-(iy1-iy0)*(dVal/dMax));
  }

	int xToPixel(int x){
		if(!bLog) return x;
		
		double dxm,dxv;
		
		dxm=Math.log(ix1);
		if(x<=1) return ix0;
		dxv=Math.log(x);
		return (int)(ix0+(ix1-ix0)*(dxv/dxm));
	}
  void drawBars(Graphics g){
    g.setColor(cBarColor);
    
    int i,idx,j;
    double dVal;
    int iStep=iRange/iSet;
    double iVal;
    idx=0;
    int ixb,ixe,iye;
    ixb=ix0+1;
	int iixb,iixe;
    for(i=0;i<iStep;++i) {
      iVal=0;
      for(j=0;j<iSet;++j){
        if(idx<iDataNum) iVal+=iData[idx++];
      }
      dVal=((double)iVal)/iSet;
      ixe=(ix1-ix0)*(i+1)/iStep+ix0;
      iye=yToPixel(dVal);
	  iixb=xToPixel(ixb);
	  iixe=xToPixel(ixe);

      switch(iDrawType){
	  case GBAR:
	      g.fillRect(iixb,iye,(iixe-iixb),(iy1-iye));
			break;
		case GCIRCLE:
		  g.drawOval(iixb-3,iye-3,7,7);
			break;
      }
      ixb=ixe;
    }
  }
  public String getCPLEString(){
  	String s;
	if(!bCalCPLE) return "";

  	s=cple.toString();
  	return s;
  }
}