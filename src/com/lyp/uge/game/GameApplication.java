package com.lyp.uge.game;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.File;

import org.lwjgl.opengl.GL;

import org.lwjgl.util.vector.Vector2f;
import com.lyp.uge.fontMeshCreator.FontType;
import com.lyp.uge.fontMeshCreator.GUIText;
import com.lyp.uge.fontRendering.GUITextManager;
import com.lyp.uge.gameObject.camera.Camera;
import com.lyp.uge.gameObject.camera.FirstPersonCamera;
import com.lyp.uge.gui.widget.CursorIndicator;
import com.lyp.uge.gui.widget.FpsCounterView;
import com.lyp.uge.gui.widget.Toolbar;
import com.lyp.uge.input.KeyboardInput;
import com.lyp.uge.input.MouseInput;
import com.lyp.uge.input.Keyboard;
import com.lyp.uge.input.Keyboard.OnKeyboardListener;
import com.lyp.uge.logger.Logger;
import com.lyp.uge.logger.Logger.Level;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.RendererManager;
import com.lyp.uge.terrain.TerrainManager;
import com.lyp.uge.utils.DataUtils;
import com.lyp.uge.utils.StringUtils;
import com.lyp.uge.window.Window;
import com.lyp.uge.window.WindowManager;
import com.sun.istack.internal.NotNull;

public abstract class GameApplication implements Runnable, OnKeyboardListener {
	
	private int fps = 0; //current frame-per-second
	private int ups = 0; //current update-per-second
	private int runtime = 0;
	protected boolean running = false;
	
	private Thread thread;
	private Window window;	
	private Camera camera;
	private Loader loader = new Loader();
	
	// Engine GUI
	private FontType fpsFont;	//unused
	private GUIText fpsGuiText;	//unused
	private FpsCounterView guiFps;
	private Toolbar guiToolbar;
	private CursorIndicator guiCursorIndicator;
	
	protected abstract void onCreate();
	protected abstract void onUpdate();
	protected abstract void onRender();
	protected abstract void onDestory();
	protected void onDrawGUI() { /* Override when needed. */ }

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
		
		if (Global.debug_level) { Logger.setLogOutLevel(Level.DEBUG); }
		Logger.i("Hardware", glGetString(GL_VENDOR) + ", " + glGetString(GL_RENDERER));
		Logger.i("OpenGL", glGetString(GL_VERSION));
		Logger.i("Window", window.toString());
		
		if(Global.mode_hide_cursor) { glfwSetInputMode(window.getId(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN); }
		
		initEvent();
		
		RendererManager.restoreRenderState();
		camera = new Camera();
		
		// if (Global.debug_fps_gui) { initFPSTextGUI(); }
		if (Global.debug_fps_gui) { guiFps = new FpsCounterView(0.f, 0.f); }
		guiToolbar = new Toolbar(window);
		guiCursorIndicator = new CursorIndicator();
		
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
		if (Global.debug_camera) { Logger.d("Camera", camera.toShortString()); }
		
		// if (Global.debug_fps_gui) { updateFPSTextGUI(); }
		if (Global.debug_fps_gui) { guiFps.update(fps, ups, getRunTimer(), window); }
		guiToolbar.update(window);
		guiCursorIndicator.update(window);
		
		onUpdate();
	}
	
	private void render() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		onRender();
		drawGUI();
		
		// if (Global.debug_fps_gui) { GUITextManager.render(); }
		
		int e = glGetError();
		if (e != GL_NO_ERROR) { Logger.e("Ops! OpenGL has error : " + e); }
		
		glfwSwapBuffers(window.getId());
	}
	
	private void drawGUI() {
		if (Global.debug_fps_gui) { guiFps.onDraw(window); }
		guiToolbar.onDraw(window);
		guiCursorIndicator.onDraw(window);
		onDrawGUI();
	}
	
	@Override
	public void onKeyPressed(int keycode) {
	}
	
	@Override
	public void onKeyReleased(int keycode) {
		if (keycode == Keyboard.KEY_TAB && (RendererManager.enablePolygonMode || Global.mdoe_polygon_view)) {
			RendererManager.changePolygonMode();
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
		destoryEngineGui();
		onDestory();
		WindowManager.destoryWindow();
	}
	
	private void destoryEngineGui() {
		// if (Global.debug_fps_gui) { GUITextManager.cleanUp(); }
		if (Global.debug_fps_gui) { guiFps.destory(); }
		guiToolbar.destory();
		guiCursorIndicator.destory();
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
				runtime++;
				
				fps = framesCounter;
				ups = updatesCounter;
				if (Global.debug_fps_window_title) {
					Logger.d("UPS : " + updatesCounter + ",  FPS : " + framesCounter + ", Runtime: " + getRunTimer());
					WindowManager.setWindowTitle(window.getId(), window.getTitle() + " (UPS : " + updatesCounter + ",  FPS : " + framesCounter + ", Runtime: " + getRunTimer() + ")");
				}
				
				updatesCounter = 0;
				framesCounter = 0;
			}

			if (WindowManager.windowShouldClose(window.getId())) {
				running = false;
			}
		}
	}
	
	protected Window getMainWindow() {
		return window;
	}
	
/*----------------------for FPS GUI (Implement without nanovg)----------------------*/
	@SuppressWarnings("unused")
	@Deprecated
	private void initFPSTextGUI() {
		GUITextManager.init(loader);
		fpsFont = new FontType(loader.loadTexture(DataUtils.TEX_FONT_ARIAL).getID(), new File(DataUtils.FNT_ARIAL));
		setFPSTextGUI("FPS : " + getFPS());
	}
	
	@Deprecated
	private void setFPSTextGUI(String text) {
		fpsGuiText = new GUIText(text, 1, fpsFont, new Vector2f(0.935f, 0.02f), 1.0f, false);
		// fpsGuiText.setColor(0.96f, 0.84f, 0.0f); //yellow
		fpsGuiText.setColor(1.0f, 0.20f, 0.20f); //red
	}
	
	@SuppressWarnings("unused")
	@Deprecated
	private void updateFPSTextGUI() {
		fpsGuiText.remove();
		setFPSTextGUI("FPS : " + getFPS());
	}
	
/*----------------------for runtime----------------------*/
	public String getRunTimer() {
		int hour = runtime / 3600;
		int minute = runtime / 60 % 60;
		int second = runtime % 60;
		return StringUtils.formatTime(hour) + ":" + StringUtils.formatTime(minute) + ":" + StringUtils.formatTime(second);
	}
	
	public int getFPS() {
		return fps;
	}
	
	public int getUPS() {
		return ups;
	}
	
/*----------------------for Camera----------------------*/
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
	
	/**
	 * Bind collision between terrains and special camera(e.g. FirstPersonCamera).
	 * @param terrainManager
	 * @return
	 */
	protected boolean pushToCamera(@NotNull TerrainManager terrainManager) {
		if (this.camera instanceof FirstPersonCamera) {
			((FirstPersonCamera) camera).setTerrainManager(terrainManager);
			return true;
		}
		return false;
	}
	
	protected Camera getMainCamera() {
		return camera;
	}
	
/*----------------------for modes----------------------*/
	protected void enablePolygonMode() {
		RendererManager.enablePolygonMode = true;
	}
	
	protected void disablePolygonMode() {
		RendererManager.enablePolygonMode = false;
	}
}
