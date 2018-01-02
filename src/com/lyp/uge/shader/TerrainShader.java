package com.lyp.uge.shader;

import org.lwjgl.util.vector.Vector3f;

public class TerrainShader extends SpecularLightShader {

	private static String VERTEX_FILE = "shader/vertexShader3_terrain.vs";
	private static String FRAGMENT_FILE = "shader/fragShader3_terrain.fs";
	
	protected int uniform_skyColor;
	protected int uniform_fogDensity;
	protected int uniform_fogGradient;
	
	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
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
