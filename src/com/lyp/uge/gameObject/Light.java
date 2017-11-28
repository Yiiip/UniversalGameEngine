package com.lyp.uge.gameObject;

import com.lwjgl.util.vector.Vector3f;
import com.lyp.uge.input.Input;
import com.lyp.uge.input.Keyboard;

public class Light {

	private Vector3f position;
	private Vector3f color;
	private float moveSpeed = 0.06f;

	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
	}
	
	public void onMove() {
		if (Input.getInstance().isKeyDown(Keyboard.KEY_W)) {
			position.y += moveSpeed;
		}
		if (Input.getInstance().isKeyDown(Keyboard.KEY_D)) {
			position.x += moveSpeed;
		}
		if (Input.getInstance().isKeyDown(Keyboard.KEY_A)) {
			position.x -= moveSpeed;
		}
		if (Input.getInstance().isKeyDown(Keyboard.KEY_S)) {
			position.y -= moveSpeed;
		}
		if (Input.getInstance().isKeyDown(Keyboard.KEY_UP)) {
			position.z -= moveSpeed;
		}
		if (Input.getInstance().isKeyDown(Keyboard.KEY_DOWN)) {
			position.z += moveSpeed;
		}
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}

}
