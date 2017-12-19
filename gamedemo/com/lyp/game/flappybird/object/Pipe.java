package com.lyp.game.flappybird.object;

import java.util.Random;

import org.lwjgl.util.vector.Vector2f;

import com.lyp.game.flappybird.FlappyBird;
import com.lyp.game.flappybird.FlappyBird.LayerID;
import com.lyp.game.flappybird.FlappyBird.ObjectID;
import com.lyp.game.flappybird.FlappyBird.Status;
import com.lyp.game.flappybird.shader.PipeShader;
import com.lyp.uge.ai.Collision;
import com.lyp.uge.gameObject.Sprite2D;
import com.lyp.uge.model.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.Shader;

public class Pipe extends Sprite2D {
	
	private static final int WIDTH = 20;
	private static final int HEIGHT = 80;
	
	private int direction;
	
	private Random r = new Random();
	
	public Pipe(Loader loader, int direction, float x) {
		this.setWidth(WIDTH);
		this.setHeight(HEIGHT);
		
		float[] vertices = new float[] { 
			0.0f, getRealHeight(), 0.0f,
			0.0f, 0.0f, 0.0f,
			getRealWidth(), 0.0f, 0.0f,
			getRealWidth(), getRealHeight(), 0.0f,
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
		
		this.id = ObjectID.PIPE.value();
		this.setLayer(LayerID.INSTANCE_BACK.value());
		this.setX(x);
		this.direction = direction;
		this.setY(direction == 0 ? -100+(10-r.nextInt(20)) : 20+((10-r.nextInt(20))));
		this.speed = 0.007f;
		this.model = new TextureModel(
			loader.loadToVAO(vertices, textureCoordinates, indices),
			loader.loadTexture("gamedemo/com/lyp/game/flappybird/res/pipe.png"));
		
		this.setShader(new PipeShader());
	}

	@Override
	public void update() {
		if (FlappyBird.STATUS == Status.GAMEOVER) {
			return;
		}
		doMove(-speed, 0.0f, 0.0f);
		if (getX() < -95) {
			setX(170f + r.nextInt(12));
			setY(direction == 0 ? -100+(10-r.nextInt(20)) : 20+((10-r.nextInt(20))));
		}
		if (collision()) {
			FlappyBird.STATUS = Status.GAMEOVER;
			return;
		}
	}
	
	private boolean collision() {
		float birdX = FlappyBird.getBird().getX();
		float birdY = FlappyBird.getBird().getY();
		float birdSize = FlappyBird.getBird().getSize();
		Vector2f birdStart = new Vector2f(birdX - birdSize/2, birdY - birdSize/2);
		Vector2f birdEnd = new Vector2f(birdX + birdSize/2, birdY + birdSize/2);
		Vector2f pipeStart = new Vector2f(getX(), getY());
		Vector2f pipeEnd = new Vector2f(getX() + getWidth(), getY() + getHeight());
		return Collision.rectangleCollision(birdStart, birdEnd, pipeStart, pipeEnd);
	}

	@Override
	public void render(Renderer renderer, Shader shader) {
		((PipeShader) shader).setupPipeDirection(direction);
		((PipeShader) shader).setupBirdPosition(FlappyBird.getBird().getPosition());
	}

	@Override
	public void destory() {
	}
}
