package com.lyp.gamedemo.flappybird;

import org.lwjgl.util.vector.Vector3f;

import com.lyp.gamedemo.flappybird.object.Bird;
import com.lyp.uge.game.GameApplication;
import com.lyp.uge.logger.Logger;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.Renderer2dManager;

public class FlappyBird extends GameApplication {
	
	private static Bird bird;
	private Level level;
	
	private Loader loader = new Loader();
	private Renderer2dManager rendererManager;

	@Override
	protected void onInitWindow(int winWidth, int winHeight, String winTitle, boolean winResizeable) {
		super.onInitWindow(1280, 720, "FlappyBird", false);
		Logger.setLogOutLevel(com.lyp.uge.logger.Logger.Level.DEBUG);
	}
	
	@Override
	protected void onCreate() {
		enablePolygonMode();
		level = new Level(loader);
		bird = new Bird(loader);
		
		getMainCamera().setPosition(new Vector3f(0.3f, 0.0f, 2.5f));
		getMainCamera().setControl(true);
		rendererManager = new Renderer2dManager();
	}

	@Override
	protected void onUpdate() {
		bird.update();
		level.update();
	}

	@Override
	protected void onRender() {
		rendererManager.addObject(bird);
		level.addToRender(rendererManager);
		rendererManager.renderAll(getMainCamera());
	}

	@Override
	protected void onDestory() {
		loader.cleanUp();
	}
	
	public static Bird getBird() {
		return bird;
	}
	
	public static enum LayerID {
		BACKGROUND(0),
		INSTANCE(1);
		private int lid;
		private LayerID(int lid) {
			this.lid = lid;
		}
		public int value() {
			return lid;
		}
	}
	
	public static enum ObjectID {
		BIRD(0),
		PIPE(1);
		private int oid;
		private ObjectID(int oid) {
			this.oid = oid;
		}
		public int value() {
			return oid;
		}
	}

	public static void main(String[] args) {
		new FlappyBird().start();
	}
}
