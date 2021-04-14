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
		//TODO Heart A형태의 카드를 만들고 값을 만듬
		for(int i = 0; i < suit.length ; i++) {
			for(int j = 0 ; j < strNum.length ; j++) {
				String deck = suit[i] + " "  + strNum[j];
				DeckVO deckVO = new DeckVO();
				deckVO.setDeck(deck);
				deckList.add(deckVO); //deckList에 저장
				try {//strNum 을 Integer형으로 바꿈
					//오류가 없으면 strNum 에 담긴 값은 숫자값 >> 숫가 그대로 
					//deckList 의 value값에 담음
					Integer intNum = Integer.valueOf(strNum[j]);
					//System.out.println(strNum[j]);
					deckVO.setValue(intNum);
					deckList.add(deckVO);
				} catch (NumberFormatException e) {
					// 만약 Integer 바꾸는데 오류가 났으면 숫자가 아닌 문자형이담긴 카드
					// 만약 A라면 value 1을 그외의 문자는 10을 deckList에 담음 
					if(strNum[j].equals("A")) {
						Integer intNum = 1;
						deckVO.setValue(intNum);
						//System.out.println(intNum);
						deckList.add(deckVO);
					}else {
						Integer intNum = 10;
						deckVO.setValue(intNum);
						//System.out.println(intNum);
						deckList.add(deckVO);
					}
					
				}
			}
		}
		
	}
	
	
		
	
	public void testPrint() { //테스트용
		for(int i = 0; i < deckList.size(); i++) {
			
			System.out.println(deckList.get(i).getDeck());
			System.out.println(deckList.get(i).getValue());
			System.out.println("=======================");
			}
	}
	public void shufflDeck() {
		
	}
}

