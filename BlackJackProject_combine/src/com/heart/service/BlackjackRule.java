package com.heart.service;

import java.util.List;

import com.heart.model.DeckVO;
import com.heart.model.PlayerVO;



public interface BlackjackRule {
	public void gameMainScreen(); 				// 게임 첫화면

	public void playScreen();					// 게임의 진행 컨트롤 장소

	public void inputGamer(); 				// 플레이어 이름 입력, 기존에 파일이 있으면 기존 파일 로드 : 소정

	public Integer bettingMoney(); 				// 판돈 걸기

	public void shuffleDeck(); 					// 덱 셔플 : 아영

	public void handDeck(PlayerVO vo); 			// 덱 1장씩 배분 : 아영

	public void checkBJ(PlayerVO vo); 	// 블랙잭 판정 : 소정

	public void pHitAndStand(); 				// 플레이어 힛 앤 검증 된 판정 : 혜미

	public void dHitAndStand(); 				// 딜러 힛 앤 맞는 판정 : 혜미

	public void gameResult(); 					// 게임 결과 판정

	public void gamerMoney(); 					// 돈 계산 : 선영

	
}
