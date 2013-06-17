package AnswerSet;

import java.util.Random;
import java.util.Vector;

public class FastNegationTwoRuleGenerator {
	int m_n; // number of items
	int m_l; // number of rules
	boolean b_IndependentRepeatLiteral;
	int m_IndependentRepeatLiteralNum;
	
	boolean m_bNoRepeat;
	Vector <Integer> vPool,vIndPool;
	
	public FastNegationTwoRuleGenerator(int n, int l, boolean bNoRepeat, boolean indrep, int indrepnum){
		m_n=n;
		m_l=l;
		m_bNoRepeat=bNoRepeat;
		b_IndependentRepeatLiteral=indrep;
		m_IndependentRepeatLiteralNum=indrepnum;
		initPool();
	}
	void initPool(){
		vPool=new Vector<Integer>();
		vIndPool=new Vector<Integer>();
		int len=m_n*(m_n);
		int i;
		
		for(i=0;i<len;++i){
			vPool.add(new Integer(i));
		}
		int idx;
		if(b_IndependentRepeatLiteral){
			for(i=0;i<m_n;++i){
				idx=(i)*m_n+i;
				vIndPool.add(new Integer(idx));
			}
			m_l-=m_IndependentRepeatLiteralNum;
		}

		if(m_bNoRepeat||b_IndependentRepeatLiteral) {
			for(i=m_n;i>0;--i){
				idx=(i-1)*m_n+i-1; // remove repeat head and tail rules
				vPool.remove(idx);
				len--;
			}
		}
		int irand;
		Random rdm=new Random();
		while(len>m_l){
			irand=rdm.nextInt(len);
			vPool.remove(irand);
			len--;
		}
		if(b_IndependentRepeatLiteral){
			len=m_n;
			while(len>m_IndependentRepeatLiteralNum){
				irand=rdm.nextInt(len);
				vIndPool.remove(irand);
				len--;
				
			}
		}
	}
	String getRuleStringFromIdx(int idx){
		int iHead,iTail;
		iTail=idx%m_n;
		iHead=(idx-iTail)/m_n;
		return "p_"+iHead+ " :- not p_"+iTail+".\r\n";
	}
	public String getRules(){
		StringBuffer sbRet=new StringBuffer();
		Integer itg;
		for(int i=0;i<m_l;++i){
			itg=vPool.get(i);
			sbRet.append(getRuleStringFromIdx(itg.intValue()));
		}
		if(b_IndependentRepeatLiteral){
			for(int i=0;i<m_IndependentRepeatLiteralNum;++i){
				itg=vIndPool.get(i);
				sbRet.append(getRuleStringFromIdx(itg.intValue()));
			}
			
		}
		
		return sbRet.toString();
	}
}
