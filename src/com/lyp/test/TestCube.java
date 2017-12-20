package com.lyp.test;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;
import com.lyp.uge.game.GameApplication;
import com.lyp.uge.logger.Logger;
import com.lyp.uge.logger.Logger.Level;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.model.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.DefaultShader;
import com.lyp.uge.shader.Shader;
import com.lyp.uge.utils.DataUtils;

public class TestCube extends GameApplication {

	private Loader loader = new Loader();
	private Renderer renderer;
	private Shader shader;
	private TextureModel textureModel;
	private RawModel model_cube;
	
	private DemoObject entity;
	private DemoObject[] entities;
	
	private Random random = new Random();

	@Override
	protected void onInitWindow(int winWidth, int winHeight, String winTitle, boolean winResizeable) {
		super.onInitWindow(1366, 768, winTitle, false);
		Logger.setLogOutLevel(Level.DEBUG);
	}

	@Override
	protected void onCreate() {
		enablePolygonMode();
		model_cube = loader.loadToVAO(DataUtils.CUBE_VERTICES_II, DataUtils.CUBE_TEXTURE_COORDS_II, DataUtils.CUBE_INDICES_II);
//		shader = new StaticShader("shader/vertexShader_cube_without_nor.vs" , "shader/fragShader_cube_without_nor.fs");
		shader = new DefaultShader();
		renderer = new Renderer(shader);
		textureModel = new TextureModel(model_cube, loader.loadTexture("res/texture/" + DataUtils.TEX_MC_CUBE));
		entity = new DemoObject(textureModel, new Vector3f(0.0f, 0.0f, -6.0f), 0f, 0f, 0f, 1f);
		entities = new DemoObject[1000];
		for (int i = 0; i < entities.length; i++) {
			entities[i] = new DemoObject(textureModel, new Vector3f(random.nextInt(30) - 15, random.nextInt(30) - 15, random.nextInt(15) - 15), 0f, 0f, 0f, 1f);
		}
	}
	
	@Override
	protected void onUpdate() {
		entity.doMove(0f, 0f, 0f);
		entity.doRotate(0.6f, 0.6f, 0.6f);
		//Logger.d(entity.toString());
		Logger.d("Camera", getMainCamera().toShortString());
	}

	@Override
	protected void onRender() {
		renderer.prepare();
		shader.start();
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
