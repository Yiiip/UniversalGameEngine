package com.lyp.gamedemo.flappybird.object;

import static com.lyp.uge.input.Keyboard.*;

import com.lyp.gamedemo.flappybird.FlappyBird.LayerID;
import com.lyp.gamedemo.flappybird.FlappyBird.ObjectID;
import com.lyp.gamedemo.flappybird.shader.BirdShader;
import com.lyp.uge.gameObject.Sprite2D;
import com.lyp.uge.model.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.Shader;

public class Bird extends Sprite2D {
	
	private static final int SIZE = 10;
	
	public boolean canControl = true;
	
	public Bird(Loader loader) {
		this.setWidth(SIZE);
		this.setHeight(SIZE);
		
		float[] vertices = new float[] { 
			-getRealWidth()/2,  getRealHeight()/2, 0.0f,//V0
			-getRealWidth()/2, -getRealHeight()/2, 0.0f,//V1 
			 getRealWidth()/2, -getRealHeight()/2, 0.0f,//V2
			 getRealWidth()/2,  getRealHeight()/2, 0.0f,//V3
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
		
		this.id = ObjectID.BIRD.value();
		this.setLawyer(LayerID.INSTANCE.value());
		this.model = new TextureModel(
			loader.loadToVAO(vertices, textureCoordinates, indices),
			loader.loadTexture("src/com/lyp/gamedemo/flappybird/res/bird.png"));
		
		this.setShader(new BirdShader());
	}

	@Override
	public void update() {
		if (isKeyPressed(KEY_G)) {
			speed = 0.015f;
			angleSpeed = 2.0f;
		} else {
			speed -= 0.001f;
			angleSpeed -= 0.1f;
		}
		doMove(0.0f, speed, 0.0f);
		setRotateZ(angleSpeed * 10f);
	}

	@Override
	public void render(Renderer renderer, Shader shader) {
	}

	@Override
	public void destory() {
	}

	public float getSize() {
		return getWidth();
	}
}
