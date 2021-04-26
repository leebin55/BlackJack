package com.heart.impl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import com.heart.model.DeckVO;
import com.heart.model.PlayerVO;
import com.heart.service.BlackjackRule;

public class BlackJacReal implements BlackjackRule {

	protected final int lineNum = 36;

	protected Scanner scan;
	protected Random rnd;

	protected String basePath = "src/com/heart/game/";

	protected List<DeckVO> deckList; // 셔플된 덱을 저장하는 리스트
	protected int deckIndex = 0; // deckList에서 하나씩 차례대로 선택할수있도록 참조하는 인덱스값

	protected BlackJackYubin makeDeck; // 카드덱을 만드는 메소드가 있는 클래스

	protected DeckVO deckVO; // 덱VO 객체 생성
	protected PlayerVO voP; // 플레이어 정보 저장 값
	protected PlayerVO voD; // 딜러 정보 저장 값

	protected int betMoney; // 플레이어가 베팅한 금액

	protected boolean inYN; // 인슈어런스 판단 변수
	protected int inMoney; // 인슈어런스 머니

	// TODO 생성자
	public BlackJacReal() {
		scan = new Scanner(System.in);
		rnd = new Random();

		makeDeck = new BlackJackYubin();
		deckVO = new DeckVO();
		deckList = new ArrayList<DeckVO>();

		voP = new PlayerVO();
		voD = new PlayerVO("딜러");

	}
	
	// TODO 게임 메인 화면
	// 모든 메소드가 콘트롤 되는 장소
	@Override
	public void gameMainScreen() {
		System.out.println("*".repeat(lineNum));
		System.out.println("*" + "            " + "블랙잭게임" + "            " + "*");
		System.out.println("*".repeat(lineNum));

		// 플레이어 이름 입력
		// 기존에 있는 이름이라면 파일 로드
		this.inputGamer();

		// inputGame에서 Quit 입력시 게임종료
		if (voP.getName().equals("QUIT")) {
			System.out.println("\n ** 게임을 종료합니다. ** ");
			return;
		}

		// 메뉴 입력
		while (voP.getHaveMoney()) {
			System.out.println("\n현재 " + voP.getName() + "님의 재산은 " + voP.getMoney() + "원 입니다.");
			System.out.println("\n" + "-".repeat(lineNum));
			System.out.println("게임을 시작하시겠습니까?");
			System.out.println("▷ 0 : 새로운 이름 설정하기");
			System.out.println("▷ 1 : 게임 하기");
			System.out.println("▷ 2 : 게임 저장하기");
			System.out.println("▷ 3 : 게임 끝내기");
			System.out.println("-".repeat(lineNum));
			System.out.print(" ▷ ");
			String selecM = scan.nextLine();

			Integer intM;
			try {
				intM = Integer.valueOf(selecM);
			} catch (NumberFormatException e) {
				System.out.println("숫자 1, 2, 3만 입력하세요");
				continue;
			}

			// 게임 선택
			if (intM == 1) {
				this.playScreen();
			}

			// 게임 저장하기 선택
			else if (intM == 2) {
				this.saveGame(voP.getName(), voP.getMoney());
			}

			// 게임종료 선택
			else if (intM == 3) {
				System.out.println("\n ** 게임을 종료합니다. ** ");
				return;
			}

			else if (intM == 0) {
				this.inputGamer();
//				voP = new PlayerVO();
			}

			// 잘못입력
			else {
				System.out.println("입력은 1, 2, 3만 가능합니다.");
				continue;
			}

		}
		if(voP.getHaveMoney() == false) {
			System.out.println("현재 "+ voP.getName() + "님의 재산이 없으므로 게임에 참여하실 수 없습니다.");
		}

	}

