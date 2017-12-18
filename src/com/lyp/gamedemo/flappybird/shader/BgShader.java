package com.lyp.gamedemo.flappybird.shader;

import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.shader.Static2dShader;

public class BgShader extends Static2dShader {
	
	protected int uniform_birdPos;

	public BgShader() {
		super("src/com/lyp/gamedemo/flappybird/shader/bg.vert",
			"src/com/lyp/gamedemo/flappybird/shader/bg.frag");
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		uniform_birdPos = super.getUniformLocation("birdPos");
	}
	
	public void setupBirdUniform(Vector3f position) {
		super.loadVector(uniform_birdPos, position);
	}
}
