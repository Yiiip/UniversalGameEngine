package com.lyp.uge.fontRendering;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.gameObject.camera.Camera;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.shader.Shader;

public class FontShader extends Shader{

	private static final String VERTEX_FILE = "shader/fontVertex.vs";
	private static final String FRAGMENT_FILE = "shader/fontFragment.fs";
	
	private int uniform_color;
	private int uniform_translation;
	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		uniform_color = super.getUniformLocation("color");
		uniform_translation = super.getUniformLocation("translation");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(Loader.ATTR_POSITIONS, "position");
		super.bindAttribute(Loader.ATTR_COORDINATES, "tc");
	}

	protected void loadColor(Vector3f color) {
		super.loadVector(uniform_color, color);
	}
	
	protected void loadTranslation(Vector2f translation) {
		super.load2DVector(uniform_translation, translation);
	}

	@Override
	public void loadModelMatrix(Matrix4f modelMatrix) {
		return;
	}

	@Override
	public void loadViewMatrix(Camera camera) {
		return;
	}

	@Override
	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		return;
	}
}
