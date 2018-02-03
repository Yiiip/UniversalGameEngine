package com.lyp.uge.gameObject.sprite;

import com.lyp.uge.gameObject.GameObject;
import com.lyp.uge.shader.Static2dShader;

public abstract class Sprite2D extends GameObject {

	private int layer = 0;
	private float width = 0.0f;
	private float height = 0.0f;
	private Static2dShader shader;
	
	public void doRotate2D(float dz) {
		doRotate(0.0f, 0.0f, dz);
	}
	
	public void setShader(Static2dShader shader) {
		this.shader = shader;
	}
	
	public Static2dShader getShader() {
		return shader;
	}
	
	public void setLayer(int layer) {
		this.layer = layer;
		this.position.z = normalize(layer);
	}
	
	public int getLayer() {
		return layer;
	}
	
	public void setWidth(float width) {
		this.width = normalize(width);
	}
	
	public float getWidth() {
		return unnormalize(width);
	}
	
	public float getRealWidth() {
		return width;
	}
	
	public void setHeight(float height) {
		this.height = normalize(height);
	}
	
	public float getHeight() {
		return unnormalize(height);
	}
	
	public float getRealHeight() {
		return height;
	}
	
	public void setX(float x) {
		this.position.x = normalize(x);
	}
	
	public float getX() {
		return unnormalize(this.position.x);
	}
	
	public void setY(float y) {
		this.position.y = normalize(y);
	}
	
	public float getY() {
		return unnormalize(this.position.y);
	}

	public static float normalize(float n) {
		return n/100;
	}
	
	public static float unnormalize(float n) {
		return n*100;
	}
}
