package com.lyp.game.minecraft;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.lyp.game.minecraft.object.CubeObject;
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
	
	private Loader loader = new Loader();
	private TextureModel textureModel;
	private CubeObject[] cubes;
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
		texture.setShineDamper(5.0f);
		texture.setReflectivity(0.02f);
		textureModel = new TextureModel(rawModel, texture);
		light = new Light(new Vector3f(0.0f, 0.0f, 90.0f), new Vector3f(1, 1, 1), loader);
		cubes = new CubeObject[500];
		for (int i = 0; i < cubes.length; i++) {
			cubes[i] = new CubeObject(textureModel,new Vector3f(random.nextInt(30) - 15, random.nextInt(30) - 15, random.nextInt(15) - 15), 0f, 0f, 0f, 1f);
			cubes[i].addFoggy(0.013f, 1.5f);
		}
		
		rendererManager = new RendererManager(ShaderFactry.WITH_FOG);
	}

	@Override
	protected void onUpdate() {
		light.update();
	}

	@Override
	protected void onRender() {
		for (int i = 0; i < cubes.length; i++) {
			rendererManager.addObject(cubes[i]);
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
