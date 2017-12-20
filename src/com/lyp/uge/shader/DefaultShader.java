package com.lyp.uge.shader;

import org.lwjgl.util.vector.Matrix4f;
import com.lyp.uge.gameObject.Camera;
import com.lyp.uge.math.MathTools;
import com.lyp.uge.renderEngine.Loader;

public class DefaultShader extends Shader {
	
	private static final String DEFAULT_VERTEX_FILE = "shader/default.vert";
	private static final String DEFAULT_FRAGMENT_FILE = "shader/default.frag";
	
	protected int uniform_modelMatrix;
	protected int uniform_viewMatrix;
	protected int uniform_projectionMatrix;

	public DefaultShader() {
		super(DEFAULT_VERTEX_FILE, DEFAULT_FRAGMENT_FILE);
	}
	
	/*public DefaultShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
	}*/

	@Override
	protected void bindAttributes() { //input variable into vertex shader from java client
		super.bindAttribute(Loader.ATTR_POSITIONS, "position");
		super.bindAttribute(Loader.ATTR_COORDINATES, "tc");
	}

	@Override
	protected void getAllUniformLocations() {
		uniform_modelMatrix = super.getUniformLocation("modelMatrix");
		uniform_viewMatrix = super.getUniformLocation("viewMatrix");
		uniform_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	public void loadModelMatrix(Matrix4f modelMatrix) {
		super.loadMatrix(uniform_modelMatrix, modelMatrix);
	}
	
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = MathTools.createViewMatrix(camera);
		super.loadMatrix(uniform_viewMatrix, viewMatrix);
	}
	
	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(uniform_projectionMatrix, projectionMatrix);
	}
}