	// TODO 게임 플레이 전반을 콘트롤 하는 장소
	@Override
	public void playScreen() {
		//플레이어의 소지금이 100원 이하(최소 베팅금)일땐
	      // 게임 강제 종료
	      if(voP.getMoney() < 100) {
	         System.out.println("소지금이 부족합니다");
	         System.out.println("게임선택화면으로 돌아갑니다.");
	         return ;
	      }

		makeDeck.createDeck(deckList); // 새 게임이 시작될 때 마다 새로운 덱
		deckIndex = 0; // 새 게임이 시작될 때 마다 초기화

		// 플레이어와 딜러의
		// (순서대로) 점수의 합, 블랙잭여부, 버스트여부, 더블다운여부, 덱리스트를
		// 초기화
		voP.resetVO(0, false, false, false, null);
		voD.resetVO(0, false, false, false, null);

		this.shuffleDeck(); // 새로운 덱을 셔플하여 deckList에 다시 저장

		System.out.println("\n게임을 시작합니다.");

		// 베팅금 메소드를 실행하여 베팅금을 변수에 저장
		betMoney = this.bettingMoney();
		if(betMoney == -1) return ;   // QUIT 입력시 게임 종료

		System.out.println("\n" + "-".repeat(lineNum));
		System.out.println("플레이어와 딜러에게 카드를 두 장씩 드립니다.");
		System.out.println("-".repeat(lineNum));

		// 딜러 카드 리스트에 카드 두 장 추가
		this.handDeck(voD);
		this.handDeck(voD);

		// 플레이어 카드 리스트에 카드 두 장 추가
		this.handDeck(voP);
		this.handDeck(voP);

		// 블랙잭 확인하기 위한 디버깅 코드
//			int num1 = 1 ;
//			int num2 = 10 ;
//					
//			voP.setDeckList();		

		// 플레이어 블랙잭 판단.
		this.checkBJ(voP);

		// 카드 보여주는 메소드
		this.showCard();

		// 인슈어런스 여부 물어보기
		this.insurance();

		// 플레이어가 블랙잭
		if (voP.getBj())
			System.out.println("블랙잭입니다!");

		// 플레이어가 블랙잭이 아닐 경우
		else if (!voP.getBj()) {

			this.askD_Down(); // pHitAndStand 도 여기서 진행하는게 더 보기 좋을 거같아서 pHitAndStand에 조건으로 진행

			if (voP.getD_Down() == false) { // 더블다운을 진행하지 않을때 pHitAndStand 진행
				this.pHitAndStand();
			}

		}

		// 딜러의 블랙잭 판단
		this.checkBJ(voD);

		// 플레이어가 블랙잭이 아니고 동시에
		// 딜러가 블랙잭이 아니면 딜러가 힛앤스탠드를 진행한다.
		if (!voP.getBj() && !voD.getBj()) {
			this.dHitAndStand();

		}

		// 결과창
		this.gameResult();

		// 돈 반환
		this.gamerMoney();

	}

	public void askD_Down() {
		if (voP.getMoney() - betMoney  >= 0) {
			//playScreen 에서 bettingMoney 를 실행해서 이미 voP.getMoney() 에서 betMoney가 빠져있는상태
			// 그 값에서 - 다시한번 betMoney 를 뺀값이 0 이상이면 실행(doubleDown은 베팅금 2배)

			while (true) {
				System.out.println("더블다운을 진행 하시겠습니까?( Y / N)");
				System.out.print(" ▷ ");
				String strAnswer = scan.nextLine();
				if (strAnswer.trim().equals("")) {
					// 아무것도 입력안했을때
					System.out.println(" Y 와 N 중 반드시 선택해 주세요");
					continue;
				}
				if (strAnswer.trim().equalsIgnoreCase("Y")) {
					this.doubleDown();

				} else if (!(strAnswer.trim().equalsIgnoreCase("N"))) {// Y 값도 아니고 N 값도 아닌 그 외의 값
					System.out.println("Y 또는 N 중에 선택해 주세요");
					continue;
				}
				break;
			}
		}

	}

	// 이름때문에 더블다운 진행한거 같은 그냥 askDDown 에서 진행
	public void doubleDown() {
		voP.setD_Down(true);// true 로 변경
		this.handDeck(voP);// 악 한장만 받고 끝

		int sum = voP.getMoney() - betMoney;
		voP.setMoney(sum);

		betMoney *= 2; // 베팅금 2배로
		System.out.println("더블다운하셨으므로 베팅금이 두배가 됩니다.");

		if (voP.getScore() > 21)
			voP.setBust(true);
		System.out.println("!BUST!");

	}

	// TODO 김소정
	// 플레이어의 정보 입력

	@Override
	public void inputGamer() {

		while (true) {
			System.out.println("\n" + "-".repeat(50));
			System.out.println("플레이어의 이름을 입력하세요.(QUIT : 종료)");
			System.out.print(" 이름 입력 >> ");
			String id = scan.nextLine();
			voP.setName(id);
			if (id.trim().equals("")) {
				System.out.println("아이디는 빈칸으로 사용하실 수 없습니다");
				continue;
			}
			
			if (id.trim().equalsIgnoreCase("QUIT")) {
				voP.setName(id);
				break;
			}

			this.loadGame(id);

			break;
		} // while end

	}


