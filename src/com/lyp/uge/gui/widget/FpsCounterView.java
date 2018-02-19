package com.lyp.uge.gui.widget;

import static org.lwjgl.nanovg.NanoVG.*;

import java.nio.ByteBuffer;

import org.lwjgl.nanovg.NVGColor;

import com.lyp.uge.utils.DataUtils;
import com.lyp.uge.window.Window;

public class FpsCounterView extends Widget {

	private static final String TEXT_FPS = "FPS:";
	private static final String TEXT_UPS = " | UPS:";
	private static final String TEXT_RUNTIME = "Running: ";

	private float x;
	private float y;
	private float w = 220.0f;
	private float h = 100.0f;
	private float padding = 12.0f;
	private float fSize = 22.0f;
	private final String fName = "RobotoMono-Regular";
	private ByteBuffer fRes = loadResource(DataUtils.FONT_ROBOTO_MONO_REGULAR, 150 * 1024);
	private NVGColor color = NVGColor.create();

	public int fps = 0;
	public int ups = 0;
	public String runtime = "";

	public FpsCounterView(float x, float y) {
		this.x = x;
		this.y = y;
		this.ctx = guiCreate();
		guiFontCreate(ctx, fName, fRes);
	}

	@Override
	protected void draw(Window window) {
		// Draw HUD panel
		nvgRect(ctx, x, y, w, h);
		nvgFillColor(ctx, rgba(61, 61, 63, 190, color));
		nvgFill(ctx);

		// Draw text
		nvgFontSize(ctx, fSize);
		nvgFontFace(ctx, fName);
		nvgTextAlign(ctx, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
		nvgFillColor(ctx, rgba(139, 229, 165, 255, color));
		nvgText(ctx, x + padding, y + padding, TEXT_FPS + fps + TEXT_UPS + ups);
		nvgText(ctx, x + padding, y + fSize + padding, TEXT_RUNTIME + runtime);
	}

	@Override
	public void update() {
	}
	
	public void update(int fps, int ups, String runtime) {
		this.fps = fps;
		this.ups = ups;
		this.runtime = runtime;
		this.update();
	}

	@Override
	public void destory() {
		guiDestory(ctx);
	}
}
