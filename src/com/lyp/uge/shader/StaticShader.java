package com.lyp.uge.shader;

import org.lwjgl.util.vector.Matrix4f;

import com.lyp.uge.gameObject.camera.Camera;
import com.lyp.uge.gameObject.light.Light;
import com.lyp.uge.math.MathTools;
import com.lyp.uge.renderEngine.Loader;

public class StaticShader extends Shader {
	
	private static String VERTEX_FILE = "shader/vertexShader2.vs";
	private static String FRAGMENT_FILE = "shader/fragShader2.fs";
	
	protected int uniform_modelMatrix;
	protected int uniform_projectionMatrix;
	protected int uniform_viewMatrix;
	protected int uniform_lightPos;
	protected int uniform_lightColor;

	public StaticShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}
	
	public StaticShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(Loader.ATTR_POSITIONS, "position");
		super.bindAttribute(Loader.ATTR_COORDINATES, "tc");
		super.bindAttribute(Loader.ATTR_NORMALS, "normal");
	}

	@Override
	protected void getAllUniformLocations() {
		uniform_modelMatrix = super.getUniformLocation("modelMatrix");
		uniform_projectionMatrix = super.getUniformLocation("projectionMatrix");
		uniform_viewMatrix = super.getUniformLocation("viewMatrix");
		uniform_lightPos = super.getUniformLocation("lightPos");
		uniform_lightColor = super.getUniformLocation("lightColor");
	}

	@Override
	public void loadModelMatrix(Matrix4f matrix4f) {
		super.loadMatrix(uniform_modelMatrix, matrix4f);
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
	
	public void loadLight(Light light) {
		super.loadVector(uniform_lightPos, light.getPosition());
		super.loadVector(uniform_lightColor, light.getColor());
	}
}
