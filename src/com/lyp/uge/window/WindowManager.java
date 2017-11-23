package com.lyp.uge.window;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.system.MemoryStack;

public class WindowManager {

	// Default window values
	private static int WIDTH = 1280;
	private static int HEIGHT = 720;
	private static String TITLE = "Universal Game Engine";

	private static long mWindow; // The window handle id
	
	private WindowManager() {} //no instance

	public static long createWindow() {
		// Setup an error callback. The default implementation
		// will print the error message in System.err.
		GLFWErrorCallback.createPrint(System.err).set();
		
		// Initialize GLFW. Most GLFW functions will not work before doing this.
		if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");

		// Configure GLFW
		glfwDefaultWindowHints(); // optional, the current window hints are already the default
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
		glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable

		// Create the window
		mWindow = glfwCreateWindow(WIDTH, HEIGHT, TITLE, NULL, NULL);
		if (mWindow == NULL) throw new RuntimeException("Failed to create the GLFW window");
		
		// Setup a key callback. It will be called every time a key is pressed, repeated or released.
		glfwSetKeyCallback(mWindow, (window, key, scancode, action, mods) -> {
			if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
				glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
		});

		// Get the thread stack and push a new frame
		try (MemoryStack stack = stackPush()) {
			IntBuffer pWidth = stack.mallocInt(1); // int*
			IntBuffer pHeight = stack.mallocInt(1); // int*

			// Get the window size passed to glfwCreateWindow
			glfwGetWindowSize(mWindow, pWidth, pHeight);

			// Get the resolution of the primary monitor
			GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			// Center the window
			glfwSetWindowPos(mWindow, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);
		} // the stack frame is popped automatically

		// Make the OpenGL context current
		glfwMakeContextCurrent(mWindow);
		// Enable v-sync
		glfwSwapInterval(1);
		// Make the window visible
		glfwShowWindow(mWindow);
		
		return mWindow;
	}
	
	public static long createWindow(int width, int height) {
		WIDTH = width;
		HEIGHT = height;
		return createWindow();
	}
	
	public static long createWindow(int width, int height, String title) {
		TITLE = title;
		return createWindow(width, height);
	}

	public static void updateWindow() {
	}
	
	public static void setWindowTitle(String title) {
		glfwSetWindowTitle(mWindow, title);
	}
	
	public static String getWindowTitle() {
		return TITLE;
	}

	public static void destoryWindow() {
		// Free the window callbacks and destroy the window
		glfwFreeCallbacks(mWindow);
		glfwDestroyWindow(mWindow);
		// Terminate GLFW and free the error callback
		glfwTerminate();
		glfwSetErrorCallback(null).free();
	}
}
