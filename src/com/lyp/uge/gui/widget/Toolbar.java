package com.lyp.uge.gui.widget;

import static org.lwjgl.nanovg.NanoVG.*;

import java.awt.Rectangle;
import java.nio.ByteBuffer;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import com.lyp.uge.input.Mouse;
import com.lyp.uge.input.MouseInput;
import com.lyp.uge.renderEngine.RendererManager;
import com.lyp.uge.utils.DataUtils;
import com.lyp.uge.window.Window;

public class Toolbar extends Widget {

	private static final String TEXT_MENU_01 = "Display Mode";

	private Rectangle bounds;
	
	private int imgP = 0;
	private ByteBuffer imgRes = loadResource(DataUtils.TEX_GUI_ICON_POLYGON, 1 * 1024);
	
	private float fSize = 22.0f;
	private final String fName = "RobotoMono-Regular";
	private ByteBuffer fRes = loadResource(DataUtils.FONT_ROBOTO_MONO_REGULAR, 150 * 1024);

	private NVGColor color = NVGColor.create();
	private NVGPaint paint = NVGPaint.create();
	
	private int textAlpha = 0;

	public Toolbar(Window window) {
		this.ctx = guiCreate();
		this.imgP = guiImageCreate(ctx, imgRes);
		this.bounds = new Rectangle(window.getWidth() - 54, 0, 48, 48);
		guiFontCreate(ctx, fName, fRes);
	}

	@Override
	protected void draw(Window window) {
		nvgImagePattern(ctx, window.getWidth() - 46, 6, 34, 34, 0, imgP, 1, paint);
		
		nvgBeginPath(ctx);
		nvgRect(ctx, window.getWidth() - 6, 0, 6, 48);
		nvgFillColor(ctx, rgba(139, 229, 165, 255, color));
		nvgFill(ctx);
		
		nvgBeginPath(ctx);
		nvgRect(ctx, bounds.x, bounds.y, bounds.width, bounds.height);
		if (isMouseHover(bounds)) {
			nvgFillColor(ctx, rgba(139, 229, 165, 200, color));
		} else {
			nvgFillColor(ctx, rgba(61, 61, 63, 190, color));
		}
		nvgFill(ctx);

		nvgFillPaint(ctx, paint);
		nvgFill(ctx);
		
		nvgFontSize(ctx, fSize);
		nvgFontFace(ctx, fName);
		nvgTextAlign(ctx, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
		nvgFillColor(ctx, rgba(255, 255, 255, textAlpha, color));
		nvgText(ctx, window.getWidth() - 190, 16, TEXT_MENU_01);
	}

	@Override
	public void update(Window window) {
		if (isMouseHover(bounds)) {
			if (MouseInput.getInstance().isMouseClicked(Mouse.MOUSE_BUTTON_LEFT)) {
				RendererManager.changePolygonMode();
			}
			
			textAlpha = 255;
		} else {
			
			if (textAlpha > 0) {
				textAlpha -= 25;
			}
		}
	}
	
	@Override
	public void destory() {
		guiImageDestory(ctx, imgP);
		guiDestory(ctx);
	}

}
