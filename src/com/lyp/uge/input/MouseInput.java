package com.lyp.uge.input;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;

import com.lwjgl.util.vector.Vector2f;
import com.lyp.uge.logger.Logger;

public class MouseInput {

	private static MouseInput mInstance;
	
	private CursorPosCallback cursorPosCallback;
	private MouseButtonCallback mouseButtonCallback;
	private CursorEnterCallback cursorEnterCallback;
	
	private final Vector2f previousPos;
    private final Vector2f currentPos;
    private final Vector2f deltaVec;

    private boolean inWindow = false;
    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;
    private boolean middleButtonPressed = false;
	
	private MouseInput() {
		previousPos = new Vector2f(-1.0f, -1.0f);
		currentPos = new Vector2f(0.0f, 0.0f);
		deltaVec = new Vector2f();
		
		if (cursorPosCallback == null) {
			cursorPosCallback = new CursorPosCallback();
		}
		if (mouseButtonCallback == null) {
			mouseButtonCallback = new MouseButtonCallback();
		}
		if (cursorEnterCallback == null) {
			cursorEnterCallback = new CursorEnterCallback();
		}
	}
	
	public static MouseInput getInstance() {
		if (mInstance == null) {
			mInstance = new MouseInput();
		}
		return mInstance;
	}
	
	class CursorPosCallback extends GLFWCursorPosCallback {
		@Override
		public void invoke(long window, double xpos, double ypos) {
			currentPos.x = (float) xpos;
            currentPos.y = (float) ypos;
			Logger.d("Mouse", "(" + currentPos.x + ", " + currentPos.y + ")");
		}
	}
	
	class MouseButtonCallback extends GLFWMouseButtonCallback {
		@Override
		public void invoke(long window, int button, int action, int mods) {
			leftButtonPressed		 = (button == Mouse.MOUSE_BUTTON_LEFT)		 && (action == GLFW_PRESS);
            rightButtonPressed	 = (button == Mouse.MOUSE_BUTTON_RIGHT)	 && (action == GLFW_PRESS);
            middleButtonPressed = (button == Mouse.MOUSE_BUTTON_MIDDLE) && (action == GLFW_PRESS);
			Logger.d("Mouse", "btn" + button + " | pressed" + action);
		}
	}
	
	class CursorEnterCallback extends GLFWCursorEnterCallback {
		@Override
		public void invoke(long window, boolean entered) {
			inWindow = entered;
			Logger.d("Mouse", inWindow ? "inside" : "outside");
		}
	}
	
	public void update(long windowId) {
		deltaVec.x = 0;
		deltaVec.y = 0;
        if (inWindow && previousPos.x > 0 && previousPos.y > 0) {
            float deltaX = currentPos.x - previousPos.x;
            float deltaY = currentPos.y - previousPos.y;
            boolean rotateX = deltaX != 0;
            boolean rotateY = deltaY != 0;
            if (rotateX) {
            	deltaVec.y = deltaX;
            }
            if (rotateY) {
            	deltaVec.x = deltaY;
            }
        }
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
	}
	
	public CursorPosCallback getCursorPosCallback() {
		return cursorPosCallback;
	}
	
	public MouseButtonCallback getMouseButtonCallback() {
		return mouseButtonCallback;
	}
	
	public CursorEnterCallback getCursorEnterCallback() {
		return cursorEnterCallback;
	}
	
	public boolean isMousePressed(int buttonCode) {
		if (buttonCode == Mouse.MOUSE_BUTTON_LEFT) {
			return leftButtonPressed;
		} else if (buttonCode == Mouse.MOUSE_BUTTON_RIGHT) {
			return rightButtonPressed;
		} else if (buttonCode == Mouse.MOUSE_BUTTON_MIDDLE) {
			return middleButtonPressed;
		} else {
			return false;
		}
	}
	
	public boolean isInWindow() {
		return inWindow;
	}
	
	public Vector2f getDeltaVec() {
		return deltaVec;
	}
}
