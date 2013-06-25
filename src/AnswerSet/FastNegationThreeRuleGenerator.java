package AnswerSet;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

public class FastNegationThreeRuleGenerator {
	int iHead;
	int body1, body2;
	int m_n; // number of items
	int m_l; // number of rules
	int remainRules;
	HashSet<ArrayList<Integer>> ruleSet = new HashSet<ArrayList<Integer>>(); //set contain atoms in each rule.
	
	Random rdm;	
	
	public FastNegationThreeRuleGenerator(int n, int l){
		rdm=new Random();
		m_n = n;
		m_l = l;
		remainRules = l;
		RandomGenerate();
	}
	
	
	public void RandomGenerate(){// k is the number of literals in the rule. n is the number of atoms.		
		while (remainRules > 0) {
			
			iHead = rdm.nextInt(m_n);		
			do {
				body1 = rdm.nextInt(m_n);
			} while (body1 == iHead);
			
			do {
				body2 = rdm.nextInt(m_n);
			} while (body2 == iHead || body2 == body1);
			
			if (!tryInsertRule()) {
				++remainRules;
			}
			--remainRules;
		}
	}
	
	private boolean tryInsertRule() {
		ArrayList<Integer> ruleAtom = new ArrayList<Integer>();
		if (body1 < body2) {
			ruleAtom.add(Integer.valueOf(iHead));
			ruleAtom.add(Integer.valueOf(body1));
			ruleAtom.add(Integer.valueOf(body2));
		}
		else {
			ruleAtom.add(Integer.valueOf(iHead));
			ruleAtom.add(Integer.valueOf(body2));
			ruleAtom.add(Integer.valueOf(body1));
		}
		
		if(ruleSet.add(ruleAtom)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	
	String getRuleString(int head, int bodyOne, int bodyTwo){
		return "p_" + head + " :- not p_" + bodyOne + ", not p_" + bodyTwo + ".\r\n";
	}
	
	public String getRules(){
		StringBuffer sbRet=new StringBuffer();
		
		Iterator<ArrayList<Integer>> ruleIt = ruleSet.iterator();
		
		while (ruleIt.hasNext()) {
			ArrayList<Integer> rule = ruleIt.next();
			sbRet.append(getRuleString(rule.get(0), rule.get(1), rule.get(2)));
		}

		
		return sbRet.toString();
	}
	

}
