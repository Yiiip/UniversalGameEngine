package com.lyp.uge.gameObject;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import com.lyp.uge.input.KeyboardInput;
import com.lyp.uge.input.MouseInput;
import com.lyp.uge.input.Keyboard.OnKeyboardListener;
import com.lyp.uge.prefab.TextureModel;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.Shader;

public abstract class GameObject {

	protected int id;
	protected TextureModel model;
	protected Vector3f position = new Vector3f(0.0f, 0.0f, 0.0f);
	protected float rotateX = 0.0f;
	protected float rotateY = 0.0f;
	protected float rotateZ = 0.0f;
	protected float scale = 1.0f;
	protected float speed = 0.0f;
	protected float angleSpeed = 0.0f;

	protected boolean isActive = true;
	
	protected OnKeyboardListener onKeyboardListener;
	
	public abstract void update();
	public void render(Renderer renderer, Shader shader) {}
	public abstract void destory();
	
	protected boolean isKeyPressed(int keycode) {
		return KeyboardInput.getInstance().isKeyDown(keycode);
	}
	
	protected boolean isMousePressed(int buttonCode) {
		return MouseInput.getInstance().isMousePressed(buttonCode);
	}
	
	protected boolean isWheelScrollUp() {
		return MouseInput.getInstance().isWheelScrollUp();
	}
	
	protected boolean isWheelScrollDown() {
		return MouseInput.getInstance().isWheelScrollDown();
	}
	
	protected Vector2f getWheelOffests() {
		return MouseInput.getInstance().getWheelOffsets();
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
	
	public void setOnKeyboardListener(OnKeyboardListener onKeyboardListener) {
		this.onKeyboardListener = onKeyboardListener;
		KeyboardInput.getInstance().registerOnKeyboardListener(onKeyboardListener);
	}
	
	public TextureModel getModel() {
		return model;
	}

	public void setModel(TextureModel model) {
		this.model = model;
	}

	public boolean isActive()
	{
		return isActive;
	}

	public void setActive(boolean isActive)
	{
		this.isActive = isActive;
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
	
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	
	public float getSpeed() {
		return speed;
	}
	
	public void setAngleSpeed(float angleSpeed) {
		this.angleSpeed = angleSpeed;
	}
	
	public float getAngleSpeed() {
		return angleSpeed;
	}
	
	public GameObject addFoggy(float fogDensity, float fogGradient) {
		this.model.getTexture().addFoggy(fogDensity, fogGradient);
		return this;
	}
	
	public GameObject removeFoggy() {
		this.model.getTexture().removeFoggy();
		return this;
	}
	
	public boolean isFoggy() {
		return this.model.getTexture().isFoggy();
	}
	
	public float getFoggyDensity() {
		return this.model.getTexture().getFoggyDensity();
	}
	
	public float getFoggyGradient() {
		return this.model.getTexture().getFoggyGradient();
	}
	
	public GameObject addAmbientLightness(float ambientLightness) {
		this.model.getTexture().setAmbientLightness(ambientLightness);
		return this;
	}
	
	@Override
	public String toString() {
		return "Position[" + position.x + ", " + position.y + ", " + position.z + "]  "
				+ "Rotation[" + rotateX + ", " + rotateY + ", " + rotateZ + "]  "
				+ "Scale[" + scale + "]";
	}
}
