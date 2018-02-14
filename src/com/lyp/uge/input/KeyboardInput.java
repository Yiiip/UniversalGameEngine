package com.lyp.uge.input;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.glfw.GLFWKeyCallback;

import com.lyp.uge.input.Keyboard.OnKeyboardListener;
import com.lyp.uge.window.WindowManager;

public class KeyboardInput extends GLFWKeyCallback {
	
	private static final int MAX_KEY_INDEX = 65535;
	
	private static KeyboardInput mInput;
	private static boolean[] keys = new boolean[MAX_KEY_INDEX + 1];

	private List<OnKeyboardListener> mListeners = new ArrayList<>();
	
	private KeyboardInput() {
	}
	
	public static KeyboardInput getInstance() {
		if (mInput == null) {
			mInput = new KeyboardInput();
		}
		return mInput;
	}
	
	@Override
	public void invoke(long window, int key, int scancode, int action, int mods) {
		if (key < 0 || key > MAX_KEY_INDEX) {
			return;
		}
		
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
