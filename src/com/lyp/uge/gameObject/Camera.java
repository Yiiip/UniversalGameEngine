package com.lyp.uge.gameObject;

import com.lwjgl.util.vector.Vector3f;

import static com.lyp.uge.input.Keyboard.*;

public class Camera extends GameObject {

	private float pitch; //倾斜度
	private float yaw; //偏航 left or right the camera is aiming
	private float roll; //侧面翻转 how much it's tilted(倾斜) to one side
	
	private boolean control = true;
	
	public Camera() {
		position = new Vector3f(0, 0, 0);
		speed = 0.05f;
	}
	
	@Override
	public void update() {
		if (control) {
			if (isKeyPressed(KEY_W)) {
				position.y += speed;
			}
			if (isKeyPressed(KEY_D)) {
				position.x += speed;
			}
			if (isKeyPressed(KEY_A)) {
				position.x -= speed;
			}
			if (isKeyPressed(KEY_S)) {
				position.y -= speed;
			}
			if (isKeyPressed(KEY_UP)) {
				position.z -= speed;
			}
			if (isKeyPressed(KEY_DOWN)) {
				position.z += speed;
			}
			if (isKeyPressed(KEY_LEFT)) {
				yaw -= speed * 10;
			}
			if (isKeyPressed(KEY_RIGHT)) {
				yaw += speed * 10;
			}
		}
	}
	
	@Override
	public void render() {
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
	
	public boolean isControlEnable() {
		return control;
	}
	
	public void setControl(boolean enable) {
		this.control = enable;
	}

}
