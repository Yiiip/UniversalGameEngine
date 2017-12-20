package com.lyp.game.flappybird;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import com.lyp.game.flappybird.object.Bird;
import com.lyp.game.flappybird.object.Level;
import com.lyp.game.flappybird.object.Pipe;
import com.lyp.uge.game.GameApplication;
import com.lyp.uge.input.Keyboard;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.Renderer2dManager;

public class FlappyBird extends GameApplication {
	
	public static Status STATUS = Status.PLAYING;
	
	private static Bird bird;
	private static Pipe[] pipes;
	private Level level;
	
	private Random r = new Random();
	private Loader loader = new Loader();
	private Renderer2dManager rendererManager;

	@Override
	protected void onInitWindow(int winWidth, int winHeight, String winTitle, boolean winResizeable) {
		super.onInitWindow(1280, 720, "FlappyBird", false);
	}
	
	@Override
	protected void onCreate() {
		enablePolygonMode();
		initGame();
		
		getMainCamera().setPosition(new Vector3f(0.3f, 0.0f, 2.5f));
		getMainCamera().setControl(true);
		rendererManager = new Renderer2dManager();
	}
	
	private void initGame() {
		level = new Level(loader);
		bird = new Bird(loader);
		pipes = new Pipe[6];
		for (int i = 0; i < pipes.length; i++) {
			pipes[i] = new Pipe(loader, i%2==0 ? 1 : 0, (i+3)*45.0f + r.nextInt(12));
		}
	}

	@Override
	protected void onUpdate() {
		bird.update();
		updatePipes();
		level.update();
	}

	@Override
	protected void onRender() {
		rendererManager.addObject(bird);
		addPipesToRender();
		level.addToRender(rendererManager);
		
		rendererManager.renderAll(getMainCamera());
	}
	
	@Override
	public void onKeyReleased(int keycode) {
		super.onKeyReleased(keycode);
		if (STATUS == Status.GAMEOVER && Keyboard.KEY_G == keycode) {
			resetGame();
		}
	}
	
	private void resetGame() {
		level = null;
		bird = null;
		pipes = null;
		STATUS = Status.PLAYING;
		initGame();
	}

	@Override
	protected void onDestory() {
		loader.cleanUp();
	}
	
	private void addPipesToRender() {
		for (int i = 0; i < pipes.length; i++) {
			rendererManager.addObject(pipes[i]);
		}
	}
	
	private void updatePipes() {
		for (int i = 0; i < pipes.length; i++) {
			pipes[i].update();
		}
	}
	
	public static Bird getBird() {
		return bird;
	}
	
	public static enum LayerID {
		BACKGROUND(0),
		INSTANCE_BACK(1),
		INSTANCE_FRONT(2);
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
	
	public static enum Status {
		GAMEOVER(),
		PLAYING();
	}

	public static void main(String[] args) {
		new FlappyBird().start();
	}
}
