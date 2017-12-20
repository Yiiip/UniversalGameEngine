package com.lyp.uge.renderEngine;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;

import com.lyp.uge.game.Global;
import com.lyp.uge.gameObject.Camera;
import com.lyp.uge.gameObject.GameObject;
import com.lyp.uge.gameObject.Light;
import com.lyp.uge.math.MathTools;
import com.lyp.uge.model.TextureModel;
import com.lyp.uge.shader.Shader;
import com.lyp.uge.shader.SpecularLightShader;
import com.lyp.uge.shader.StaticShader;
import com.lyp.uge.shader.TerrainShader;
import com.lyp.uge.terrain.Terrain;
import com.lyp.uge.window.WindowManager;

public class RendererManager {
	
	private static float FIELD_OF_VIEW_ANGLE = 70; //视域最大角度
	private static float NEAR_PLANE = 0.1f; //最近平面处
	private static float FAR_PLANE = 1000.0f; //最远平面处

	private Renderer mRenderer;
	private Shader mShader;
	private Matrix4f mProjectionMatrix;

	private TerrainRenderer mTerrainRenderer;
	private TerrainShader mTerrainShader;
	
	private Map<TextureModel, List<GameObject>> mObjects = null;
	private List<Terrain> mTerrains = null;
	
	public RendererManager() {
		if (Global.mode_culling_back) { enableCulling(); }
		
		this.mProjectionMatrix = MathTools.createProjectionMatrix(FIELD_OF_VIEW_ANGLE, NEAR_PLANE, FAR_PLANE, (float) WindowManager.getWindowWidth(), (float) WindowManager.getWindowHeight());
		this.mShader = new SpecularLightShader();
		this.mRenderer = new Renderer(mShader, mProjectionMatrix);
		this.mObjects = new HashMap<TextureModel, List<GameObject>>();
		
		this.mTerrainShader = new TerrainShader();
		this.mTerrainRenderer = new TerrainRenderer(mTerrainShader, mProjectionMatrix);
		this.mTerrains = new ArrayList<Terrain>();
	}
	
	public void prepare() {
		this.prepare(.12f, .12f, .12f, 1.0f);
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
	
	public void renderAll(Light light, Camera camera) {
		this.prepare();
		if (mObjects != null && !mObjects.isEmpty()) {
			mShader.start();
			if (mShader instanceof StaticShader) {
				((StaticShader) mShader).loadLight(light);
			}
			mShader.loadViewMatrix(camera);
			mRenderer.render(mObjects);
			light.render(mRenderer, mShader);
			mShader.stop();
		}
		
		if (mTerrains != null && !mTerrains.isEmpty()) {
			mTerrainShader.start();
			mTerrainShader.loadLight(light);
			mTerrainShader.loadViewMatrix(camera);
			mTerrainRenderer.render(mTerrains);
			mTerrainShader.stop();
		}
		
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
}
