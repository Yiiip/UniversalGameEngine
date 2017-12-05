package com.lyp.uge.demo;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;
import com.lyp.uge.fontRendering.GUITextManager;
import com.lyp.uge.game.GameApplication;
import com.lyp.uge.gameObject.Light;
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

public class TestOBJData extends GameApplication {

	private Loader loader = new Loader();
	private Renderer renderer;
	private StaticShader shader;
	private TextureModel textureModel;
	private DemoObject entity;
	private Light light;
	
	private RawModel model;
	private DemoObject[] es;
	
	private Random random = new Random();

	@Override
	protected void onInitWindow(int winWidth, int winHeight, String winTitle, boolean winResizeable) {
		super.onInitWindow(1600, 900, winTitle, winResizeable);
		Logger.setLogOutLevel(Level.DEBUG);
	}

	@Override
	protected void onCreate() {
		enablePolygonMode();
		
		model = OBJLoader.loadObjModel(DataUtils.OBJ_RABBIT, loader);
		shader = new StaticShader();
		renderer = new Renderer(shader);
		textureModel = new TextureModel(model, loader.loadTexture("res/texture/" + DataUtils.TEX_COLOR_LIGHT_GRAY));
		entity = new DemoObject(textureModel, new Vector3f(0f, -3.0f, -6.0f), 0f, 0f, 0f, 1f);
		light = new Light(new Vector3f(0, 0, -12), new Vector3f(1, 1, 1));
		es = new DemoObject[10];
		for (int i = 0; i < es.length; i++) {
			es[i] = new DemoObject(textureModel, new Vector3f(-random.nextInt(20), -random.nextInt(20), -random.nextInt(30)), 0f, 0f, 0f, 0.22f + 0.01f * i);
		}
	}

	@Override
	protected void onUpdate() {
		light.update();
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
	public void onKeyReleased(int keycode) {
		super.onKeyReleased(keycode);
		if (keycode == Keyboard.KEY_R) {
			model = OBJLoader.loadObjModel(DataUtils.OBJ_ARMADILLO, loader);
			textureModel = new TextureModel(model, loader.loadTexture("res/texture/" + DataUtils.TEX_COLOR_YELLOW_GRAY));
			entity = new DemoObject(textureModel, new Vector3f(0f, -3.0f, -6.0f), 0f, 0f, 0f, 1.5f);
		}
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
