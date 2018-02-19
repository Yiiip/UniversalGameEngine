package com.lyp.uge.window;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

import com.lyp.uge.game.Global;

public class WindowManager {

	//(16:9) 1366*768, 1600*900, 1920*1080
	public final static int DEFAULT_WIDTH = 1280;
	public final static int DEFAULT_HEIGHT = 720;
	public final static String DEFAULT_TITLE = "Universal Game Engine";
	public final static boolean DEFAULT_RESIZEABLE = true;
	
	private static Window mWindow;
	
	private WindowManager() {} //no instance

	public static Window createWindow() {
		return createWindow(WindowManager.DEFAULT_WIDTH, WindowManager.DEFAULT_HEIGHT, WindowManager.DEFAULT_TITLE, WindowManager.DEFAULT_RESIZEABLE);
	}
	
	public static Window createWindow(int width, int height) {
		return createWindow(width, height, WindowManager.DEFAULT_TITLE, WindowManager.DEFAULT_RESIZEABLE);
	}
	
	public static Window createWindow(int width, int height, String title) {
		return createWindow(width, height, title, WindowManager.DEFAULT_RESIZEABLE);
	}
	
	public static Window createWindow(int width, int height, String title, boolean resizeable) {
		GLFWErrorCallback.createPrint(System.err).set();
		if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW !");

		// Configure GLFW
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
		glfwWindowHint(GLFW_SAMPLES, Global.anti_aliasing); // Anti-aliasing
		setWindowResizable(resizeable);

		// Create the window
		long genWinID = glfwCreateWindow(width, height, title, NULL, NULL);
		if (genWinID == NULL) throw new RuntimeException("Failed to create window !");
		
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		/*glfwSetKeyCallback(mWindow, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});*/

		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(genWinID, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(genWinID, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(genWinID);
		// Enable v-sync
		glfwSwapInterval(1);
		// Make the window visible
		glfwShowWindow(genWinID);
		
		mWindow = new Window(genWinID, width, height, title, resizeable);
		return mWindow;
	}

	public static void updateWindow() {
	}
	
	private static void setWindowResizable(boolean enable) {
		glfwWindowHint(GLFW_RESIZABLE, enable ? GLFW_TRUE : GLFW_FALSE);
	}
	
	public static boolean windowShouldClose(long window) {
		return glfwWindowShouldClose(window);
	}
	
	public static void colseWindow() {
		glfwSetWindowShouldClose(mWindow.getId(), true);
	}
	
	public static void colseWindow(long window) {
		glfwSetWindowShouldClose(window, true);
	}

	public static void destoryWindow() {
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(mWindow.getId());
		glfwDestroyWindow(mWindow.getId());
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public static void destoryWindow(long window) {
		glfwFreeCallbacks(window);
		glfwDestroyWindow(window);
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
	
	public static void setWindowTitle(long window, String title) {
		glfwSetWindowTitle(window, title);
	}
	
	public static int getWindowWidth() {
		return mWindow.getWidth();
	}
	
	public static int getWindowHeight() {
		return mWindow.getHeight();
	}
	
	public static float getAspectRatio() {
		return (float)getWindowWidth() / (float)getWindowHeight();
	}
}
