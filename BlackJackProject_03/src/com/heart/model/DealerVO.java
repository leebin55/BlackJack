package com.heart.model;

public class DealerVO {
	String deck; //카드
	Integer value; // 가지고 있는 카드의 각각의 값
	Integer total; // 총 카드의 합 
	
	
	public String getDeck() {
		return deck;
	}
	public void setDeck(String deck) {
		this.deck = deck;
	}
	public Integer getValue() {
		return value;
	}
	public void setValue(Integer value) {
		this.value = value;
	}
	public Integer getTotal() {
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	  
	
	

}
