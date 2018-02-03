package com.lyp.uge.renderEngine;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Vector4f;

import com.lyp.uge.game.Global;
import com.lyp.uge.gameObject.camera.Camera;
import com.lyp.uge.gameObject.sprite.Sprite2D;
import com.lyp.uge.prefab.TextureModel;

public class Renderer2dManager {
	
	private Renderer2d mRenderer;

	private Map<TextureModel, List<Sprite2D>> mObjects = null;
	
	public Renderer2dManager() {
		if (Global.mode_culling_back) { enableCulling();}
		
		this.mRenderer = new Renderer2d();
		this.mObjects = new HashMap<TextureModel, List<Sprite2D>>();
	}
	
	public void prepare() {
		this.prepare(.12f, .12f, .12f, 1.0f);
	}
	
	public void prepare(float r, float g, float b, float a) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(r, g, b, a);
	}
	
	public void addObject(Sprite2D object) {
		if (object == null) {
			return;
		}
		TextureModel textureModel = object.getModel();
		List<Sprite2D> objs = mObjects.get(textureModel);
		if (objs != null) {
			objs.add(object);
		} else {
			List<Sprite2D> newObjs = new ArrayList<>();
			newObjs.add(object);
			mObjects.put(textureModel, newObjs);
		}
	}
	
	public void renderAll(Camera camera) {
		prepare();
		if (mObjects != null && !mObjects.isEmpty()) {
			mRenderer.render(mObjects, camera);
		}
		mObjects.clear();
	}
	
	public void renderAll(Camera camera, Vector4f preColor) {
		prepare(preColor.x, preColor.y, preColor.z, preColor.w);
		if (mObjects != null && !mObjects.isEmpty()) {
			mRenderer.render(mObjects, camera);
		}
		mObjects.clear();
	}
	
	public void cleanUp() {
		mRenderer = null;
		mObjects.clear();
		mObjects = null;
	}
	
	public static void enableCulling() {
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);
	}
	
	public static void disableCulling() {
		glDisable(GL_CULL_FACE);
	}
}
