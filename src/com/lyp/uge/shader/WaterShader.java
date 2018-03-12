package com.lyp.uge.shader;

public class WaterShader extends StaticShader {

	private static String WATER_VERTEX_FILE = "shader/water.vert";
	private static String WATER_FRAGMENT_FILE = "shader/water.frag";
	
	protected int uniform_reflectionTexture;
	
	public WaterShader() {
		super(WATER_VERTEX_FILE, WATER_FRAGMENT_FILE);
	}
	
	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		uniform_reflectionTexture = getUniformLocation("reflectionTexture");
	}
	
	public void connectTextureUnits() {
		super.loadInt(uniform_reflectionTexture, 0);
	}
}
