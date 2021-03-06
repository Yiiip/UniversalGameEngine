package com.lyp.test;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.lyp.uge.gameObject.tool.MousePicker;
import com.lyp.uge.gui.widget.FogControllerView;
import com.lyp.uge.input.Keyboard;
import com.lyp.uge.logger.Logger;
import com.lyp.uge.particle.ComplexParticleGenerator;
import com.lyp.uge.particle.ParticleGenerator;
import com.lyp.uge.particle.ParticlesManager;
import com.lyp.uge.prefab.PrefabsManager;
import com.lyp.uge.prefab.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.RendererManager;
import com.lyp.uge.renderEngine.SkyboxRender;
import com.lyp.uge.scene.Scene;
import com.lyp.uge.shader.ShaderFactry;
import com.lyp.uge.shader.WaterShader;
import com.lyp.uge.terrain.Terrain;
import com.lyp.uge.terrain.TerrainTexturePack;
import com.lyp.uge.texture.ParticleTexture;
import com.lyp.uge.texture.Texture;
import com.lyp.uge.utils.DataUtils;
import com.lyp.uge.water.WaterFrameBuffers;
import com.lyp.uge.water.WaterTile;

public class TestTerrainsWithFog extends GameApplication {

	private static Vector4f SKY_COLOR_DAY = new Vector4f(0.78f, 0.85f, 0.95f, 1.0f);
	private static Vector4f SKY_COLOR_NIGHT = new Vector4f(33f/255f, 57f/255f, 88f/255f, 1.0f);
	
	private static Vector3f COLOR_RED = new Vector3f(1.f, 0.1f, 0.1f);
	private static Vector3f COLOR_YELLOW = new Vector3f(1.f, 223f/255f, 68f/255f);
	private static Vector3f COLOR_DARK = new Vector3f(.33f, .33f, .33f);
	
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
	private TextureModel prefabTree, prefabGrass, prefabFern;
	private WaterFrameBuffers waterFrameBuffers;
	private List<ParticleGenerator> particleGenerators;
	private List<ComplexParticleGenerator> complexParticleGenerators;
	
	private AudioManager audioManager = AudioManager.GetInstance();
	
	private Random random = new Random();

	private FogControllerView fogControllerView;

	@Override
	protected void onInitWindow(int winWidth, int winHeight, String winTitle, boolean winResizeable) {
		super.onInitWindow(1600, 900, winTitle, winResizeable);
	}

