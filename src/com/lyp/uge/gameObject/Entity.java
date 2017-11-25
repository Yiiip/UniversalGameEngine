package com.lyp.uge.gameObject;

import com.lwjgl.util.vector.Vector3f;
import com.lyp.uge.model.TextureModel;

public class Entity {

	private TextureModel model;
	private Vector3f position;
	private float rotateX, rotateY, rotateZ;
	private float scale;
	
	public Entity(TextureModel model, Vector3f position, float rotateX, float rotateY, float rotateZ, float scale) {
		this.model = model;
		this.position = position;
		this.rotateX = rotateX;
		this.rotateY = rotateY;
		this.rotateZ = rotateZ;
		this.scale = scale;
	}
	
	public void doMove(float dx, float dy, float dz) {
		this.position.x += dx;
		this.position.y += dy;
		this.position.z += dz;
	}
	
	public void doRotate(float dx, float dy, float dz) {
		this.rotateX += dx;
		this.rotateY += dy;
		this.rotateZ += dz;
	}

	public TextureModel getModel() {
		return model;
	}

	public void setModel(TextureModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public float getRotateX() {
		return rotateX;
	}

	public void setRotateX(float rotateX) {
		this.rotateX = rotateX;
	}

	public float getRotateY() {
		return rotateY;
	}

	public void setRotateY(float rotateY) {
		this.rotateY = rotateY;
	}

	public float getRotateZ() {
		return rotateZ;
	}

	public void setRotateZ(float rotateZ) {
		this.rotateZ = rotateZ;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
}
