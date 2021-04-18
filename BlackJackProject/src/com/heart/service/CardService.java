package com.heart.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.heart.model.DeckVO;

public class CardService {
	List<DeckVO> deckList;
	String[] suit;
	String[] strNum;
	private int deckIndex = 0 ; // deckList에서 하나씩 차례대로 선택할수있도록 참조하는 인덱스값
	public CardService() {
		deckList= new ArrayList<DeckVO>();
		suit =  new String[] {"Heart","Diamond","Clover","Space"};
		strNum = new String[] {"A","2","3","4","5","6","7","8","9","10","K","Q","J"};
	}
	
	public void createDeck(){ 
		//TODO 카드형식을 만들고 각 카드에 따른 값을 만듬
		for(int i = 0; i < suit.length ; i++) {
			for(int j = 0 ; j < strNum.length ; j++) {
				String deck = suit[i] + " "  + strNum[j];
				//Heart A Diamond 2 형식으로
				DeckVO deckVO = new DeckVO();
				deckVO.setDeck(deck);
				deckList.add(deckVO); //deckList에 저장
				try {//strNum 을 Integer형으로 바꿈
					//오류가 없으면 strNum 에 담긴 값은 숫자값 >> 숫가 그대로 
					//deckVO클래스 의 value값에 담고 deckList에 저장
					Integer intNum = Integer.valueOf(strNum[j]);
					deckVO.setValue(intNum);
					deckList.add(deckVO);
				} catch (NumberFormatException e) {
					// 만약 Integer 바꾸는데 오류가 났으면 숫자가 아닌 문자형이담긴 카드
					// 만약 A라면 value 1을 그외의 문자는 10을 deckVO클래스에 담고 List 에 저장
					if(strNum[j].equals("A")) {
						Integer intNum = 1;
						deckVO.setValue(intNum);
						deckList.add(deckVO);
					}else {
						Integer intNum = 10;
						deckVO.setValue(intNum);
						deckList.add(deckVO);
					}
				}
			}
		}
		
	}
	
	
	public void shuffleDeck() {
		// TODO 조아영
		/*
		 * 생성된 덱을 셔플하는 메소드 셔플 후 리스트 deckList에 저장됨 deckIndex 값을 참조하여 deckList에서 차례대로 덱을
		 * 하나씩 뽑아내면 deckList는 이미 셔플된 덱의 리스트이기 때문에 랜덤으로 덱을 뽑을 수 있음과 동시에 중복성검사도 할 필요가 없다.
		 */

		Collections.shuffle(deckList); // deckList의 덱을 셔플하여 다시 deckList에 저장

	}

	
	public void handDeck(List<DeckVO> list) {
		// TODO 조아영
		/*
		 * 딜러와 플레이어의 카드 리스트를 매개변수로 받아서 deckList에서 카드정보(카드이름,점수)를 0번부터 차례대로 배분하여 나눠주는 메소드
		 */

		DeckVO vo = deckList.get(deckIndex); // 셔플된 dexkList에서 deckIndex번째 값을 호출하여 vo에 저장

		vo.getDeck();
		vo.getValue();

		deckIndex++; // deckIndex 값은 handDeck이 실행될때마다 증가

		list.add(vo); // 매개변수로 받아온 list에 뽑은 카드를 추가

	}

}
