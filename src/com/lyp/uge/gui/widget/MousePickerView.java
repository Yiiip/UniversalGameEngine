package com.lyp.uge.gui.widget;

import static org.lwjgl.nanovg.NanoVG.*;

import java.nio.ByteBuffer;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.util.vector.Matrix4f;

import com.lyp.uge.utils.DataUtils;
import com.lyp.uge.gameObject.camera.Camera;
import com.lyp.uge.gameObject.tool.MousePicker;
import com.lyp.uge.window.Window;

public class MousePickerView extends Widget {

	private static final String TEXT_INFO = "Ray ";

	private float x;
	private float y;
	private float w = 420.0f;
	private float h = 23.0f;
	private float fSize = 17.0f;
	private final String fName = "RobotoMono-Regular";
	private ByteBuffer fRes = loadResource(DataUtils.FONT_ROBOTO_MONO_REGULAR, 150 * 1024);
	private NVGColor color = NVGColor.create();

	private MousePicker mousePicker;

	public MousePickerView(Window window, Camera camera, Matrix4f projectionMatrix) {
		this.x = (window.getWidth() - w) / 2.0f;
		this.y = 0.0f;
		this.ctx = guiCreate();
		guiFontCreate(ctx, fName, fRes);
		this.mousePicker = new MousePicker(camera, projectionMatrix);
	}

	@Override
	protected void draw(Window window) {
		// Draw HUD
		nvgBeginPath(ctx);
		nvgRect(ctx, x, y, w, h);
		nvgFillColor(ctx, rgba(61, 61, 63, 200, color));
		nvgFill(ctx);

		// Draw text
		nvgFontSize(ctx, fSize);
		nvgFontFace(ctx, fName);
		nvgTextAlign(ctx, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
		nvgFillColor(ctx, rgba(139, 229, 165, 255, color));
		nvgText(ctx, x + 6.0f, y + 2.0f, TEXT_INFO + mousePicker.getCurrentRay().toString());
	}

	@Override
	public void update(Window window) {
		mousePicker.update();
	}

	@Override
	public void destory() {
		guiDestory(ctx);
	}

	public MousePicker getMousePicker() {
		return mousePicker;
	}
}
