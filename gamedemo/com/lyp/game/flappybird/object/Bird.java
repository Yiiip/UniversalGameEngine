package com.lyp.game.flappybird.object;

import static com.lyp.uge.input.Keyboard.*;

import com.lyp.game.flappybird.FlappyBird;
import com.lyp.game.flappybird.FlappyBird.LayerID;
import com.lyp.game.flappybird.FlappyBird.ObjectID;
import com.lyp.game.flappybird.FlappyBird.Status;
import com.lyp.game.flappybird.shader.BirdShader;
import com.lyp.uge.gameObject.Sprite2D;
import com.lyp.uge.prefab.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.Shader;

public class Bird extends Sprite2D {
	
	private static final int SIZE = 10;
	
	public boolean deadAnim = true;
	
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
		this.setLayer(LayerID.INSTANCE_FRONT.value());
		this.speed = 0.015f;
		this.angleSpeed = 1.0f;
		this.model = new TextureModel(
			loader.loadToVAO(vertices, textureCoordinates, indices),
			loader.loadTexture("gamedemo/com/lyp/game/flappybird/res/bird.png"));
		
		this.setShader(new BirdShader());
	}

	@Override
	public void update() {
		if (FlappyBird.STATUS == Status.GAMEOVER && deadAnim) {
			setX(getX() + 1);
			speed = 0.015f;
			angleSpeed = 2.0f;
			deadAnim = false;
		}
		if (getY() > -110f) {
			gravity();
			doMove(0.0f, speed, 0.0f);
			setRotateZ(angleSpeed * 9f);
			//setRotateZ(speed * 900 + 5);
		} else {
			if (FlappyBird.STATUS == Status.PLAYING) {
				FlappyBird.STATUS = Status.GAMEOVER;
			}
			return;
		}
	}

	private void gravity() {
		if (isKeyPressed(KEY_G)) {
			speed = 0.015f;
			angleSpeed = 2.0f;
		} else {
			speed -= 0.001f;
			angleSpeed -= 0.1f;
		}
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
