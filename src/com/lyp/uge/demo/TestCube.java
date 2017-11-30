package com.lyp.uge.demo;

import java.util.Random;

import com.lwjgl.util.vector.Vector3f;
import com.lyp.uge.game.GameApplication;
import com.lyp.uge.gameObject.Entity;
import com.lyp.uge.gameObject.Light;
import com.lyp.uge.input.Input;
import com.lyp.uge.input.Keyboard;
import com.lyp.uge.logger.Logger;
import com.lyp.uge.logger.Logger.Level;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.model.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.OBJLoader;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.StaticShader;
import com.lyp.uge.utils.DataUtils;

public class TestCube extends GameApplication {

	private Loader loader = new Loader();
	private Renderer renderer;
	private StaticShader shader;
	private TextureModel textureModel;
	private RawModel model_cube;
	
	private Entity entity;
	private Entity[] entities;
	//private Light light;
	
	private Random random = new Random();

	@Override
	protected void onInitWindow(int winWidth, int winHeight, String winTitle, boolean winResizeable) {
		super.onInitWindow(1366, 768, winTitle, false);
		Logger.setLogOutLevel(Level.DEBUG);
	}

	@Override
	protected void onCreate() {
		model_cube = loader.loadToVAO(DataUtils.CUBE_VERTICES, DataUtils.CUBE_TEXTURE_COORDS, DataUtils.CUBE_INDICES);
		shader = new StaticShader("shader/vertexShader_cube_without_nor.txt" , "shader/fragShader_cube_without_nor.txt");
		renderer = new Renderer(shader);
		textureModel = new TextureModel(model_cube, loader.loadTexture("res/texture/" + DataUtils.TEX_MC_DIRT_GRASS));
		entity = new Entity(textureModel, new Vector3f(0.0f, 0.0f, -6.0f), 0f, 0f, 0f, 1f);
		entities = new Entity[1000];
		for (int i = 0; i < entities.length; i++) {
			entities[i] = new Entity(textureModel, new Vector3f(random.nextInt(30) - 15, random.nextInt(30) - 15, random.nextInt(15) - 15), 0f, 0f, 0f, 1f);
		}
		//light = new Light(new Vector3f(0.0f, 0.0f, -1.0f), new Vector3f(1.0f, 1.0f, 1.0f));
	}
	
	@Override
	public void onKeyDown(Input input) {
		super.onKeyDown(input);
		if (input.isKeyDown(Keyboard.KEY_E)) {
		}
	}

	@Override
	protected void onUpdate() {
		//light.onMove();
		entity.doMove(0f, 0f, 0f);
		entity.doRotate(0.6f, 0.6f, 0.6f);
		//Logger.d(entity.toString());
	}

	@Override
	protected void onRender() {
		renderer.prepare();
		shader.start();
		//shader.loadLight(light);
		shader.loadViewMatrix(getMainCamera());
		renderer.render(entity, shader);
		for (int i = 0; i < entities.length; i++) {
			renderer.render(entities[i], shader);
		}
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
