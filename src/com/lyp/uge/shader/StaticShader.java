package com.lyp.uge.shader;

import com.lwjgl.util.vector.Matrix4f;
import com.lyp.uge.renderEngine.Loader;

public class StaticShader extends ShaderProgram {
	
	private static String VERTEX_FILE = "shader/vertexShader2.txt";
	private static String FRAGMENT_FILE = "shader/fragShader2.txt";
	
	private int uniform_transformationMatrix;

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

	@Override
	protected void getAllUniformLocations() {
		uniform_transformationMatrix = super.getUniformLocation("transformationMatrix");
	}

	public void loadTransformationMatrix(Matrix4f matrix4f) {
		super.loadMatrix(uniform_transformationMatrix, matrix4f);
	}
}
