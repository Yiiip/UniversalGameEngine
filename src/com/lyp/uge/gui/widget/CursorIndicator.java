package com.lyp.uge.gui.widget;

import static org.lwjgl.nanovg.NanoVG.*;

import org.lwjgl.nanovg.NVGColor;

import com.lyp.uge.window.Window;

public class CursorIndicator extends Widget {

	private NVGColor color = rgba(255, 255, 255, 200);
	
	private final float hWidth	= 26.0f;
	private final float hHeight	= 2.0f;
	private final float vWidth	= 2.0f;
	private final float vHeight	= 16.0f;
	
	public CursorIndicator() {
		this.ctx = guiCreate();
	}

	@Override
	protected void draw(Window window) {
		nvgBeginPath(ctx);
		nvgRect(ctx, window.getCenter().x - hWidth / 2, window.getCenter().y - hHeight / 2, hWidth, hHeight);
		nvgFillColor(ctx, color);
		nvgFill(ctx);

		nvgBeginPath(ctx);
		nvgRect(ctx, window.getCenter().x - vWidth / 2, window.getCenter().y - vHeight / 2, vWidth, vHeight);
		nvgFillColor(ctx, color);
		nvgFill(ctx);
	}

	@Override
	public void update(Window window) {
	}

	@Override
	public void destory() {
		guiDestory(ctx);
	}

}
