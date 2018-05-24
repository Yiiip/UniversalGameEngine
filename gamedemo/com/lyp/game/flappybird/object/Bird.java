package com.lyp.game.flappybird.object;

import com.lyp.game.flappybird.FlappyBird.LayerID;
import com.lyp.game.flappybird.FlappyBird.ObjectID;
import com.lyp.game.flappybird.shader.BirdShader;
import com.lyp.uge.ai.fsm.State;
import com.lyp.uge.ai.fsm.StateMachine;
import com.lyp.uge.gameObject.sprite.Sprite2D;
import com.lyp.uge.input.Mouse;
import com.lyp.uge.prefab.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.Shader;

public class Bird extends Sprite2D {
	
	private static final int SIZE = 10;
	
	public boolean deadAnim = true;

	public static enum BirdState {
		PLAYING(0),
		GAMEOVER(1);

		public int value;
		BirdState(int value) {
			this.value = value;
		}
	}

	public class PlayingState extends State {
		@Override
		public void update(Object object) {
			if (getY() > -110f) {
				gravity();
				doMove(0.0f, speed, 0.0f);
				setRotateZ(angleSpeed * 9f);
				//setRotateZ(speed * 900 + 5);
			} else {
				mStateMachine.setCurrState(BirdState.GAMEOVER.value);
				return;
			}
		}
	}

	public class GameOverState extends State {
		@Override
		public void update(Object object) {
			if (deadAnim) {
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
			}
		}
	}

	private StateMachine mStateMachine;
	
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

		this.mStateMachine = new StateMachine();
		this.mStateMachine
			.addState(BirdState.PLAYING.value, new PlayingState())
			.addState(BirdState.GAMEOVER.value, new GameOverState());
		this.mStateMachine.setCurrState(BirdState.PLAYING.value);
	}

	@Override
	public void update() {
		mStateMachine.update(this);
	}

	private void gravity() {
		if (isMousePressed(Mouse.MOUSE_BUTTON_LEFT)) {
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

	public StateMachine getStateMachine() {
		return mStateMachine;
	}
}
