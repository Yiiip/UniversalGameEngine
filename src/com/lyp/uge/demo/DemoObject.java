package com.lyp.uge.demo;

import org.lwjgl.util.vector.Vector3f;
import com.lyp.uge.gameObject.GameObject;
import com.lyp.uge.model.TextureModel;

public class DemoObject extends GameObject {
	
	public DemoObject(TextureModel model, Vector3f position, float rotateX, float rotateY, float rotateZ, float scale) {
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
	public void render() {
	}

}
