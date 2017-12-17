package com.lyp.gamedemo.flappybird.object;

import java.util.Random;

import com.lyp.gamedemo.flappybird.FlappyBird.LayerID;
import com.lyp.gamedemo.flappybird.FlappyBird.ObjectID;
import com.lyp.gamedemo.flappybird.shader.PipeShader;
import com.lyp.uge.gameObject.Sprite2D;
import com.lyp.uge.model.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.Shader;

public class Pipe extends Sprite2D {
	
	private static final int WIDTH = 20;
	private static final int HEIGHT = 60;
	
	private int direction;
	public boolean canControl = true;
	
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
		this.setLawyer(LayerID.INSTANCE.value());
		this.setX(x);
		this.direction = direction;
		this.setY(direction == 0 ? -80+(10-r.nextInt(20)) : 20+((10-r.nextInt(20))));
		this.speed = 0.007f;
		this.model = new TextureModel(
			loader.loadToVAO(vertices, textureCoordinates, indices),
			loader.loadTexture("src/com/lyp/gamedemo/flappybird/res/pipe.png"));
		
		this.setShader(new PipeShader());
	}

	@Override
	public void update() {
		doMove(-speed, 0.0f, 0.0f);
	}

	@Override
	public void render(Renderer renderer, Shader shader) {
		((PipeShader) shader).setupPipeDirection(direction);
	}

	@Override
	public void destory() {
	}
}
