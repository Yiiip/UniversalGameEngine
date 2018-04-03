package com.lyp.uge.renderEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import com.lyp.uge.game.Global;
import com.lyp.uge.gameObject.GameObject;
import com.lyp.uge.gameObject.camera.Camera;
import com.lyp.uge.gameObject.light.Light;
import com.lyp.uge.logger.Logger;
import com.lyp.uge.math.MathTools;
import com.lyp.uge.prefab.TextureModel;
import com.lyp.uge.scene.Scene;
import com.lyp.uge.shader.FoggyShader;
import com.lyp.uge.shader.MultiLightsShader;
import com.lyp.uge.shader.Shader;
import com.lyp.uge.shader.ShaderFactry;
import com.lyp.uge.shader.StaticShader;
import com.lyp.uge.shader.TerrainShader;
import com.lyp.uge.terrain.Terrain;
import com.lyp.uge.water.WaterFrameBuffers;
import com.lyp.uge.water.WaterTile;
import com.lyp.uge.window.WindowManager;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public class RendererManager {
	
	// Default parameters of the Perspective Projection Matrix.
	private static float FIELD_OF_VIEW_ANGLE	= 70; //视域最大角度
	private static float NEAR_PLANE				= 0.1f; //最近平面处
	private static float FAR_PLANE				= 1000.0f; //最远平面处
	
	// Background sky color.
	private static final float PRE_COLOR_RED	= .12f;
	private static final float PRE_COLOR_GREEN	= .12f;
	private static final float PRE_COLOR_BLUE	= .12f;
	private static final float PRE_COLOR_ALPHA	= 1.0f;

	private Renderer mRenderer;
	private Shader mShader;
	private Matrix4f mProjectionMatrix;

	private TerrainRenderer mTerrainRenderer;
	private TerrainShader mTerrainShader;
	
	private SkyboxRender mSkyboxRender;

	private WaterRender mWaterRender;
	
	public RendererManager(Loader loader, int shaderType) {
		if (Global.mode_culling_back) { enableCulling(); }
		
		this.mProjectionMatrix = MathTools.createProjectionMatrix(FIELD_OF_VIEW_ANGLE, NEAR_PLANE, FAR_PLANE, (float) WindowManager.getWindowWidth(), (float) WindowManager.getWindowHeight());
		
		this.mShader = ShaderFactry.instance().make(shaderType);
		this.mRenderer = new Renderer(mShader, mProjectionMatrix);
		
		this.mTerrainShader = new TerrainShader();
		this.mTerrainRenderer = new TerrainRenderer(mTerrainShader, mProjectionMatrix);
		
		this.mSkyboxRender = new SkyboxRender(loader, mProjectionMatrix);
		
		this.mWaterRender = new WaterRender(loader, mProjectionMatrix, null);
	}
	
	public void prepare() {
		this.prepare(PRE_COLOR_RED, PRE_COLOR_GREEN, PRE_COLOR_BLUE, PRE_COLOR_ALPHA);
	}
	
	public void prepare(float r, float g, float b, float a) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(r, g, b, a);
	}
	
	public void renderScene(@NotNull Scene scene, @NotNull List<Light> lights, @NotNull Camera camera, @Nullable Vector4f preSceneBgColor) {
		// Clear scene with specific color.
		if (preSceneBgColor == null) {
			this.prepare();
		} else {
			this.prepare(preSceneBgColor.x, preSceneBgColor.y, preSceneBgColor.z, preSceneBgColor.w);
		}
		
		// Render game objects.
		Map<TextureModel, List<GameObject>> sceneObjects = scene.getObjectManager().getObjects();
		if (sceneObjects != null && !sceneObjects.isEmpty()) {
			mShader.start();
			if (mShader instanceof StaticShader) {
				if (mShader instanceof MultiLightsShader) {
					((MultiLightsShader) mShader).loadMultiLights(lights);
				} else {
					((StaticShader) mShader).loadLight(lights.get(0));
				}
			}
			if (mShader instanceof FoggyShader) {
				((FoggyShader) mShader).setupSkyColor(preSceneBgColor==null?PRE_COLOR_RED:preSceneBgColor.x, preSceneBgColor==null?PRE_COLOR_GREEN:preSceneBgColor.y, preSceneBgColor==null?PRE_COLOR_BLUE:preSceneBgColor.z);
			}
			mShader.loadViewMatrix(camera);
			mRenderer.render(sceneObjects);
			for (Light l : lights) {
				l.render(mRenderer, mShader);
			}
			mShader.stop();
		}
		
		// Render terrains.
		List<Terrain> sceneTerrains = scene.getTerrainManager().getTerrains();
		if (sceneTerrains != null && !sceneTerrains.isEmpty()) {
			mTerrainShader.start();
			mTerrainShader.loadMultiLights(lights);
			mTerrainShader.loadViewMatrix(camera);
			mTerrainShader.setupSkyColor(preSceneBgColor==null?PRE_COLOR_RED:preSceneBgColor.x, preSceneBgColor==null?PRE_COLOR_GREEN:preSceneBgColor.y, preSceneBgColor==null?PRE_COLOR_BLUE:preSceneBgColor.z);
			mTerrainRenderer.render(sceneTerrains);
			mTerrainShader.stop();
		}
		
		// Render water titles.
		List<WaterTile> sceneWaterTiles = scene.getWaterTiles();
		if (sceneWaterTiles != null && !sceneWaterTiles.isEmpty()) {
			mWaterRender.render(sceneWaterTiles, camera, lights);
		}
		
		// Render skybox.
		if (Global.mode_display_skybox) {
			mSkyboxRender.render(camera, preSceneBgColor==null?PRE_COLOR_RED:preSceneBgColor.x, preSceneBgColor==null?PRE_COLOR_GREEN:preSceneBgColor.y, preSceneBgColor==null?PRE_COLOR_BLUE:preSceneBgColor.z);
		}
	}
	
	public void cleanUp() {
		mShader.cleanUp();
		mTerrainShader.cleanUp();
	}
	
	public static void enableCulling() {
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK); //模型背面（反面）不渲染着色
	}
	
	public static void disableCulling() {
		glDisable(GL_CULL_FACE);
	}
	
	public static void restoreRenderState() {
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glEnable(GL_STENCIL_TEST);
		glEnable(GL_CLIP_DISTANCE0);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}

	public Matrix4f getProjectionMatrix() {
		return mProjectionMatrix;
	}
	
	public void setSkyboxDayRes(String[] dayTextureRes, Loader loader) {
		if (mSkyboxRender != null) {
			mSkyboxRender.setDayTextureBoxRes(dayTextureRes, loader);
		}
	}

	public void setSkyboxNightRes(String[] nightTextureRes, Loader loader) {
		if (mSkyboxRender != null) {
			mSkyboxRender.setNightTextureBoxRes(nightTextureRes, loader);
		}
	}
	
	public void setSkyboxRecycleSpeed(float recycleSpeed) {
		mSkyboxRender.setRecycleSpeed(recycleSpeed);
	}

	public void setFbos(WaterFrameBuffers fbos) {
		mWaterRender.setFbos(fbos);
	}
	
	// Manage glPolygonMode switch.
	public static boolean	enablePolygonMode	= false;
	public static int[]		polygonModes		= {GL_POINT, GL_LINE, GL_FILL};
	public static int		polygonModeIndex	= 0;
	public static String[]	polygonModeNames	= {"点", "线", "填充"};
	
	public static void changePolygonMode() {
		glPolygonMode(GL_FRONT_AND_BACK, polygonModes[polygonModeIndex]);
		Logger.d("多边形模式", polygonModeNames[polygonModeIndex]);
		polygonModeIndex = (polygonModeIndex >= polygonModes.length-1) ? 0 : (polygonModeIndex + 1);
	}
}
