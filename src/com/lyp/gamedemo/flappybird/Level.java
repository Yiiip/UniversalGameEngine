package com.lyp.gamedemo.flappybird;

import com.lyp.uge.gameObject.Camera;
import com.lyp.uge.gameObject.GameObject;
import com.lyp.uge.model.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.Shader;
import com.lyp.uge.shader.StaticShader;

public class Level extends GameObject {

	private Loader mLoader = new Loader();
	private Renderer mRenderer;
	private StaticShader mShader;
	private Camera mCamera;
	
	public Level(Camera camera) {
		mCamera = camera;
		
		float[] vertices = new float[] { 
			-0.5f, 0.5f, 0, //V0
			-0.5f, -0.5f, 0,//V1 
			0.5f, -0.5f, 0, //V2
			0.5f, 0.5f, 0f  //V3
		};
		
		int[] indices = new int[] {
			0, 1, 2,
			2, 3, 0,
		};
		
		float[] textureCoordinates = new float[] {
			0, 1,
			0, 0,
			1, 0,
			1, 1
		};
		
		this.position.z = -0.2f;
		
		this.model = new TextureModel(
				mLoader.loadToVAO(vertices, textureCoordinates, indices),
				mLoader.loadTexture("src/com/lyp/gamedemo/flappybird/res/bg.jpg"));
		mShader = new StaticShader("src/com/lyp/gamedemo/flappybird/shader/bg.vs" , "src/com/lyp/gamedemo/flappybird/shader/bg.fs");
		mRenderer = new Renderer(mShader);
	}
	
	@Override
	public void update() {
	}
	
	private boolean collision() {
		return false;
	}
	
	@Override
	public void render(Renderer renderer, Shader shader) {
		mRenderer.prepare();
		mShader.start();
		mShader.loadViewMatrix(mCamera);
		mRenderer.render(this, mShader);
		mShader.stop();
	}
	
	public boolean isGameOver() {
		return false;
	}

	@Override
	public void destory() {
	}
}
