package com.lyp.uge.shader;

import org.lwjgl.util.vector.Matrix4f;

import com.lyp.uge.gameObject.camera.Camera;
import com.lyp.uge.math.MathTools;

public class SkyboxShder extends StaticShader {

	private static String SKYBOX_VERT = "shader/skybox.vert";
	private static String SKYBOX_FRAG = "shader/skybox.frag";
	
	public SkyboxShder() {
		super(SKYBOX_VERT, SKYBOX_FRAG);
	}
	
	@Override
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = MathTools.createViewMatrix(camera);
		viewMatrix.m30 = 0;
		viewMatrix.m31 = 0;
		viewMatrix.m32 = 0;
		super.loadMatrix(uniform_viewMatrix, viewMatrix);
	}
}
