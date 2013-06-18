package AnswerSet;


import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;

public class LinerTest {

	Apfloat factres[];
	
	Apfloat calCulatePm(double p, int n, int m){
		Apfloat q=new Apfloat(1-p);
		Apfloat Pb= ApfloatMath.pow(q, m*(m-1));
		Apfloat Pa= ApfloatMath.pow(q,m);
		Apfloat one=new Apfloat(1);
		Pa=one.subtract(Pa);
		Pa=ApfloatMath.pow(Pa,n-m);
		Pa=Pa.multiply(Pb);
		return Pa;
	}
	Apfloat calFactory(int n){
		if(n==0)return new Apfloat(1);
		Apfloat npart=new Apfloat(n); // without the exp part
		npart=ApfloatMath.pow(npart, n);
		Apfloat pipart=new Apfloat(2*n*Math.PI);
		pipart=ApfloatMath.pow(pipart, new Apfloat(0.5));
		Apfloat ret=pipart.multiply(npart);
		return ret;
	}

	void initFacotryTable(int n){
		factres=new Apfloat[n];
		Apfloat res=new Apfloat(1),inc;
		for(int i=0;i<n;++i){
			factres[i]=res;
			inc=new Apfloat(i+1);
			res=inc.multiply(res);
		}
	}
	Apfloat calFactorya(int i){
		String str;
		str=factres[i].toString();
		return new Apfloat(str);
	}
	
	
	Apfloat calCulateNm(int n, int m){
		Apfloat an,am,anm;
		an=calFactorya(n);
		an.doubleValue();
		am=calFactorya(m);
		am.doubleValue();

		anm=calFactorya(n-m);
		anm.doubleValue();
	
		an=an.divide(am);
		an.doubleValue();

		an=an.divide(anm);
		an.doubleValue();
		return an;
		
	}
	void simpleLinerMode(int n, int k){
		double dn,dk;
		dn=n;dk=k;
		double p=dk/dn;

		initFacotryTable(n+1);
		
		
		Apfloat Pms[], Nms[], Pnoms[];

		Pms=new Apfloat[n];
		Nms=new Apfloat[n];
		Pnoms=new Apfloat[n]; // the possibility no answer sets with m support atoms
		
		Apfloat pno=new Apfloat(1);
		for(int i=0;i<n;++i){
			Pms[i]=calCulatePm(p,n,i);
			Nms[i]=calCulateNm(n,i);
			Pnoms[i]=new Apfloat(1);
			Pnoms[i]=Pnoms[i].subtract(Pms[i]);
			Pnoms[i]=ApfloatMath.pow(Pnoms[i],Nms[i]);
			pno=pno.multiply(Pnoms[i]);
			System.out.println(i);
		}
		
		
		double tmp=pno.doubleValue();
		tmp=1-tmp;
		
		
		
	}
	
	double simpleTest(String inp){
		Apfloat x=new Apfloat(inp);
		Apfloat d=new Apfloat(-1);
		d=d.divide(x);
		d=d.add(new Apfloat(1));
		Apfloat res;

		d.doubleValue();
		x.doubleValue();
		res=ApfloatMath.pow(d,x);
		return res.doubleValue();
		
	}
}
