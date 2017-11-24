package com.lyp.uge.shader;

public class StaticShader extends ShaderProgram {
	
	private static final String VERTEX_FILE = "shader/vertexShader.txt";
	private static final String FRAGMENT_FILE = "shader/fragShader.txt";

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
