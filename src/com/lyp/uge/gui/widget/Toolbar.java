package com.lyp.uge.gui.widget;

import static org.lwjgl.nanovg.NanoVG.*;

import java.awt.Rectangle;
import java.nio.ByteBuffer;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.util.vector.Vector2f;

import com.lyp.uge.ai.collision.Collision;
import com.lyp.uge.input.MouseInput;
import com.lyp.uge.utils.DataUtils;
import com.lyp.uge.window.Window;

public class Toolbar extends Widget {

	private Rectangle bounds;
	private int imgP = 0;
	private ByteBuffer imgRes = loadResource(DataUtils.TEX_GUI_ICON_POLYGON, 1 * 1024);

	private NVGColor color = NVGColor.create();
	private NVGPaint paint = NVGPaint.create();

	public Toolbar(Window window) {
		this.ctx = guiCreate();
		this.imgP = guiImageCreate(ctx, imgRes);
		this.bounds = new Rectangle(window.getWidth() - 54, 0, 48, 48);
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
			nvgFillColor(ctx, rgba(139, 229, 165, 210, color));
		} else {
			nvgFillColor(ctx, rgba(61, 61, 63, 190, color));
		}
		nvgFill(ctx);

		nvgFillPaint(ctx, paint);
		nvgFill(ctx);
	}

	@Override
	public void update(Window window) {
	}
	
	private boolean isMouseHover(Rectangle bounds) {
		if (Collision.isPointInRect(new Vector2f(MouseInput.getInstance().getPosX(), MouseInput.getInstance().getPosY()), new Vector2f(bounds.x, bounds.y), bounds.width, bounds.height)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void destory() {
		guiImageDestory(ctx, imgP);
		guiDestory(ctx);
	}

}
