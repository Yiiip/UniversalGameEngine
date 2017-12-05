package com.lyp.uge.gameObject;

import org.lwjgl.util.vector.Vector3f;

import static com.lyp.uge.input.Keyboard.*;

public class Light extends GameObject {

	private Vector3f color;

	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
		this.speed = 0.06f;
	}

	@Override
	public void update() {
		if (isKeyPressed(KEY_I)) {
			position.y += speed;
		}
		if (isKeyPressed(KEY_L)) {
			position.x += speed;
		}
		if (isKeyPressed(KEY_J)) {
			position.x -= speed;
		}
		if (isKeyPressed(KEY_K)) {
			position.y -= speed;
		}
		if (isKeyPressed(KEY_P)) {
			position.z -= speed;
		}
		if (isKeyPressed(KEY_SEMICOLON)) {
			position.z += speed;
		}
	}

	@Override
	public void render() {
	}

	public Vector3f getColor() {
		return color;
	}
	
	public void setColor(Vector3f color) {
		this.color = color;
	}
}
