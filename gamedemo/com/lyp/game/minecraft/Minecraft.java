package com.lyp.game.minecraft;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.lyp.game.minecraft.object.Block;
import com.lyp.game.minecraft.util.DataUtil;
import com.lyp.uge.game.GameApplication;
import com.lyp.uge.gameObject.Light;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.model.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.RendererManager;
import com.lyp.uge.shader.ShaderFactry;
import com.lyp.uge.texture.Texture;

public class Minecraft extends GameApplication {
	
	private Vector4f mSkyColor = new Vector4f(0.0f, 0.68f, 1.0f, 1.0f);
	private static final int CHUNK_HEIGHT = 32;
	private static final int CHUNK_WIDTH = 16;
	
	private Loader loader = new Loader();
	private TextureModel textureModel;
	private Block[] blocks;
	private Light light;
	private RendererManager rendererManager;
	
	private Random random = new Random();
	
	@Override
	protected void onInitWindow(int winWidth, int winHeight, String winTitle, boolean winResizeable) {
		super.onInitWindow(1600, 900, "Minecraft Demo", false);
	}

	@Override
	protected void onCreate() {
		enablePolygonMode();
		getMainCamera().setSpeed(0.4f);
		
		RawModel rawModel = loader.loadToVAO(DataUtil.CUBE_VERTICES, DataUtil.CUBE_TEXTURE_COORDS, DataUtil.CUBE_NORMALS, DataUtil.CUBE_INDICES);
		Texture texture = loader.loadTexture(DataUtil.TEX_GRASS_WITH_DIRT);
		texture.setShineDamper(5.0f)
				.setReflectivity(0.02f)
				.setAmbientLightness(0.8f);
				//.addFoggy(0.013f, 1.5f);
		textureModel = new TextureModel(rawModel, texture);
		light = new Light(new Vector3f(0.0f, 50.0f, 20.0f), new Vector3f(1, 1, 1), loader);
		blocks = new Block[CHUNK_WIDTH * CHUNK_WIDTH];
		for (int x = 0; x < CHUNK_WIDTH; x++) {
			for (int z = 0; z < CHUNK_WIDTH; z++) {
				blocks[CHUNK_WIDTH*x + z] = new Block(textureModel, new Vector3f(x, 0, z), 0f, 0f, 0f, 1f); //chunk head
			}
		}
		
		rendererManager = new RendererManager(ShaderFactry.WITH_FOG);
	}

	@Override
	protected void onUpdate() {
		light.update();
	}

	@Override
	protected void onRender() {
		for (int i = 0; i < blocks.length; i++) {
			rendererManager.addObject(blocks[i]);
		}
		rendererManager.renderAll(light, getMainCamera(), mSkyColor);
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
