package com.heart.service;

public class Card {

	String cardDeck;
	Integer cardValue;
	Integer cardSum;
	
	public Card(String cardDeck,Integer cardValue) { //카드와 카드 값을 먼저 입력받아서
		this.cardDeck = cardDeck;
		this.cardValue = cardValue;
	}
	
	
	public void cardTotalSum() {
		this.cardSum += cardValue;
	}
	
	public void showTotal() {
		System.out.println("카드 점수의 합 : " + cardSum);
	}
}
