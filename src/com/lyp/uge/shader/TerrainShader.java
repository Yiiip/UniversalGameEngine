package com.lyp.uge.shader;

public class TerrainShader extends MultiLightsShader {

	private static String VERTEX_FILE = "shader/terrain.vert";
	private static String FRAGMENT_FILE = "shader/terrain.frag";
	
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
}
