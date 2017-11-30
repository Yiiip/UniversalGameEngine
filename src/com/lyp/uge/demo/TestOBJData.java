package com.lyp.uge.demo;

import java.util.Random;

import com.lwjgl.util.vector.Vector3f;
import com.lyp.uge.game.GameApplication;
import com.lyp.uge.gameObject.Entity;
import com.lyp.uge.gameObject.Light;
import com.lyp.uge.logger.Logger;
import com.lyp.uge.logger.Logger.Level;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.model.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.OBJLoader;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.StaticShader;
import com.lyp.uge.utils.DataUtils;

public class TestOBJData extends GameApplication {

	private Loader loader = new Loader();
	private Renderer renderer;
	private StaticShader shader;
	private TextureModel textureModel;
	private Entity entity;
	private Light light;
	
	private RawModel model;
	private Entity[] es;
	
	private Random random = new Random();

	@Override
	protected void onInitWindow(int winWidth, int winHeight, String winTitle, boolean winResizeable) {
		super.onInitWindow(1366, 768, winTitle, winResizeable);
		Logger.setLogOutLevel(Level.DEBUG);
	}

	@Override
	protected void onCreate() {
		model = OBJLoader.loadObjModel(DataUtils.OBJ_RABBIT, loader);

		shader = new StaticShader();
		renderer = new Renderer(shader);
		textureModel = new TextureModel(model, loader.loadTexture("res/texture/" + DataUtils.TEX_COLOR_YELLOW));
		entity = new Entity(textureModel, new Vector3f(0f, -3.0f, -6.0f), 0f, 0f, 0f, 1f);
		light = new Light(new Vector3f(0, 0, -12), new Vector3f(1, 1, 1));
		es = new Entity[10];
		for (int i = 0; i < es.length; i++) {
			es[i] = new Entity(textureModel, new Vector3f(-random.nextInt(20), -random.nextInt(20), -random.nextInt(20)), 0f, 0f, 0f, 0.2f + 0.01f*i);
		}
	}

	@Override
	protected void onUpdate() {
		light.onMove();
		entity.doMove(0f, 0f, 0f);
		entity.doRotate(0.0f, 0.6f, 0.0f);
		Logger.d("Lighting", light.getPosition().toString());
	}

	@Override
	protected void onRender() {
		renderer.prepare();
		shader.start();
		shader.loadLight(light);
		shader.loadViewMatrix(getMainCamera());
		renderer.render(entity, shader);
		for (int i = 0; i < es.length; i++) {
			renderer.render(es[i], shader);
		}
		shader.stop();
	}

	@Override
	protected void onDestory() {
		shader.cleanUp();
		loader.cleanUp();
	}

	public static void main(String[] args) {
		new TestOBJData().start();
	}

}
