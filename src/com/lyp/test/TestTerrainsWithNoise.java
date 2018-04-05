package com.lyp.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.lyp.uge.game.GameApplication;
import com.lyp.uge.gameObject.SimpleObject;
import com.lyp.uge.gameObject.light.Light;
import com.lyp.uge.input.Keyboard;
import com.lyp.uge.prefab.PrefabsManager;
import com.lyp.uge.prefab.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.RendererManager;
import com.lyp.uge.renderEngine.SkyboxRender;
import com.lyp.uge.scene.Scene;
import com.lyp.uge.shader.ShaderFactry;
import com.lyp.uge.terrain.NoiseTerrain;
import com.lyp.uge.terrain.Terrain;
import com.lyp.uge.terrain.TerrainTexturePack;
import com.lyp.uge.texture.Texture;
import com.lyp.uge.utils.DataUtils;
import com.lyp.uge.water.WaterFrameBuffers;
import com.lyp.uge.water.WaterTile;

public class TestTerrainsWithNoise extends GameApplication {

	private static Vector4f SKY_COLOR_DAY = new Vector4f(0.78f, 0.85f, 0.95f, 1.0f);
	private static Vector3f COLOR_LIGHT = new Vector3f(225f/255f, 220f/255f, 116f/255f);
	
	private SimpleObject[] oTrees;
	private SimpleObject[] oGrasses;
	private SimpleObject[] oFerns;
	private Terrain[] terrains;
	private List<Light> lights;
	private List<WaterTile> waterTiles;

	private Scene mainScene;
	
	private Loader loader = new Loader();
	private RendererManager rendererManager;
	private PrefabsManager prefabsManager;
	private WaterFrameBuffers waterFrameBuffers;
	
	private TerrainTexturePack texturePack;
	private Texture blendMapTexture;
	
	private Random random = new Random();
	
	@Override
	protected void onInitWindow(int winWidth, int winHeight, String winTitle, boolean winResizeable) {
		super.onInitWindow(1600, 900, winTitle, winResizeable);
	}

