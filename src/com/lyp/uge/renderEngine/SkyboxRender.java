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
	
	private static String[] CUBE_TEXTURE_FILES = {
		DataUtils.PATH_SKYBOX + "skybox01_right.png",
		DataUtils.PATH_SKYBOX + "skybox01_left.png",
		DataUtils.PATH_SKYBOX + "skybox01_top.png",
		DataUtils.PATH_SKYBOX + "skybox01_bottom.png",
		DataUtils.PATH_SKYBOX + "skybox01_back.png",
		DataUtils.PATH_SKYBOX + "skybox01_front.png",
	};
	
	private RawModel mRawModel;
	private int mTexID;
	private SkyboxShder mSkyboxShder;
	
	public SkyboxRender(Loader loader, Matrix4f projectionMatrix) {
		mRawModel = loader.loadToVAO(SKYBOX_VERTICES);
		mTexID = loader.loadTextureCubeMap(CUBE_TEXTURE_FILES).getID();
		mSkyboxShder = new SkyboxShder();
		mSkyboxShder.start();
		mSkyboxShder.loadProjectionMatrix(projectionMatrix);
		mSkyboxShder.stop();
	}
	
	public void render(Camera camera) {
		mSkyboxShder.start();
		mSkyboxShder.loadViewMatrix(camera);
		glBindVertexArray(mRawModel.getVaoID());
		glEnableVertexAttribArray(Loader.ATTR_POSITIONS);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, mTexID);
		glDrawArrays(GL_TRIANGLES, 0, mRawModel.getVertexCount());
		glDisableVertexAttribArray(0);
		glBindVertexArray(0);
		mSkyboxShder.stop();
	}
}
