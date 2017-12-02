package com.lyp.uge.input;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFWKeyCallback;

import com.lyp.uge.input.Keyboard.OnKeyboardListener;
import com.lyp.uge.window.WindowManager;

public class Input extends GLFWKeyCallback {
	
	private static Input mInput;
	private static boolean[] keys = new boolean[350];

	private List<OnKeyboardListener> mListeners = new ArrayList<>();
	
	private Input() {
	}
	
	public static Input getInstance() {
		if (mInput == null) {
			mInput = new Input();
		}
		return mInput;
	}
	
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		//release ESC to close window
		if (Keyboard.KEY_ESCAPE == key && GLFW_RELEASE == action) {
			WindowManager.colseWindow(window);
		}
		
		if (mListeners != null && action == GLFW_RELEASE) {
			for (OnKeyboardListener listener : mListeners) {
				listener.onKeyReleased(key);
			}
		}
		
		keys[key] = (action != GLFW_RELEASE);
	}
	
	public boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
	
	public void registerOnKeyboardListener(OnKeyboardListener listener) {
		mListeners.add(listener);
	}
}
