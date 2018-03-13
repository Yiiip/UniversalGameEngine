package com.lyp.game.minecraft;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.lyp.game.minecraft.object.Block;
import com.lyp.game.minecraft.util.DataUtil;
import com.lyp.game.minecraft.util.NoiseGenerator;
import com.lyp.uge.ai.noise.OpenSimplexNoise;
import com.lyp.uge.game.GameApplication;
import com.lyp.uge.game.Global;
import com.lyp.uge.gameObject.GameObject;
import com.lyp.uge.gameObject.light.Light;
import com.lyp.uge.logger.Logger;
import com.lyp.uge.prefab.PrefabsManager;
import com.lyp.uge.prefab.TextureModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.RendererManager;
import com.lyp.uge.scene.Scene;
import com.lyp.uge.shader.ShaderFactry;

public class Minecraft extends GameApplication {
	
	private Vector4f mSkyColor = new Vector4f(0.0f, 0.68f, 1.0f, 1.0f);
	private static final int CHUNK_HEIGHT = 32;
	private static final int CHUNK_WIDTH = 64;
	
	private Loader loader = new Loader();
	private TextureModel prefabGrass, prefabDirt;
	private Block[] blocksHeader;
	private ArrayList<GameObject> blocksBody;
	private List<Light> lights;
	private RendererManager rendererManager;
	private PrefabsManager prefabsManager;
	
	private Scene mainScene;
	
	private Random r = new Random();
	
	@Override
	protected void onInitWindow(int winWidth, int winHeight, String winTitle, boolean winResizeable) {
		super.onInitWindow(1920, 1080, "Minecraft Demo", false);
		Global.debug_camera = false;
	}

	@Override
	protected void onCreate() {
		enablePolygonMode();
		//enableFirstPersonCamera();
		getMainCamera().setPosition(new Vector3f(10, 150, 10));
		getMainCamera().setSpeed(0.4f);
		
		prefabsManager = new PrefabsManager(loader);
		prefabsManager.loadPrefabs(DataUtil.CONFIG_PREFABS);
		prefabGrass = prefabsManager.getPrefabByName("BLOCK_GRASS");
		prefabDirt = prefabsManager.getPrefabByName("BLOCK_DIRT");
		
		lights = new ArrayList<>();
		lights.add(new Light(new Vector3f(0.0f, 50.0f, 20.0f), new Vector3f(1, 1, 1), loader));
		
		int seed = r.nextInt();
		Logger.d("SEED", "seed = " + seed);
		OpenSimplexNoise noise = new OpenSimplexNoise(seed);
		
//		BufferedImage heightMapImage = null;
//		try {
//			heightMapImage = ImageIO.read(new File(DataUtil.TERRAIN_HEIGHT_MAP03));
//		} catch (IOException e) {
//			Logger.e(e);
//		}
		
		blocksHeader = new Block[CHUNK_WIDTH * CHUNK_WIDTH];
		for (int x = 0; x < CHUNK_WIDTH; x++) {
			for (int z = 0; z < CHUNK_WIDTH; z++) {
				int y = (int) (noise.eval(x, z)*40);
				System.out.println("y=" + y);
				blocksHeader[CHUNK_WIDTH*x + z] = new Block(prefabGrass,
						new Vector3f(x, y, z)); //chunk head
			}
		}
//		blocksBody = new ArrayList<Block>();
//		for (int i = 0; i < blocksHeader.length; i++) {
//			float x = blocksHeader[i].getPosition().x;
//			float z = blocksHeader[i].getPosition().z;
//			float y = blocksHeader[i].getPosition().y - 1.0f;
//			for (int yy = (int) y; yy >= -10; yy--) {
//				blocksBody.add(new Block(prefabDirt, new Vector3f(x, yy, z)));
//			}
//		}
		
		mainScene = new Scene();
		mainScene.addObjects(Arrays.asList(blocksHeader));
//		mainScene.addObjects(blocksBody);
		
		rendererManager = new RendererManager(loader, ShaderFactry.WITH_FOG);
	}
	
	private static final int MAX_COLOR = 256 * 256 * 256;
	
	private int getAltitude(int x, int z, BufferedImage image) {
		if (x <= 0 || x >= image.getHeight()
				|| z <= 0 || z >= image.getHeight()) {
			return 0;
		}
		float height = image.getRGB(x, z);
		height += MAX_COLOR/2.f; //keep -MAX_COLOR/2 <= height <= MAX_COLOR/2
		height /= MAX_COLOR/2.f; //keep -1 <= height <= 1
		height *= 40;
		return (int) height;
	}

	@Override
	protected void onUpdate() {
		lights.get(0).update();
	}

	@Override
	protected void onRender() {
		rendererManager.renderScene(mainScene, lights, getMainCamera(), mSkyColor);
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
