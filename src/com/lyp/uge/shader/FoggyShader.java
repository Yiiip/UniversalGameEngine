package com.lyp.uge.shader;

import org.lwjgl.util.vector.Vector3f;

public class FoggyShader extends SpecularLightShader {

	private static String FOGGY_VERTEX_FILE = "shader/foggy.vert";
	private static String FOGGY_FRAGMENT_FILE = "shader/foggy.frag";
	
	protected int uniform_skyColor;
	
	public FoggyShader() {
		super(FOGGY_VERTEX_FILE, FOGGY_FRAGMENT_FILE);
	}
	
	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		uniform_skyColor = super.getUniformLocation("skyColor");
	}
	
	public void setupSkyColor(float r, float g, float b) {
		super.loadVector(uniform_skyColor, new Vector3f(r, g, b));
	}
}
