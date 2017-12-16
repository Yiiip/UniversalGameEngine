package com.lyp.gamedemo.flappybird.shader;

import com.lyp.uge.shader.Static2dShader;

public class BirdShader extends Static2dShader {
	
	protected int uniform_birdPos;

	public BirdShader() {
		super("src/com/lyp/gamedemo/flappybird/shader/bird.vert",
				"src/com/lyp/gamedemo/flappybird/shader/bird.frag");
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
	}
}
