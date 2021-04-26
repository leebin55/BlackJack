package com.heart.impl;
/*
 * XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
 *수정 전용 오류만 안뜨도록만 해놓음
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.heart.model.DeckVO;
import com.heart.model.PlayerVO;
import com.heart.service.BlackjackRule;

public class BlackJackRuleImplV2 implements BlackjackRule {

	protected final int lineNum = 36;

	protected Scanner scan;
	protected Random rnd;

	protected List<DeckVO> deckList; // 셔플된 덱을 저장하는 리스트
	protected int deckIndex = 0; // deckList에서 하나씩 차례대로 선택할수있도록 참조하는 인덱스값

	protected BlackJackYubin makeDeck; // 카드덱을 만드는 메소드가 있는 클래스
	protected DeckVO deckVO; // 덱VO 객체 생성

	protected List<DeckVO> playerList; // 플레이어의 카드리스트
	protected List<DeckVO> dealerList; // 딜러의 카드리스트

	protected PlayerVO voP; // 플레이어 정보 저장 값
	protected PlayerVO voD; // 딜러 정보 저장 값

	protected int betMoney; // 플레이어가 베팅한 금액


	protected boolean inYN; // 인슈어런스 판단 변수
	protected int inMoney; // 인슈어런스 머니

	
	
	// TODO 생성자
	public BlackJackRuleImplV2() {
		scan = new Scanner(System.in);
		rnd = new Random();

		makeDeck = new BlackJackYubin();
		deckVO = new DeckVO();
		deckList = new ArrayList<DeckVO>();
		
		voP = new PlayerVO() ; 
		voD = new PlayerVO() ; 

		voP.setMoney(10000);// 플레이어의 돈 10000원으로 디폴트값 설정
	}

	// TODO 게임 메인 화면
	// 모든 메소드가 콘트롤 되는 장소
	@Override
	public void gameMainScreen() {
		System.out.println("*".repeat(lineNum));
		System.out.println("*" + "            " + "블랙잭게임" + "            " + "*");
		System.out.println("*".repeat(lineNum));

		voP.setName(this.inputGamer1());
		
		
		if (voP.getName() == null) {
			System.out.println("\n ** 게임을 종료합니다. ** ");
			return;
		}

		while (true) {
			System.out.println("\n현재 " + voP.getName() + "님의 재산은 " + voP.getMoney() + "원 입니다.");
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
				System.out.println("\n ** 게임을 종료합니다. ** ");
				return;
			}

			// 잘못입력
			else {
				System.out.println("입력은 GO / QUIT 만 가능합니다.");
				continue;
			}

		}

	}

	// TODO 게임 플레이 전반을 콘트롤 하는 장소
	@Override
	public void playScreen() {
		deckList = makeDeck.deckList; // deckList에 original 덱을 생성하여 저장
		makeDeck.createDeck(); // 새 게임이 시작될 때 마다 새로운 덱
		deckIndex = 0; // 새 게임이 시작될 때 마다 초기화

		playerList = new ArrayList<DeckVO>(); // 새 게임이 시작될 떄 마다 플레이어의 카드리스트 초기화
		dealerList = new ArrayList<DeckVO>(); // 새 게임이 시작될 떄 마다 딜러의 카드리스트 초기화

		this.shuffleDeck(); // 새로운 덱을 셔플하여 deckList에 다시 저장

		System.out.println("\n게임을 시작합니다.");

		// 베팅금 메소드
		betMoney = this.bettingMoney();

		System.out.println("\n" + "-".repeat(lineNum * 2));
		System.out.println("플레이어와 딜러에게 카드를 두 장씩 드립니다.");
		System.out.println("-".repeat(lineNum * 2));

		// 딜러 카드 리스트에 카드 두 장 추가
		this.handDeck(dealerList);
		this.handDeck(dealerList);

		// 플레이어 카드 리스트에 카드 두 장 추가
		this.handDeck(playerList);
		this.handDeck(playerList);

		// 블랙잭 확인하기 위한 디버깅 코드
//		DeckVO vo0 = playerList.get(0);
//		vo0.setValue(1);
//		DeckVO vo1 = playerList.get(1);
//		vo1.setValue(10);

		for (int i = 0; i < 2; i++) {
			DeckVO vo = playerList.get(i);
			Integer sum = 0;
			sum += vo.getValue();
			voP.setScore(sum);
		}

		for (int i = 0; i < 2; i++) {
			DeckVO vo = dealerList.get(i);
			Integer sum = 0;
			sum += vo.getValue();
			voD.setScore(sum);
		}

		// 플레이어 블랙잭 판단.
		voP.setBj(this.checkBJ(playerList));

		// 카드 보여주는 메소드
		this.showCard();
		
		

		// 플레이어가 블랙잭이 아닐 경우 hit & stand 진행
		if (voP.getBj())
			System.out.println("블랙잭입니다!");
		else if (!voP.getBj())
			this.pHitAndStand();

		voD.setBj(this.checkBJ(dealerList));

		if (!voP.getBj() && !voD.getBj() ) {
			this.dHitAndStand();
			if (voD.getScore() > 21)
				voD.setBust(true);
		}

		this.gameResult(); // 결과창
		this.gamerMoney(); // 돈 반환

	}

	// TODO 플레이어의 정보 입력
	
	public String inputGamer1() {

		System.out.println("\n" + "-".repeat(lineNum));
		System.out.println("플레이어의 이름을 입력하세요.(QUIT : 종료)");
		System.out.print(" 이름 입력 >> ");
		voP.setName(scan.nextLine());
		if (voP.getName().equals("QUIT"))
			return null;

		System.out.println("-".repeat(lineNum));
		return voP.getName();

	}

	// TODO 베팅금 입력 메소드
	@Override
	public Integer bettingMoney() {

		Integer intMoney = null;

		while (true) {
			System.out.println("\n" + "-".repeat(lineNum));
			System.out.println("베팅금을 거세요.(100단위로 가능합니다.)");
			System.out.print(" 베팅금 입력 >> ");
			String strMoney = scan.nextLine();

			try {
				intMoney = Integer.valueOf(strMoney);
				int sum = voP.getMoney() - intMoney;
				voP.setMoney(sum);
			} catch (NumberFormatException e) {
				System.out.println("베팅금은 숫자 입력만 가능합니다.");
				continue;
			}

			if (intMoney > voP.getMoney()) {
				System.out.println("베팅금은 소지금액보다 작아야합니다.");
				continue;
			} else if (!(intMoney % 100 == 0)) { // 베팅금은 100 단위만 가능
				System.out.println("베팅금은 100단위로만 가능합니다.");
				continue;
			}
			return intMoney;
		}
	}
	
	//TODO 카드 한 장을 보여주는 메소드
	protected void showOneCard(List<DeckVO> list, int num) {
		DeckVO vo = list.get(num);
		System.out.println(vo.getDeck());
	}

	
	
	// TODO 초기 카드 보여주는 메소드
	protected void showCard() {

		// 딜러의 카드 보여줌
		System.out.println("딜러의 카드는 다음과 같습니다.");
		this.showOneCard(dealerList, 0);
		System.out.println("????");
		System.out.println("-".repeat(lineNum * 2));

		// 플레이어의 카드 보여줌

		System.out.println(voP.getName() + "의 카드는 다음과 같습니다.");
		this.showOneCard(playerList, 0);
		this.showOneCard(playerList, 1);
		
		if (voP.getBj()) {
			System.out.println("블랙잭!");
		} else {
			System.out.println("플레이어의 점수 합 : " + voP.getScore());
		}
		System.out.println("-".repeat(lineNum * 2));
	}

	
	
	
	// TODO 조아영
	// 덱을 셔플하는 메소드
	@Override
	public void shuffleDeck() {
		/*
		 * 생성된 덱을 셔플하는 메소드 셔플 후 리스트 deckList에 저장됨 deckIndex 값을 참조하여 deckList에서 차례대로 덱을
		 * 하나씩 뽑아내면 deckList는 이미 셔플된 덱의 리스트이기 때문에 랜덤으로 덱을 뽑을 수 있음과 동시에 중복성검사도 할 필요가 없다.
		 */
