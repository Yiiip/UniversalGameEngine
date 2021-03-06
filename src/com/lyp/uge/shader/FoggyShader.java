package com.lyp.uge.shader;

import org.lwjgl.util.vector.Vector3f;

public class FoggyShader extends SpecularLightShader {

	private static String FOGGY_VERTEX_FILE = "shader/foggy.vert";
	private static String FOGGY_FRAGMENT_FILE = "shader/foggy.frag";
	
	protected int uniform_skyColor;
	protected int uniform_fogDensity;
	protected int uniform_fogGradient;
	
	public FoggyShader() {
		super(FOGGY_VERTEX_FILE, FOGGY_FRAGMENT_FILE);
	}
	
	public FoggyShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
	}
	
	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		uniform_skyColor = super.getUniformLocation("skyColor");
		uniform_fogDensity = super.getUniformLocation("fogDensity");
		uniform_fogGradient = super.getUniformLocation("fogGradient");
	}
	
	public void setupSkyColor(float r, float g, float b) {
		super.loadVector(uniform_skyColor, new Vector3f(r, g, b));
	}
	
	public void setupFogDensity(float density) {
		super.loadFloat(uniform_fogDensity, density);
	}
	
	public void setupFogGradient(float gradient) {
		super.loadFloat(uniform_fogGradient, gradient);
	}
}
