package com.lyp.uge.demo;

import com.lyp.uge.game.GameApplication;
import com.lyp.uge.renderEngine.ModelLoader;
import com.lyp.uge.renderEngine.RawModel;
import com.lyp.uge.renderEngine.Renderer;

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
	private ModelLoader loader = new ModelLoader();
	private Renderer renderer = new Renderer();
	private RawModel model;

	@Override
	protected void onCreate(int winWidth, int winHeight, String winTitle) {
		super.onCreate(100, 100, winTitle);
	}

	@Override
	protected void afterCreate() {
		model = loader.loadToVAO(vertices, indices);

	}

	@Override
	protected void onUpdate() {
	}

	@Override
	protected void onRender() {
		renderer.prepare();
		renderer.render(model);
	}

	@Override
	protected void onDestory() {
	}

	public static void main(String[] args) {
		new Test().start();
	}

}