//		Collections.shuffle(deckList); 

		int dSize = deckList.size();

		for (int i = 0; i < 50; i++) {
			int num = rnd.nextInt(dSize);
			DeckVO vo1 = deckList.get(i);
			DeckVO voNum = deckList.get(num);
			DeckVO tempVO = vo1;

			vo1 = voNum;
			voNum = tempVO;

			deckList.set(i, vo1);
			deckList.set(num, voNum);

		}

		// 디버깅 코드
//		for(int i = 0 ; i < dSize ; i ++) {
//			DeckVO vo = deckList.get(i) ;
//			System.out.println(vo.toString());
//		}

	}

	// TODO 조아영
	// 카드를 한 장씩 나눠주는 메소드
	
	public void handDeck(List<DeckVO> list) {

		// 딜러와 플레이어의 카드 리스트를 매개변수로 받아서
		// deckList에서 카드정보(카드이름,점수)를 0번부터 차례대로 배분하여 나눠주는 메소드

		DeckVO vo = deckList.get(deckIndex); // 셔플된 dexkList에서 deckIndex번째 값을 호출하여 vo에 저장

		deckIndex++; // deckIndex 값은 handDeck이 실행될때마다 증가

		list.add(vo); // 매개변수로 받아온 list에 뽑은 카드를 추가

	}

	
	
	
	// TODO 김소정
	// 블랙잭 판단 메소드
	public Boolean checkBJ(List<DeckVO> list) {

		DeckVO vo0 = list.get(0);
		DeckVO vo1 = list.get(1);

		if (vo0.getValue() == 1 && vo1.getValue() == 10) {
			return true;
		} else if (vo1.getValue() == 1 && vo0.getValue() == 10) {
			return true;
		} else {
			return false;
		}

	}

	
	
	
	// TODO 장혜미
	// 플레이어와 딜러의 히트 스탠드를 진행한다
	@Override
	public void pHitAndStand() {
		// 플레이어와 딜러 모두 카드를 2장씩 들고 있는 상황
		// 블랙잭이 아님
		// 플레이어가 히트와 스탠드 중 선택할 수 있음

		// 플레이어 진행
		while (true) {
			String hOs = this.askhOs(); // 히트 스탠드를 묻는 프롬프트와 입력받기

			if (hOs.equals("hit")) {
				voP.setScore(this.gamerHit()); // 플레이어가 현재 가진 카드합이 리턴되는 메서드
				if (voP.getScore() > 21) {
					voP.setBust(true);
					System.out.println("!BUST!");
					break;
				}

				else if (voP.getScore() == 21)
					break;

				// if 점수가 21이상(bust)이면 break, 아니면 반복(이미됨)
			} else if (hOs.equals("stnd")) {
				System.out.println("-".repeat(lineNum));
				System.out.println("플레이어가 STAND를 선언했습니다");
				break;
			} else {
				System.out.println("!입력 오류!");
				System.out.println("HIT와 STAND 중 하나를 선택해주세요");
				System.out.println("블랙잭은 중도 포기할 수 없습니다");
				continue;
			}
		} // while end (플레이어)

	}// pHitAndStand end

	
	
	// TODO 장혜미
	// 딜러의 히트앤 스탠드 진행
	@Override
	public void dHitAndStand() {
		// 선택지 없이 카드만 공개
		while (true) {
			voD.setScore(dealerHit());// 딜러 현재 가진 카드합이 리턴되는 메서드
			if (voD.getScore() > 16)
				break;
			else if (voD.getScore() == 21)
				break;
		} // while end (딜러)
	}// dHitAndStand end

	// TODO 장혜미
	// 히트 스탠드 묻는 메서드
	protected String askhOs() {

		System.out.println("HIT OR STAND?");
		System.out.println("HIT : hit");
		System.out.println("STAND : stnd");
		System.out.print(">> ");
		String answer = scan.nextLine();

		return answer;
	}

	// TODO 장혜미
	// 플레이어의 카드 점수가 합산되는 히트 메서드
	protected Integer gamerHit() {

		this.handDeck(playerList);
		Integer sum = 0;

		int nSize = playerList.size();
		for (int i = 0; i < nSize; i++) {
			DeckVO vo = playerList.get(i);
			sum += vo.getValue();
		}
		System.out.println("-".repeat(lineNum));
		for (int i = 0; i < nSize; i++) {
			this.showOneCard(playerList, i);
		}

		System.out.println("플레이어의 점수 합 : " + sum);
		System.out.println("-".repeat(lineNum));

		return sum;
		// 리턴 썸값
	}

	// TODO 장혜미
	// 딜러의 카드 점수가 합산되는 히트 메서드
	protected Integer dealerHit() {
		// 딜러 카드 가져오기
		this.handDeck(dealerList);
		Integer sum = 0;

		int nSize = dealerList.size();
		for (int i = 0; i < dealerList.size(); i++) {
			DeckVO vo = dealerList.get(i);
			sum += vo.getValue();
		}

		System.out.println("-".repeat(lineNum));
		for (int i = 0; i < nSize; i++) {
			this.showOneCard(dealerList, i);
		}
		System.out.println("딜러의 점수 합 : " + sum);
		System.out.println("-".repeat(lineNum));

		return sum;
	}

	
	
	
	//TODO 게임 결과 창
	@Override
	public void gameResult() {
		System.out.println("*".repeat(lineNum));
		System.out.println("···게임결과···");
		System.out.println("*".repeat(lineNum));

		System.out.println(voP.getName() + "님의 게임결과");
		System.out.println("-".repeat(lineNum));

		showResultCard(playerList);

		System.out.println(".".repeat(lineNum));

		if (voP.getBj()) {
			System.out.println(voP.getName() + " 블랙잭!");
		} else if (voP.getBust()) {
			System.out.println(voP.getName() + " BUST!");
		} else {
			System.out.println(voP.getName() + "님의 점수 : " + voP.getScore());
		}

		System.out.println("*".repeat(lineNum));

		System.out.println("딜러님의 게임결과");
		System.out.println("-".repeat(lineNum));

		showResultCard(dealerList);

		System.out.println(".".repeat(lineNum));
		if (voD.getBj()) {
			System.out.println("딜러 블랙잭!");
		} else if (voD.getBust()) {
			System.out.println("딜러 Bust!");
		} else {
			System.out.println("딜러의 점수 : " + voD.getScore());
		}
		System.out.println();

	}

	//TODO 카드 리스트 보여주는 메소드
	protected void showResultCard(List<DeckVO> list) {

		int nSize = list.size();
		for (int i = 0; i < nSize; i++) {
			this.showOneCard(list, i);
		}
	}

	
	
	
	// TODO 최선영 돈계산
	@Override
	public void gamerMoney() {
		
		if(voD.getBj()) {
			System.out.println("인슈어런스 보상금을 돌려드립니다.");
			System.out.println( betMoney + "원 지급");
		}
			
		
		// 플레이어, 딜러 둘 다 블랙잭,BUST 아님
		// 양 쪽 점수 비교
		if (voP.getBust()) {
			lose();
		} else if ((voP.getScore() > voD.getScore()) || voD.getBust()) {
			win();
		} else if (voD.getScore() > voP.getScore()) {
			lose();
		} else if (voD.getScore() == voP.getScore()) {
			push();
		}

		if (voP.getBj()) {
			if (voP.getBj()) {
				this.push();
			} else {
				this.win_bj();
			}
		}

	}
	
	// TODO 최선영 돈계산
	public void win_bj() {
		// TODO 플레이어가 블랙잭으로 이겼을 경우
		System.out.println("\n" + voP.getName() + "님 BlackJack으로 승리");
		voP.setMoney((int) (voP.getMoney() + (betMoney * 2.5)));
	}

	// TODO 최선영 돈계산
	public void win() {
		// TODO 플레이어가 이겼을 경우 돈계산
		// 양쪽 카드 오픈 후 플레이어 점수 합이 더 높을 때
		// (플레이어는 BUST가 아니고) 딜러가 BUST일 때

		// 배팅금의 2배 돌려받음
		System.out.println("\n" + voP.getName() + "님 승리");
		voP.setMoney((voP.getMoney() + (betMoney * 2)));
	}

	// TODO 최선영 돈계산
	public void lose() {
		// TODO 플레이어가 졌을 경우 돈계산
		// 플레이어가 BUST일 때
		// 양쪽 카드 오픈 후 딜러 점수 합이 더 높을 때
		// (플레이어는 블랙잭이 아니고) 딜러가 블랙잭일 때

		// 배팅금 잃은 거라 그대로임

		System.out.println("\n" + voP.getName() + "님 패배");
		return;
	}

	// TODO 최선영 돈계산
	public void push() {
		// TODO 비겼을 경우 돈계산
		// 양쪽 카드 오픈 후 플레이어와 딜러 점수 합이 같을 때
		// 양쪽 다 블랙잭인 경우

		// 배팅금 다시 돌려줌
		System.out.println("\n" + "무 승 부");
		voP.setMoney((voP.getMoney() + betMoney));
	}
	
	
	
	
	
	// TODO 조아영 인슈런스
	public void insurance() {
		
	}

	@Override
	public void handDeck(PlayerVO vo) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputGamer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkBJ(PlayerVO vo) {
		// TODO Auto-generated method stub
		
	}

	
	
}