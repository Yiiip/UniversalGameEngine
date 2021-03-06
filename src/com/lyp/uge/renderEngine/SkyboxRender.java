package com.lyp.uge.renderEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import org.lwjgl.util.vector.Matrix4f;

import com.lyp.uge.gameObject.camera.Camera;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.shader.SkyboxShder;
import com.lyp.uge.utils.DataUtils;

public class SkyboxRender {

	private static final float SKYBOX_SIZE = 500.0f;
	
	public static float[] SKYBOX_VERTICES = {
		-SKYBOX_SIZE,  SKYBOX_SIZE, -SKYBOX_SIZE,
	    -SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,
	    SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,
	     SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,
	     SKYBOX_SIZE,  SKYBOX_SIZE, -SKYBOX_SIZE,
	    -SKYBOX_SIZE,  SKYBOX_SIZE, -SKYBOX_SIZE,

	    -SKYBOX_SIZE, -SKYBOX_SIZE,  SKYBOX_SIZE,
	    -SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,
	    -SKYBOX_SIZE,  SKYBOX_SIZE, -SKYBOX_SIZE,
	    -SKYBOX_SIZE,  SKYBOX_SIZE, -SKYBOX_SIZE,
	    -SKYBOX_SIZE,  SKYBOX_SIZE,  SKYBOX_SIZE,
	    -SKYBOX_SIZE, -SKYBOX_SIZE,  SKYBOX_SIZE,

	     SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,
	     SKYBOX_SIZE, -SKYBOX_SIZE,  SKYBOX_SIZE,
	     SKYBOX_SIZE,  SKYBOX_SIZE,  SKYBOX_SIZE,
	     SKYBOX_SIZE,  SKYBOX_SIZE,  SKYBOX_SIZE,
	     SKYBOX_SIZE,  SKYBOX_SIZE, -SKYBOX_SIZE,
	     SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,

	    -SKYBOX_SIZE, -SKYBOX_SIZE,  SKYBOX_SIZE,
	    -SKYBOX_SIZE,  SKYBOX_SIZE,  SKYBOX_SIZE,
	     SKYBOX_SIZE,  SKYBOX_SIZE,  SKYBOX_SIZE,
	     SKYBOX_SIZE,  SKYBOX_SIZE,  SKYBOX_SIZE,
	     SKYBOX_SIZE, -SKYBOX_SIZE,  SKYBOX_SIZE,
	    -SKYBOX_SIZE, -SKYBOX_SIZE,  SKYBOX_SIZE,

	    -SKYBOX_SIZE,  SKYBOX_SIZE, -SKYBOX_SIZE,
	     SKYBOX_SIZE,  SKYBOX_SIZE, -SKYBOX_SIZE,
	     SKYBOX_SIZE,  SKYBOX_SIZE,  SKYBOX_SIZE,
	     SKYBOX_SIZE,  SKYBOX_SIZE,  SKYBOX_SIZE,
	    -SKYBOX_SIZE,  SKYBOX_SIZE,  SKYBOX_SIZE,
	    -SKYBOX_SIZE,  SKYBOX_SIZE, -SKYBOX_SIZE,

	    -SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,
	    -SKYBOX_SIZE, -SKYBOX_SIZE,  SKYBOX_SIZE,
	     SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,
	     SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,
	    -SKYBOX_SIZE, -SKYBOX_SIZE,  SKYBOX_SIZE,
	     SKYBOX_SIZE, -SKYBOX_SIZE,  SKYBOX_SIZE
	};
	
	public static final String[] CUBE_TEXTURE_DAY_FILES_1 = {
		DataUtils.PATH_SKYBOX + "skybox01_right.png",
		DataUtils.PATH_SKYBOX + "skybox01_left.png",
		DataUtils.PATH_SKYBOX + "skybox01_top.png",
		DataUtils.PATH_SKYBOX + "skybox01_bottom.png",
		DataUtils.PATH_SKYBOX + "skybox01_back.png",
		DataUtils.PATH_SKYBOX + "skybox01_front.png",
	};
	
	public static final String[] CUBE_TEXTURE_DAY_FILES_2 = {
		DataUtils.PATH_SKYBOX + "cubemap_px.png",
		DataUtils.PATH_SKYBOX + "cubemap_nx.png",
		DataUtils.PATH_SKYBOX + "cubemap_py.png",
		DataUtils.PATH_SKYBOX + "cubemap_ny.png",
		DataUtils.PATH_SKYBOX + "cubemap_pz.png",
		DataUtils.PATH_SKYBOX + "cubemap_nz.png",
	};
	
