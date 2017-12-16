package com.lyp.uge.renderEngine;

import static org.lwjgl.opengl.GL11.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lyp.uge.game.Global;
import com.lyp.uge.gameObject.Camera;
import com.lyp.uge.gameObject.Sprite2D;
import com.lyp.uge.model.TextureModel;

public class Renderer2dManager {
	
	private Renderer2d mRenderer;

	private Map<TextureModel, List<Sprite2D>> objects = null; //model map objects
	
	public Renderer2dManager() {
		if (Global.mode_culling_back) { enableCulling();}
		
		this.mRenderer = new Renderer2d();
		this.objects = new HashMap<TextureModel, List<Sprite2D>>();
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
		List<Sprite2D> objs = objects.get(textureModel);
		if (objs != null) {
			objs.add(object);
		} else {
			List<Sprite2D> newObjs = new ArrayList<>();
			newObjs.add(object);
			objects.put(textureModel, newObjs);
		}
	}
	
	public void renderAll(Camera camera) {
		prepare();
		if (objects != null && !objects.isEmpty()) {
			mRenderer.render(objects, camera);
		}
		objects.clear();
	}
	
	public void cleanUp() {
	}
	
	public static void enableCulling() {
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK); //模型背面（反面）不渲染着色
	}
	
	public static void disableCulling() {
		glDisable(GL_CULL_FACE);
	}
}
