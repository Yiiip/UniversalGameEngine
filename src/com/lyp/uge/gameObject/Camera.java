package com.lyp.uge.gameObject;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import com.lyp.uge.input.MouseInput;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.Shader;

import static com.lyp.uge.input.Mouse.*;
import static com.lyp.uge.input.Keyboard.*;

public class Camera extends GameObject {

	private float pitch; //倾斜度. 上下环顾，上负下正
	private float yaw; //偏航 left or right the camera is aiming. 左右环顾，左负右正
	private float roll; //侧面翻转 how much it's tilted(倾斜) to one side. 侧旋
	
	private float arountSpeed = 0.05f;
	
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
				position.z += Math.abs(speed * getWheelOffests().y);
			}
		}
	}
	
	@Override
	public void render(Renderer renderer, Shader shader) {
	}
	
	@Override
	public void destory() {
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
