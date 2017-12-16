package com.lyp.gamedemo.flappybird;

import com.lyp.uge.game.GameApplication;
import com.lyp.uge.logger.Logger;

public class FlappyBird extends GameApplication {
	
	private Level level;

	@Override
	protected void onInitWindow(int winWidth, int winHeight, String winTitle, boolean winResizeable) {
		super.onInitWindow(1280, 720, "FlappyBird", false);
		Logger.setLogOutLevel(com.lyp.uge.logger.Logger.Level.DEBUG);
	}
	
	@Override
	protected void onCreate() {
		enablePolygonMode();
		level = new Level();
	}

	@Override
	protected void onUpdate() {
		level.update();
	}

	@Override
	protected void onRender() {
		level.render();
	}

	@Override
	protected void onDestory() {

	}

	public static void main(String[] args) {
		new FlappyBird().start();
	}
}
