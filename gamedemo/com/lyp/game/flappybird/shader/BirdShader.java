package com.lyp.game.flappybird.shader;

import com.lyp.uge.shader.Static2dShader;

public class BirdShader extends Static2dShader {
	
	protected int uniform_birdPos;

	public BirdShader() {
		super("gamedemo/com/lyp/game/flappybird/shader/bird.vert",
			"gamedemo/com/lyp/game/flappybird/shader/bird.frag");
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
	}
}
