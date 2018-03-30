package com.lyp.uge.shader;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;

import com.lyp.uge.renderEngine.Loader;

public class ParticleShader extends MultiLightsShader {

	private static final String PARTICLE_VERTEX_FILE = "shader/particle.vert";
	private static final String PARTICLE_FRAGMENT_FILE = "shader/particle.frag";

	protected int uniform_modelViewMatrix;
	protected int uniform_spriteOffset_current;
	protected int uniform_spriteOffset_next;
	protected int uniform_rows;
	protected int uniform_blend;

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
		uniform_spriteOffset_current = super.getUniformLocation("spriteOffset_current");
		uniform_spriteOffset_next = super.getUniformLocation("spriteOffset_next");
		uniform_rows = super.getUniformLocation("rows");
		uniform_blend = super.getUniformLocation("blend");
	}

	/**
	 * Preprocessing model matrix and view matrix together.
	 * @param modelViewMatrix
	 */
	public void loadModelViewMatrix(Matrix4f modelViewMatrix) {
		super.loadMatrix(uniform_modelViewMatrix, modelViewMatrix);
	}
	
	public void setupSpritesCoordInfo(Vector2f currentOffset, Vector2f nextOffset, int rows, float blend) {
		super.load2DVector(uniform_spriteOffset_current, currentOffset);
		super.load2DVector(uniform_spriteOffset_next, nextOffset);
		super.loadInt(uniform_rows, rows);
		super.loadFloat(uniform_blend, blend);
	}
}
