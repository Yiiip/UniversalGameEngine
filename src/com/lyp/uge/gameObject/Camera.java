package com.lyp.uge.gameObject;

import com.lwjgl.util.vector.Vector3f;
import com.lyp.uge.input.Input;
import com.lyp.uge.input.Keyboard;

public class Camera {

	private Vector3f position;
	private float pitch; //倾斜度
	private float yaw; //偏航 left or right the camera is aiming
	private float roll; //侧面翻转 how much it's tilted(倾斜) to one side
	private float moveSpeed;
	
	private boolean control = true;
	
	public Camera() {
		position = new Vector3f(0, 0, 0);
		moveSpeed = 0.05f;
	}

	public void onMove() {
		if (!control) {
			return;
		}
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
		if (Input.getInstance().isKeyDown(Keyboard.KEY_LEFT)) {
			yaw -= moveSpeed * 10;
		}
		if (Input.getInstance().isKeyDown(Keyboard.KEY_RIGHT)) {
			yaw += moveSpeed * 10;
		}
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getRoll() {
		return roll;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public void setMoveSpeed(float moveSpeed) {
		this.moveSpeed = moveSpeed;
	}
	
	public float getMoveSpeed() {
		return moveSpeed;
	}
	
	public boolean isControlEnable() {
		return control;
	}
	
	public void setControl(boolean enable) {
		this.control = enable;
	}
}
