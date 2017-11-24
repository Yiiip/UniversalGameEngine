package com.lyp.uge.game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL;

import com.lyp.uge.logger.Logger;
import com.lyp.uge.logger.Logger.Level;
import com.lyp.uge.window.WindowManager;

public abstract class GameApplication implements Runnable {
	
	protected long window;
	private int winWidth, winHeight;
	private String winTitle;
	protected boolean running = false;
	
	protected abstract void onUpdate();
	protected abstract void onRender();
	protected abstract void onDestory();
	protected abstract void afterCreate();

	protected void onCreate(int winWidth, int winHeight, String winTitle) {
		this.winWidth = winWidth == 0 ? WindowManager.DEFAULT_WIDTH : winWidth;
		this.winHeight = winHeight == 0 ? WindowManager.DEFAULT_HEIGHT : winHeight;
		this.winTitle = (winTitle == null || "".equals(winTitle)) ? WindowManager.DEFAULT_TITLE : winTitle;
	}
	
	private void init() {
		onCreate(winWidth, winHeight, winTitle);
		window = WindowManager.createWindow(winWidth, winHeight, winTitle);
		GL.createCapabilities(); //创建OpenGL Context
		Logger.i("OpenGL", glGetString(GL_VERSION));
		//glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		afterCreate();
	}
	
	private void update() {
		glfwPollEvents();
		onUpdate();
	}
	
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		onRender();
		int e = glGetError();
		if (e != GL_NO_ERROR) {
			Logger.e("Ops! gl has error : " + e);
		}
		glfwSwapBuffers(window);
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
				WindowManager.setWindowTitle(window, winTitle + " (Updates : " + updates + ",  fps : " + frames + ")");
				updates = 0;
				frames = 0;
			}

			if (glfwWindowShouldClose(window)) {
				running = false;
			}
		}
	}
	
}
