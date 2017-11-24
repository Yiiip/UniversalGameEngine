package com.lyp.uge.demo;

import com.lyp.uge.game.GameApplication;
import com.lyp.uge.logger.Logger;
import com.lyp.uge.logger.Logger.Level;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.RawModel;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.StaticShader;

public class Test extends GameApplication {

	float[] vertices = { 
		-0.5f, 0.5f, 0, 
		-0.5f, -0.5f, 0, 
		0.5f, -0.5f, 0, 
		0.5f, 0.5f, 0f 
	};

	int[] indices = { 
		0, 1, 3, 
		3, 1, 2 
	};
	private Loader loader = new Loader();
	private Renderer renderer = new Renderer();
	private StaticShader shader;
	private RawModel model;

	@Override
	protected void onCreate(int winWidth, int winHeight, String winTitle) {
		super.onCreate(winWidth, winHeight, winTitle);
		Logger.setLogOutLevel(Level.DEBUG);
	}

	@Override
	protected void afterCreate() {
		model = loader.loadToVAO(vertices, indices);
		shader = new StaticShader();
	}

	@Override
	protected void onUpdate() {
	}

	@Override
	protected void onRender() {
		renderer.prepare();
		shader.start();
		renderer.render(model);
		shader.stop();
	}

	@Override
	protected void onDestory() {
		shader.cleanUp();
		loader.cleanUp();
	}

	public static void main(String[] args) {
		new Test().start();
	}

}
