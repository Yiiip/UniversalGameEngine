package com.lyp.uge.renderEngine;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector4f;

import com.lyp.uge.game.Global;
import com.lyp.uge.gameObject.GameObject;
import com.lyp.uge.gameObject.camera.Camera;
import com.lyp.uge.gameObject.light.Light;
import com.lyp.uge.math.MathTools;
import com.lyp.uge.prefab.TextureModel;
import com.lyp.uge.shader.FoggyShader;
import com.lyp.uge.shader.MultiLightsShader;
import com.lyp.uge.shader.Shader;
import com.lyp.uge.shader.ShaderFactry;
import com.lyp.uge.shader.StaticShader;
import com.lyp.uge.shader.TerrainShader;
import com.lyp.uge.terrain.Terrain;
import com.lyp.uge.window.WindowManager;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

public class RendererManager {
	
	//Projection parameters of camera
	private static float FIELD_OF_VIEW_ANGLE = 70; //视域最大角度
	private static float NEAR_PLANE = 0.1f; //最近平面处
	private static float FAR_PLANE = 1000.0f; //最远平面处
	
	//background sky color
	private static final float PRE_COLOR_RED = .12f;
	private static final float PRE_COLOR_GREEN = .12f;
	private static final float PRE_COLOR_BLUE = .12f;
	private static final float PRE_COLOR_ALPHA = 1.0f;

	private Renderer mRenderer;
	private Shader mShader;
	private Matrix4f mProjectionMatrix;

	private TerrainRenderer mTerrainRenderer;
	private TerrainShader mTerrainShader;
	
	private SkyboxRender mSkyboxRender;
	
	private Map<TextureModel, List<GameObject>> mObjects = null;
	private List<Terrain> mTerrains = null;
	
	public RendererManager(Loader loader, int shaderType) {
		if (Global.mode_culling_back) { enableCulling(); }
		
		this.mProjectionMatrix = MathTools.createProjectionMatrix(FIELD_OF_VIEW_ANGLE, NEAR_PLANE, FAR_PLANE, (float) WindowManager.getWindowWidth(), (float) WindowManager.getWindowHeight());
		
		this.mShader = ShaderFactry.instance().make(shaderType);
		this.mRenderer = new Renderer(mShader, mProjectionMatrix);
		this.mObjects = new HashMap<TextureModel, List<GameObject>>();
		
		this.mTerrainShader = new TerrainShader();
		this.mTerrainRenderer = new TerrainRenderer(mTerrainShader, mProjectionMatrix);
		this.mTerrains = new ArrayList<Terrain>();
		
		this.mSkyboxRender = new SkyboxRender(loader, mProjectionMatrix);
	}
	
	public void prepare() {
		this.prepare(PRE_COLOR_RED, PRE_COLOR_GREEN, PRE_COLOR_BLUE, PRE_COLOR_ALPHA);
	}
	
	public void prepare(float r, float g, float b, float a) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(r, g, b, a);
	}
	
	public void addObject(GameObject object) {
		if (object == null) {
			return;
		}
		TextureModel textureModel = object.getModel();
		List<GameObject> objs = mObjects.get(textureModel);
		if (objs != null) {
			objs.add(object);
		} else {
			List<GameObject> newObjs = new ArrayList<>();
			newObjs.add(object);
			mObjects.put(textureModel, newObjs);
		}
	}
	
	public void addTerrain(Terrain terrain) {
		mTerrains.add(terrain);
	}
	
	public void renderAll(@NotNull List<Light> lights, @NotNull Camera camera, @Nullable Vector4f preSkyColor) {
		if (preSkyColor == null) {
			this.prepare();
		} else {
			this.prepare(preSkyColor.x, preSkyColor.y, preSkyColor.z, preSkyColor.w);
		}
		if (mObjects != null && !mObjects.isEmpty()) {
			mShader.start();
			if (mShader instanceof StaticShader) {
				if (mShader instanceof MultiLightsShader) {
					((MultiLightsShader) mShader).loadMultiLights(lights);
				} else {
					((StaticShader) mShader).loadLight(lights.get(0));
				}
			}
			if (mShader instanceof FoggyShader) {
				((FoggyShader) mShader).setupSkyColor(preSkyColor==null?PRE_COLOR_RED:preSkyColor.x, preSkyColor==null?PRE_COLOR_GREEN:preSkyColor.y, preSkyColor==null?PRE_COLOR_BLUE:preSkyColor.z);
			}
			mShader.loadViewMatrix(camera);
			mRenderer.render(mObjects);
			for (Light l : lights) {
				l.render(mRenderer, mShader);
			}
			mShader.stop();
		}
		
		if (mTerrains != null && !mTerrains.isEmpty()) {
			mTerrainShader.start();
			mTerrainShader.loadMultiLights(lights);
			mTerrainShader.loadViewMatrix(camera);
			mTerrainShader.setupSkyColor(preSkyColor==null?PRE_COLOR_RED:preSkyColor.x, preSkyColor==null?PRE_COLOR_GREEN:preSkyColor.y, preSkyColor==null?PRE_COLOR_BLUE:preSkyColor.z);
			mTerrainRenderer.render(mTerrains);
			mTerrainShader.stop();
		}
		
		mSkyboxRender.render(camera);
		
		mObjects.clear();
		mTerrains.clear();
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
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	}
}
