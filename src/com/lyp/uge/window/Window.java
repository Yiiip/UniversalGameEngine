package com.lyp.uge.window;

import org.lwjgl.util.vector.Vector2f;

public class Window {

	private long id;
	private int width = WindowManager.DEFAULT_WIDTH;
	private int height = WindowManager.DEFAULT_HEIGHT;
	private String title = WindowManager.DEFAULT_TITLE;
	private boolean resizeable = WindowManager.DEFAULT_RESIZEABLE;

	public Window(long id) {
		this.id = id;
	}

	public Window(long id, int width, int height, String title, boolean resizeable) {
		this.id = id;
		this.width = width;
		this.height = height;
		this.title = title;
		this.resizeable = resizeable;
	}

	public long getId() {
		return id;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public Vector2f getCenter() {
		return new Vector2f(width / 2f, height / 2f);
	}

	public float getAspectRatio() {
		return (float) width / (float) height;
	}

	public String getTitle() {
		return title;
	}

	public boolean isResizeable() {
		return resizeable;
	}

	@Override
	public String toString() {
		return "[id=" + id + ", size=" + width + "x" + height + ", title=" + title + ", resizeable=" + resizeable + "]";
	}
}
