package com.lyp.uge.gameObject;

import static com.lyp.uge.input.Keyboard.*;

import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.Shader;

public class FirstPersonCamera extends Camera {

	protected static float GRAVITY = -0.02f;
	protected static float JUMP_HEIGHT = 0.4f; //可跳起的高度
	protected static float CAMERA_CHARACTER_HEIGHT = 4.0f; //考虑角色身高后相机所处的高度
	protected static float TERRAIN_LOWEST_HEIGHT = 0.0f + CAMERA_CHARACTER_HEIGHT; //落地海拔高度
	
	protected float upwardSpeed = 0.05f;
	
	@Override
	public void update() {
		super.update();
		upwardSpeed += GRAVITY;
		position.y += upwardSpeed;
		if (position.y < TERRAIN_LOWEST_HEIGHT) {
			upwardSpeed = 0.0f;
			position.y = TERRAIN_LOWEST_HEIGHT;
		}
		
		if (isKeyPressed(KEY_G)) {
			upwardSpeed = JUMP_HEIGHT;
		}
	}

	@Override
	public void render(Renderer renderer, Shader shader) {
		super.render(renderer, shader);
	}

	@Override
	public void destory() {
		super.destory();
	}
}
