package com.lyp.uge.shader;

public class SpecularLightShader extends StaticShader {

	private static String VERTEX_FILE = "shader/vertexShader2.1_specularLight.vs";
	private static String FRAGMENT_FILE = "shader/fragShader2.1_specularLight.fs";
	
	protected int uniform_shineDamper;
	protected int uniform_reflectivity;
	protected int uniform_ambientLightness;
	protected int uniform_useFakeLighting;
	
	public SpecularLightShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public SpecularLightShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
	}
	
	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		uniform_shineDamper = super.getUniformLocation("shineDamper");
		uniform_reflectivity = super.getUniformLocation("reflectivity");
		uniform_ambientLightness = super.getUniformLocation("ambientLightness");
		uniform_useFakeLighting = super.getUniformLocation("useFakeLighting");
	}
	
	/**
	 * 镜面反射光照
	 * @param damper 亮度衰减率
	 * @param reflectivity 反射率
	 */
	public void loadSpecularLightingParms(float damper, float reflectivity) {
		super.loadFloat(uniform_shineDamper, damper);
		super.loadFloat(uniform_reflectivity, reflectivity);
	}
	
	/**
	 * 环境光强度
	 * @param ambientLightness
	 */
	public void loadAmbientLightness(float ambientLightness) {
		super.loadFloat(uniform_ambientLightness, ambientLightness);
	}
	
	/**
	 * 竖直的假光法线替代原有法线
	 * @param useFakeLighting
	 */
	public void loadFakeLightingParms(boolean useFakeLighting) {
		super.loadBoolean(uniform_useFakeLighting, useFakeLighting);
	}
}
