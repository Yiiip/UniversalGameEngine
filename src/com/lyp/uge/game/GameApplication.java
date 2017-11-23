package com.lyp.uge.game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL;

import com.lyp.uge.logger.Logger;
import com.lyp.uge.logger.Logger.Level;
import com.lyp.uge.window.WindowManager;

public abstract class GameApplication implements Runnable {
	
	protected long window;
	protected boolean running = false;
	
	protected abstract void onUpdate();
	protected abstract void onRender();
	protected abstract void onDestory();

	private void init() {
		window = WindowManager.createWindow(100, 50);
		GL.createCapabilities(); //创建OpenGL Context
		Logger.i("OpenGL", glGetString(GL_VERSION));
		glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		glEnable(GL_DEPTH_TEST);
	}
	
	private void update() {
		glfwPollEvents();
		onUpdate();
	}
	
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		int e = glGetError();
		if (e != GL_NO_ERROR) {
			System.out.println("Ops! gl has error : " + e);
		}
		onRender();
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
				Logger.setLogOutLevel(Level.DEBUG);
				Logger.d("Updates : " + updates + ",  fps : " + frames);
				//TODO title
				updates = 0;
				frames = 0;
			}

			if (glfwWindowShouldClose(window)) {
				running = false;
			}
		}
	}
}
