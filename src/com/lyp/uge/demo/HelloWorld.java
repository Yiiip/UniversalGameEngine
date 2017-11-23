package com.lyp.uge.demo;
import org.lwjgl.opengl.*;

import com.lyp.uge.window.WindowManager;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class HelloWorld {

	// The window handle
	private long window;

	public void run() {
		window = WindowManager.createWindow(100, 50);
		loop();
		WindowManager.destoryWindow();
	}

	private void loop() {
		// This line is critical for LWJGL's interoperation with GLFW's
		// OpenGL context, or any context that is managed externally.
		// LWJGL detects the context that is current in the current thread,
		// creates the GLCapabilities instance and makes the OpenGL
		// bindings available for use.
		GL.createCapabilities();

		// Set the clear color
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);

		// Run the rendering loop until the user has attempted to close
		// the window or has pressed the ESCAPE key.
		while ( !glfwWindowShouldClose(window) ) {
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT); // clear the framebuffer

			glfwSwapBuffers(window); // swap the color buffers

			// Poll for window events. The key callback above will only be
			// invoked during this call.
			glfwPollEvents();
		}
	}

	public static void main(String[] args) {
		new HelloWorld().run();
//		Logger.setLogOutLevel(Level.ERROR);
//		Logger.d("HELLO");
//		Logger.i("HELLO");
//		Logger.e("HELLO");
//		Logger.w("HELLO");
	}
}