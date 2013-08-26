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
	int m_c; // number of constraints;
	int remainRules;
	int remainConstraints;
	HashSet<ArrayList<Integer>> ruleSet = new HashSet<ArrayList<Integer>>(); //set contains atoms in each rule.
	HashSet<ArrayList<Integer>> constraintSet = new HashSet<ArrayList<Integer>>(); //set contains constraint rule.
	
	Random rdm;	
	
	public FastNegationThreeRuleGenerator(int n, int l, int c){
		rdm=new Random();
		m_n = n;
		m_l = l;
		m_c = c;
		remainRules = l;
		remainConstraints = c;
		RandomGenerate();
	}
	
	
	public void RandomGenerate(){// k is the number of literals in the rule. n is the number of atoms.		
		while (remainConstraints > 0) {
			//iHead = rdm.nextInt(m_n);
			body1 = rdm.nextInt(m_n);
			do {
				body2 = rdm.nextInt(m_n);
			} while (body2 == body1);
			
			if (!tryInsertConstraint()) {
				++remainConstraints;
			}
			--remainConstraints;
		}
		
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
	
	private boolean tryInsertConstraint() {
		ArrayList<Integer> constraintAtom = new ArrayList<Integer>();
		if (body1 < body2) {
			constraintAtom.add(Integer.valueOf(body1));
			constraintAtom.add(Integer.valueOf(body2));
		}
		else {
			constraintAtom.add(Integer.valueOf(body2));
			constraintAtom.add(Integer.valueOf(body1));
		}
		
		if(constraintSet.add(constraintAtom)) {
			return true;
		}
		else {
			return false;
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
	
	String getConstraintString(int bodyOne, int bodyTwo) {
		return ":- not p_" + bodyOne + ", not p_" + bodyTwo + ".\r\n";
	}
	
	public String getRules(){
		StringBuffer sbRet=new StringBuffer();
		
		//TreeSet<ArrayList<Integer>> sortedRuleSet = new TreeSet<ArrayList<Integer>>(ruleSet);
		//sortedRuleSet.comparator();
		
		Iterator<ArrayList<Integer>> ruleIt = ruleSet.iterator();
		
		//Iterator<ArrayList<Integer>> ruleIt = sortedRuleSet.iterator();
		
		while (ruleIt.hasNext()) {
			ArrayList<Integer> rule = ruleIt.next();
			sbRet.append(getRuleString(rule.get(0), rule.get(1), rule.get(2)));
		}
		
		//TreeSet<ArrayList<Integer>> sortedConstraintSet = new TreeSet<ArrayList<Integer>>(constraintSet);
		//sortedConstraintSet.comparator();
		Iterator<ArrayList<Integer>> constraintIt = constraintSet.iterator();

		while (constraintIt.hasNext()) {
			ArrayList<Integer> constraint = constraintIt.next();
			sbRet.append(getConstraintString(constraint.get(0), constraint.get(1)));
		}
		
		return sbRet.toString();
	}
	

}