	// TODO 김소정
	// 플레이어의 정보 불러오기
	public void loadGame(String id) {
		id = voP.getName();

		while (true) {
			System.out.println("게임을 불러옵니다");

			FileReader fileReader = null;
			BufferedReader buffer = null;

			try {
				fileReader = new FileReader(basePath + id);
				buffer = new BufferedReader(fileReader);

				String reader = buffer.readLine();
				String[] source = reader.split(":");

				System.out.println("저장된 기록을 불러왔습니다");

				voP.setName(id);
				voP.setMoney(Integer.valueOf(source[1]));
				System.out.printf("%s 님의 잔액은 %d 입니다.\n", id, voP.getMoney());
				buffer.close();
				return;

			} catch (FileNotFoundException e) {
				System.out.println("\n저장된 파일이 없습니다.");
				System.out.println("입력하신 이름으로 게임을 진행합니다.");
				voP.setName(id);
				return;
			} catch (IOException e) {
				System.out.println("\n파일에 문제가 있습니다");
				System.out.println("입력하신 이름으로 게임을 진행합니다.");
				voP.setName(id);
				return;
			}
		} // while end

	}
	

	// TODO 베팅금 입력 메소드
	@Override
	
		public Integer bettingMoney() {

		      Integer intMoney = null;

		      while (true) {
		         System.out.println("\n" + "-".repeat(lineNum));
		         System.out.println("베팅금을 거세요.(100단위로 가능합니다.)");
		         System.out.println("QUIT 입력시 메뉴로 돌아갑니다.");
		         System.out.print(" 베팅금 입력 >> ");
		         String strMoney = scan.nextLine();
		         
		         if(strMoney.equals("QUIT")) return -1;
		         
		         
		         try {
		            intMoney = Integer.valueOf(strMoney);

		         } catch (NumberFormatException e) {
		            System.out.println("베팅금은 숫자 입력만 가능합니다.");
		            continue;
		         }

		         if (intMoney > voP.getMoney()) {
		            System.out.println("베팅금은 소지금액보다 작아야합니다.");
		            continue;
		         } 
		                  
		         else if (!(intMoney % 100 == 0)) { // 베팅금은 100 단위만 가능
		            System.out.println("베팅금은 100단위로만 가능합니다.");
		            continue;
		         }

		         int sum = voP.getMoney() - intMoney;
		         voP.setMoney(sum);
		         
		         

		         return intMoney;
		      }
		   }

	// TODO 카드 한 장을 보여주는 메소드
	protected void showOneCard(List<DeckVO> list, int num) {

		// list의 num번째 카드를 보여주는 메소드
		DeckVO vo = list.get(num);
		System.out.println(vo.getDeck());
	}

	// TODO 초기 카드 보여주는 메소드
	protected void showCard() {

		// 딜러의 카드 보여줌
		System.out.println(voD.getName() + "의 카드는 다음과 같습니다.");
		// 첫번째 카드만 보여주고 한장은 히든카드로 남겨둠
		this.showOneCard(voD.getDeckList(), 0);
		System.out.println("????");
		System.out.println("-".repeat(lineNum));

		// 플레이어의 카드 보여줌
		System.out.println(voP.getName() + "의 카드는 다음과 같습니다.");
		this.showOneCard(voP.getDeckList(), 0);
		this.showOneCard(voP.getDeckList(), 1);

		// 만약에 플레이어가 블랙잭이라면
		if (voP.getBj()) {
			// 블랙잭 선언
			System.out.println("블랙잭!");
		} else {
			// 아니라면 점수 보여주기
			System.out.println(voP.getName() + "의 점수 합 : " + voP.getScore());
		}

		System.out.println("-".repeat(lineNum));
	}

	// TODO 조아영
	// 덱을 셔플하는 메소드
	@Override
	public void shuffleDeck() {

		/*
		 * 생성된 덱을 셔플하는 메소드
		 * 
		 * 셔플 후 리스트 deckList에 저장됨 deckIndex 값을 참조하여 deckList에서 차례대로 덱을 하나씩 뽑아내면
		 * deckList는 이미 셔플된 덱의 리스트이기 때문에 랜덤으로 덱을 뽑을 수 있음과 동시에 중복성검사도 할 필요가 없다.
		 * 
		 */
//			Collections.shuffle(deckList); 

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
//			for(int i = 0 ; i < dSize ; i ++) {
//				DeckVO3 vo = deckList.get(i) ;
//				System.out.println(vo.toString());
//			}

	}

