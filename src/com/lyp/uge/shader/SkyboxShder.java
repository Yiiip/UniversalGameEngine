package com.lyp.uge.shader;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.gameObject.camera.Camera;
import com.lyp.uge.math.MathTools;

public class SkyboxShder extends StaticShader {

	private static String SKYBOX_VERT = "shader/skybox.vert";
	private static String SKYBOX_FRAG = "shader/skybox.frag";

	protected int uniform_fogColor; // (= skyColor)
	protected int uniform_blendFactor;
	protected int uniform_cubeMapTexture1;
	protected int uniform_cubeMapTexture2;

	protected static float rotateSpeed = 1.f;
	protected float rotation = 0.f;

	public SkyboxShder() {
		super(SKYBOX_VERT, SKYBOX_FRAG);
	}

	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		uniform_fogColor = super.getUniformLocation("fogColor");
		uniform_blendFactor = super.getUniformLocation("blendFactor");
		uniform_cubeMapTexture1 = super.getUniformLocation("textureCubeMap");
		uniform_cubeMapTexture2 = super.getUniformLocation("textureCubeMap2");
	}

	@Override
	public void loadViewMatrix(Camera camera) {
		Matrix4f viewMatrix = MathTools.createViewMatrix(camera);
		viewMatrix.m30 = 0;
		viewMatrix.m31 = 0;
		viewMatrix.m32 = 0;
		rotation += rotateSpeed * (1.f / 60.f); // TODO (speed * current FPS)
		Matrix4f.rotate((float) Math.toRadians(rotation), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		super.loadMatrix(uniform_viewMatrix, viewMatrix);
	}

	public void setupFogColor(float r, float g, float b) {
		super.loadVector(uniform_fogColor, new Vector3f(r, g, b));
	}

	public void setupBlendFactor(float blendFactor) {
		super.loadFloat(uniform_blendFactor, blendFactor);
	}

	public void connectTextureUnits() {
		super.loadInt(uniform_cubeMapTexture1, 0);
		super.loadInt(uniform_cubeMapTexture2, 1);
	}
}
