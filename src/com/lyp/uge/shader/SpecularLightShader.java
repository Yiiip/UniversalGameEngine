package com.lyp.uge.shader;

public class SpecularLightShader extends StaticShader {

	private static String VERTEX_FILE = "shader/vertexShader2.1_specularLight.txt";
	private static String FRAGMENT_FILE = "shader/fragShader2.1_specularLight.txt";
	
	private int uniform_shineDamper;
	private int uniform_reflectivity;
	
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
	}
	
	public void loadSpecularLightingParms(float damper, float reflectivity) {
		super.loadFloat(uniform_shineDamper, damper);
		super.loadFloat(uniform_reflectivity, reflectivity);
	}
}
