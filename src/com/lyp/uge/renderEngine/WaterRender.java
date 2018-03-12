package com.lyp.uge.renderEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.gameObject.camera.Camera;
import com.lyp.uge.math.MathTools;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.shader.WaterShader;
import com.lyp.uge.water.WaterFrameBuffers;
import com.lyp.uge.water.WaterTile;

public class WaterRender {

	public static float[] QUAD_VERTICES = {
		-1f, 0f, -1f, 
		-1f, 0f,  1f, 
		 1f, 0f, -1f, 
		 1f, 0f, -1f, 
		-1f, 0f,  1f, 
		 1f, 0f,  1f
	};

	private RawModel mQuadModel;
	private WaterShader mShader;
	private WaterFrameBuffers mFbos;
	
	public WaterRender(Loader loader, Matrix4f projectionMatrix, WaterFrameBuffers fbos) {
		mFbos = fbos;
		mQuadModel = loader.loadToVAO(QUAD_VERTICES);
		mShader = new WaterShader();
		mShader.start();
		mShader.connectTextureUnits();
		mShader.loadProjectionMatrix(projectionMatrix);
		mShader.stop();
	}
	
	public void render(List<WaterTile> waterList, Camera camera) {
		prepareRender(camera);
		
		for (WaterTile water : waterList) {
			Matrix4f modelMatrix = MathTools.createModelMatrix(
					new Vector3f(water.getX(), water.getHeight(), water.getZ()), 
					0.0f, 0.0f, 0.0f, WaterTile.WATER_TILE_SIZE);
			mShader.loadModelMatrix(modelMatrix);
			glDrawArrays(GL_TRIANGLES, 0, mQuadModel.getVertexCount());
		}
		
		endRender();
	}
	
	private void prepareRender(Camera camera) {
		mShader.start();
		mShader.loadViewMatrix(camera);
		glBindVertexArray(mQuadModel.getVaoID());
		glEnableVertexAttribArray(Loader.ATTR_POSITIONS);
		
		if (mFbos != null) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, mFbos.getReflectionTexture());
			glActiveTexture(GL_TEXTURE1);
			glBindTexture(GL_TEXTURE_2D, mFbos.getRefractionTexture());
		}
	}
	
	private void endRender() {
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		mShader.stop();
	}
	
	public void setFbos(WaterFrameBuffers fbos) {
		this.mFbos = fbos;
	}
}
