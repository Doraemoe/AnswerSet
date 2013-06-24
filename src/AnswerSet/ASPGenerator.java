package AnswerSet;

import java.util.HashSet;
import java.util.Set;

import data.TestData;

public class ASPGenerator {
	Set<ASPRule> sRules;
	
	public ASPGenerator(){
		sRules=new HashSet<ASPRule>();
	}
	public String RandomGenerator(int k, int n,int l,int iType, boolean noRepeatLiteral, boolean bUsePowerLaw){//k: number of literals in a rule. n: number of atoms. l, number of rules 
		sRules.clear();
		ASPRule rule;

		PowerLaw pLaw=null;
		if(bUsePowerLaw)
			pLaw=new PowerLaw(n);
		
		for(int i=0;i<l;++i){
			rule=new ASPRule();
			if(bUsePowerLaw)
				rule.setPowerLaw(pLaw);
			do {
				rule.RandomGenerate(k, n,iType,noRepeatLiteral);
			}while (sRules.contains(rule));
			sRules.add(rule);
			if(bUsePowerLaw)
				rule.acceptRule();
		}
		String sRet="";
		Object arrRules[];
		arrRules= sRules.toArray();
		for (int i=0;i<l;++i){
			Object o=arrRules[i];
			sRet+=o.toString()+"\r\n";
		}
		return sRet;
	}
	
	public String RandomGenerator(TestData dt, int litNum){
		int p_k,p_n,p_l;
		p_k=dt.getLiteralNum();
		p_n=dt.getAtomNum();
		p_l=dt.getRulesNum();
		int iType;
		boolean noRepeatLiteral;
		boolean independentRepeatLiteral;
		int independentRepeatLiteralNum;

		independentRepeatLiteral=dt.isIndependentRepeatLiteral();
		independentRepeatLiteralNum=dt.getIndependentRepeatLiteral();
		
		String strClassType;
		strClassType=dt.getDataClass();

		boolean usePowerLaw;
		usePowerLaw=dt.usePowerLaw();
		
		iType=0; // random
		if(strClassType.equals("mR-"))
			iType=1; // Negative only
		if(strClassType.equals("mR+"))
			iType=2; // positive only
		
		noRepeatLiteral=dt.isNoRepeatLiteral();
		if(usePowerLaw||(iType!=1)||(p_k!=2)){
			ASP.showMessageBox("Using fast mode. Only support no powerlaw type R- k=2");
			return "";
		}
		if(litNum == 2) {
			FastNegationTwoRuleGenerator fnt;
			fnt=new FastNegationTwoRuleGenerator(p_n,p_l,noRepeatLiteral,independentRepeatLiteral,independentRepeatLiteralNum);
			return fnt.getRules();
		}
		else {
			FastNegationThreeRuleGenerator fnt;
			fnt=new FastNegationThreeRuleGenerator(p_n,p_l);	
			return fnt.getRules();
		}
		
		
//		return RandomGenerator(p_k,p_n,p_l,iType,noRepeatLiteral,usePowerLaw);
	}
}
