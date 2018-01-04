package com.lyp.uge.shader;

import org.lwjgl.util.vector.Vector3f;

public class TerrainShader extends SpecularLightShader {

	private static String VERTEX_FILE = "shader/vertexShader3_terrain.vert";
	private static String FRAGMENT_FILE = "shader/fragShader3_terrain.frag";
	
	protected int uniform_skyColor;
	protected int uniform_fogDensity;
	protected int uniform_fogGradient;
	
	protected int uniform_bgTexture;
	protected int uniform_rTexture;
	protected int uniform_gTexture;
	protected int uniform_bTexture;
	protected int uniform_blendMap;
	
	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		uniform_skyColor = super.getUniformLocation("skyColor");
		uniform_fogDensity = super.getUniformLocation("fogDensity");
		uniform_fogGradient = super.getUniformLocation("fogGradient");
		uniform_bgTexture = super.getUniformLocation("bgTexture");
		uniform_rTexture = super.getUniformLocation("rTexture");
		uniform_gTexture = super.getUniformLocation("gTexture");
		uniform_bTexture = super.getUniformLocation("bTexture");
		uniform_blendMap = super.getUniformLocation("blendMap");
	}
	
	public void setupTextureUnits() {
		super.loadInt(uniform_bgTexture, 0);
		super.loadInt(uniform_rTexture, 1);
		super.loadInt(uniform_gTexture, 2);
		super.loadInt(uniform_bTexture, 3);
		super.loadInt(uniform_blendMap, 4);
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
