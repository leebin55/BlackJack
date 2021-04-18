package com.heart.game;

import java.util.ArrayList;
import java.util.List;

import com.heart.impl.BlackJackRuleImplV1;
import com.heart.model.DeckVO;
import com.heart.service.BlackjackRule;

public class Ex_01 {
	public static void main(String[] args) {

		BlackjackRule ex = new BlackJackRuleImplV1();

		List<DeckVO> playerList = new ArrayList<DeckVO>();
		List<DeckVO> dealerList = new ArrayList<DeckVO>();

		//1차 배분
		ex.shuffleDeck();
		ex.handDeck(playerList);
		ex.handDeck(playerList);
		
		int nSizeP = playerList.size();

		for (int i = 0; i < nSizeP; i++) {
			DeckVO vo = playerList.get(i);

			System.out.print(vo.getDeck() + " : ");
			System.out.println(vo.getValue());
		}
		System.out.println("------------------");

		ex.handDeck(dealerList);
		ex.handDeck(dealerList);

		int nSizeD = dealerList.size();

		for (int i = 0; i < nSizeD; i++) {
			DeckVO vo = dealerList.get(i);

			System.out.print(vo.getDeck() + " : ");
			System.out.println(vo.getValue());
		}
		System.out.println("=================");
		
		//2차 배분
		ex.shuffleDeck();
		ex.handDeck(playerList);
		ex.handDeck(playerList);
		
		nSizeP = playerList.size();

		for (int i = 0; i < nSizeP; i++) {
			DeckVO vo = playerList.get(i);

			System.out.print(vo.getDeck() + " : ");
			System.out.println(vo.getValue());
		}
		System.out.println("------------------");

		ex.handDeck(dealerList);
		ex.handDeck(dealerList);

		nSizeD = dealerList.size();

		for (int i = 0; i < nSizeD; i++) {
			DeckVO vo = dealerList.get(i);

			System.out.print(vo.getDeck() + " : ");
			System.out.println(vo.getValue());
		}
		System.out.println("=================");
				
		
		//2차 배분
		ex.shuffleDeck();
		ex.handDeck(playerList);
		ex.handDeck(playerList);
		
		nSizeP = playerList.size();

		for (int i = 0; i < nSizeP; i++) {
			DeckVO vo = playerList.get(i);

			System.out.print(vo.getDeck() + " : ");
			System.out.println(vo.getValue());
		}
		System.out.println("------------------");

		ex.handDeck(dealerList);
		ex.handDeck(dealerList);

		nSizeD = dealerList.size();

		for (int i = 0; i < nSizeD; i++) {
			DeckVO vo = dealerList.get(i);

			System.out.print(vo.getDeck() + " : ");
			System.out.println(vo.getValue());
		}
		System.out.println("=================");		
		
		
		
		
		
		
		
		
	}
}
