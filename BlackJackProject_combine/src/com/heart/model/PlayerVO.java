package com.heart.model;

import java.util.ArrayList;
import java.util.List;

public class PlayerVO {
	
	private String name; // 이름
	private Integer money; // 재산
	private Integer score; // 오픈된 카드들의 점수 합
	private Boolean bj; // 블랙잭 여부

	private Boolean bust; // BUST 여부
	private Boolean d_Down; // 더블다운 여부
	private Boolean haveMoney;

	private List<DeckVO> deckList;

	
	public PlayerVO() {
		name = null ;
		money = 10000 ;
		score = 0 ;
		bj = false ;
		bust = false ;
		d_Down = false ;
		deckList = new ArrayList<DeckVO>();
		haveMoney = true;
	}
	
	
	public Boolean getHaveMoney() {
		return haveMoney;
	}


	public void setHaveMoney(Boolean haveMoney) {
		this.haveMoney = haveMoney;
	}


	public void setDeckList(List<DeckVO> deckList) {
		this.deckList = deckList;
	}


	public PlayerVO(String dealer) {
		name = dealer;
		money = 0 ;
		score = 0 ;
		bj = false ;
		bust = false ;
		d_Down = false ;
		deckList = new ArrayList<DeckVO>();
	}
	
	
	public void resetVO(int score1, boolean bj1, boolean bust1, boolean ddown1, Integer i) {
		score = score1 ;
		bj = bj1 ;
		bust = bust1 ;
		d_Down = ddown1 ;
		if(i == null)
		deckList = new ArrayList<DeckVO>();
	}
	

	public List<DeckVO> getDeckList() {
		return deckList;
	}

	
	public void setDeckList(List<DeckVO> voList, int num) {

		DeckVO vo1 = voList.get(num) ;
		deckList.add(vo1);

	}
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getMoney() {
		return money;
	}

	public void setMoney(Integer money) {
		this.money = money;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer num) {
		this.score = num ;
	}
	
	public void setAddScore(Integer value) { // 뽑은 카드의 숫자값 매개변수로 받아와서 기존 점수합에 더함
		this.score += value;
	}

	public Boolean getBj() {
		return bj;
	}

	public void setBj(Boolean bj) {
		this.bj = bj;
	}

	public Boolean getBust() {
		return bust;
	}

	public void setBust(Boolean bust) {
		this.bust = bust;
	}

	public Boolean getD_Down() {
		return d_Down;
	}

	public void setD_Down(Boolean d_Down) {
		this.d_Down = d_Down;
	}

	@Override
	public String toString() {
		return "PlayerVO [name=" + name + ", money=" + money + ", score=" + score + ", bj=" + bj + ", bust=" + bust
				+ ", d_Down=" + d_Down + "]";
	}
}