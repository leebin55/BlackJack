package com.heart.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.heart.service.BlackjackRule;

public class BlackJackImplV1 implements BlackjackRule{
	protected Scanner scan;
	protected Random rnd ;
	protected BlackJackYubin createDeck;
	
	public BlackJackImplV1() {
		scan = new Scanner(System.in);
		rnd = new Random();
		createDeck = new BlackJackYubin();
		
	}
	

	@Override
	public void gameMainScreen() {
	// TODO 게임 첫 화면
		
		System.out.println("*".repeat(36));
		System.out.println("*" + "            " + "블랙잭게임" + "            " + "*");
		System.out.println("*".repeat(36));

		System.out.println("\n현재 겜블러의 재산은"  + "원 입니다.");

		while (true) {
			System.out.println("\n" + "-".repeat(36));
			System.out.println("게임을 시작하시겠습니까?");
			System.out.println("▷ GO : 게임하기");
			System.out.println("▷ QUIT : 그만하기");
			System.out.println("-".repeat(36));
			System.out.print(" ▷ ");
			String goQuit = scan.nextLine();

			// 게임 선택
			if (goQuit.trim().equalsIgnoreCase("GO")) {
				this.playScreen();
			}

			// 게임종료 선택
			else if (goQuit.trim().equalsIgnoreCase("QUIT")) {
				System.out.println("\n게임을 종료합니다.");
				return;
			}

			// 잘못입력
			else {
				System.out.println("입력은 GO / QUIT 만 가능합니다.");
				continue;
			}

		}
		
		
	}

	@Override
	public void playScreen() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputGamer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Integer bettingMoney() {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public void suffleDeck() {
	}
	
	

	@Override
	public void handDeck() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkBJ() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hitAndStand() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameResult() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gamerMoney() {
		// TODO Auto-generated method stub
		
	}
	

}
