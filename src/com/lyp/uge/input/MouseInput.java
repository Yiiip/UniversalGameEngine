package com.lyp.uge.input;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.glfw.GLFWCursorEnterCallback;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.util.vector.Vector2f;

import com.lyp.uge.game.Global;
import com.lyp.uge.logger.Logger;

public class MouseInput {

	private static MouseInput mInstance;
	
	private CursorPosCallback cursorPosCallback;
	private MouseButtonCallback mouseButtonCallback;
	private CursorEnterCallback cursorEnterCallback;
	private MouseWheelCallback mouseWheelCallback;
	
	private final Vector2f previousPos;
    private final Vector2f currentPos;
    private final Vector2f deltaVec;
    private final Vector2f wheelOffsets;

    private boolean inWindow = false;
    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;
    private boolean middleButtonPressed = false;
    private boolean wheelScrollUp = false;
    private boolean wheelScrollDown = false;
	
	private MouseInput() {
		previousPos = new Vector2f(-1.0f, -1.0f);
		currentPos = new Vector2f(0.0f, 0.0f);
		deltaVec = new Vector2f();
		wheelOffsets = new Vector2f(0.0f, 0.0f);
		
		if (cursorPosCallback == null) {
			cursorPosCallback = new CursorPosCallback();
		}
		if (mouseButtonCallback == null) {
			mouseButtonCallback = new MouseButtonCallback();
		}
		if (cursorEnterCallback == null) {
			cursorEnterCallback = new CursorEnterCallback();
		}
		if (mouseWheelCallback == null) {
			mouseWheelCallback = new MouseWheelCallback();
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
		}
	}
	
	class MouseButtonCallback extends GLFWMouseButtonCallback {
		@Override
		public void invoke(long window, int button, int action, int mods) {
			leftButtonPressed = (button == Mouse.MOUSE_BUTTON_LEFT) && (action == GLFW_PRESS);
            rightButtonPressed = (button == Mouse.MOUSE_BUTTON_RIGHT) && (action == GLFW_PRESS);
            middleButtonPressed = (button == Mouse.MOUSE_BUTTON_MIDDLE) && (action == GLFW_PRESS);
			Logger.d("MouseBtn", "btn" + button + " | pressed" + action);
		}
	}
	
	class CursorEnterCallback extends GLFWCursorEnterCallback {
		@Override
		public void invoke(long window, boolean entered) {
			inWindow = entered;
			Logger.d("Mouse", inWindow ? "inside" : "outside");
		}
	}
	
	class MouseWheelCallback extends GLFWScrollCallback {
		@Override
		public void invoke(long window, double xoffset, double yoffset) {
			if (yoffset > 0) {
				wheelScrollUp = true;
				wheelScrollDown = false;
			} else if (yoffset < 0) {
				wheelScrollUp = false;
				wheelScrollDown = true;
			}
			wheelOffsets.y = (float) yoffset;
			wheelOffsets.x = (float) xoffset;
			Logger.d("MouseScroll", "[" + xoffset + ", " + yoffset + "]");
		}
	}
	
	public void update(long windowId) {
		if (Global.debug_mouse) { Logger.d("Mouse", toString()); }
		
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
        
        if (wheelOffsets.y != 0) {
        	wheelOffsets.y += (-1*wheelOffsets.y * 0.050f); //后面的乘数越小衰减越慢
			if (Math.abs(wheelOffsets.y) <= 0.03) { //最小值
				wheelOffsets.y = 0;
				wheelScrollDown = false;
				wheelScrollUp = false;
			}
		}
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
	
	public MouseWheelCallback getMouseWheelCallback() {
		return mouseWheelCallback;
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
	
	public boolean isWheelScrollUp() {
		return wheelScrollUp;
	}
	
	public boolean isWheelScrollDown() {
		return wheelScrollDown;
	}
	
	public Vector2f getWheelOffsets() {
		return wheelOffsets;
	}
	
	public float getPosX() {
		return currentPos.x;
	}
	
	public float getPosY() {
		return currentPos.y;
	}
	
	@Override
	public String toString() {
		return "(" + getPosX() + ", " + getPosY() + ")";
	}
}
