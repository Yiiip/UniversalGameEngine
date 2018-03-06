package com.lyp.uge.input;

import static org.lwjgl.glfw.GLFW.*;

public class Mouse {
	
	public static float MOUSE_SENSITIVITY = 4.0f;

	// Mouse button codes.
	public static final int MOUSE_BUTTON_LEFT	= GLFW_MOUSE_BUTTON_LEFT;
	public static final int MOUSE_BUTTON_RIGHT	= GLFW_MOUSE_BUTTON_RIGHT;
	public static final int MOUSE_BUTTON_MIDDLE	= GLFW_MOUSE_BUTTON_MIDDLE;
	
	// Mouse button states.
	public static final int MOUSE_WAITING	= -1;
	public static final int MOUSE_PRESS		= GLFW_PRESS;
	public static final int MOUSE_RELEASE	= GLFW_RELEASE;
}
