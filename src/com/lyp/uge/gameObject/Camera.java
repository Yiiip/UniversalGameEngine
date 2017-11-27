package com.lyp.uge.gameObject;

import com.lwjgl.util.vector.Vector3f;
import com.lyp.uge.input.Input;
import com.lyp.uge.input.Keyboard;

public class Camera {

	private Vector3f position;
	private float pitch; //倾斜度
	private float yaw; //偏航 left or right the camera is aiming
	private float roll; //侧面翻转 how much it's tilted(倾斜) to one side
	
	public Camera() {
		position = new Vector3f(0, 0, 0);
	}

	public void onMove() {
		if (Input.isKeyDown(Keyboard.KEY_W)) {
			position.z -= 0.05f;
		}
		if (Input.isKeyDown(Keyboard.KEY_D)) {
			position.x += 0.05f;
		}
		if (Input.isKeyDown(Keyboard.KEY_A)) {
			position.x -= 0.05f;
		}
		if (Input.isKeyDown(Keyboard.KEY_S)) {
			position.z += 0.05f;
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
}
