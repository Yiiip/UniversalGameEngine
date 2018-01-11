package com.lyp.uge.gameObject;

import static com.lyp.uge.input.Keyboard.*;

import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.Shader;
import com.lyp.uge.terrain.Terrain;
import com.lyp.uge.terrain.TerrainManager;

public class FirstPersonCamera extends Camera {

	protected static float GRAVITY = -0.02f;
	protected static float JUMP_HEIGHT = 0.4f; //可跳起的高度
	protected static float CAMERA_CHARACTER_HEIGHT = 5.0f; //考虑角色身高后相机所处的高度
	protected static float DEFAULT_TERRAIN_LOWEST_HEIGHT = 0.0f + CAMERA_CHARACTER_HEIGHT; //默认落地后的海拔高度
	
	protected float upwardSpeed = 0.05f;
	protected boolean isInAir = false; //是否已起跳到空中
	
	protected TerrainManager mTerrainManager = null;
	
	@Override
	public void update() {
		super.update();
		
		float landHeight = DEFAULT_TERRAIN_LOWEST_HEIGHT;
		if (mTerrainManager != null) {
			Terrain targetTerrain = mTerrainManager.inside(position.x, position.z);
			if (targetTerrain != null) {
				landHeight = targetTerrain.getHeightOfTerrain(position.x, position.z) + CAMERA_CHARACTER_HEIGHT;
			}
		}
		upwardSpeed += GRAVITY;
		position.y += upwardSpeed;
		if (position.y < landHeight) {
			upwardSpeed = 0.0f;
			position.y = landHeight;
			isInAir = false;
		}
		
		if (isKeyPressed(KEY_G)) {
			jump();
		}
	}
	
	private void jump() {
		if (isInAir) return;
		
		upwardSpeed = JUMP_HEIGHT;
		isInAir = true;
	}

	@Override
	public void render(Renderer renderer, Shader shader) {
		super.render(renderer, shader);
	}

	@Override
	public void destory() {
		super.destory();
	}
	
	public void setTerrainManager(TerrainManager terrainManager) {
		this.mTerrainManager = terrainManager;
	}
}
