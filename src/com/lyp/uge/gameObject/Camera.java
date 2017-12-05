package com.lyp.uge.gameObject;

import com.lwjgl.util.vector.Vector2f;
import com.lwjgl.util.vector.Vector3f;
import com.lyp.uge.input.MouseInput;
import com.lyp.uge.logger.Logger;

import static com.lyp.uge.input.Mouse.*;
import static com.lyp.uge.input.Keyboard.*;

public class Camera extends GameObject {

	private float pitch; //倾斜度. 上下环顾，上负下正
	private float yaw; //偏航 left or right the camera is aiming. 左右环顾，左负右正
	private float roll; //侧面翻转 how much it's tilted(倾斜) to one side. 侧旋
	
	private boolean control = true;
	
	public Camera() {
		position = new Vector3f(0, 0, 0);
		speed = 0.05f;
		pitch = 0.0f;
	}
	
	@Override
	public void update() {
		if (control) {
			if (isKeyPressed(KEY_SPACE)) {
				position.y += speed;
			}
			if (isKeyPressed(KEY_D)) {
				position.x += speed;
			}
			if (isKeyPressed(KEY_A)) {
				position.x -= speed;
			}
			if (isKeyPressed(KEY_LEFT_SHIFT)) {
				position.y -= speed;
			}
			if (isKeyPressed(KEY_W)) {
				position.z -= speed;
			}
			if (isKeyPressed(KEY_S)) {
				position.z += speed;
			}
			if (isKeyPressed(KEY_LEFT)) {
				yaw -= speed * 10;
			}
			if (isKeyPressed(KEY_RIGHT)) {
				yaw += speed * 10;
			}
			if (isKeyPressed(KEY_Q)) {
				roll += speed * 10;
			}
			if (isKeyPressed(KEY_E)) {
				roll -= speed * 10;
			}
			
			if (isMousePressed(MOUSE_BUTTON_RIGHT)) {
				Vector2f rotVec = MouseInput.getInstance().getDeltaVec();
				yaw += speed * rotVec.y * MOUSE_SENSITIVITY;
				pitch += speed * rotVec.x * MOUSE_SENSITIVITY;
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
	
	public String toShortString() {
		return "[ pitch " + pitch + " | "
				+ "yaw " + yaw + " | "
				+ "roll " + roll + " ]";
	}
}
