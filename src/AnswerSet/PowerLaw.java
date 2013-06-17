package AnswerSet;

import java.util.Random;

public class PowerLaw {
	int m_n;
	int m_range[];
	int m_top;
	Random m_rdm;
	public PowerLaw(int n){
		m_n=n;
		m_range=new int[n];
		m_top=n;
		for(int i=0;i<n;++i)
			m_range[i]=i;
		m_rdm=new Random();
	}
	public int getNextValue(){
		int iSeed;
		iSeed=m_rdm.nextInt(m_top);
		
		int iTestBeg,iTestEnd;
		iTestBeg=0;
		iTestEnd=m_n-1;

		return tryTest(iTestBeg,iTestEnd,iSeed);
	}
	int tryTest(int iBeg,int iEnd, int iSeed){
		int iTest;
		if(iBeg==iEnd)return iBeg;
		if(iBeg>=(m_n-1)) return iBeg;

		if((iEnd-iBeg)==1) {
			if(iSeed<=m_range[iBeg]) return iBeg;
			return iEnd;
		}
		
		iTest=(iBeg+iEnd)/2;
		if(m_range[iTest]==iSeed) return iTest;
		if(iSeed>m_range[iTest])return tryTest(iTest,iEnd,iSeed);
		return tryTest(iBeg,iTest,iSeed);
	}
	public void confirmGet(int k){
		int i;
		m_top++;
		for(i=k;i<m_n;++i)
			m_range[i]++;
	}
}