	@Override
	protected void onCreate() {
		enablePolygonMode();
		getMainCamera().setSpeed(0.7f);
		getMainCamera().setPosition(new Vector3f(0.0f, 5.0f, 80.0f));
		getMainCamera().setYaw(40.0f);
		
		//光源
		lights = new ArrayList<>();
		lights.add(new Light(new Vector3f(0.0f, 1000.0f, -500.0f), new Vector3f(125f/255f, 120f/255f, 56f/255f), loader));
		lights.add(new PointLight(new Vector3f(-35.0f, 15.0f, -90.0f), COLOR_YELLOW, loader, new Vector3f(1.f, .01f, .002f)));
		lights.add(new PointLight(new Vector3f(35.0f, 15.0f, -90.0f), COLOR_RED, loader, new Vector3f(1.f, .01f, .002f)));
		lights.add(new PointLight(new Vector3f(85.0f, 15.0f, -110.0f), COLOR_YELLOW, loader, new Vector3f(1.f, .01f, .002f)));
		
		//水
		waterTiles = new ArrayList<>();
		waterTiles.add(new WaterTile(85.0f, -85.0f, -3.0f));
		waterTiles.add(new WaterTile(0, Terrain.SIZE_DEFAULT - 1.5f, 0.0f, Terrain.SIZE_DEFAULT, 30));
		
		//地形
		Texture bgTexture = loader.loadTexture("res/texture/" + DataUtils.TEX_GRASS)
				.setShineDamper(1.1f)
				.setReflectivity(0.6f)
				.addFoggy(0.003f, 1.5f);
		Texture rTexture = loader.loadTexture(DataUtils.TEX_MUD);
		Texture gTexture = loader.loadTexture(DataUtils.TEX_GRASS_WITH_FLOWERS);
		Texture bTexture = loader.loadTexture(DataUtils.TEX_GROUND01);
		Texture blendMapTexture = loader.loadTexture(DataUtils.TEX_TERRAIN_BLEND_MAP);
		TerrainTexturePack texturePack = new TerrainTexturePack(bgTexture, rTexture, gTexture, bTexture);
		terrains = new Terrain[2]; //基于HeightMap的地形
		terrains[0] = new Terrain(0, -1, loader, texturePack, blendMapTexture, DataUtils.TEX_TERRAIN_HEIGHT_MAP01, 60);
		terrains[1] = new Terrain(-1, -1, loader, texturePack, blendMapTexture, DataUtils.TEX_TERRAIN_HEIGHT_MAP01, 60);
		
		//加载预制体
		prefabsManager = new PrefabsManager(loader);
		prefabsManager.loadPrefabs(DataUtils.CONFIG_PREFABS);
		
		//树木
		prefabTree = prefabsManager.getPrefabByName("tree");
		oTrees = new SimpleObject[1000];
		for (int i = 0; i < oTrees.length / 2; i++) {
			float randomX = random.nextFloat() * terrains[0].getSize();
			float randomZ = -random.nextInt((int) terrains[0].getSize());
			float randomY = terrains[0].getHeightOfTerrain(randomX, randomZ);
			oTrees[i] = new SimpleObject(prefabTree, new Vector3f(randomX, randomY, randomZ), 0f, 0f, 0f, 2.0f+random.nextFloat()*2);
		}
		for (int i = oTrees.length / 2; i < oTrees.length; i++) {
			float randomX = -random.nextFloat() * terrains[1].getSize();
			float randomZ = -random.nextInt((int) terrains[1].getSize());
			float randomY = terrains[1].getHeightOfTerrain(randomX, randomZ);
			oTrees[i] = new SimpleObject(prefabTree, new Vector3f(randomX, randomY, randomZ), 0f, 0f, 0f, 2.0f+random.nextFloat()*2);
		}
		
		//草类植物1
		prefabGrass = prefabsManager.getPrefabByName("grass");
		oGrasses = new SimpleObject[2000];
		for (int i = 0; i < oGrasses.length / 2; i++) {
			float randomX = random.nextFloat() * terrains[0].getSize();
			float randomZ = -random.nextInt((int) terrains[0].getSize()) - 10.0f;
			float randomY = terrains[0].getHeightOfTerrain(randomX, randomZ);
			oGrasses[i] = new SimpleObject(prefabGrass, new Vector3f(randomX, randomY, randomZ), 0f, 0f, 0f, random.nextFloat()+0.05f);
		}
		for (int i = oGrasses.length / 2; i < oGrasses.length; i++) {
			float randomX = -random.nextFloat() * terrains[1].getSize();
			float randomZ = -random.nextInt((int) terrains[1].getSize()) - 10.0f;
			float randomY = terrains[1].getHeightOfTerrain(randomX, randomZ);
			oGrasses[i] = new SimpleObject(prefabGrass, new Vector3f(randomX, randomY, randomZ), 0f, 0f, 0f, random.nextFloat()+0.05f);
		}
		//草类植物2
		prefabFern = prefabsManager.getPrefabByName("fern");
		oFerns = new SimpleObject[1100];
		for (int i = 0; i < oFerns.length / 2; i++) {
			float randomX = random.nextFloat() * terrains[0].getSize();
			float randomZ = -random.nextInt((int) terrains[0].getSize()) - 10.0f;
			float randomY = terrains[0].getHeightOfTerrain(randomX, randomZ);
			oFerns[i] = new SimpleObject(prefabFern, new Vector3f(randomX, randomY, randomZ), 0f, 0f, 0f, random.nextFloat()+0.04f);
		}
		for (int i = oFerns.length / 2; i < oFerns.length; i++) {
			float randomX = -random.nextFloat() * terrains[1].getSize();
			float randomZ = -random.nextInt((int) terrains[1].getSize()) - 10.0f;
			float randomY = terrains[1].getHeightOfTerrain(randomX, randomZ);
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
		rendererManager.setFbos(waterFrameBuffers);
		
		//粒子系统
		ParticlesManager.init(loader, rendererManager.getProjectionMatrix());
		complexParticleGenerators = new ArrayList<ComplexParticleGenerator>();
		complexParticleGenerators.add(new ComplexParticleGenerator(60, 8, -0.5f, 3, 20, new ParticleTexture(DataUtils.TEX_PARTICLE_SMOKE, 8)));
		particleGenerators = new ArrayList<ParticleGenerator>();
		particleGenerators.add(new ParticleGenerator(40, 25, 0.2f, 4, new ParticleTexture(DataUtils.TEX_PARTICLE_STAR_02)));
		particleGenerators.add(new ParticleGenerator(100, 18, 0.3f, 4, new ParticleTexture(DataUtils.TEX_PARTICLE_COSMIX_02, 4)));
		particleGenerators.add(new ParticleGenerator(160, -18, 0.3f, 4, new ParticleTexture(DataUtils.TEX_PARTICLE_COSMIX_01, 4)));

		//声音
		setupSounds();

		//GUI
		fogControllerView = new FogControllerView(prefabTree.getTexture().getFoggyDensity(), prefabTree.getTexture().getFoggyGradient());
	}
	
	private void setupSounds() {
		audioManager.setAttenuationModel(AL11.AL_EXPONENT_DISTANCE);

		AudioBuffer buffBgMusic = new AudioBuffer(DataUtils.MUSIC_TOYS_UNDER_THE_TREE);
		audioManager.addSoundBuffer(buffBgMusic);
		AudioSource sourceBgMusic = new AudioSource(true, true);
		sourceBgMusic.setBuffer(buffBgMusic.getBufferId());
		audioManager.addSoundSource("BgMusic", sourceBgMusic);
		audioManager.setListener(new AudioListener());
		sourceBgMusic.play();
	}

	@Override
	protected void onUpdate() {
		lights.get(0).update();
		
		particleGenerators.get(0).generateParticles(new Vector3f(35.0f, 15.0f, -90.0f));
		particleGenerators.get(1).generateParticles(new Vector3f(85.0f, 15.0f, -110.0f));
		particleGenerators.get(2).generateParticles(new Vector3f(0.0f, 15.0f, -110.0f));
		complexParticleGenerators.get(0).generateParticles(new Vector3f(-65.0f, 3.0f, -130.0f));
		ParticlesManager.update(getMainCamera());

		audioManager.updateListenerPosition(getMainCamera());

		fogControllerView.update(getMainWindow());
		for (int i = 0; i < terrains.length; i++) {
			terrains[i].addFoggy(fogControllerView.getFogDensity(), fogControllerView.getFogGradient());
		}
		prefabTree.getTexture().addFoggy(fogControllerView.getFogDensity(), fogControllerView.getFogGradient());
		prefabGrass.getTexture().addFoggy(fogControllerView.getFogDensity(), fogControllerView.getFogGradient());
		prefabFern.getTexture().addFoggy(fogControllerView.getFogDensity(), fogControllerView.getFogGradient());
	}
	
	@Override
	protected void onRender() {
		for (int i = 0; i < waterTiles.size(); i++) {
			waterFrameBuffers.bindReflectionFrameBuffer();
			float distance = 2 * (getMainCamera().getPosition().y - waterTiles.get(i).getHeight());
			getMainCamera().getPosition().y -= distance;
			getMainCamera().invertPitch();
			rendererManager.renderScene(mainScene, lights, getMainCamera(), SKY_COLOR_NIGHT);
			getMainCamera().getPosition().y += distance;
			getMainCamera().invertPitch();
			
			waterFrameBuffers.bindRefractionFrameBuffer();
			rendererManager.renderScene(mainScene, lights, getMainCamera(), SKY_COLOR_NIGHT);
			waterFrameBuffers.unbindCurrentFrameBuffer();
		}
		
		rendererManager.renderScene(mainScene, lights, getMainCamera(), SKY_COLOR_NIGHT);
		ParticlesManager.render(getMainCamera());

		fogControllerView.onDraw(getMainWindow());
	}

	@Override
	protected void onDrawGUI() {
		super.onDrawGUI();
	}

	@Override
	public void onKeyReleased(int keycode) {
		super.onKeyReleased(keycode);
		if (keycode == Keyboard.KEY_LEFT_CONTROL) {
			WaterShader.changeDebugMode();
		}
	}

	@Override
	protected void onDestory() {
		ParticlesManager.cleanUp();
		waterFrameBuffers.cleanUp();
		rendererManager.cleanUp();
		loader.cleanUp();
		audioManager.cleanup();
		fogControllerView.destory();
	}

	public static void main(String[] args) {
		new TestTerrainsWithFog().start();
	}

}
