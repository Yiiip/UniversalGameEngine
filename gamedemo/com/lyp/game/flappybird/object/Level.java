package com.lyp.game.flappybird.object;

import com.lyp.game.flappybird.FlappyBird;
import com.lyp.game.flappybird.FlappyBird.Status;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.Renderer2dManager;

public class Level {
	
	private Loader mLoader;
	private Bg[] bgs;
	
	public Level(Loader loader) {
		mLoader = loader;
		bgs = new Bg[4];
		for (int i = 0; i < bgs.length; i++) {
			bgs[i] = new Bg(mLoader, i);
		}
	}
	
	public void update() {
		if (FlappyBird.STATUS == Status.GAMEOVER) {
			return;
		}
		for (int i = 0; i < bgs.length; i++) {
			if (bgs[i].getX() <= -bgs[0].getWidth()) {
				bgs[i].setX((bgs[0].getWidth()) * 3 - 0.5f);
				//Logger.d("BG", "Number "+(i+1)+" bg reset position.");
			}
			bgs[i].update();
		}
	}
	
	public void addToRender(Renderer2dManager manager) {
		for (int i = 0; i < bgs.length; i++) {
			manager.addObject(bgs[i]);
		}
	}
	
	public void destory() {
		for (int i = 0; i < bgs.length; i++) {
			bgs[i].destory();
		}
	}
}
