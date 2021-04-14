
	package com.heart.service;

	public interface BlackjackRule {
	   
	   public void gameMainScreen() ;   //게임 첫화면
	   public void playScreen() ;
	   
	   public void inputGamer() ;      //게이머 정보 입력 
	                           //이름 , 초기자본(디폴트)
	   
	   public Integer bettingMoney() ;   // 판돈 걸기
	   public void suffleDeck() ;      // 덱 셔플            : 유빈
	   public void handDeck() ;      // 덱 1장씩 배분      : 아영
	   public void checkBJ() ;         // 블랙잭 판정         : 소정
	   public void hitAndStand() ;      //힛 앤 스탠드 판정      : 혜미
	   public void gameResult() ;      //게임 결과 판정      : 선영
	   
	   
	   public void gamerMoney() ;      // 돈 계산
	   
	}

