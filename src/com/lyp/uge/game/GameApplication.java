package com.lyp.uge.game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.File;

import org.lwjgl.opengl.GL;

import org.lwjgl.util.vector.Vector2f;
import com.lyp.uge.fontMeshCreator.FontType;
import com.lyp.uge.fontMeshCreator.GUIText;
import com.lyp.uge.fontRendering.GUITextManager;
import com.lyp.uge.gameObject.Camera;
import com.lyp.uge.gameObject.FirstPersonCamera;
import com.lyp.uge.input.KeyboardInput;
import com.lyp.uge.input.MouseInput;
import com.lyp.uge.input.Keyboard;
import com.lyp.uge.input.Keyboard.OnKeyboardListener;
import com.lyp.uge.logger.Logger;
import com.lyp.uge.logger.Logger.Level;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.utils.DataUtils;
import com.lyp.uge.utils.StringUtils;
import com.lyp.uge.window.Window;
import com.lyp.uge.window.WindowManager;
import com.sun.istack.internal.NotNull;

public abstract class GameApplication implements Runnable, OnKeyboardListener {
	
	private int runTimer = 0;
	protected boolean running = false;
	
	private int fps = 0;
	
	private boolean	enablePolygonMode	= false;
	private int[] polygonModes			= {GL_POINT, GL_LINE, GL_FILL};
	private String[] polygonModeNames	= {"点", "线", "填充"};
	private int polygonModeIndex		= 0;
	
	private Thread thread;
	private Window window;	
	private Camera camera;
	
	private Loader loader = new Loader();
	private FontType fpsFont;
	private GUIText fpsGuiText;
	
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
		GL.createCapabilities(); //create OpenGL Context
		
		if (Global.debug_log) { Logger.setLogOutLevel(Level.DEBUG); }
		Logger.i("OpenGL", glGetString(GL_VERSION));
		Logger.i("Window", window.toString());
		
		if(Global.mode_hide_cursor) { glfwSetInputMode(window.getId(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN); }
		
		initEvent();
		
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		camera = new Camera();
		initFPSTextGUI();
		
		onCreate();
	}
	
	private void initEvent() {
		glfwSetKeyCallback(window.getId(), KeyboardInput.getInstance());
		glfwSetCursorPosCallback(window.getId(), MouseInput.getInstance().getCursorPosCallback());
		glfwSetMouseButtonCallback(window.getId(), MouseInput.getInstance().getMouseButtonCallback());
		glfwSetCursorEnterCallback(window.getId(), MouseInput.getInstance().getCursorEnterCallback());
		glfwSetScrollCallback(window.getId(), MouseInput.getInstance().getMouseWheelCallback());
		KeyboardInput.getInstance().registerOnKeyboardListener(this);
	}
	
	private void update() {
		glfwPollEvents();
		MouseInput.getInstance().update(window.getId());
		camera.update();
		updateFPSTextGUI();
		
		onUpdate();
		
		if (Global.debug_camera) {
			Logger.d("Camera", camera.toShortString());
		}
	}
	
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		onRender();
		GUITextManager.render();
		
		int e = glGetError();
		if (e != GL_NO_ERROR) { Logger.e("Ops! OpenGL has error : " + e); }
		glfwSwapBuffers(window.getId());
	}
	
	@Override
	public void onKeyPressed(int keycode) {
	}
	
	@Override
	public void onKeyReleased(int keycode) {
		if ((enablePolygonMode || Global.mdoe_polygon_view) 
				&& keycode == Keyboard.KEY_TAB) {
			Logger.d("多边形模式", polygonModeNames[polygonModeIndex]);
			glPolygonMode(GL_FRONT_AND_BACK, polygonModes[polygonModeIndex]);
			polygonModeIndex = (polygonModeIndex >= polygonModes.length-1) ? 0 : (polygonModeIndex + 1);
		}
	}
	
	@Override
	public void run() {
		init();
		loop();
		destory();
	}
	
	protected synchronized void start() {
		if (running) {
			return;
		}
		running = true;
		thread = new Thread(this, "GameThread");
		thread.start();
	}
	
	private synchronized void destory() {
		GUITextManager.cleanUp();
		onDestory();
		WindowManager.destoryWindow();
	}
	
	private void loop() {
		long lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double totalUpadates = 60.0;
		final double ns = 1000000000.0 * 1 / totalUpadates; //1s(10^9ns)有60帧，即求每帧有几秒。
		double delta = 0.0;

		int updatesCounter = 0;
		int framesCounter = 0;
		
		while (running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while (delta >= 1.0) {
				update();
				updatesCounter++;
				delta--;
			}
			
			render();
			framesCounter++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				runTimer++;
				
				fps = framesCounter;
				Logger.d("Updates : " + updatesCounter + ",  FPS : " + framesCounter + ", Runtime: " + getRunTimer());
				WindowManager.setWindowTitle(window.getId(), window.getTitle() 
						+ " (Updates : " + updatesCounter + ",  FPS : " + framesCounter + ", Runtime: " + getRunTimer() + ")");
				
				updatesCounter = 0;
				framesCounter = 0;
			}

			if (WindowManager.windowShouldClose(window.getId())) {
				running = false;
			}
		}
	}
	
	private void initFPSTextGUI() {
		GUITextManager.init(loader);
		fpsFont = new FontType(loader.loadTexture("res/texture/" + DataUtils.TEX_FONT_ARIAL).getID(), new File("res/font/" + DataUtils.FONT_ARIAL));
		setFPSTextGUI("FPS : " + getFPS());
	}
	
	private void setFPSTextGUI(String text) {
		fpsGuiText = new GUIText(text, 1, fpsFont, new Vector2f(0.935f, 0.02f), 1.0f, false);
		// fpsGuiText.setColor(0.96f, 0.84f, 0.0f); //yellow
		fpsGuiText.setColor(1.0f, 0.20f, 0.20f); //red
	}
	
	private void updateFPSTextGUI() {
		fpsGuiText.remove();
		setFPSTextGUI("FPS : " + getFPS());
	}
	
	public String getRunTimer() {
		int hour = runTimer / 3600;
		int minute = runTimer / 60 % 60;
		int second = runTimer % 60;
		return StringUtils.formatTime(hour) + ":" + StringUtils.formatTime(minute) + ":" + StringUtils.formatTime(second);
	}
	
	public int getFPS() {
		return fps;
	}
	
	/**
	 * Use engine default first-person camera.
	 * Only must be called in {@code onCreate()}
	 */
	protected void enableFirstPersonCamera() {
		this.camera = new FirstPersonCamera();
	}
	
	/**
	 * Set customized camera.
	 * Only must be called in {@code onCreate()}
	 * 
	 * @param userCamera
	 */
	protected void setUserCamera(@NotNull Camera userCamera) {
		if (userCamera != null) {
			this.camera = userCamera;
		}
	}
	
	protected Camera getMainCamera() {
		return camera;
	}
	
	protected Window getMainWindow() {
		return window;
	}
	
	protected void enablePolygonMode() {
		this.enablePolygonMode = true;
	}
	
	protected void disablePolygonMode() {
		this.enablePolygonMode = false;
	}
}
