package com.heart.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.heart.model.DeckVO;
import com.heart.model.PlayerVO;
import com.heart.service.BlackjackRule;

public class BlackJackRuleImplV1 implements BlackjackRule {

	protected final int lineNum = 36;

	protected Scanner scan;
	protected Random rnd = new Random();

	protected List<DeckVO> deckList; // 셔플된 덱을 저장하는 리스트
	private int deckIndex = 0; // deckList에서 하나씩 차례대로 선택할수있도록 참조하는 인덱스값

	protected BlackJackYubin makeDeck; // 카드덱을 만드는 메소드가 있는 클래스
	protected DeckVO deckVO; // 덱VO 객체 생성

	protected List<DeckVO> playerList; // 플레이어의 카드리스트
	protected List<DeckVO> dealerList; // 딜러의 카드리스트

	protected String playerName; // 플레이어의 이름
	protected int playerMoney; // 플레이어의 돈
	protected int betMoney; // 플레이어가 베팅한 금액

	protected Integer pSum; // 플레이어 점수 합 변수
	protected Integer dSum; // 플레이어 점수 합 변수

	public BlackJackRuleImplV1() {
		scan = new Scanner(System.in);
		makeDeck = new BlackJackYubin();
		deckVO = new DeckVO();

		playerMoney = 10000; // 플레이어의 돈 10000원으로 디폴트값 설정
	}

