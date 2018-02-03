package com.lyp.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.openal.AL11;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.lyp.uge.audio.AudioBuffer;
import com.lyp.uge.audio.AudioListener;
import com.lyp.uge.audio.AudioManager;
import com.lyp.uge.audio.AudioSource;
import com.lyp.uge.game.GameApplication;
import com.lyp.uge.gameObject.SimpleObject;
import com.lyp.uge.gameObject.light.Light;
import com.lyp.uge.gameObject.light.PointLight;
import com.lyp.uge.prefab.PrefabsManager;
import com.lyp.uge.prefab.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.RendererManager;
import com.lyp.uge.shader.ShaderFactry;
import com.lyp.uge.terrain.Terrain;
import com.lyp.uge.terrain.TerrainTexturePack;
import com.lyp.uge.texture.Texture;
import com.lyp.uge.utils.DataUtils;

public class TestTerrainsWithFog extends GameApplication {

	private static Vector4f SKY_COLOR_DAY = new Vector4f(0.78f, 0.85f, 0.95f, 1.0f);
	private static Vector4f SKY_COLOR_NIGHT = new Vector4f(33f/255f, 57f/255f, 88f/255f, 1.0f);
	
	private static Vector3f COLOR_WHITE = new Vector3f(1.f, 1.f, 1.f);
	private static Vector3f COLOR_RED = new Vector3f(1.f, 0.f, 0.f);
	private static Vector3f COLOR_YELLOW = new Vector3f(1.f, 223f/255f, 68f/255f);
	private static Vector3f COLOR_DARK = new Vector3f(.33f, .33f, .33f);
	
	private Loader loader = new Loader();
	private SimpleObject[] oTrees;
	private SimpleObject[] oGrasses;
	private SimpleObject[] oFerns;
	private Terrain[] terrains;
	private List<Light> lights;
	private RendererManager rendererManager;
	private PrefabsManager prefabsManager;
	
	private AudioManager soundMgr;
	
	private Random random = new Random();

	@Override
	protected void onInitWindow(int winWidth, int winHeight, String winTitle, boolean winResizeable) {
		super.onInitWindow(1600, 900, winTitle, winResizeable);
	}

	@Override
	protected void onCreate() {
		enablePolygonMode();
		getMainCamera().setSpeed(0.7f);
		getMainCamera().setPosition(new Vector3f(0, 5, 0));
		
		lights = new ArrayList<>();
		lights.add(new Light(new Vector3f(0.0f, 1000.0f, -500.0f), COLOR_DARK, loader));
		lights.add(new PointLight(new Vector3f(-35.0f, 15.0f, -90.0f), COLOR_YELLOW, loader, new Vector3f(1.f, .01f, .002f)));
		lights.add(new PointLight(new Vector3f(35.0f, 15.0f, -90.0f), COLOR_RED, loader, new Vector3f(1.f, .01f, .002f)));

		prefabsManager = new PrefabsManager(loader);
		prefabsManager.loadPrefabs(DataUtils.CONFIG_PREFABS);
		
		//树木
		TextureModel prefabTree = prefabsManager.getPrefabByName("tree");
		oTrees = new SimpleObject[1000];
		for (int i = 0; i < oTrees.length; i++) {
			oTrees[i] = new SimpleObject(prefabTree, new Vector3f(
					random.nextFloat() * Terrain.SIZE*2 - Terrain.SIZE, 0, -random.nextInt((int) Terrain.SIZE)), 0f, 0f, 0f, 2.0f +random.nextFloat()*2);
		}
		
		//草类植物
		TextureModel prefabGrass = prefabsManager.getPrefabByName("grass");
		oGrasses = new SimpleObject[2000];
		for (int i = 0; i < oGrasses.length; i++) {
			oGrasses[i] = new SimpleObject(prefabGrass, new Vector3f(
					random.nextFloat() * Terrain.SIZE*2 - Terrain.SIZE, 0, -random.nextInt((int) Terrain.SIZE)-10.0f), 0f, 0f, 0f, random.nextFloat()+0.05f);
		}
		TextureModel prefabFern = prefabsManager.getPrefabByName("fern");
		oFerns = new SimpleObject[1100];
		for (int i = 0; i < oFerns.length; i++) {
			oFerns[i] = new SimpleObject(prefabFern, new Vector3f(
					random.nextFloat() * Terrain.SIZE*2 - Terrain.SIZE, 0, -random.nextInt((int) Terrain.SIZE)-10.0f), 0f, 0f, 0f, random.nextFloat()+0.04f);
		}
		
		//地形
		Texture bgTexture = loader.loadTexture("res/texture/" + DataUtils.TEX_GRASS)
				.setShineDamper(10.0f)
				.setReflectivity(0.5f)
				.addFoggy(0.003f, 1.5f);
		Texture rTexture = loader.loadTexture(DataUtils.TEX_MUD);
		Texture gTexture = loader.loadTexture(DataUtils.TEX_GRASS_WITH_FLOWERS);
		Texture bTexture = loader.loadTexture(DataUtils.TEX_GROUND01);
		Texture blendMapTexture = loader.loadTexture(DataUtils.TEX_TERRAIN_BLEND_MAP);
		TerrainTexturePack texturePack = new TerrainTexturePack(bgTexture, rTexture, gTexture, bTexture);
		terrains = new Terrain[2];
		terrains[0] = new Terrain(0, -1, loader, texturePack, blendMapTexture, DataUtils.TEX_TERRAIN_HEIGHT_MAP01, 60);
		terrains[1] = new Terrain(-1, -1, loader, texturePack, blendMapTexture, DataUtils.TEX_TERRAIN_HEIGHT_MAP01, 60);
		
		rendererManager = new RendererManager(ShaderFactry.WITH_MULTI_LIGHTS);
		
		soundMgr = new AudioManager();
		soundMgr.init();
		soundMgr.setAttenuationModel(AL11.AL_EXPONENT_DISTANCE);
		setupSounds();
	}
	
	private void setupSounds() {
		AudioBuffer buffBack = new AudioBuffer(DataUtils.SOUND_HAILSTORM);
		soundMgr.addSoundBuffer(buffBack);
		AudioSource sourceBack = new AudioSource(true, true);
		sourceBack.setBuffer(buffBack.getBufferId());
		soundMgr.addSoundSource("hailstorm.ogg", sourceBack);
		soundMgr.setListener(new AudioListener(new Vector3f(0, 0, 0)));
		sourceBack.play();        
	}

	@Override
	protected void onUpdate() {
		lights.get(0).update();
		soundMgr.updateListenerPosition(getMainCamera());
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
		rendererManager.renderAll(lights, getMainCamera(), SKY_COLOR_NIGHT);
	}
	
	@Override
	public void onKeyReleased(int keycode) {
		super.onKeyReleased(keycode);
	}

	@Override
	protected void onDestory() {
		rendererManager.cleanUp();
		loader.cleanUp();
		soundMgr.cleanup();
	}

	public static void main(String[] args) {
		new TestTerrainsWithFog().start();
	}

}
