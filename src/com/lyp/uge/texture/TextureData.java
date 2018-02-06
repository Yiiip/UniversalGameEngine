package com.lyp.uge.texture;

import java.nio.ByteBuffer;

public class TextureData {

	private int width = 0;
	private int height = 0;
	private ByteBuffer byteBuffer = null;
	
	public TextureData(int width, int height, ByteBuffer byteBuffer) {
		this.width = width;
		this.height = height;
		this.byteBuffer = byteBuffer;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public ByteBuffer getByteBuffer() {
		return byteBuffer;
	}

	public void setByteBuffer(ByteBuffer byteBuffer) {
		this.byteBuffer = byteBuffer;
	}
}
