package com.lyp.uge.shader;

import org.lwjgl.util.vector.Matrix4f;

import com.lyp.uge.renderEngine.Loader;

public class ParticleShader extends MultiLightsShader {

	private static final String PARTICLE_VERTEX_FILE = "shader/particle.vert";
	private static final String PARTICLE_FRAGMENT_FILE = "shader/particle.frag";

	protected int uniform_modelViewMatrix;

	public ParticleShader() {
		super(PARTICLE_VERTEX_FILE, PARTICLE_FRAGMENT_FILE);
	}
	
	@Override
	protected void bindAttributes() {
		super.bindAttribute(Loader.ATTR_POSITIONS, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		uniform_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
	}

	/**
	 * Preprocessing model matrix and view matrix together.
	 * @param modelViewMatrix
	 */
	public void loadModelViewMatrix(Matrix4f modelViewMatrix) {
		super.loadMatrix(uniform_modelViewMatrix, modelViewMatrix);
	}
}
