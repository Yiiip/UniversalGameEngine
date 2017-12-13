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
import com.lyp.uge.model.TextureModel;
import com.lyp.uge.shader.SpecularLightShader;
import com.lyp.uge.shader.StaticShader;
import com.lyp.uge.shader.TerrainShader;
import com.lyp.uge.terrain.Terrain;
import com.lyp.uge.window.WindowManager;

public class RendererManager {
	
	private static float FIELD_OF_VIEW_ANGLE = 70;
	private static float NEAR_PLANE = 0.1f; //最近平面处
	private static float FAR_PLANE = 1000.0f; //最远平面处

	private Renderer renderer;
	private StaticShader shaderProgram;
	private Matrix4f projectionMatrix;

	private TerrainRenderer terrainRenderer;
	private TerrainShader terrainShader;
	
	private Map<TextureModel, List<GameObject>> objects = null;
	private List<Terrain> terrains = null;
	
	public RendererManager() {
		if (Global.mode_culling_back) { enableCulling();}
		createProjectionMatrix();
		this.shaderProgram = new SpecularLightShader();
		this.renderer = new Renderer(shaderProgram, projectionMatrix);
		this.objects = new HashMap<TextureModel, List<GameObject>>();
		
		this.terrainShader = new TerrainShader();
		this.terrainRenderer = new TerrainRenderer(terrainShader, projectionMatrix);
		this.terrains = new ArrayList<>();
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
		List<GameObject> batch = objects.get(textureModel);
		if (batch != null) {
			batch.add(object);
		} else {
			List<GameObject> newBatch = new ArrayList<>();
			newBatch.add(object);
			objects.put(textureModel, newBatch);
		}
	}
	
	public void addTerrain(Terrain terrain) {
		terrains.add(terrain);
	}
	
	public void renderAll(Light light, Camera camera) {
		prepare();
		shaderProgram.start();
		shaderProgram.loadLight(light);
		shaderProgram.loadViewMatrix(camera);
		renderer.render(objects);
		light.render(renderer, shaderProgram);
		shaderProgram.stop();
		
		terrainShader.start();
		terrainShader.loadLight(light);
		terrainShader.loadViewMatrix(camera);
		terrainRenderer.render(terrains);
		terrainShader.stop();
		
		objects.clear();
		terrains.clear();
	}
	
	public void cleanUp() {
		shaderProgram.cleanUp();
		terrainShader.cleanUp();
	}
	
	private void createProjectionMatrix() {
		float aspectRatio = (float) WindowManager.getWindowWidth() / (float) WindowManager.getWindowHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FIELD_OF_VIEW_ANGLE/2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
        
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
	}
	
	public static void enableCulling() {
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK); //模型背面（反面）不渲染着色
	}
	
	public static void disableCulling() {
		glDisable(GL_CULL_FACE);
	}
}
