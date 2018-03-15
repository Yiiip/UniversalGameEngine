package com.lyp.uge.shader;

public class WaterShader extends StaticShader {

	private static String WATER_VERTEX_FILE = "shader/water.vert";
	private static String WATER_FRAGMENT_FILE = "shader/water.frag";

	protected int uniform_reflectionTexture;
	protected int uniform_refractionTexture;
	protected int uniform_dudvMap;
	protected int uniform_tilingCount; // for repeat dudvMap
	protected int uniform_moveFactor;

	public WaterShader() {
		super(WATER_VERTEX_FILE, WATER_FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		uniform_reflectionTexture = getUniformLocation("reflectionTexture");
		uniform_refractionTexture = getUniformLocation("refractionTexture");
		uniform_dudvMap = getUniformLocation("dudvMap");
		uniform_moveFactor = getUniformLocation("moveFactor");
		uniform_tilingCount = getUniformLocation("tilingCount");
	}

	public void connectTextureUnits() {
		super.loadInt(uniform_reflectionTexture, 0);
		super.loadInt(uniform_refractionTexture, 1);
		super.loadInt(uniform_dudvMap, 2);
	}

	public void setupMoveFactor(float moveFactor) {
		super.loadFloat(uniform_moveFactor, moveFactor);
	}
	
	public void setupTilingCount(int tilingCount) {
		super.loadInt(uniform_tilingCount, tilingCount);
	}
}
