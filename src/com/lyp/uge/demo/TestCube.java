package com.lyp.uge.demo;

import java.util.Random;

import com.lwjgl.util.vector.Vector3f;
import com.lyp.uge.game.GameApplication;
import com.lyp.uge.gameObject.Camera;
import com.lyp.uge.gameObject.Entity;
import com.lyp.uge.logger.Logger;
import com.lyp.uge.logger.Logger.Level;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.model.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.StaticShader;
import com.lyp.uge.utils.DataUtils;

public class TestCube extends GameApplication {

	private Loader loader = new Loader();
	private Renderer renderer;
	private StaticShader shader;
	private TextureModel textureModel;
	private Entity entity;
	private Camera camera;
	
	private RawModel model_cube;
	
	private Random random = new Random();

	@Override
	protected void onCreate(int winWidth, int winHeight, String winTitle) {
		super.onCreate(1366, 768, "基本渲染测试");
		Logger.setLogOutLevel(Level.DEBUG);
	}

	@Override
	protected void afterCreate() {
		model_cube = loader.loadToVAO(DataUtils.CUBE_VERTICES, DataUtils.CUBE_TEXTURE_COORDS, DataUtils.CUBE_INDICES);
		shader = new StaticShader();
		renderer = new Renderer(shader);
		textureModel = new TextureModel(model_cube, loader.loadTexture("res/mc_dirt_grass.png"));
		entity = new Entity(textureModel, new Vector3f(0.0f, 0.0f, -6.0f), 0f, 0f, 0f, 1f);
		camera = new Camera();
	}

	@Override
	protected void onUpdate() {
		entity.doMove(0f, 0f, 0f);
		Logger.d(entity.getPosition().toString());
		entity.doRotate(0.6f, 0.6f, 0.6f);
		camera.onMove();
	}

	@Override
	protected void onRender() {
		renderer.prepare();
		shader.start();
		shader.loadViewMatrix(camera);
		renderer.render(entity, shader);
		shader.stop();
	}

	@Override
	protected void onDestory() {
		shader.cleanUp();
		loader.cleanUp();
	}

	public static void main(String[] args) {
		new TestCube().start();
	}

}
