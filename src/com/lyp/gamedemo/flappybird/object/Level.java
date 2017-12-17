package com.lyp.gamedemo.flappybird.object;

import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.Renderer2dManager;

public class Level {
	
	private Bg[] bgs;
	
	public Level(Loader loader) {
		bgs = new Bg[4];
		for (int i = 0; i < bgs.length; i++) {
			bgs[i] = new Bg(loader, i);
		}
	}
	
	public void update() {
		for (int i = 0; i < bgs.length; i++) {
			if (bgs[i].getX() <= -bgs[0].getWidth()) {
				bgs[i].setX((bgs[0].getWidth()) * 3 - 0.5f);
				//Logger.d("BG", "第"+(i+1)+"个背景重置了");
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
	
	public boolean isGameOver() {
		return false;
	}
}
