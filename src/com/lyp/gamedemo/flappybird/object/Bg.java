package com.lyp.gamedemo.flappybird.object;

import com.lyp.uge.gameObject.GameObject;
import com.lyp.uge.model.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.Shader;
import com.lyp.uge.shader.StaticShader;

public class Bg extends GameObject {
	
	private Loader mLoader = new Loader();
	
	public Bg(float x) {
		float[] vertices = new float[] { 
			-1.0f ,  1.0f, 0.0f,//V0
			-1.0f , -1.0f, 0.0f,//V1 
			-0.25f, -1.0f, 0.0f,//V2
			-0.25f,  1.0f, 0.0f,//V3
		};
		float[] textureCoordinates = new float[] {
			0, 0,
			0, 1,
			1, 1,
			1, 0,
		};
		int[] indices = new int[] {
			0, 1, 2,
			2, 3, 0,
		};
		
		this.speed = 0.004f;
		this.position.x = x;
		this.position.z = 0.0f;
		this.model = new TextureModel(
				mLoader.loadToVAO(vertices, textureCoordinates, indices),
				mLoader.loadTexture("src/com/lyp/gamedemo/flappybird/res/bg.jpg"));
	}

	@Override
	public void update() {
		position.x -= speed;
	}

	@Override
	public void render(Renderer renderer, Shader shader) {
		renderer.render(this, (StaticShader) shader);
	}

	@Override
	public void destory() {
	}
}
