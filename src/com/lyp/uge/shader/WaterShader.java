package com.lyp.uge.shader;

public class WaterShader extends StaticShader {

	private static String WATER_VERTEX_FILE = "shader/water.vert";
	private static String WATER_FRAGMENT_FILE = "shader/water.frag";
	
	protected int uniform_shineDamper;
	protected int uniform_reflectivity;
	protected int uniform_ambientLightness;
	protected int uniform_useFakeLighting;
	
	public WaterShader() {
		super(WATER_VERTEX_FILE, WATER_FRAGMENT_FILE);
	}
	
}
