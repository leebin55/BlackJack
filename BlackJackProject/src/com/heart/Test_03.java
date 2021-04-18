package com.heart;

import com.heart.service.BlackjackRule;
import com.heart.service.impl.BlackJackImplV1;

public class Test_03 {
	public static void main(String[] args) {
		BlackjackRule bJ = new BlackJackImplV1();
		bJ.gameMainScreen();
	}

}
