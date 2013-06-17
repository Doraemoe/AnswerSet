package AnswerSet;
/*
n * q^(m0-1)=m0
n * q^m1=m1

q=((n*(n-1))-l)/(n*(n-1))

Liner model.

m_l=m_k*m_n;
m_alpha=m_n/m1

*/
public class CalculateM {
	double m_n;
	double m_l;
	double m_q;
	public CalculateM(double n, double l){
		setNL(n,l);
	}
	public void setNL(double n, double l){
		m_n=n;
		m_l=l;
		m_q=(m_n*(m_n-1)-m_l)/(m_n*(m_n-1));
	}
	public double calculateM1(){
		return calculateM1(0.0,m_n);
	}
	public double calculateM0(){
		return calculateM0(0.0,m_n);
	}
	
	double calculateM1(double plow, double phigh){
		double pmid;
		pmid=(phigh+plow)/2;
		if((phigh-plow)<0.00001) return pmid;
		if(calculateValM(pmid)>0) return calculateM1(pmid,phigh);
		return calculateM1(plow,pmid);
	}
	
	double calculateValM(double pinp){
		return m_n*Math.pow(m_q, pinp)-pinp;
	}
	

	double calculateM0(double plow, double phigh){
		double pmid;
		pmid=(phigh+plow)/2;
		if((phigh-plow)<0.00001) return pmid;
		if(calculateValM0(pmid)>0) return calculateM0(pmid,phigh);
		return calculateM0(plow,pmid);
	}
	
	double calculateValM0(double pinp){
		return m_n*Math.pow(m_q, pinp-1)-pinp;
	}

	public void calLinerModel(double maxK, int step){ // maxK is the maximum of k step is how many want to calculate
		
		double k;
		double k_step;
		double dn,dl;
		double dm,dalpha,dEas, dPras, dm0,Em0,Taosq;
		k_step=maxK/step;
		
		int i;
		k=k_step;
		dn=300;
		for(i=0;i<step;++i){
			dl=dn*k;
			setNL(dn,dl);
			dm=this.calculateM0();
			dalpha=dn/dm;

			dEas=dalpha*dalpha/Math.sqrt(k*k*(dalpha-1)+dalpha*(2*k+dalpha));
			dPras=1-Math.exp(-dEas);
			dm0=dm;
			Em0=dalpha*dalpha/Math.sqrt(2*3.1415926*(dalpha-1)*dn);
			Taosq=(dalpha-1)*dn/(k*k*(dalpha-1)+dalpha*(2*k+dalpha));
			System.out.println(""+k+","+dalpha+","+dEas+","+dPras+","+dm0+","+Em0+","+Taosq);
			k+=k_step;
		}
		
	}


}
