package com.lyp.gamedemo.flappybird.shader;

import com.lyp.uge.shader.Static2dShader;

public class PipeShader extends Static2dShader {
	
	//protected int uniform_birdPos;
	protected int uniform_pipeDirectioin;

	public PipeShader() {
		super("src/com/lyp/gamedemo/flappybird/shader/pipe.vert",
				"src/com/lyp/gamedemo/flappybird/shader/pipe.frag");
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		//uniform_birdPos = super.getUniformLocation("birdPos");
		uniform_pipeDirectioin = super.getUniformLocation("pipeDirection");
	}
	
//	public void setupBirdUniform(Vector3f position) {
//		super.loadVector(uniform_birdPos, position);
//	}
	
	/**
	 * @param dir
	 * 0: top pipe. 
	 * 1: bottom pipe.
	 */
	public void setupPipeDirection(int dir) {
		super.loadInt(uniform_pipeDirectioin, dir);
	}
}
