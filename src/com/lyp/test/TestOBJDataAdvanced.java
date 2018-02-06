package com.lyp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;
import com.lyp.uge.game.GameApplication;
import com.lyp.uge.gameObject.SimpleObject;
import com.lyp.uge.gameObject.light.Light;
import com.lyp.uge.logger.Logger;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.prefab.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.OBJLoader;
import com.lyp.uge.renderEngine.RendererManager;
import com.lyp.uge.shader.ShaderFactry;
import com.lyp.uge.texture.Texture;
import com.lyp.uge.utils.DataUtils;

public class TestOBJDataAdvanced extends GameApplication {

	private Loader loader = new Loader();
	private TextureModel textureModel;
	private SimpleObject objectMain;
	private SimpleObject[] objects;
	private List<Light> lights;
	private RendererManager rendererManager;
	
	private Random random = new Random();

	@Override
	protected void onInitWindow(int winWidth, int winHeight, String winTitle, boolean winResizeable) {
		super.onInitWindow(1600, 900, winTitle, winResizeable);
	}

	@Override
	protected void onCreate() {
		enablePolygonMode();
		getMainCamera().setSpeed(0.4f);
		
		lights = new ArrayList<>();
		//lights.add(new Light(new Vector3f(0.0f, 0.0f, -60.0f), new Vector3f(1, 1, 1), loader));
		lights.add(new Light(new Vector3f(0.0f, 10.0f, 0.0f), new Vector3f(1, 0, 0), loader));
		lights.add(new Light(new Vector3f(0.0f, -50.0f, 0.0f), new Vector3f(0, 1, 0), loader));
//		lights.add(new Light(new Vector3f(-50.0f, 0.0f, 0.0f), new Vector3f(0, 0, 1), loader));

		RawModel rawModel = OBJLoader.loadObjModel(DataUtils.OBJ_SPHERE_HIGH_QUALITY, loader);
		
		Texture texture = loader.loadTexture("res/texture/" + DataUtils.TEX_COLOR_LIGHT_GRAY);
		texture.setShineDamper(10.0f);	//设置反射光亮度衰减因子
		texture.setReflectivity(0.1f);	//设置反射光反射率因子
		textureModel = new TextureModel(rawModel, texture); //粗糙的材质效果
		
		Texture texture2 = new Texture("res/texture/" + DataUtils.TEX_COLOR_LIGHT_GRAY);
		texture2.setReflectivity(5.0f)
				.setShineDamper(20.0f)
				.setAmbientLightness(0.165f);
		TextureModel textureModel2 = new TextureModel(rawModel, texture2); //光滑且反射力强的材质效果
		
		objectMain = new SimpleObject(textureModel2, new Vector3f(0f, -3.0f, -40.0f), 0f, 0f, 0f, 1.0f);
		
		objects = new SimpleObject[20];
		for (int i = 0; i < objects.length; i++) {
			objects[i] = new SimpleObject(textureModel, new Vector3f(
					random.nextFloat() * 100 - 50, random.nextFloat() * 100 - 50, -random.nextInt(200)), 0f, 0f, 0f, 0.22f + 0.01f * i);
		}
		
		rendererManager = new RendererManager(loader, ShaderFactry.WITH_MULTI_LIGHTS);
	}

	@Override
	protected void onUpdate() {
		lights.get(0).update();
		objectMain.doMove(0f, 0f, 0f);
		objectMain.doRotate(0.0f, 0.6f, 0.0f);
		Logger.d("Lighting", lights.get(0).getPosition().toString());
	}
	
	@Override
	protected void onRender() {
		rendererManager.addObject(objectMain);
		for (int i = 0; i < objects.length; i++) {
			rendererManager.addObject(objects[i]);
		}
		rendererManager.renderAll(lights, getMainCamera(), null);
	}
	
	@Override
	public void onKeyReleased(int keycode) {
		super.onKeyReleased(keycode);
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
