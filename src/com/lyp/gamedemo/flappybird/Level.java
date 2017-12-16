package com.lyp.gamedemo.flappybird;

import com.lyp.gamedemo.flappybird.object.Bg;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.StaticShader;

public class Level {

	private Renderer mRenderer;
	private StaticShader mShader;
	
	private Bg[] bgs;
	
	public Level() {
		bgs = new Bg[4];
		for (int i = 0; i < bgs.length; i++) {
			bgs[i] = new Bg(i * Bg.normalize(75));
		}
		mShader = new StaticShader("src/com/lyp/gamedemo/flappybird/shader/bg.vs" , "src/com/lyp/gamedemo/flappybird/shader/bg.fs");
		mRenderer = new Renderer(mShader);
	}
	
	public void update() {
		for (int i = 0; i < bgs.length; i++) {
			bgs[i].update();
			if (bgs[i].getPosition().x < -Bg.normalize(75)) {
				bgs[i].getPosition().x = Bg.normalize(75) * (bgs.length-1) - 0.01f;
			}
		}
	}
	
	public void render() {
		mRenderer.prepare();
		mShader.start();
		for (int i = 0; i < bgs.length; i++) {
			bgs[i].render(mRenderer, mShader);
		}
		mShader.stop();
	}
	
	public boolean isGameOver() {
		return false;
	}
}
