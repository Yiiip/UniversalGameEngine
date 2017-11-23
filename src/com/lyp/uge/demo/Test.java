package com.lyp.uge.demo;

import com.lyp.uge.game.GameApplication;

public class Test extends GameApplication {
	
	@Override
	protected void onCreate(int winWidth, int winHeight, String winTitle) {
		super.onCreate(winWidth, winHeight, winTitle);
	}

	@Override
	protected void onUpdate() {
	}

	@Override
	protected void onRender() {
	}

	@Override
	protected void onDestory() {
		
	}
	
	public static void main(String[] args) {
		new Test().start();
	}

}
