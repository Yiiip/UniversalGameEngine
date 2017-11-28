package com.lyp.uge.game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL;

import com.lyp.uge.gameObject.Camera;
import com.lyp.uge.input.Input;
import com.lyp.uge.input.Keyboard.OnKeyboardListener;
import com.lyp.uge.logger.Logger;
import com.lyp.uge.window.Window;
import com.lyp.uge.window.WindowManager;

public abstract class GameApplication implements Runnable, OnKeyboardListener {
	
	protected boolean running = false;
	
	private Window window;	
	private Camera camera;
	
	protected abstract void onCreate();
	protected abstract void onUpdate();
	protected abstract void onRender();
	protected abstract void onDestory();

	protected void onInitWindow(int winWidth, int winHeight, String winTitle, boolean winResizeable) {
		int w = winWidth <= 0 ? WindowManager.DEFAULT_WIDTH : winWidth;
		int h = winHeight <= 0 ? WindowManager.DEFAULT_HEIGHT : winHeight;
		String t = (winTitle == null || "".equals(winTitle)) ? WindowManager.DEFAULT_TITLE : winTitle;
		boolean rs = winResizeable;
		this.window = WindowManager.createWindow(w, h, t, rs);
	}
	
	private void init() {
		onInitWindow(WindowManager.DEFAULT_WIDTH, WindowManager.DEFAULT_HEIGHT, WindowManager.DEFAULT_TITLE, WindowManager.DEFAULT_RESIZEABLE);
		glfwSetKeyCallback(window.getId(), Input.getInstance());
		GL.createCapabilities(); //create OpenGL Context
		Logger.i("OpenGL", glGetString(GL_VERSION));
		Logger.d("Window", window.toString());
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		camera = new Camera();
		onCreate();
	}
	
	private void update() {
		glfwPollEvents();
		camera.onMove();
		onKeyDown(Input.getInstance());
		onUpdate();
	}
	
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		onRender();
		int e = glGetError();
		if (e != GL_NO_ERROR) {
			Logger.e("Ops! gl has error : " + e);
		}
		glfwSwapBuffers(window.getId());
	}
	
	@Override
	public void onKeyDown(Input input) {
	}
	
	@Override
	public void run() {
		init();
		loop();
		destory();
	}
	
	private void destory() {
		onDestory();
		WindowManager.destoryWindow();
	}
	
	protected void start() {
		running = true;
		new Thread(this, "GameThread").start();
	}
	
	private void loop() {
		long lastTime = System.nanoTime();
		double ns = 1000000000.0 * 1 / 60.0; //1s(10^9ns)有60帧，即求每帧有几秒。
		double delta = 0.0;
		int updates = 0;
		int frames = 0;
		long timer = System.currentTimeMillis();
		
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			if (delta >= 1.0) {
				update();
				updates++;
				delta--;
			}
			
			render();
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				Logger.d("Updates : " + updates + ",  fps : " + frames);
				WindowManager.setWindowTitle(window.getId(), window.getTitle() + " (Updates : " + updates + ",  fps : " + frames + ")");
				updates = 0;
				frames = 0;
			}

			if (WindowManager.windowShouldClose(window.getId())) {
				running = false;
			}
		}
	}
	
	protected Camera getMainCamera() {
		return camera;
	}
	
	protected Window getMainWindow() {
		return window;
	}
}