	@Override
	protected void onCreate() {
		enablePolygonMode();
		getMainCamera().setSpeed(2.5f);
		getMainCamera().setPosition(new Vector3f(1000.0f, 70.0f, -1000.0f));
		getMainCamera().setPitch(1.0f);
		
		//光源
		lights = new ArrayList<>();
		lights.add(new Light(new Vector3f(0.0f, 1000.0f, -500.0f), COLOR_LIGHT, loader));
		
		//水
		waterTiles = new ArrayList<>();
		waterTiles.add(new WaterTile(0.0f, -3000 / 2, 0.0f, 3000 * 2, 80));
		
		//地形
		Texture bgTexture = loader.loadTexture("res/texture/" + DataUtils.TEX_GRASS)
				.setShineDamper(1.1f)
				.setReflectivity(0.6f)
				.addFoggy(0.003f, 1.5f);
		Texture rTexture = loader.loadTexture(DataUtils.TEX_MUD);
		Texture gTexture = loader.loadTexture(DataUtils.TEX_GRASS_WITH_FLOWERS);
		Texture bTexture = loader.loadTexture(DataUtils.TEX_GROUND01);
		blendMapTexture = loader.loadTexture(DataUtils.TEX_TERRAIN_BLEND_MAP);
		texturePack = new TerrainTexturePack(bgTexture, rTexture, gTexture, bTexture);
		terrains = new NoiseTerrain[1]; //基于噪声的地形
		terrains[0] = new NoiseTerrain(3000, 0, -1, loader, texturePack, blendMapTexture);
		
		//加载预制体
		prefabsManager = new PrefabsManager(loader);
		prefabsManager.loadPrefabs(DataUtils.CONFIG_PREFABS);
		
		//树木
		TextureModel prefabTree = prefabsManager.getPrefabByName("tree");
		oTrees = new SimpleObject[2000];
		for (int i = 0; i < oTrees.length; i++) {
			float randomX = random.nextFloat() * terrains[0].getSize();
			float randomZ = -random.nextInt((int) terrains[0].getSize());
			float randomY = terrains[0].getHeightOfTerrain(randomX, randomZ);
			oTrees[i] = new SimpleObject(prefabTree, new Vector3f(randomX, randomY, randomZ), 0f, 0f, 0f, 2.0f+random.nextFloat()*2);
		}
		
		//草类植物1
		TextureModel prefabGrass = prefabsManager.getPrefabByName("grass");
		prefabGrass.getTexture().setAmbientLightness(1.2f);
		oGrasses = new SimpleObject[1000];
		for (int i = 0; i < oGrasses.length; i++) {
			float randomX = random.nextFloat() * terrains[0].getSize();
			float randomZ = -random.nextInt((int) terrains[0].getSize()) - 10.0f;
			float randomY = terrains[0].getHeightOfTerrain(randomX, randomZ);
			oGrasses[i] = new SimpleObject(prefabGrass, new Vector3f(randomX, randomY, randomZ), 0f, 0f, 0f, random.nextFloat()+0.05f);
		}
		//草类植物2
		TextureModel prefabFern = prefabsManager.getPrefabByName("fern");
		oFerns = new SimpleObject[1000];
		for (int i = 0; i < oFerns.length; i++) {
			float randomX = random.nextFloat() * terrains[0].getSize();
			float randomZ = -random.nextInt((int) terrains[0].getSize()) - 10.0f;
			float randomY = terrains[0].getHeightOfTerrain(randomX, randomZ);
			oFerns[i] = new SimpleObject(prefabFern, new Vector3f(randomX, randomY, randomZ), 0f, 0f, 0f, random.nextFloat()+0.04f);
		}
		
		//场景
		mainScene = new Scene();
		mainScene.addObjects(Arrays.asList(oTrees));
		mainScene.addObjects(Arrays.asList(oGrasses));
		mainScene.addObjects(Arrays.asList(oFerns));
		mainScene.addTerrains(Arrays.asList(terrains));
		mainScene.addWaterTiles(waterTiles);
		
		//用于水面的FrameBuffer
		waterFrameBuffers = new WaterFrameBuffers();
		
		//渲染器
		rendererManager = new RendererManager(loader, ShaderFactry.WITH_MULTI_LIGHTS);
		rendererManager.setSkyboxDayRes(SkyboxRender.CUBE_TEXTURE_DAY_FILES_2, loader);
		rendererManager.setSkyboxRecycleSpeed(0.1f);
		rendererManager.setFbos(waterFrameBuffers);
	}

	@Override
	protected void onUpdate() {
		lights.get(0).update();
	}
	
	@Override
	protected void onRender() {
		for (int i = 0; i < waterTiles.size(); i++) {
			waterFrameBuffers.bindReflectionFrameBuffer();
			float distance = 2 * (getMainCamera().getPosition().y - waterTiles.get(i).getHeight());
			getMainCamera().getPosition().y -= distance;
			getMainCamera().invertPitch();
			rendererManager.renderScene(mainScene, lights, getMainCamera(), SKY_COLOR_DAY);
			getMainCamera().getPosition().y += distance;
			getMainCamera().invertPitch();
			
			waterFrameBuffers.bindRefractionFrameBuffer();
			rendererManager.renderScene(mainScene, lights, getMainCamera(), SKY_COLOR_DAY);
			waterFrameBuffers.unbindCurrentFrameBuffer();
		}
		
		rendererManager.renderScene(mainScene, lights, getMainCamera(), SKY_COLOR_DAY);
	}

	@Override
	protected void onDestory() {
		waterFrameBuffers.cleanUp();
		rendererManager.cleanUp();
		loader.cleanUp();
	}

	public static void main(String[] args) {
		new TestTerrainsWithNoise().start();
	}

}
