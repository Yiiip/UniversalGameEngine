package com.lyp.uge.renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lyp.uge.gameObject.Camera;
import com.lyp.uge.gameObject.GameObject;
import com.lyp.uge.gameObject.Light;
import com.lyp.uge.model.TextureModel;
import com.lyp.uge.shader.SpecularLightShader;
import com.lyp.uge.shader.StaticShader;

public class RendererManager {

	private StaticShader shaderProgram;
	private Renderer renderer;
	
	private Map<TextureModel, List<GameObject>> objects = null;
	
	public RendererManager() {
		this.shaderProgram = new SpecularLightShader();
		this.renderer = new Renderer(shaderProgram);
		this.objects = new HashMap<TextureModel, List<GameObject>>();
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
	
	public void renderAll(Light light, Camera camera) {
		renderer.prepare();
		shaderProgram.start();
		shaderProgram.loadLight(light);
		shaderProgram.loadViewMatrix(camera);
		renderer.render(objects);
		light.render(renderer, shaderProgram);
		shaderProgram.stop();
		objects.clear();
	}
	
	public void cleanUp() {
		shaderProgram.cleanUp();
	}
}
