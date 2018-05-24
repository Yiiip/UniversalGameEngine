package com.lyp.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import com.lyp.uge.renderEngine.RendererManager;
import com.lyp.uge.scene.Scene;
import com.lyp.uge.shader.ShaderFactry;
import com.lyp.uge.texture.Texture;
import com.lyp.uge.utils.DataUtils;

public class TestOBJDataAdvanced extends GameApplication {

	private Loader loader = new Loader();
	private SimpleObject objectMain;
	private SimpleObject[] objects;
	private SimpleObject[] others;
	private List<Light> lights;
	private RendererManager rendererManager;
	private Scene mainScene;
	
	private Random random = new Random();

	@Override
	protected void onInitWindow(int winWidth, int winHeight, String winTitle, boolean winResizeable) {
		super.onInitWindow(1600, 900, winTitle, winResizeable);
	}

	@Override
	protected void onCreate() {
		enablePolygonMode();
		getMainCamera().setSpeed(0.5f);
		getMainCamera().setPosition(new Vector3f(0.0f, 0.0f, 90.0f));
		
		lights = new ArrayList<>();
		//lights.add(new Light(new Vector3f(0.0f, 0.0f, -60.0f), new Vector3f(1, 1, 1), loader));

		RawModel sphereModel = OBJLoader.loadObjModel(DataUtils.OBJ_SPHERE_HIGH_QUALITY, loader);
		RawModel dragonModel = OBJLoader.loadObjModel(DataUtils.OBJ_DRAGON, loader);
		RawModel rabbitModel = OBJLoader.loadObjModel(DataUtils.OBJ_RABBIT, loader);
		
		Texture texture = new Texture("res/texture/" + DataUtils.TEX_COLOR_LIGHT_GRAY);
		texture.setShineDamper(10.0f)	//设置反射光亮度衰减因子
				.setReflectivity(0.1f);	//设置反射光反射率因子
		TextureModel roughTextureModel = new TextureModel(sphereModel, texture); //粗糙的材质效果
		
		Texture texture2 = new Texture("res/texture/" + DataUtils.TEX_COLOR_LIGHT_GRAY);
		texture2.setReflectivity(5.0f)
				.setShineDamper(20.0f)
				.setAmbientLightness(0.165f);
		TextureModel smoothTextureModel = new TextureModel(sphereModel, texture2); //光滑的材质效果

		// 大球
		objectMain = new SimpleObject(smoothTextureModel, new Vector3f(0f, -3.0f, -40.0f), 0f, 0f, 0f, 1.0f);
		// 周围小球
		objects = new SimpleObject[15];
		for (int i = 0; i < objects.length; i++) {
			objects[i] = new SimpleObject(roughTextureModel, new Vector3f(
					random.nextFloat() * 100 - 50, random.nextFloat() * 100 - 50, -random.nextInt(200)), 0f, 0f, 0f, 0.22f + 0.01f * i);
		}
		// 其他三维模型
		others = new SimpleObject[2];
		others[0] = new SimpleObject(new TextureModel(dragonModel, texture), new Vector3f(60f, 0f, -50.0f), 0f, 0f, 0f, 2.0f);
		others[1] = new SimpleObject(new TextureModel(rabbitModel, texture2), new Vector3f(-50f, 0f, -50.0f), 0f, 0f, 0f, 1.2f);
		
		mainScene = new Scene();
		mainScene.addObject(objectMain);
		mainScene.addObjects(Arrays.asList(objects));
		mainScene.addObjects(Arrays.asList(others));
		
		rendererManager = new RendererManager(loader, ShaderFactry.WITH_MULTI_LIGHTS);
	}

	@Override
	protected void onUpdate() {
		objectMain.doRotate(0.0f, 0.6f, 0.0f);
		
		others[0].doRotate(0.0f, 0.8f, 0.0f);
		others[0].doMove(0.0f, (float) Math.cos(getRunTimerInt()) / 15.0f, 0.0f);
		others[1].doRotate(0.0f, -0.8f, 0.0f);
		others[1].doMove(0.0f, (float) Math.cos(getRunTimerInt() + 10) / 15.0f, 0.0f);

		if (!lights.isEmpty()) {
			lights.get(lights.size() - 1).update();
			Logger.d("Lighting", lights.get(lights.size() - 1).getPosition().toString());
		}
	}
	
	@Override
	protected void onRender() {
		rendererManager.renderScene(mainScene, lights, getMainCamera(), null);
	}
	
	@Override
	public void onKeyReleased(int keycode) {
		super.onKeyReleased(keycode);
		switch (keycode) {
			case Keyboard.KEY_1:
				if (lights.size() == 0) {
					lights.add(new Light(new Vector3f(0.0f, 10.0f, 0.0f), new Vector3f(1, 0, 0), loader));
					Logger.d("Light", "添加红色光源");
				} else {
					lights.get(0).setActive(!lights.get(0).isActive());
				}
				break;

			case Keyboard.KEY_2:
				if (lights.size() == 1) {
					lights.add(new Light(new Vector3f(0.0f, -50.0f, 0.0f), new Vector3f(0, 1, 0), loader));
					Logger.d("Light", "添加绿色光源");
				} else if (lights.size() > 1) {
					lights.get(1).setActive(!lights.get(1).isActive());
				}
				break;

			case Keyboard.KEY_3:
				if (lights.size() == 2) {
					lights.add(new Light(new Vector3f(-50.0f, -3.0f, 28.0f), new Vector3f(0, 0, 1), loader));
					Logger.d("Light", "添加蓝色光源");
				} else if (lights.size() > 2) {
					lights.get(2).setActive(!lights.get(2).isActive());
				}
				break;
		
			default:
				break;
		}
	}

	@Override
	protected void onDestory() {
		rendererManager.cleanUp();
		loader.cleanUp();
	}

	public static void main(String[] args) {
		new TestOBJDataAdvanced().start();
	}
}
