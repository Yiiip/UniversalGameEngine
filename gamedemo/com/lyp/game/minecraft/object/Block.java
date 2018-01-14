package com.lyp.game.minecraft.object;

import org.lwjgl.util.vector.Vector3f;
import com.lyp.uge.gameObject.GameObject;
import com.lyp.uge.prefab.TextureModel;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.Shader;

public class Block extends GameObject {
	
	public Block(TextureModel model, Vector3f position) {
		this.model = model;
		this.position = position;
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
