package com.lyp.uge.demo;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;
import com.lyp.uge.game.GameApplication;
import com.lyp.uge.gameObject.Light;
import com.lyp.uge.logger.Logger;
import com.lyp.uge.logger.Logger.Level;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.model.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.OBJLoader;
import com.lyp.uge.renderEngine.RendererManager;
import com.lyp.uge.terrain.Terrain;
import com.lyp.uge.texture.Texture;
import com.lyp.uge.utils.DataUtils;

public class TestTerrains extends GameApplication {

	private Loader loader = new Loader();
	private DemoObject[] oTrees;
	private DemoObject[] oGrasses;
	private DemoObject[] oFerns;
	private Terrain[] terrains;
	private Light light;
	private RendererManager rendererManager;
	
	private Random random = new Random();

	@Override
	protected void onInitWindow(int winWidth, int winHeight, String winTitle, boolean winResizeable) {
		super.onInitWindow(1600, 900, winTitle, winResizeable);
		Logger.setLogOutLevel(Level.DEBUG);
	}

	@Override
	protected void onCreate() {
		enablePolygonMode();
		getMainCamera().setSpeed(0.7f);
		getMainCamera().setPosition(new Vector3f(0, 5, 0));
		
		light = new Light(new Vector3f(0.0f, 1000.0f, -500.0f), new Vector3f(1, 1, 1), loader);

		//树木
		RawModel rawModel = OBJLoader.loadObjModel(DataUtils.OBJ_TREE, loader);
		Texture texture = loader.loadTexture("res/texture/" + DataUtils.TEX_TREE);
		texture.setShineDamper(8.0f);	//设置反射光亮度衰减因子
		texture.setReflectivity(0.8f);	//设置反射光反射率因子
		TextureModel textureModel = new TextureModel(rawModel, texture);
		oTrees = new DemoObject[1000];
		for (int i = 0; i < oTrees.length; i++) {
			oTrees[i] = new DemoObject(textureModel, new Vector3f(
					random.nextFloat() * Terrain.SIZE*2 - Terrain.SIZE, 0, -random.nextInt((int) Terrain.SIZE)), 0f, 0f, 0f, 2.0f +random.nextFloat()*2);
		}
		
		//草类植物
		RawModel grassRawModel = OBJLoader.loadObjModel(DataUtils.OBJ_GRASS_REAL, loader);
		Texture grassTexture = loader.loadTexture("res/texture/" + DataUtils.TEX_GRASS_REAL);
		grassTexture.setShineDamper(8.0f);
		grassTexture.setReflectivity(2.0f);
		TextureModel grassTextureModel = new TextureModel(grassRawModel, grassTexture);
		oGrasses = new DemoObject[1500];
		for (int i = 0; i < oGrasses.length; i++) {
			oGrasses[i] = new DemoObject(grassTextureModel, new Vector3f(
					random.nextFloat() * Terrain.SIZE*2 - Terrain.SIZE, 0, -random.nextInt((int) Terrain.SIZE)-10.0f), 0f, 0f, 0f, random.nextFloat()+0.05f);
		}
		RawModel fernRawModel = OBJLoader.loadObjModel(DataUtils.OBJ_FERN, loader);
		Texture fernTexture = loader.loadTexture("res/texture/" + DataUtils.TEX_FERN);
		fernTexture.setShineDamper(10.0f);
		fernTexture.setReflectivity(1.0f);
		TextureModel fernTextureModel = new TextureModel(fernRawModel, fernTexture);
		oFerns = new DemoObject[1000];
		for (int i = 0; i < oFerns.length; i++) {
			oFerns[i] = new DemoObject(fernTextureModel, new Vector3f(
					random.nextFloat() * Terrain.SIZE*2 - Terrain.SIZE, 0, -random.nextInt((int) Terrain.SIZE)-10.0f), 0f, 0f, 0f, random.nextFloat()+0.04f);
		}
		
		//地形
		Texture textureTerrain = loader.loadTexture("res/texture/" + DataUtils.TEX_GRASS);
		textureTerrain.setShineDamper(10.0f);
		textureTerrain.setReflectivity(0.7f);
		terrains = new Terrain[2];
		terrains[0] = new Terrain(0, -1, loader, textureTerrain);
		terrains[1] = new Terrain(-1, -1, loader, textureTerrain);
		
		rendererManager = new RendererManager();
	}

	@Override
	protected void onUpdate() {
		light.update();
		Logger.d("Lighting", light.getPosition().toString());
	}
	
	@Override
	protected void onRender() {
		rendererManager.prepare(0.5f, 0.8f, 0.95f, 1.0f);
		for (int i = 0; i < oTrees.length; i++) {
			rendererManager.addObject(oTrees[i]);
		}
		for (int i = 0; i < oGrasses.length; i++) {
			rendererManager.addObject(oGrasses[i]);
		}
		for (int i = 0; i < oFerns.length; i++) {
			rendererManager.addObject(oFerns[i]);
		}
		rendererManager.addTerrain(terrains[0]);
		rendererManager.addTerrain(terrains[1]);
		rendererManager.renderAll(light, getMainCamera());
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
		new TestTerrains().start();
	}

}
