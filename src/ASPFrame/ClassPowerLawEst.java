/*
 * Created on 2003/6/3
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
package ASPFrame;

import java.text.DecimalFormat;

/**
 * @author larry
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
public class ClassPowerLawEst {
	double iData[];
	int iDataNum;
	double m_dTotal;
	int iCalPoint;
	static final double  DMIN=1,DMAX=100,DSTEP=0.000001;
	double dMin,dMax,dOffMin,dOffMax;
	double dGm,dOf;
	static final double  DOFFMIN=0,DOFFMAX=50;

	double dTest[];
	double dError;
	double dScale;
	int iStep=20;
	double dErrNet[][];
	double dErrNetNew[];
	public ClassPowerLawEst(double id[]){
		iData=id;
		iDataNum=iData.length;
		dMin=DMIN;
		dMax=DMAX;
		dOffMin=DOFFMIN;
		dOffMax=DOFFMAX;
		int i;
		double dcom=0;
		m_dTotal=0;
		iCalPoint=0;
		for(i=0;i<iDataNum;++i){
			m_dTotal+=iData[i];
			if(iData[i]>dcom){
				dcom=iData[i];
				iCalPoint=i;
				m_dTotal=iData[i];
			}
		}
		
		dTest=new double[iDataNum];
		dErrNet=new double[iStep+1][iStep+1];
		dErrNetNew=new double[iStep+1];
	}

	double calculateError(double dT,double dO){
		int i;
		double dSum;
		dSum=0.0;
		for(i=iCalPoint;i<iDataNum;++i){
			dTest[i]=getY(i, dT,dO,1);
			dSum+=dTest[i];
		}
		dScale=((double)m_dTotal)/dSum;
		dError=0.0;
		for(i=iCalPoint;i<iDataNum;++i){
			dTest[i]*=dScale;		
			dError+=(dTest[i]-iData[i])*(dTest[i]-iData[i]);
		}
		return dError;
	}



	double calculateErrorOld(double dT,double dO){
		int i;
		double dSum=0,dV;
		dV=dO;
		dT=-dT;
		for(i=0;i<iDataNum;++i){
			dTest[i]=Math.pow(dV,dT);
			dSum+=dTest[i];
			dV++;
		}
		dScale=((double)m_dTotal)/dSum;
		dError=0.0;
		for(i=0;i<iDataNum;++i){
			dTest[i]*=dScale;		
			dError+=(dTest[i]-iData[i])*(dTest[i]-iData[i]);
		}
		return dError;
	}
	void calculateErrorNet(){
		int i,j;
		double dST,dSO;
		dST=(dMax-dMin)/iStep;
		dSO=(dOffMax-dOffMin)/iStep;
		double dT=dMin,dO=dOffMin;
		double dErrorMin;
		double dNewOffMax,dNewOffMin,dNewMax,dNewMin;
		dNewOffMax=dOffMax;
		dNewOffMin=dOffMin;
		dNewMax=dMax;
		dNewMin=dMin;
		dErrorMin=calculateError(dT,dO)+1;
		for(i=0;i<=iStep;++i){
			dO=dOffMin;
			for(j=0;j<=iStep;++j){
				dErrNet[i][j]=calculateError(dT,dO);
				if(dErrNet[i][j]<dErrorMin){
					dErrorMin=dErrNet[i][j];
					dGm=dT;
					dOf=dO;
					dNewMin=dGm-dST;
					dNewMax=dGm+dST;
					dNewOffMin=dOf-dSO;
					dNewOffMax=dOf+dSO;
					if(dNewOffMax>DOFFMAX) dNewOffMax=DOFFMAX;
					if(dNewMax>DMAX)dNewMax=DMAX;
					if(dNewOffMin<DOFFMIN) dNewOffMin=DOFFMIN;
					if(dNewMin<DMIN)dNewMin=DMIN;
				}
				dO+=dSO;
			}
			dT+=dST;
		}
		dOffMax=dNewOffMax;
		dOffMin=dNewOffMin;
		dMax=dNewMax;
		dMin=dNewMin;

	}

	void calculateErrorNetNew(){
		int i;
		double dST;
		dST=(dMax-dMin)/iStep;
		double dT=dMin;
		double dErrorMin;
		double dNewMax,dNewMin;
		dNewMax=dMax;
		dNewMin=dMin;
		dErrorMin=calculateError(dT,0)+1;
		for(i=0;i<=iStep;++i){
			dErrNetNew[i]=calculateError(dT,0);
			if(dErrNetNew[i]<dErrorMin){
				dErrorMin=dErrNetNew[i];
				dGm=dT;
				dNewMin=dGm-dST;
				dNewMax=dGm+dST;
				if(dNewMax>DMAX)dNewMax=DMAX;
				if(dNewMin<DMIN)dNewMin=DMIN;
			}

			dT+=dST;
		}
		dMax=dNewMax;
		dMin=dNewMin;

	}
	
	
	
	
	void calculate(){
/*	get rid of the offset	
		while(((dMax-dMin)>DSTEP)||((dOffMax-dOffMin)>DSTEP)){
			calculateErrorNet();
		}
*/		
		while((dMax-dMin)>DSTEP){
			calculateErrorNetNew();
		}
		calculateError(dGm,dOf);
	}
	double getY(double dx){
		return getY(dx,dGm,dOf,dScale);
	}
	double getY(double dx, double gam,double off, double scale){
		if(dx<iCalPoint) return 0.0;

/*    // to get rid of the offset		
		dx-=iCalPoint;
		dx+=off;
*/
		
		if(dx<=0.0000001) return 0;
		return scale*Math.pow(dx,-gam);
	}
	public String toString(){
		DecimalFormat df = new DecimalFormat("###.##");
		DecimalFormat df2=new DecimalFormat("0.###E0" );
		return "Sp "+iCalPoint+", Gm: "+df.format(dGm)+ ", off: "+df.format(dOf)+", scale: "+df2.format(dScale)+", error: "+df.format(dError)+", ";
	}
}
