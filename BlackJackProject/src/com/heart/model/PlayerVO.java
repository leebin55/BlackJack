package com.heart.model;

public class PlayerVO {
	
	String name; 
	String deck;
	Integer value; 
	Integer total; // 각 카드들의 총 합
	int money; // 현재 플레이어가 가지고 있는 돈
	int bet; // 베팅하는 금액
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
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
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
	}
	public int getBet() {
		return bet;
	}
	public void setBet(int bet) {
		this.bet = bet;
	}
	@Override
	public String toString() {
		return name +" 님은" +  ", deck=" + deck + ", value=" + value + ", total=" + total + ", money="
				+ money + ", bet=" + bet + "]";
	}
	
	
	
	
	
}