	@Override
	public void gameMainScreen() {
		// TODO 게임 첫 화면

		System.out.println("*".repeat(lineNum));
		System.out.println("*" + "            " + "블랙잭게임" + "            " + "*");
		System.out.println("*".repeat(lineNum));

		this.inputGamer1();

		while (true) {
			System.out.println("\n현재" + playerName + "님의 재산은" + playerMoney + "원 입니다.");
			System.out.println("\n" + "-".repeat(lineNum));
			System.out.println("게임을 시작하시겠습니까?");
			System.out.println("▷ GO : 게임하기");
			System.out.println("▷ QUIT : 그만하기");
			System.out.println("-".repeat(lineNum));
			System.out.print(" ▷ ");
			String goQuit = scan.nextLine();

			// 게임 선택
			if (goQuit.equals("GO")) {
				this.playScreen();
			}

			// 게임종료 선택
			else if (goQuit.equals("QUIT")) {
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

	public void playerTotalVale() {
		pSum = 0;
		for(DeckVO vo : playerList) {
			pSum +=vo.getValue(); 
		}
	}
	@Override
	public void playScreen() {
		// TODO 게임 메소드 호출 장소

		deckList = makeDeck.deckList; // deckList에 original 덱을 생성하여 저장
		makeDeck.createDeck(); // 새 게임이 시작될 때 마다 새로운 덱
		deckIndex = 0; // 새 게임이 시작될 때 마다 초기화

		playerList = new ArrayList<DeckVO>(); // 새 게임이 시작될 떄 마다 플레이어의 카드리스트 초기화
		dealerList = new ArrayList<DeckVO>(); // 새 게임이 시작될 떄 마다 딜러의 카드리스트 초기화

		this.shuffleDeck(); // 새로운 덱을 셔플하여 deckList에 다시 저장

		System.out.println("게임을 시작합니다.");

		betMoney = this.bettingMoney(); // 베팅금 메소드

		System.out.println("\n" + "-".repeat(lineNum * 2));
		System.out.println("플레이어와 딜러에게 카드를 두 장씩 드립니다.");
		System.out.println("-".repeat(lineNum * 2));

		this.handDeck(dealerList); // 딜러 카드 리스트에 카드 두 장 추가
		this.handDeck(dealerList);

		System.out.println("딜러의 카드는 다음과 같습니다.");
		System.out.println(dealerList.get(0).getDeck());
		System.out.println("????");
		System.out.println("-".repeat(lineNum * 2));

		this.handDeck(playerList); // 플레이어 카드 리스트에 카드 두 장 추가
		this.handDeck(playerList);

		System.out.println(playerName + "카드는 다음과 같습니다.");
		System.out.println(playerList.get(0).getDeck());
		System.out.println(playerList.get(1).getDeck());
		this.playerTotalVale();
		System.out.println("플레이어의 점수 합 : " + pSum);
		System.out.println("-".repeat(lineNum * 2));

		boolean bjPC = this.checkBJ(playerList);

		if (!bjPC) {
			this.hitAndStand();
		}

//		if (bjPC) { // 플레이어가 블랙잭이라면
//
//			if (checkBJ(dealerList)) { // 딜러도 확인해보고 딜러도 블랙잭이라면
//				this.push(); // 무승부로 돈계산
//			} else {
//				this.win_bj(); // 딜러는 블랙잭이 아니라면 플레이어 win으로 돈계산
//			}
//
//		} else if (checkBJ(dealerList)) {
//			lose(); // 플레이어 lose로 돈계산
//		} else {
//			hitAndStand(); // 플레이어, 딜러 둘 다 블랙잭이 아니면 힛앤스탠드 함수 실행
//		}

		// bjC == true 

		boolean bjDC = this.checkBJ(dealerList);

		if (!bjDC) {
			this.hitAndStand();
		}

		// 결과확인창

	}
	
	public void deakerTotalVale() { //dealer 카드 합
		pSum = 0;
		for(DeckVO vo : dealerList) {
			dSum +=vo.getValue(); 
		}
	
	}
//나중에 수정하거나 메소드 삭제
	public void playerCheckTotal(){ // 플레이어가 hit할때마다 체크하는 메소드
		if(pSum >21) {
			System.out.println("Bust"); //그 뒤 적절한 코드 진행
		}else if(pSum<=21) {
			return; //불러온 메소드로 
		}
	}
	public void inputGamer1() {
		// TODO 플레이어의 정보 입력

		System.out.println("-".repeat(lineNum));
		System.out.println("플레이어의 이름을 입력하세요.");
		System.out.print(" 이름 입력 >> ");
		playerName = scan.nextLine();
		System.out.println("-".repeat(lineNum));

	}

	@Override
	public Integer bettingMoney() {
		// TODO 베팅금 입력 메소드

		Integer intMoney = null;

		while (true) {
			System.out.println("-".repeat(lineNum));
			System.out.println("베팅금을 거세요.(100단위로 가능합니다.)");
			System.out.print(" 베팅금 입력 >> ");
			String strMoney = scan.nextLine();

			try {
				intMoney = Integer.valueOf(strMoney);
			} catch (NumberFormatException e) {
				System.out.println("베팅금은 숫자 입력만 가능합니다.");
				continue;
			}

			if (intMoney > playerMoney) {
				System.out.println("베팅금은 소지금액보다 작아야합니다.");
				continue;
			} else if (!(intMoney % 100 == 0)) { // 베팅금은 100 단위만 가능
				System.out.println("베팅금은 100단위로만 가능합니다.");
				continue;
			}
			return intMoney;
		}
	}

	@Override
	public void shuffleDeck() {
		// TODO 조아영
		/*
		 * 생성된 덱을 셔플하는 메소드 셔플 후 리스트 deckList에 저장됨 deckIndex 값을 참조하여 deckList에서 차례대로 덱을
		 * 하나씩 뽑아내면 deckList는 이미 셔플된 덱의 리스트이기 때문에 랜덤으로 덱을 뽑을 수 있음과 동시에 중복성검사도 할 필요가 없다.
		 */

		Collections.shuffle(deckList); // deckList의 덱을 셔플하여 다시 deckList에 저장

	}

	public void handDeck(List<DeckVO> list) {
		// TODO 조아영
		// 딜러와 플레이어의 카드 리스트를 매개변수로 받아서 deckList에서 카드정보(카드이름,점수)를 0번부터 차례대로 배분하여 나눠주는 메소드

		DeckVO vo = deckList.get(deckIndex); // 셔플된 dexkList에서 deckIndex번째 값을 호출하여 vo에 저장

		deckIndex++; // deckIndex 값은 handDeck이 실행될때마다 증가

		list.add(vo); // 매개변수로 받아온 list에 뽑은 카드를 추가

	}

	
	public Boolean checkBJ(List<DeckVO> list) {
		// TODO 김소정

		if (list.get(0).getValue() == 1 && list.get(1).getValue() == 10) {
			return true;
		} else if (list.get(1).getValue() == 1 && list.get(0).getValue() == 10) {
			return true;
		} else {
			return false;
		}

	}

	public void hitAndStand() {
		// TODO 장혜미
		// 플레이어와 딜러의 히트 스탠드를 진행한다

		// 플레이어와 딜러 모두 카드를 2장씩 들고 있는 상황
		// 블랙잭이 아님
		// 플레이어가 히트와 스탠드 중 선택할 수 있음

		// 플레이어 진행
		while (true) {
			String hOs = this.askhOs(); // 히트 스탠드를 묻는 프롬프트와 입력받기

			if (hOs.equals("hit")) {
				pSum = this.gamerHit(); // 플레이어가 현재 가진 카드합이 리턴되는 메서드
				if (pSum > 21) {
					break;
				}
				// if 점수가 21이상이면 break, 아니면 반복(이미됨)
			} else if (hOs.equals("stnd")) {
				break;
			} else {
				System.out.println("!입력 오류!");
				System.out.println("HIT와 STAND 중 하나를 선택해주세요");
				System.out.println("블랙잭은 중도 포기할 수 없습니다");
				continue;
			}
		} // while end (플레이어)

		// 딜러의 진행
		// 선택지 없이 카드만 공개
		while (true) {
			dSum = this.dealerHit();// 딜러 현재 가진 카드합이 리턴되는 메서드
			if (dSum > 16) {
				break;
			}
		}

	}// hitAndStand end

	// TODO 히트 스탠드 묻는 메서드
	protected String askhOs() {
		System.out.println("HIT OR STAND?");
		System.out.println("HIT : hit");
		System.out.println("STAND : stnd");
		System.out.print(">> ");
		String answer = scan.nextLine();

		return answer;
	}

	// TODO 플레이어의 카드 점수가 합산되는 히트 메서드
	protected Integer gamerHit() {
		this.handDeck(playerList); // 점수 합까지 추가될 예정
		Integer sum = 0;

		int nSize = playerList.size();
		for (int i = 0; i < nSize; i++) {
			DeckVO vo = playerList.get(i);
			sum += vo.getValue();
		}

		for (int i = 0; i < nSize; i++) {
			System.out.println(playerList.get(i).getDeck());
		}
		System.out.println("플레이어의 점수 합 : " + sum);
		return sum;
		// 리턴 썸값
	}

	// TODO 딜러의 카드 점수가 합산되는 히트 메서드
	protected Integer dealerHit() {
		// 딜러 카드 가져오기
		this.handDeck(dealerList);
		Integer sum = 0;
		for (int i = 0; i < dealerList.size(); i++) {
			DeckVO vo = dealerList.get(i);
			sum += vo.getValue();
		}
		return sum;
	}

	@Override
	public void gameResult() {
		// TODO Auto-generated method stub

	}

	@Override
	public void gamerMoney() {
		// TODO 돈계산
		// 플레이어, 딜러 둘 다 블랙잭,BUST 아님
		// 양 쪽 점수 비교
		if (pSum > dSum) {
			win();
		} else if (dSum > pSum) {
			lose();
		} else if (dSum == pSum) {
			push();
		}
	}

	public void win_bj() {
		// TODO 플레이어가 블랙잭으로 이겼을 경우
		playerMoney += ((float) betMoney * 2.5);
	}

	public void win() {
		// TODO 플레이어가 이겼을 경우 돈계산
		// 양쪽 카드 오픈 후 플레이어 점수 합이 더 높을 때
		// (플레이어는 BUST가 아니고) 딜러가 BUST일 때

		// 배팅금의 2배 돌려받음
		playerMoney += (betMoney * 2);
	}

	public void lose() {
		// TODO 플레이어가 졌을 경우 돈계산
		// 플레이어가 BUST일 때
		// 양쪽 카드 오픈 후 딜러 점수 합이 더 높을 때
		// (플레이어는 블랙잭이 아니고) 딜러가 블랙잭일 때

		// 배팅금 잃은 거라 그대로임
		return;
	}

	public void push() {
		// TODO 비겼을 경우 돈계산
		// 양쪽 카드 오픈 후 플레이어와 딜러 점수 합이 같을 때
		// 양쪽 다 블랙잭인 경우

		// 배팅금 다시 돌려줌
		playerMoney += betMoney;
	}

	@Override
	public String inputGamer() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void handDeck(PlayerVO vo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pHitAndStand() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dHitAndStand() {
		// TODO Auto-generated method stub
		
	}

	
	public void inputGamer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkBJ(PlayerVO vo) {
		// TODO Auto-generated method stub
		
	}

}