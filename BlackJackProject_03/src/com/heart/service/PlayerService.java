package com.heart.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;

import com.heart.impl.BlackJackYubin;
import com.heart.model.DeckVO;
import com.heart.model.PlayerVO;
import javax.swing.JOptionPane;

public class PlayerService { //
	protected PlayerVO player;
	protected Scanner scan;
	protected final int lineNum;
	protected List<PlayerVO> playerInfoList;
	//protected Random rnd = new Random();
	protected List<DeckVO> deckList; // 셔플된 덱을 저장하는 리스트
	private int deckIndex = 0; // deckList에서 하나씩 차례대로 선택할수있도록 참조하는 인덱스값
	protected BlackJackYubin makeDeck; // 카드덱을 만드는 메소드가 있는 클래스
	//protected DeckVO deckVO; // 덱VO 객체 생성
	protected List<DeckVO> playerList; // 플레이어의 카드리스트


	public PlayerService() {
		scan = new Scanner(System.in);
		lineNum = 36;
		player = new PlayerVO();
		makeDeck = new BlackJackYubin();
		playerInfoList = new ArrayList<PlayerVO>();
	}

	public void inputGamer() {
		// 플에이어 이름 입력 .> 값을 아직 vo에 안담음

		while (true) {
			System.out.println("-".repeat(lineNum));
			System.out.println("플레이어의 이름을 입력하세요.");
			System.out.print(" 이름 입력 >> ");
			// TODO 수정한 부분 앞에 String형으로 생성 , 만약 이름을 입력안했을때도 추가
			// 메세지 박스도 넣어봄
			String playerName = scan.nextLine();
			if (playerName.trim().equals("")) {
				// JOptionPane.showMessageDialog(null, "이름을 입력하셔야 합니다.", "오류",
				// JOptionPane.INFORMATION_MESSAGE);
				// ;
				System.out.println("이름을 입력하셔야 합니다.");
				continue;
			}
			System.out.println("-".repeat(lineNum));
			player.setName(playerName);
			playerInfoList.add(player);
			break;
		}
		
	}

	public void inputIntiMoney() {
		// 처음에 돈 입력받기
	
		Integer intMoney = null;
		while (true) {
			System.out.println("게임을 시작할 금액을 입력하세요");
			System.out.print("   >>   ");
			String strMoney = scan.nextLine();
			//금액 입력안했을때
			if (strMoney.trim().equals(""))
			{
				System.out.println(" 금액을 입력하셔야 합니다.");
				continue;
			}
			//숫자값이 아닌 값을 입력
			try {
				intMoney = Integer.valueOf(strMoney);
			} catch (NumberFormatException e) {
				System.out.println("금액은 숫자만 입력 가능합니다.");
				continue;
			}
			//최소 베팅금액보다 낮은 금액 입력
			if(intMoney<100) {
				System.out.println("100 원 이상부터 게임을 시작하실 수 있습니다.");
				continue;
			}
			player.setMoney(intMoney); // vo클래스에 현재가지고 있는 돈 저장\
			playerInfoList.add(player);
		}
	}

	public void betMoney() { // playerVO 에 현재 가지고 있는 돈 저장하고 거기서 불러오면??
		// 베팅금 입력 메소드
		// TODO intMoney >>intBetMoney
		Integer intBetMoney = null;

		while (true) {

			System.out.println("-".repeat(lineNum));
			System.out.println("베팅금을 거세요.(100단위로 가능합니다.)");
			System.out.print(" 베팅금 입력 >> ");
			// TODO strMoney >>strBetMoney 로 이름 바꿈(현재 가지고 있는 돈이랑 헷갈려서)
			String strBetMoney = scan.nextLine();

			try {
				intBetMoney = Integer.valueOf(strBetMoney);
			} catch (NumberFormatException e) {

				System.out.println("베팅금은 숫자 입력만 가능합니다.");
				continue;
			}

			if (intBetMoney > player.getMoney()) {
				System.out.println("베팅금은 소지금액보다 작아야합니다.");
				continue;
			} else if (!(intBetMoney % 100 == 0)) { // 베팅금은 100 단위만 가능
				System.out.println("베팅금은 100단위로만 가능합니다.");
				continue;
			}
			player.setBet(intBetMoney);
			playerInfoList.add(player);
		}
	}
	
	
}
