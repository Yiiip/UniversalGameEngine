package com.lyp.uge.input;

import static com.lyp.uge.input.Mouse.*;

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
	
	private int leftBtnState = MOUSE_RELEASE;
	private int rightBtnState = MOUSE_RELEASE;
	private int midBtnState = MOUSE_RELEASE;
	
	private boolean leftBtnClicked = false;
	private boolean rightBtnClicked = false;
	private boolean midBtnClicked = false;
	
	private boolean wheelScrollUp = false;
	private boolean wheelScrollDown = false;

	private MouseInput() {
		previousPos = new Vector2f(-1.0f, -1.0f);
		currentPos = new Vector2f(0.0f, 0.0f);
		deltaVec = new Vector2f(0.0f, 0.0f);
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
			// Update mouse button state.
			if (button == MOUSE_BUTTON_LEFT) {
				leftBtnState = action;
			} else if (button == MOUSE_BUTTON_RIGHT) {
				rightBtnState = action;
			} else if (button == MOUSE_BUTTON_MIDDLE) {
				midBtnState = action;
			}
			if (Global.debug_mouse) { Logger.d("MouseBtn", "btn" + button + ((action == MOUSE_PRESS) ? " pressed" : " released")); }

			// Reset click flag.
			if (leftBtnState == MOUSE_RELEASE) {
				leftBtnClicked = false;
			}
			if (rightBtnState == MOUSE_RELEASE) {
				rightBtnClicked = false;
			}
			if (midBtnState == MOUSE_RELEASE) {
				midBtnClicked = false;
			}
		}
	}

	class CursorEnterCallback extends GLFWCursorEnterCallback {
		@Override
		public void invoke(long window, boolean entered) {
			inWindow = entered;
			if (Global.debug_mouse) { Logger.d("Mouse", inWindow ? "inside" : "outside"); }
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
			if (Global.debug_mouse) { Logger.d("MouseScroll", "[" + xoffset + ", " + yoffset + "]"); }
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
			wheelOffsets.y += (-1 * wheelOffsets.y * 0.050f); // 后面的乘数越小衰减越慢
			if (Math.abs(wheelOffsets.y) <= 0.03) { // 最小值
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
		if (buttonCode == MOUSE_BUTTON_LEFT) {
			return (leftBtnState == MOUSE_PRESS);
		} else if (buttonCode == MOUSE_BUTTON_RIGHT) {
			return (rightBtnState == MOUSE_PRESS);
		} else if (buttonCode == MOUSE_BUTTON_MIDDLE) {
			return (midBtnState == MOUSE_PRESS);
		} else {
			return false;
		}
	}

	public boolean isMouseClicked(int buttonCode) {
		if (buttonCode == MOUSE_BUTTON_LEFT) {
			if ((leftBtnState == MOUSE_PRESS) && !leftBtnClicked) {
				leftBtnClicked = true;
				return true;
			} else {
				return false;
			}
		} else if (buttonCode == MOUSE_BUTTON_RIGHT) {
			if ((rightBtnState == MOUSE_PRESS) && !rightBtnClicked) {
				rightBtnClicked = true;
				return true;
			} else {
				return false;
			}
		} else if (buttonCode == MOUSE_BUTTON_MIDDLE) {
			if ((midBtnState == MOUSE_PRESS) && !midBtnClicked) {
				midBtnClicked = true;
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	public int getMouseBottonCurrentState(int buttonCode) {
		if (buttonCode == MOUSE_BUTTON_LEFT) {
			return leftBtnState;
		} else if (buttonCode == MOUSE_BUTTON_RIGHT) {
			return rightBtnState;
		} else if (buttonCode == MOUSE_BUTTON_MIDDLE) {
			return midBtnState;
		} else {
			return MOUSE_WAITING;
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
