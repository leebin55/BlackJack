package com.heart.service;

public class Player {
//Player클래스를 만들면 플레이어의 이름과 초기자본을  초기화
	String playerName;
	String playerCard;
	Integer playerValue;
	Integer playerSum;
	int InitMoney;
	int playerMoney;
	int betMoney;
	
	public Player(String playerName,int InitMoney) { // 플레이어 이름을 매개변수로 받는
		this.playerName = playerName;
		this.InitMoney = InitMoney;
		
	}
}

