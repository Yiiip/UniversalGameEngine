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

public class TestOBJData extends GameApplication {

	private Loader loader = new Loader();
	private Renderer renderer;
	private StaticShader shader;
	private TextureModel textureModel;
	private Entity entity;
	private Light light;
	
	private RawModel model_stall;
	private RawModel model_dragon;
	
	private Random random = new Random();

	@Override
	protected void onInitWindow(int winWidth, int winHeight, String winTitle, boolean winResizeable) {
		super.onInitWindow(1366, 768, winTitle, winResizeable);
		Logger.setLogOutLevel(Level.DEBUG);
	}

	@Override
	protected void onCreate() {
//		model_stall = OBJLoader.loadObjModel("stall.obj", loader);
		model_dragon = OBJLoader.loadObjModel("dragon.obj", loader);

		shader = new StaticShader();
		renderer = new Renderer(shader);
		textureModel = new TextureModel(model_dragon, loader.loadTexture("res/texture/dragonTexture.png"));
		entity = new Entity(textureModel, new Vector3f(0f, -3.0f, -6.0f), 0f, 0f, 0f, 1f);
		light = new Light(new Vector3f(0, 0, -12), new Vector3f(1, 1, 1));
	}

	@Override
	protected void onUpdate() {
		light.onMove();
		entity.doMove(0f, 0f, 0f);
		entity.doRotate(0.0f, 0.6f, 0.0f);
	}

	@Override
	protected void onRender() {
		renderer.prepare();
		shader.start();
		shader.loadLight(light);
		shader.loadViewMatrix(getMainCamera());
		renderer.render(entity, shader);
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