	// TODO 조아영
	// 카드를 한 장씩 나눠주는 메소드
	@Override
	public void handDeck(PlayerVO vo) {

		// 랜덤셔플된 deckList에서
		// deckList번째에 해당하는 카드 정보 하나를 불러와서
		// vo의 deckList에 add한다

		vo.setDeckList(deckList, deckIndex);
		// deckList.add(vo.get(num));

		// 카드를 한 장씩 나눠줄 때 마다
		// deckList의 deckIndex번째 카드의 점수를
		// vo의 score에 누적한다.
		DeckVO vo1 = deckList.get(deckIndex);
		vo.setAddScore(vo1.getValue());

		deckIndex++; // deckIndex 값은 handDeck이 실행될때마다 증가
	}

	// TODO 김소정
	// 블랙잭 판단 메소드
	@Override
	public void checkBJ(PlayerVO vo) {

		List<DeckVO> voList0 = vo.getDeckList();
		DeckVO vo0 = voList0.get(0);

		List<DeckVO> voList1 = vo.getDeckList();
		DeckVO vo1 = voList1.get(1);

		if (vo0.getValue() == 1 && vo1.getValue() == 10) {
			vo.setBj(true);
		} else if (vo1.getValue() == 1 && vo0.getValue() == 10) {
			vo.setBj(true);
		}
	}

	// 일단 진행하려고
	// 혜미씨 메소드 PlayerVO 수정했어요

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

	         if (hOs.equalsIgnoreCase("hit")) {
	            this.hit(voP); // 플레이어가 현재 가진 카드합이 리턴되는 메서드

	            if (voP.getScore() > 21) {
	               voP.setBust(true);
	               System.out.println("!BUST!");
	               break;
	            } else if (voP.getScore() == 21) {
	               System.out.println("Perfect");
	               break;
	            }
	            // if 점수가 21이상(bust)이면 break, 아니면 반복(이미됨)
	         } else if (hOs.equalsIgnoreCase("stand")) {
	            System.out.println("-".repeat(lineNum));
	            System.out.println("플레이어가 STAND를 선언했습니다");
	            break;
	         } else {
	            System.out.println("\n!입력 오류!");
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
	         // 딜러 현재 가진 카드합이 리턴되는 메서드
	         if (voD.getScore() > 16) {
	            break;
	         } else {
	            this.hit(voD);
	         }
	      } // while end (딜러)

