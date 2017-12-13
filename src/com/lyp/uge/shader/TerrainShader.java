package com.lyp.uge.shader;

public class TerrainShader extends SpecularLightShader {

	private static String VERTEX_FILE = "shader/vertexShader3_terrain.txt";
	private static String FRAGMENT_FILE = "shader/fragShader3_terrain.txt";
	
	private int uniform_useFakeLighting;
	
	public TerrainShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		uniform_useFakeLighting = super.getUniformLocation("useFakeLighting");
	}
	
	public void loadFakeLightingParms(boolean useFakeLighting) {
		super.loadBoolean(uniform_useFakeLighting, useFakeLighting);
	}
}
