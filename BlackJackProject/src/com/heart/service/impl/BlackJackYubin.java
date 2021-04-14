package com.heart.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.heart.model.DeckVO;

public class BlackJackYubin {
	List<DeckVO> deckList;
	String[] suit;
	String[] strNum;
	public BlackJackYubin() {
		deckList= new ArrayList<DeckVO>();
		suit =  new String[] {"Heart","Diamond","Clover","Space"};
		strNum = new String[] {"A","2","3","4","5","6","7","8","9","10","K","Q","J"};
	}
	
	public void createDeck(){ 
		//Heart A 형태의  카드만들기 
		for(int i = 0; i < suit.length ; i++) {
			for(int j = 0 ; j < strNum.length ; j++) {
				String deck = suit[i] + " "  + strNum[j];
				DeckVO deckVO = new DeckVO();
				deckVO.setDeck(deck);
				deckList.add(deckVO);
			}
		}
		
	}
	public void createValue() {
		//num[]에 담긴 값에 따라 값을 정함 
		for(int i = 0 ; i < suit.length ; i++) {//4번반복
			for (int j = 0; j <strNum.length; j++) {//13번반복
				DeckVO deckVO = new DeckVO();
				try {//strNum 을 Integer형으로 바꿈
					//오류가 없으면 strNum 에 담긴 값은 숫자값 >> 숫가 그대로 
					//deckList 의 value값에 담음
					Integer intNum = Integer.valueOf(strNum[j]);
					deckVO.setValue(intNum);
					deckList.add(deckVO);
				} catch (NumberFormatException e) {
					// 만약 Integer 바꾸는데 오류가 났으면 숫자가 아닌 문자형이담긴 카드
					// 만약 A라면 value 1을 그외의 문자는 10을 deckList에 담음 
					if(strNum[j].equals("A")) {
						deckVO.setValue(1);
						deckList.add(deckVO);
					}else {
						deckVO.setValue(10);
						deckList.add(deckVO);
					}
					
				}
			}
		}
	}
	public void testPrint() {
		for(int i = 0; i < 52; i++) {
			
			System.out.println(deckList.get(i).getDeck());
			System.out.println(deckList.get(i).getValue());
			System.out.println("=======================");
			}
	}
}

