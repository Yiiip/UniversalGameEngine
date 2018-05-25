package com.lyp.uge.shader;

import com.lyp.uge.gameObject.camera.Camera;

public class WaterShader extends MultiLightsShader {

	private static String WATER_VERTEX_FILE = "shader/water.vert";
	private static String WATER_FRAGMENT_FILE = "shader/water.frag";

	protected int uniform_reflectionTexture;
	protected int uniform_refractionTexture;
	protected int uniform_dudvMap;
	protected int uniform_tilingCount; // for repeat dudvMap
	protected int uniform_moveFactor;
	protected int uniform_cameraPosition;
	protected int uniform_normalMap;

	private int uniform_debugMode;
	private static int debugModeIndex = 0;

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
		uniform_cameraPosition = getUniformLocation("cameraPosition");
		uniform_normalMap = getUniformLocation("normalMap");
		uniform_debugMode = getUniformLocation("debugMode");
	}
	
	@Override
	public void loadViewMatrix(Camera camera) {
		super.loadViewMatrix(camera);
		super.loadVector(uniform_cameraPosition, camera.getPosition());
	}

	public void connectTextureUnits() {
		super.loadInt(uniform_reflectionTexture, 0);
		super.loadInt(uniform_refractionTexture, 1);
		super.loadInt(uniform_dudvMap, 2);
		super.loadInt(uniform_normalMap, 3);
	}

	public void setupMoveFactor(float moveFactor) {
		super.loadFloat(uniform_moveFactor, moveFactor);
	}
	
	public void setupTilingCount(int tilingCount) {
		super.loadInt(uniform_tilingCount, tilingCount);
	}

	public void enableDebugMode()
	{
		super.loadInt(uniform_debugMode, debugModeIndex);
	}

	public static void changeDebugMode() {
		debugModeIndex = (debugModeIndex + 1) % 3;
	}
}
