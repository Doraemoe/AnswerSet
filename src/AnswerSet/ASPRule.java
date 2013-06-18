package AnswerSet;

import java.util.Random;

public class ASPRule {
	int iHead;

	Random rdm;

	int iPositiveGroup[],iNegtiveGroup[];
	int iPositiveNum,iNegtiveNum;
	
	boolean bUsePowerLaw;
	PowerLaw pLaw;
	
	public ASPRule(){
		rdm=new Random();
		bUsePowerLaw=false;
		pLaw=null;
	}
	
	public void setPowerLaw(PowerLaw p){
		bUsePowerLaw=true;
		pLaw=p;
	}
	public void acceptRule(){
		if(!bUsePowerLaw)return; // should not be here
		pLaw.confirmGet(iHead);

		for(int i=0; i<iPositiveNum; ++i) {
			pLaw.confirmGet(iPositiveGroup[i]);
		}
		for(int i=0; i<iNegtiveNum; ++i) {
			pLaw.confirmGet(iNegtiveGroup[i]);
		}
	}
	public void RandomGenerate(int k, int n, int iType, boolean bNoRepeat){// k is the number of literals in the rule. n is the number of atoms.
		int iRemainNum;

		iPositiveGroup=new int[k-1];
		iNegtiveGroup=new int[k-1];
		iPositiveNum=0;
		iNegtiveNum=0;
		
		if(bUsePowerLaw)
			iHead=pLaw.getNextValue();
		else
			iHead=rdm.nextInt(n);
		iRemainNum=k-1;
		boolean bSelectPositive=true;
		int bNextInt;
		while(iRemainNum>0) {
			bSelectPositive=rdm.nextBoolean();
			
			if(iType==1)bSelectPositive=false; // negative
			if(iType==2)bSelectPositive=true; // positive

			if(bUsePowerLaw)
				bNextInt=pLaw.getNextValue();
			else
				bNextInt=rdm.nextInt(n);
			if(tryInsertNum(bSelectPositive, bNextInt, n, bNoRepeat)) iRemainNum--;
		}
	}
	private boolean tryInsertNum(boolean pIsPositive, int s, int n, boolean bNoRepeat){
		int i;
		if(bNoRepeat){
			if(s==iHead) return false;
			for(i=0;i<iPositiveNum;++i){
				if(s==iPositiveGroup[i]) return false; // already selected
			}
			for(i=0;i<iNegtiveNum;++i){
				if(s==iNegtiveGroup[i]) return false; // already selected
			}
		}
		if(pIsPositive){
			for(i=0;i<iPositiveNum;++i){
				if(s==iPositiveGroup[i]) return false; // already selected
				if(s<iPositiveGroup[i]){
					for(int j=iPositiveNum;j>i;--j) {
						iPositiveGroup[j]=iPositiveGroup[j-1];
					}
					iPositiveGroup[i]=s;
					iPositiveNum++;
					return true;
				}
			}
			iPositiveGroup[iPositiveNum]=s;
			iPositiveNum++;
			return true;
		}
		for(i=0;i<iNegtiveNum;++i){
			if(s==iNegtiveGroup[i]) return false; // already selected
			if(s<iNegtiveGroup[i]){
				for(int j=iNegtiveNum;j>i;--j) {
					iNegtiveGroup[j]=iNegtiveGroup[j-1];
				}
				iNegtiveGroup[i]=s;
				iNegtiveNum++;
				return true;
			}
		}
		iNegtiveGroup[iNegtiveNum]=s;
		iNegtiveNum++;
		return true;
	}
	
	public String toString(){
		String sRet;
		sRet = getLiterialString(iHead, true)+" :- ";
		for(int i=0; i<iPositiveNum;++i) {
			if((iNegtiveNum==0)&&(i==iPositiveNum-1))
				sRet+=getLiterialString(iPositiveGroup[i],true);
			else
				sRet+=getLiterialString(iPositiveGroup[i],true)+", ";
		}
		for(int i=0; i<iNegtiveNum;++i) {
			if(i==iNegtiveNum-1)
				sRet+=getLiterialString(iNegtiveGroup[i],false);
			else
				sRet+=getLiterialString(iNegtiveGroup[i],false)+", ";
		}
		sRet+=".";
		return sRet;
	}
	private String getLiterialString(int s, boolean bPositive) {
		if(bPositive) return "p_"+s;
		return "not p_"+s;
	}
	public boolean equals(Object o){

			if (o instanceof ASPRule) { // compile-time error
				return toString().equals(o.toString());
			}
			return false;
	}
}
