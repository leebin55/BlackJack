package com.heart.game;

import com.heart.impl.BlackJackRuleImplV1;
import com.heart.impl.TempBlackJackRuleImplV1;
import com.heart.service.BlackjackRule;

public class Ex_02 {
	public static void main(String[] args) {

		BlackjackRule bj = new BlackJackRuleImplV1();

		bj.gameMainScreen();

	}
}