	public static final String[] CUBE_TEXTURE_NIGHT_FILES = {
		DataUtils.PATH_SKYBOX + "nightbox01_right.png",
		DataUtils.PATH_SKYBOX + "nightbox01_left.png",
		DataUtils.PATH_SKYBOX + "nightbox01_top.png",
		DataUtils.PATH_SKYBOX + "nightbox01_bottom.png",
		DataUtils.PATH_SKYBOX + "nightbox01_back.png",
		DataUtils.PATH_SKYBOX + "nightbox01_front.png",
	};
	
	private static float RECYCLE_SPEED = 2.0f;
	
	private final static int TEX_INDEX_DAY = 0;
	private final static int TEX_INDEX_NIGHT = 1;
	
	private RawModel mRawModel;
	private String[] mDayTextureBoxRes = CUBE_TEXTURE_DAY_FILES_1;
	private String[] mNightTextureBoxRes = CUBE_TEXTURE_NIGHT_FILES;
	private int[] mTexID = new int[2];
	private SkyboxShder mSkyboxShder;
	private float mWorldTime = 0;
	
	public SkyboxRender(Loader loader, Matrix4f projectionMatrix) {
		mRawModel = loader.loadToVAO(SKYBOX_VERTICES, 3);
		mTexID[TEX_INDEX_DAY] = loader.loadTextureCubeMap(mDayTextureBoxRes).getID(); // Daytime skybox
		mTexID[TEX_INDEX_NIGHT] = loader.loadTextureCubeMap(mNightTextureBoxRes).getID(); // Night skybox
		mSkyboxShder = new SkyboxShder();
		mSkyboxShder.start();
		mSkyboxShder.connectTextureUnits();
		mSkyboxShder.loadProjectionMatrix(projectionMatrix);
		mSkyboxShder.stop();
	}
	
	public void render(Camera camera, float r, float g, float b) {
		mSkyboxShder.start();
		mSkyboxShder.loadViewMatrix(camera);
		mSkyboxShder.setupFogColor(r, g, b);
		
		glBindVertexArray(mRawModel.getVaoID());
		glEnableVertexAttribArray(Loader.ATTR_POSITIONS);
		
		bindSkyboxTextures();
		
		glDrawArrays(GL_TRIANGLES, 0, mRawModel.getVertexCount());
		
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		mSkyboxShder.stop();
	}
	
	/**
	 * Simulate day&night cycle
	 */
	private void bindSkyboxTextures() {
		int tex1, tex2;
		float blend;
		
		if (mWorldTime >= 0 && mWorldTime < 7000) {
			tex1 = mTexID[TEX_INDEX_DAY];
			tex2 = mTexID[TEX_INDEX_DAY];
			blend = (mWorldTime - 0) / (7000 - 0);
		} else if (mWorldTime >= 7000 && mWorldTime < 12000) {
			tex1 = mTexID[TEX_INDEX_DAY];
			tex2 = mTexID[TEX_INDEX_NIGHT];
			blend = (mWorldTime - 7000) / (12000 - 7000);
		} else if (mWorldTime >= 12000 && mWorldTime < 19000) {
			tex1 = mTexID[TEX_INDEX_NIGHT];
			tex2 = mTexID[TEX_INDEX_NIGHT];
			blend = (mWorldTime - 12000) / (19000 - 12000);
		} else {
			tex1 = mTexID[TEX_INDEX_NIGHT];
			tex2 = mTexID[TEX_INDEX_DAY];
			blend = (mWorldTime - 19000) / (24000 - 19000);
		}
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, tex1);
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_CUBE_MAP, tex2);
		mSkyboxShder.setupBlendFactor(blend);
		
		mWorldTime += RECYCLE_SPEED; // TODO (+= speed * current FPS)
		mWorldTime %= 24000;
	}
	
	public void setDayTextureBoxRes(String[] dayTextureBoxRes, Loader loader) {
		this.mDayTextureBoxRes = dayTextureBoxRes;
		this.mTexID[TEX_INDEX_DAY] = loader.loadTextureCubeMap(dayTextureBoxRes).getID();
	}
	
	public void setNightTextureBoxRes(String[] nightTextureBoxRes, Loader loader) {
		this.mNightTextureBoxRes = nightTextureBoxRes;
		this.mTexID[TEX_INDEX_NIGHT] = loader.loadTextureCubeMap(nightTextureBoxRes).getID();
	}

	public void setRecycleSpeed(float recycleSpeed) {
		RECYCLE_SPEED = recycleSpeed;
	}
}
