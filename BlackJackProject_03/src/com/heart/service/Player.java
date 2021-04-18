package com.heart.service;

import java.util.List;

import com.heart.model.DeckVO;

public class Player {
//Player클래스를 만들면 플레이어의 이름과 초기자본을  초기화
	String playerName;
	List<DeckVO> playerCard;
	Integer playerValue;
	Integer playerSum;
	int playerMoney; // 가지고 있는 돈 (초기 금액 입력받을 수 있음)
	int betMoney; //베팅금액
	
	public Player(String playerName,int playerMoney) { // 플레이어 이름을 매개변수로 받는
		// 게임을 아예 처음부터 시작할때 마다 이름과 초기 금액이 초기화
		this.playerName = playerName;
		this.playerMoney = playerMoney;
	}
	

	public void setPlayerCard() {//플레이어가 카드를 받으면 받은 카드 저장
		
	}
	
	public int playerLose() {//플레이어가 졌을 때 현재 가지고 있는돈에서 베팅금액이 계속 감소
		playerMoney -=betMoney;
		return playerMoney;// 현재 가지고 있는 돈 리턴> 다른클래스에서도 값을 사용할 수 있게
	}
	public int playerWin() {
		playerMoney += betMoney;
		return playerMoney;
	}
	
	public int playerBlackJack() {
		playerMoney += (betMoney*1.5);
		return playerMoney;
	}

	
	public void showPlayerInfo() {
		System.out.println( playerName + "님이 현재 가지고 있는 돈은 " + playerMoney );
		
	}
}

