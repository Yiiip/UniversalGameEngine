package com.lyp.uge.input;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWKeyCallback;

import com.lyp.uge.window.WindowManager;

public class Input extends GLFWKeyCallback {
	
	private static Input mInput;
	
	private Input() {
	}
	
	public static Input getInstance() {
		if (mInput == null) {
			mInput = new Input();
		}
		return mInput;
	}
	
	private static boolean[] keys = new boolean[65536];
	
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if (Keyboard.KEY_ESCAPE == key && GLFW_RELEASE == action) {
			WindowManager.colseWindow(window);
		}
		keys[key] = (action != GLFW_RELEASE);
	}

	public boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
}
