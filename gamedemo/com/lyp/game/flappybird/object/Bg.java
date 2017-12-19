package com.lyp.game.flappybird.object;

import com.lyp.game.flappybird.FlappyBird;
import com.lyp.game.flappybird.shader.BgShader;
import com.lyp.uge.gameObject.Sprite2D;
import com.lyp.uge.model.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.Shader;

public class Bg extends Sprite2D {
	
	private static final int WIDTH = 100;
	
	public Bg(Loader loader, int index) {
		float[] vertices = new float[] { 
			-1.0f,  1.0f, 0.0f,//V0
			-1.0f, -1.0f, 0.0f,//V1 
			 0.0f, -1.0f, 0.0f,//V2
			 0.0f,  1.0f, 0.0f,//V3
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
		
		this.setWidth(WIDTH);
		this.setLayer(FlappyBird.LayerID.BACKGROUND.value());
		this.setX(index * getWidth());
		
		this.speed = 0.005f;
		this.model = new TextureModel(
				loader.loadToVAO(vertices, textureCoordinates, indices),
				loader.loadTexture("gamedemo/com/lyp/game/flappybird/res/bg.jpg"));
		
		this.setShader(new BgShader());
	}

	@Override
	public void update() {
		doMove(-speed, 0, 0);
	}

	@Override
	public void render(Renderer renderer, Shader shader) {
		((BgShader) shader).setupBirdUniform(FlappyBird.getBird().getPosition());
	}

	@Override
	public void destory() {
	}
}
