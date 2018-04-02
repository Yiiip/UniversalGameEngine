package com.lyp.uge.renderEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL31;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.gameObject.camera.Camera;
import com.lyp.uge.math.MathTools;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.particle.Particle;
import com.lyp.uge.shader.ParticleShader;
import com.lyp.uge.texture.ParticleTexture;

public class ParticleRenderer {

	private static final int MAX_INSTANCES = 10000;
	private static final int INSTANCE_DATA_LENGTH = 4 + 4 + 4 + 4 + 4 + 1;

	public static final float[] QUAD_VERTICES = {
		-0.5f,  0.5f,
		-0.5f, -0.5f,
		 0.5f,  0.5f,
		 0.5f, -0.5f
	};

	private static final FloatBuffer mBuffer = BufferUtils.createFloatBuffer(INSTANCE_DATA_LENGTH * MAX_INSTANCES);

	private static RawModel mQuadModel;
	private ParticleShader mShader;

	private Loader mLoader;
	private int mVboID;
	private int mPointer = 0;
	
	public ParticleRenderer(Loader loader, Matrix4f projectionMatrix) {
		mLoader = loader;
		mVboID = mLoader.createVBO(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
		mQuadModel = loader.loadToVAO(QUAD_VERTICES, 2);
		mLoader.addInstancedAttribute(mQuadModel.getVaoID(), mVboID, 1, 4, INSTANCE_DATA_LENGTH, 0); // modelViewMatrix 1st column (4 float)
		mLoader.addInstancedAttribute(mQuadModel.getVaoID(), mVboID, 2, 4, INSTANCE_DATA_LENGTH, 4); // modelViewMatrix 2nd column (4 float)
		mLoader.addInstancedAttribute(mQuadModel.getVaoID(), mVboID, 3, 4, INSTANCE_DATA_LENGTH, 8); // modelViewMatrix 3rd column (4 float)
		mLoader.addInstancedAttribute(mQuadModel.getVaoID(), mVboID, 4, 4, INSTANCE_DATA_LENGTH, 12); // modelViewMatrix 4th column (4 float)
		mLoader.addInstancedAttribute(mQuadModel.getVaoID(), mVboID, 5, 4, INSTANCE_DATA_LENGTH, 16); // current sprite offset and next sprite offset (4 float)
		mLoader.addInstancedAttribute(mQuadModel.getVaoID(), mVboID, 6, 1, INSTANCE_DATA_LENGTH, 20); // blend (1 float)
		mShader = new ParticleShader();
		mShader.start();
		mShader.loadProjectionMatrix(projectionMatrix);
		mShader.stop();
	}
	
	public void render(Map<ParticleTexture, List<Particle>> particles, Camera camera) {
		Matrix4f viewMatrix = MathTools.createViewMatrix(camera);
		prepareRender();

		for (ParticleTexture particleTexture : particles.keySet()) {
			bindTexture(particleTexture);

			List<Particle> particleList = particles.get(particleTexture);
			mPointer = 0;
			float[] vboData = new float[particleList.size() * INSTANCE_DATA_LENGTH];

			mShader.setupRows(particleTexture.getTileRows());
			for (Particle particle : particleList) {
				Matrix4f modelViewMatrix = createModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), viewMatrix);
				stroreMatrixData(modelViewMatrix, vboData);
				storeTextureCoordsInfo(particle, vboData);
			}
			mLoader.updateVBO(mVboID, vboData, mBuffer);
			GL31.glDrawArraysInstanced(GL_TRIANGLE_STRIP, 0, mQuadModel.getVertexCount(), particleList.size());
		}
		
		endRender();
	}
	
	private void bindTexture(ParticleTexture particleTexture) {
		if (particleTexture.useSubtransparentMode()) {
			glBlendFunc(GL_SRC_ALPHA, GL_ONE);
		} else {
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		}
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, particleTexture.getID());
	}
	
	private Matrix4f createModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f viewMatrix) {
		Matrix4f modelMatrix = new Matrix4f();
		modelMatrix.setIdentity();
		Matrix4f.translate(position, modelMatrix, modelMatrix);
		// 3x3转置
		modelMatrix.m00 = viewMatrix.m00;
		modelMatrix.m01 = viewMatrix.m10;
		modelMatrix.m02 = viewMatrix.m20;
		modelMatrix.m10 = viewMatrix.m01;
		modelMatrix.m11 = viewMatrix.m11;
		modelMatrix.m12 = viewMatrix.m21;
		modelMatrix.m20 = viewMatrix.m02;
		modelMatrix.m21 = viewMatrix.m12;
		modelMatrix.m22 = viewMatrix.m22;
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1), modelMatrix, modelMatrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), modelMatrix, modelMatrix);
		Matrix4f modelViewMatrix = Matrix4f.mul(viewMatrix, modelMatrix, null);
		return modelViewMatrix;
	}
	
	private void stroreMatrixData(Matrix4f modelViewMatrix, float[] vboData) {
		vboData[mPointer++] = modelViewMatrix.m00;
		vboData[mPointer++] = modelViewMatrix.m01;
		vboData[mPointer++] = modelViewMatrix.m02;
		vboData[mPointer++] = modelViewMatrix.m03;
		vboData[mPointer++] = modelViewMatrix.m10;
		vboData[mPointer++] = modelViewMatrix.m11;
		vboData[mPointer++] = modelViewMatrix.m12;
		vboData[mPointer++] = modelViewMatrix.m13;
		vboData[mPointer++] = modelViewMatrix.m20;
		vboData[mPointer++] = modelViewMatrix.m21;
		vboData[mPointer++] = modelViewMatrix.m22;
		vboData[mPointer++] = modelViewMatrix.m23;
		vboData[mPointer++] = modelViewMatrix.m30;
		vboData[mPointer++] = modelViewMatrix.m31;
		vboData[mPointer++] = modelViewMatrix.m32;
		vboData[mPointer++] = modelViewMatrix.m33;
	}
	
	private void storeTextureCoordsInfo(Particle particle, float[] vboData) {
		vboData[mPointer++] = particle.getCurrentSpriteOffset().x;
		vboData[mPointer++] = particle.getCurrentSpriteOffset().y;
		vboData[mPointer++] = particle.getNextSpriteOffset().x;
		vboData[mPointer++] = particle.getNextSpriteOffset().y;
		vboData[mPointer++] = particle.getBlend();
	}

	private void prepareRender() {
		mShader.start();
		glBindVertexArray(mQuadModel.getVaoID());
		glEnableVertexAttribArray(Loader.ATTR_POSITIONS);
		glEnableVertexAttribArray(1);
		glEnableVertexAttribArray(2);
		glEnableVertexAttribArray(3);
		glEnableVertexAttribArray(4);
		glEnableVertexAttribArray(5);
		glEnableVertexAttribArray(6);
//		glEnable(GL_BLEND);
		glDepthMask(false);
	}
	
	private void endRender() {
		glDepthMask(true);
//		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		glDisableVertexAttribArray(2);
		glDisableVertexAttribArray(3);
		glDisableVertexAttribArray(4);
		glDisableVertexAttribArray(5);
		glDisableVertexAttribArray(6);
		glBindVertexArray(0);
		mShader.stop();
	}

	public void cleanUp() {
		mShader.cleanUp();
	}
	
	public static RawModel getQuadModel() {
		return mQuadModel;
	}
}