	      // 만약 딜러가 버스트라면
	      // voD의 bust판단 변수를 true로 처리하여 결과에서
	      // Bust때 딜러의 승리로 판정되지 않도록 한다.
	      if (voD.getScore() > 21)
	         voD.setBust(true);

	   }

	// TODO 장혜미
	// 히트 스탠드 묻는 메서드
	protected String askhOs() {

		System.out.println();
		System.out.println("HIT OR STAND?");
		System.out.println("HIT : hit");
		System.out.println("STAND : stand");
		System.out.print(">> ");
		String answer = scan.nextLine();

		return answer;
	}

	// TODO 장혜미
	// 카드 점수가 합산되는 히트 메서드
	protected void hit(PlayerVO vo) {
		this.handDeck(vo);

		int nSize = vo.getDeckList().size();

		System.out.println("-".repeat(lineNum));

		System.out.println(vo.getName() + "의 카드는 다음과 같습니다");
		for (int i = 0; i < nSize; i++) {
			System.out.println(vo.getDeckList().get(i).getDeck());
		}

		System.out.println("점수 합 : " + vo.getScore());
		System.out.println("-".repeat(lineNum));

	}

	// TODO 게임 결과 창
	@Override
	public void gameResult() {

		System.out.println();
		System.out.println("*".repeat(lineNum));
		System.out.println("···게임결과···");
		System.out.println("*".repeat(lineNum));
		System.out.println();

		printResult(voP);
		printResult(voD);

	}

	// TODO 결과화면 출력 메소드
	public void printResult(PlayerVO vo) {

		System.out.println();
		System.out.println(vo.getName() + "님의 게임결과");
		System.out.println("-".repeat(lineNum));

		showResultCard(vo.getDeckList());

		System.out.println(".".repeat(lineNum));

		// 블랙잭 표시
		if (vo.getBj())
			System.out.println(vo.getName() + " 블랙잭!");

		// BUST 표시
		else if (vo.getBust())
			System.out.println(vo.getName() + " BUST!");

		// 둘 다 아니면 점수 출력
		else
			System.out.println(vo.getName() + "님의 점수 : " + vo.getScore());

		System.out.println("-".repeat(lineNum));

	}

	// TODO 카드 리스트 보여주는 메소드
	protected void showResultCard(List<DeckVO> list) {

		int nSize = list.size();
		for (int i = 0; i < nSize; i++) {
			this.showOneCard(list, i);
		}
	}

	// TODO 최선영 돈계산
	@Override
	public void gamerMoney() {

	      // 인슈어런스 진행했을 시, 딜러가 블랙잭이 맞았다면 인슈어런스 금액 돌려줌
	      if (inYN && voD.getBj()) {
	         System.out.println("인슈어런스 보험금을 돌려드립니다.");
	         System.out.println((inMoney * 2) + "원 지급");
	         voP.setMoney(voP.getMoney() + (inMoney * 2));
	      }

	      Integer pScore = voP.getScore(); // 플레이어의 최종 점수
	      Integer dScore = voD.getScore(); // 딜러의 최종 점수

	      // 2021.04.23 블랙잭으로 인한 승부계산 <-> 블랙잭 아닐 때 승부 계산 함수 자리 바꾸고 return 추가
	      if (voP.getBj()) { // 플레이어가 블랙잭이라면
	         if (voD.getBj()) { // 딜러가 블랙잭인지도 확인하고 딜러도 블랙잭이라면
	            push(); // 무승부
	            return; // 아래 if 문은 실행되지 않게 return 해버리기
	         }
	         else { // 딜러는 블랙잭이 아니라면
	            win_bj(); // 플레이어 승리 ~!
	            return;
	         }
	      }
	      
	      if (voP.getBust()) { // 플레이어가 BUST
	         if (voD.getBust()) push(); // 딜러도 BUST 라면 무승부
	         else lose(); // 딜러는 BUST 아니라면 패배
	      } else if (((pScore > dScore) || voD.getBust())) win();   // 플레이어는 BUST가 아니면서 딜러보다 점수가 높거나, 딜러가 BUST면 승리
	      
	      else if (pScore < dScore) lose();   // 플레이어, 딜러는 BUST가 아니면서 플레이어가 딜러보다 점수가 낮으면 패배
	      
	      else if (pScore == dScore) push();   // 플레이어, 딜러는 BUST가 아니면서 플레이어와 딜러 점수가 같으면 무승부

	      else if (voD.getBj()) lose();

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
		if(voP.getMoney() == 0) voP.setHaveMoney(false);
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

		while (true) {

			DeckVO vo1 = voD.getDeckList().get(0);

			// 딜러VO 의
			// deckList(지니고 있는 카드 리스트)의
			// 첫번째 카드의 값이 ( 공개된 카드)
			// A이면 플레이어에게 인슈런스 할 건지 물어본다.
			if (vo1.getValue() == 1) {

				// 인슈런스 금액은 베팅금의 절반이다
				inMoney = betMoney / 2;
				System.out.println("\n" + "-".repeat(lineNum));
				System.out.println("\n인슈어런스 하시겠습니까?");
				System.out.println(" Y : 네  /  N : 아니오");
				String strIn = scan.nextLine();

				if (strIn.equals("Y")) {

					// 인슈러언스 선택시
					System.out.println("\n인슈어런스로 " + inMoney + "를 지불하였습니다.");
					System.out.println("-".repeat(lineNum));

					// 플레이어는 인슈어런스 금액 지불
					voP.setMoney(voP.getMoney() - inMoney);

					// 인슈어런스 여부를 true로 출력 후
					// 딜러가 블랙잭이 나올 경우 쓸 수 있도록
					inYN = true;
					return;
				}

				// 인슈어런스 진행 X
				else if (strIn.equals("N")) {
					System.out.println("\n게임을 계속 진행합니다.");
					System.out.println("-".repeat(lineNum));
					return;
				}

				// 입력 오류
				else {
					System.out.println("Y 나 N만 입력하세요.");
					continue;
				}
			}

			//// 딜러VO 의
			// deckList(지니고 있는 카드 리스트)의
			// 첫번째 카드의 값이 ( 공개된 카드)
			// A가 아니면 그냥 리턴
			else
				return;

		} // end while

	}

	// TODO 김소정
	// 게임 저장하기
	public void saveGame(String id, Integer money) {

		/*
		 * 입력한 아이디로 게임을 저장한다 voP.setName(this.inputGamer());
		 */

		while (true) {
			System.out.println("진행상황을 저장합니다." + "이미 저장되었다면 덮어씌움");

			FileWriter fileWriter = null;
			PrintWriter out = null;

			try {
				fileWriter = new FileWriter(basePath + id);
				out = new PrintWriter(fileWriter);

				out.printf("%s:%d\n", id, money);
				out.flush();
				out.close();
				System.out.println("\n-save complete-");
				break;

			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(id + "파일 생성 오류");
				System.out.println("파일이름을 다시입력하세요");
				continue;
			}
		} // while end

	}

	// 파일이름을 불러온다
	// 건너뛰거나 파일에 문제가있으면 새로운 게임으로 시작한다.

}
