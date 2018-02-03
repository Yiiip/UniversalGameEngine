package com.lyp.test;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;
import com.lyp.uge.game.GameApplication;
import com.lyp.uge.gameObject.SimpleObject;
import com.lyp.uge.gameObject.light.Light;
import com.lyp.uge.input.Keyboard;
import com.lyp.uge.logger.Logger;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.prefab.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.OBJLoader;
import com.lyp.uge.renderEngine.Renderer;
import com.lyp.uge.shader.SpecularLightShader;
import com.lyp.uge.shader.StaticShader;
import com.lyp.uge.texture.Texture;
import com.lyp.uge.utils.DataUtils;

public class TestOBJData extends GameApplication {

	private Loader loader = new Loader();
	private Renderer renderer;
	private StaticShader shader;
	private TextureModel textureModel;
	private SimpleObject objectMain;
	private Light light;
	
	private RawModel model;
	private SimpleObject[] objects;
	
	private Random random = new Random();

	@Override
	protected void onInitWindow(int winWidth, int winHeight, String winTitle, boolean winResizeable) {
		super.onInitWindow(1600, 900, winTitle, winResizeable);
	}

	@Override
	protected void onCreate() {
		enablePolygonMode();
		getMainCamera().setSpeed(0.4f);
		
		model = OBJLoader.loadObjModel(DataUtils.OBJ_RABBIT, loader);
//		shader = new StaticShader();
		shader = new SpecularLightShader();
		renderer = new Renderer(shader);
		Texture texture = loader.loadTexture("res/texture/" + DataUtils.TEX_COLOR_YELLOW_GRAY);
		texture.setShineDamper(10.0f);	//设置反射光亮度衰减因子
		texture.setReflectivity(1.0f);	//设置反射光反射率因子
		textureModel = new TextureModel(model, texture);
		objectMain = new SimpleObject(textureModel, new Vector3f(0f, -3.0f, -40.0f), 0f, 0f, 0f, 1.0f);
//		light = new Light(new Vector3f(0.0f, 0.0f, -50.0f), new Vector3f(1, 1, 1));
		light = new Light(new Vector3f(0.0f, 0.0f, -50.0f), new Vector3f(1, 1, 1), loader);
		objects = new SimpleObject[50];
		for (int i = 0; i < objects.length; i++) {
			objects[i] = new SimpleObject(textureModel, new Vector3f(
					random.nextFloat() * 100 - 50, random.nextFloat() * 100 - 50, -random.nextInt(200)), 0f, 0f, 0f, 0.22f + 0.01f * i);
		}
	}

	@Override
	protected void onUpdate() {
		light.update();
		objectMain.doMove(0f, 0f, 0f);
		objectMain.doRotate(0.0f, 0.6f, 0.0f);
		Logger.d("Lighting", light.getPosition().toString());
	}
	
	@Override
	protected void onRender() {
		renderer.prepare();
		shader.start();
		shader.loadLight(light);
		shader.loadViewMatrix(getMainCamera());
		renderer.render(objectMain, shader);
		for (int i = 0; i < objects.length; i++) {
			renderer.render(objects[i], shader);
		}
		light.render(renderer, shader);
		shader.stop();
	}
	
	@Override
	public void onKeyReleased(int keycode) {
		super.onKeyReleased(keycode);
		if (keycode == Keyboard.KEY_R) {
			model = OBJLoader.loadObjModel(DataUtils.OBJ_ARMADILLO, loader);
			textureModel = new TextureModel(model, loader.loadTexture("res/texture/" + DataUtils.TEX_COLOR_YELLOW_GRAY));
			objectMain = new SimpleObject(textureModel, new Vector3f(0f, -3.0f, -6.0f), 0f, 0f, 0f, 1.5f);
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
