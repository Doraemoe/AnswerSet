package AnswerSet;

import java.util.Random;
import java.util.Vector;

public class FastNegationThreeRuleGenerator {
	int m_n; // number of items
	int m_l; // number of rules

	Vector <Integer> vPool,vIndPool;
	
	public FastNegationThreeRuleGenerator(int n, int l){
		m_n=n;
		m_l=l;
		initPool();
	}
	
	void initPool(){
		vPool = new Vector<Integer>();
		vIndPool = new Vector<Integer>();
		int len = m_n * (m_n);
		int i;
		
		for(i=0;i<len;++i){
			vPool.add(new Integer(i));
		}
		
		int irand;
		Random rdm = new Random();
		
		while(len > m_l) {
			irand = rdm.nextInt(len);
			vPool.remove(irand);
			len--;
		}
	}
	
	String getRuleStringFromIdx(int idx){
		int iHead,iTail;
		iTail = idx % m_n;
		iHead = (idx-iTail) / m_n;
		return "p_" + iHead + " :- not p_" + iTail + ".\r\n";
	}
	
	public String getRules(){
		StringBuffer sbRet=new StringBuffer();
		Integer itg;
		for(int i=0;i<m_l;++i){
			itg=vPool.get(i);
			sbRet.append(getRuleStringFromIdx(itg.intValue()));
		}
		
		return sbRet.toString();
	}
}
