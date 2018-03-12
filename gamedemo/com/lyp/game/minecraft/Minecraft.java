package com.lyp.game.minecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.lyp.game.minecraft.object.Block;
import com.lyp.game.minecraft.util.DataUtil;
import com.lyp.game.minecraft.util.NoiseGenerator;
import com.lyp.uge.game.GameApplication;
import com.lyp.uge.gameObject.light.Light;
import com.lyp.uge.logger.Logger;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.prefab.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.RendererManager;
import com.lyp.uge.shader.ShaderFactry;
import com.lyp.uge.texture.Texture;

public class Minecraft extends GameApplication {
	
	private Vector4f mSkyColor = new Vector4f(0.0f, 0.68f, 1.0f, 1.0f);
	private static final int CHUNK_HEIGHT = 64;
	private static final int CHUNK_WIDTH = 16;
	
	private Loader loader = new Loader();
	private TextureModel textureModelHead, textureModelBody;
	private RawModel blockRawModel;
	private Block[] blocksHeader;
	private ArrayList<Block> blocksBody;
	private List<Light> lights;
	private RendererManager rendererManager;
	
	private Random r = new Random();
	
	@Override
	protected void onInitWindow(int winWidth, int winHeight, String winTitle, boolean winResizeable) {
		super.onInitWindow(1600, 900, "Minecraft Demo", false);
	}

	@Override
	protected void onCreate() {
		enablePolygonMode();
		getMainCamera().setSpeed(0.4f);
		
		blockRawModel = loader.loadToVAO(DataUtil.CUBE_VERTICES, DataUtil.CUBE_TEXTURE_COORDS, DataUtil.CUBE_NORMALS, DataUtil.CUBE_INDICES);
		
		Texture texGrassWithDirt = loader.loadTexture(DataUtil.TEX_GRASS_WITH_DIRT);
		texGrassWithDirt.setShineDamper(5.0f)
				.setReflectivity(0.02f)
				.setAmbientLightness(0.8f)
				.addFoggy(0.005f, 1.5f);
		textureModelHead = new TextureModel(blockRawModel, texGrassWithDirt);
		
		Texture texDirt = loader.loadTexture(DataUtil.TEX_DIRT);
		texDirt.setShineDamper(5.0f)
				.setReflectivity(0.02f)
				.setAmbientLightness(0.8f)
				.addFoggy(0.005f, 1.5f);
		textureModelBody = new TextureModel(blockRawModel, texDirt);
		
		lights = new ArrayList<>();
		lights.add(new Light(new Vector3f(0.0f, 50.0f, 20.0f), new Vector3f(1, 1, 1), loader));
		
		int seed = r.nextInt(32767);
		NoiseGenerator noiseGen = new NoiseGenerator(seed);
		Logger.d("SEED", "seed = " + seed);
		
		blocksHeader = new Block[CHUNK_WIDTH * CHUNK_WIDTH];
		for (int x = 0; x < CHUNK_WIDTH; x++) {
			for (int z = 0; z < CHUNK_WIDTH; z++) {
				blocksHeader[CHUNK_WIDTH*x + z] = new Block(textureModelHead, new Vector3f(x, (int) noiseGen.getHeight(x, z, x, z) / 5, z)); //chunk head
			}
		}
		blocksBody = new ArrayList<>();
		for (int i = 0; i < blocksHeader.length; i++) {
			float x = blocksHeader[i].getPosition().x;
			float z = blocksHeader[i].getPosition().z;
			float y = blocksHeader[i].getPosition().y - 1.0f;
			for (int yy = (int) y; yy >= -CHUNK_HEIGHT; yy--) {
				blocksBody.add(new Block(textureModelBody, new Vector3f(x, yy, z)));
			}
		}
		
		rendererManager = new RendererManager(loader, ShaderFactry.WITH_MULTI_LIGHTS);
	}

	@Override
	protected void onUpdate() {
		lights.get(0).update();
	}

	@Override
	protected void onRender() {
		for (int i = 0; i < blocksHeader.length; i++) {
			rendererManager.addObject(blocksHeader[i]);
		}
		for (int i = 0; i < blocksBody.size(); i++) {
			rendererManager.addObject(blocksBody.get(i));
		}
		rendererManager.renderAll(lights, getMainCamera(), mSkyColor);
		rendererManager.clearAll();
	}

	@Override
	protected void onDestory() {
		rendererManager.cleanUp();
		loader.cleanUp();
	}

	public static void main(String[] args) {
		new Minecraft().start();
	}

}
