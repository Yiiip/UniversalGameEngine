package com.lyp.game.flappybird.shader;

import org.lwjgl.util.vector.Vector3f;
import com.lyp.uge.shader.Static2dShader;

public class PipeShader extends Static2dShader {
	
	protected int uniform_birdPosition;
	protected int uniform_pipeDirectioin;

	public PipeShader() {
		super("gamedemo/com/lyp/game/flappybird/shader/pipe.vert",
				"gamedemo/com/lyp/game/flappybird/shader/pipe.frag");
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		uniform_birdPosition = super.getUniformLocation("birdPosition");
		uniform_pipeDirectioin = super.getUniformLocation("pipeDirection");
	}
	
	public void setupBirdPosition(Vector3f position) {
		super.loadVector(uniform_birdPosition, position);
	}
	
	/**
	 * @param dir
	 * 0: top pipe. 
	 * 1: bottom pipe.
	 */
	public void setupPipeDirection(int dir) {
		super.loadInt(uniform_pipeDirectioin, dir);
	}
}
