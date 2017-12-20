package com.lyp.uge.shader;

import org.lwjgl.util.vector.Matrix4f;
import com.lyp.uge.gameObject.Camera;
import com.lyp.uge.math.MathTools;
import com.lyp.uge.renderEngine.Loader;

public class Static2dShader extends Shader {
	
	private static String VERTEX_FILE = "shader/static2d.vert";
	private static String FRAGMENT_FILE = "shader/static2d.frag";
	
	protected int uniform_transformationMatrix;
	protected int uniform_projectionMatrix;
	protected int uniform_viewMatrix;

	public Static2dShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public Static2dShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(Loader.ATTR_POSITIONS, "position");
		super.bindAttribute(Loader.ATTR_COORDINATES, "tc");
	}

	@Override
	protected void getAllUniformLocations() {
		uniform_transformationMatrix = super.getUniformLocation("transformationMatrix");
		uniform_projectionMatrix = super.getUniformLocation("projectionMatrix");
		uniform_viewMatrix = super.getUniformLocation("viewMatrix");
	}

	@Override
	public void loadModelMatrix(Matrix4f matrix4f) {
		super.loadMatrix(uniform_transformationMatrix, matrix4f);
	}
	
	@Override
	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(uniform_projectionMatrix, projectionMatrix);
	}
	
	@Override
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = MathTools.createViewMatrix(camera);
		super.loadMatrix(uniform_viewMatrix, viewMatrix);
	}
}
