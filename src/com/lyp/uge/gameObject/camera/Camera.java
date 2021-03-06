package com.lyp.uge.gameObject.camera;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.gameObject.GameObject;
import com.lyp.uge.input.MouseInput;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.Shader;

import static com.lyp.uge.input.Mouse.*;
import static com.lyp.uge.input.Keyboard.*;

public class Camera extends GameObject {

	protected float pitch; //倾斜度. 上下环顾，上负下正
	protected float yaw; //偏航 left or right the camera is aiming. 左右环顾，左负右正
	protected float roll; //侧面翻转 how much it's tilted(倾斜) to one side. 侧旋
	
	protected float arountSpeed = 0.05f;
	
	protected boolean control = true;
	
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
				float yawTemp = yaw < 0 ? 180-(Math.abs(yaw)+90)%360 : (yaw+90)%360;
				position.z -= speed * Math.cos(Math.toRadians(yawTemp));
				position.x += speed * Math.sin(Math.toRadians(yawTemp));
			}
			if (isKeyPressed(KEY_A)) {
				float yawTemp = yaw < 0 ? 180-(Math.abs(yaw)-90)%360 : (yaw-90)%360;
				position.z -= speed * Math.cos(Math.toRadians(yawTemp));
				position.x += speed * Math.sin(Math.toRadians(yawTemp));
			}
			if (isKeyPressed(KEY_LEFT_SHIFT)) {
				position.y -= speed;
			}
			if (isKeyPressed(KEY_W)) {
				float yawTemp = yaw < 0 ? 360-Math.abs(yaw)%360 : yaw%360;
				position.z -= speed * Math.cos(Math.toRadians(yawTemp));
				position.x += speed * Math.sin(Math.toRadians(yawTemp));
			}
			if (isKeyPressed(KEY_S)) {
				float yawTemp = yaw < 0 ? 360-(Math.abs(yaw)+180)%360 : (yaw+180)%360;
				position.z -= speed * Math.cos(Math.toRadians(yawTemp));
				position.x += speed * Math.sin(Math.toRadians(yawTemp));
			}
			if (isKeyPressed(KEY_LEFT)) {
				yaw -= arountSpeed * 10;
			}
			if (isKeyPressed(KEY_RIGHT)) {
				yaw += arountSpeed * 10;
			}
			if (isKeyPressed(KEY_Q)) {
				roll += arountSpeed * 10;
			}
			if (isKeyPressed(KEY_E)) {
				roll -= arountSpeed * 10;
			}
			
			if (isMousePressed(MOUSE_BUTTON_RIGHT)) {
				Vector2f rotVec = MouseInput.getInstance().getDeltaVec();
				yaw += arountSpeed * rotVec.y * MOUSE_SENSITIVITY;
				pitch += arountSpeed * rotVec.x * MOUSE_SENSITIVITY;
			}
			
			if (isWheelScrollUp()) {
				float spd = Math.abs(speed * getWheelOffests().y);
				float yawTemp = yaw < 0 ? 360-Math.abs(yaw)%360 : yaw%360;
				position.z -= spd * Math.cos(Math.toRadians(yawTemp));
				position.x += spd * Math.sin(Math.toRadians(yawTemp));
			}
			if (isWheelScrollDown()) {
				float yawTemp = yaw < 0 ? 360-(Math.abs(yaw)+180)%360 : (yaw+180)%360;
				position.z -= speed * Math.cos(Math.toRadians(yawTemp));
				position.x += speed * Math.sin(Math.toRadians(yawTemp));
			}
		}
	}
	
	@Override
	public void render(Renderer renderer, Shader shader) {
	}
	
	@Override
	public void destory() {
	}
	
	public Vector3f getFacingVector() {
		float yawTemp = yaw < 0 ? 360-Math.abs(yaw)%360 : yaw%360;
		return new Vector3f((float) (Math.sin(Math.toRadians(yawTemp))), 0, (float) (-Math.cos(Math.toRadians(yawTemp))));
	}

	public Vector3f getUpwardVector() {
		return new Vector3f(0, 1, 0); //TODO
	}
	
	public void invertPitch() {
		pitch = -pitch;
	}
	
	/**
	 * Set up or down angle that camera is aiming
	 * @param pitch
	 */
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	/**
	 * Set dip angle to a side.
	 * @param roll
	 */
	public void setRoll(float roll) {
		this.roll = roll;
	}
	
	public float getRoll() {
		return roll;
	}
	
	/**
	 * Set left or right angle that camera is aiming.
	 * @param yaw
	 */
	public void setYaw(float yaw) {
		this.yaw = yaw;
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
	
	public void setArountSpeed(float arountSpeed) {
		this.arountSpeed = arountSpeed;
	}
	
	public float getArountSpeed() {
		return arountSpeed;
	}
	
	public String toShortString() {
		return "[ pitch " + pitch + " | "
				+ "yaw " + yaw + " | "
				+ "roll " + roll + " ]";
	}
}
