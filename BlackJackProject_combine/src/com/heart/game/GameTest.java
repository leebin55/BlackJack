package com.heart.game;

import com.heart.impl.BlacJackRuleImplV3;
import com.heart.impl.BlackJacReal;
import com.heart.service.BlackjackRule;

public class GameTest {

	public static void main(String[] args) {
		
	
		BlackjackRule bJ = new BlackJacReal();
		bJ.gameMainScreen();
	}
}
