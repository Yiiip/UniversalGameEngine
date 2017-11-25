package com.lyp.uge.shader;

import com.lyp.uge.renderEngine.Loader;

public class StaticShader extends ShaderProgram {
	
	private static String VERTEX_FILE = "shader/vertexShader2.txt";
	private static String FRAGMENT_FILE = "shader/fragShader2.txt";

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public StaticShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
		VERTEX_FILE = vertexFile;
		FRAGMENT_FILE = fragmentFile;
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(Loader.ATTR_POSITIONS, "position");
		super.bindAttribute(Loader.ATTR_COORDINATES, "tc");
	}

}
