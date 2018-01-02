package com.lyp.game.minecraft.object;

import org.lwjgl.util.vector.Vector3f;
import com.lyp.uge.gameObject.GameObject;
import com.lyp.uge.model.TextureModel;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.Shader;

public class CubeObject extends GameObject {
	
	public CubeObject(TextureModel model, Vector3f position, float rotateX, float rotateY, float rotateZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotateX = rotateX;
		this.rotateY = rotateY;
		this.rotateZ = rotateZ;
		this.scale = scale;
	}

	@Override
	public void update() {
	}

	@Override
	public void render(Renderer renderer, Shader shader) {
	}

	@Override
	public void destory() {
	}
}
