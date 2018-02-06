package com.lyp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.lyp.uge.game.GameApplication;
import com.lyp.uge.gameObject.SimpleObject;
import com.lyp.uge.gameObject.light.Light;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.prefab.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.OBJLoader;
import com.lyp.uge.renderEngine.RendererManager;
import com.lyp.uge.shader.ShaderFactry;
import com.lyp.uge.terrain.Terrain;
import com.lyp.uge.terrain.TerrainManager;
import com.lyp.uge.terrain.TerrainTexturePack;
import com.lyp.uge.texture.Texture;
import com.lyp.uge.utils.DataUtils;

public class TestTerrains extends GameApplication {

	private Loader loader = new Loader();
	private SimpleObject[] oTrees;
	private SimpleObject[] oGrasses;
	private SimpleObject[] oFerns;
	private Terrain[] terrains;
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
		enableFirstPersonCamera();
		getMainCamera().setSpeed(0.7f);
		getMainCamera().setPosition(new Vector3f(0, 100, Terrain.SIZE));
		
		lights = new ArrayList<>();
		lights.add(new Light(new Vector3f(0.0f, 1000.0f, 500.0f), new Vector3f(1, 1, 1), loader));

		//地形
		Texture bgTexture = loader.loadTexture("res/texture/" + DataUtils.TEX_GRASS)
				.setShineDamper(10.0f)
				.setReflectivity(0.5f);
		Texture rTexture = loader.loadTexture(DataUtils.TEX_MUD);
		Texture gTexture = loader.loadTexture(DataUtils.TEX_GRASS_WITH_FLOWERS);
		Texture bTexture = loader.loadTexture(DataUtils.TEX_GROUND01);
		Texture blendMapTexture = loader.loadTexture(DataUtils.TEX_TERRAIN_BLEND_MAP);
		TerrainTexturePack texturePack = new TerrainTexturePack(bgTexture, rTexture, gTexture, bTexture);
		terrains = new Terrain[2];
		terrains[0] = new Terrain(0, 0, loader, texturePack, blendMapTexture, DataUtils.TEX_TERRAIN_HEIGHT_MAP04, 50);
		terrains[1] = new Terrain(-1, 0, loader, texturePack, blendMapTexture, DataUtils.TEX_TERRAIN_HEIGHT_MAP01, 50);
		pushToCamera(new TerrainManager(terrains));
		
		//树木
		RawModel rawModel = OBJLoader.loadObjModel(DataUtils.OBJ_TREE, loader);
		Texture texture = loader.loadTexture("res/texture/" + DataUtils.TEX_TREE)
				.setShineDamper(8.0f)	//设置反射光亮度衰减因子
				.setReflectivity(0.8f);	//设置反射光反射率因子
		TextureModel textureModel = new TextureModel(rawModel, texture);
		oTrees = new SimpleObject[1000];
		for (int i = 0; i < oTrees.length / 2; i++) {
			float randomX = random.nextFloat() * Terrain.SIZE;
			float randomZ = random.nextInt((int) Terrain.SIZE);
			float randomY = terrains[0].getHeightOfTerrain(randomX, randomZ);
			oTrees[i] = new SimpleObject(textureModel, new Vector3f(randomX, randomY, randomZ), 0f, 0f, 0f, 2.0f+random.nextFloat()*2);
		}
		for (int i = oTrees.length / 2; i < oTrees.length; i++) {
			float randomX = -random.nextFloat() * Terrain.SIZE;
			float randomZ = random.nextInt((int) Terrain.SIZE);
			float randomY = terrains[1].getHeightOfTerrain(randomX, randomZ);
			oTrees[i] = new SimpleObject(textureModel, new Vector3f(randomX, randomY, randomZ), 0f, 0f, 0f, 2.0f+random.nextFloat()*2);
		}
		
		//草类植物
		RawModel grassRawModel = OBJLoader.loadObjModel(DataUtils.OBJ_GRASS_REAL, loader);
		Texture grassTexture = loader.loadTexture("res/texture/" + DataUtils.TEX_GRASS_REAL)
				.setShineDamper(8.0f)
				.setReflectivity(3.0f)
				.setHasTransparency(true)
				//.setUseFakeLighting(true)
				.setAmbientLightness(0.7f);
		TextureModel grassTextureModel = new TextureModel(grassRawModel, grassTexture);
		oGrasses = new SimpleObject[2000];
		for (int i = 0; i < oGrasses.length / 2; i++) {
			float randomX = random.nextFloat() * Terrain.SIZE;
			float randomZ = random.nextInt((int) Terrain.SIZE) - 10.0f;
			float randomY = terrains[0].getHeightOfTerrain(randomX, randomZ);
			oGrasses[i] = new SimpleObject(grassTextureModel, new Vector3f(randomX, randomY, randomZ), 0f, 0f, 0f, random.nextFloat()+0.05f);
		}
		for (int i = oGrasses.length / 2; i < oGrasses.length; i++) {
			float randomX = -random.nextFloat() * Terrain.SIZE;
			float randomZ = random.nextInt((int) Terrain.SIZE) - 10.0f;
			float randomY = terrains[1].getHeightOfTerrain(randomX, randomZ);
			oGrasses[i] = new SimpleObject(grassTextureModel, new Vector3f(randomX, randomY, randomZ), 0f, 0f, 0f, random.nextFloat()+0.05f);
		}
		RawModel fernRawModel = OBJLoader.loadObjModel(DataUtils.OBJ_FERN, loader);
		Texture fernTexture = loader.loadTexture("res/texture/" + DataUtils.TEX_FERN)
				.setShineDamper(10.0f)
				.setReflectivity(1.0f)
				.setHasTransparency(true);
		TextureModel fernTextureModel = new TextureModel(fernRawModel, fernTexture);
		oFerns = new SimpleObject[1100];
		for (int i = 0; i < oFerns.length / 2; i++) {
			float randomX = random.nextFloat() * Terrain.SIZE;
			float randomZ = random.nextInt((int) Terrain.SIZE) - 10.0f;
			float randomY = terrains[0].getHeightOfTerrain(randomX, randomZ);
			oFerns[i] = new SimpleObject(fernTextureModel, new Vector3f(randomX, randomY, randomZ), 0f, 0f, 0f, random.nextFloat()+0.04f);
		}
		for (int i = oFerns.length / 2; i < oFerns.length; i++) {
			float randomX = -random.nextFloat() * Terrain.SIZE;
			float randomZ = random.nextInt((int) Terrain.SIZE) - 10.0f;
			float randomY = terrains[1].getHeightOfTerrain(randomX, randomZ);
			oFerns[i] = new SimpleObject(fernTextureModel, new Vector3f(randomX, randomY, randomZ), 0f, 0f, 0f, random.nextFloat()+0.04f);
		}
		
		rendererManager = new RendererManager(loader, ShaderFactry.WITH_SPECULAR_LIGHT);
	}

	@Override
	protected void onUpdate() {
		lights.get(0).update();
	}
	
	@Override
	protected void onRender() {
		for (int i = 0; i < oTrees.length; i++) {
			rendererManager.addObject(oTrees[i]);
		}
		for (int i = 0; i < oGrasses.length; i++) {
			rendererManager.addObject(oGrasses[i]);
		}
		for (int i = 0; i < oFerns.length; i++) {
			rendererManager.addObject(oFerns[i]);
		}
		for (int i = 0; i < terrains.length; i++) {
			rendererManager.addTerrain(terrains[i]);
		}
		rendererManager.renderAll(lights, getMainCamera(), new Vector4f(0.5f, 0.8f, 0.95f, 1.0f));
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
