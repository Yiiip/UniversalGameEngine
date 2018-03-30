package com.lyp.uge.renderEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.gameObject.camera.Camera;
import com.lyp.uge.math.MathTools;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.particle.Particle;
import com.lyp.uge.shader.ParticleShader;
import com.lyp.uge.texture.ParticleTexture;

public class ParticleRenderer {
	
	public static final float[] QUAD_VERTICES = {
		-0.5f,  0.5f,
		-0.5f, -0.5f,
		 0.5f,  0.5f,
		 0.5f, -0.5f
	};
	
	private static RawModel mQuadModel;
	private ParticleShader mShader;
	
	public ParticleRenderer(Loader loader, Matrix4f projectionMatrix) {
		mQuadModel = loader.loadToVAO(QUAD_VERTICES, 2);
		mShader = new ParticleShader();
		mShader.start();
		mShader.loadProjectionMatrix(projectionMatrix);
		mShader.stop();
	}
	
	public void render(Map<ParticleTexture, List<Particle>> particles, Camera camera) {
		Matrix4f viewMatrix = MathTools.createViewMatrix(camera);
		prepareRender();
		
		for (ParticleTexture particleTexture : particles.keySet()) {
			
			// Bind texture.
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, particleTexture.getID());
			
			for (Particle particle : particles.get(particleTexture)) {
				Matrix4f modelViewMatrix = createModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), viewMatrix);
				mShader.loadModelViewMatrix(modelViewMatrix);
				glDrawArrays(GL_TRIANGLE_STRIP, 0, mQuadModel.getVertexCount());
			}
		}
		
		endRender();
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

	private void prepareRender() {
		mShader.start();
		glBindVertexArray(mQuadModel.getVaoID());
		glEnableVertexAttribArray(Loader.ATTR_POSITIONS);
//		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDepthMask(false);
	}
	
	private void endRender() {
		glDepthMask(true);
//		glDisable(GL_BLEND);
		glDisableVertexAttribArray(0);
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
