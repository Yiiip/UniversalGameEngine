package com.lyp.uge.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Input extends GLFWKeyCallback {
	
	public static boolean[] keys = new boolean[65536];

	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if (GLFW.GLFW_KEY_ESCAPE == key) {
			GLFW.glfwSetWindowShouldClose(window, true);
		}
		
		keys[key] = (action != GLFW.GLFW_RELEASE);
		//System.out.println("keyboard : " + key);
	}

	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
}
