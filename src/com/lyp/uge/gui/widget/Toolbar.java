package com.lyp.uge.gui.widget;

import static org.lwjgl.nanovg.NanoVG.*;

import java.nio.ByteBuffer;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;

import javafx.scene.shape.Rectangle;

import com.lyp.uge.audio.AudioManager;
import com.lyp.uge.input.Mouse;
import com.lyp.uge.input.MouseInput;
import com.lyp.uge.renderEngine.RendererManager;
import com.lyp.uge.utils.DataUtils;
import com.lyp.uge.window.Window;

public class Toolbar extends Widget {

	private static final String TEXT_MENU_01 = "Display Mode";
	private static final String TEXT_MENU_02 = "Audio ON/OFF";

	private Rectangle boundsMenu01;
	private Rectangle boundsMenu02;
	
	private int imgId01;
	private int imgId02;
	private ByteBuffer imgPloygon = loadResource(DataUtils.TEX_GUI_ICON_POLYGON_CUBE, 1 * 1024);
	private ByteBuffer imgMute = loadResource(DataUtils.TEX_GUI_ICON_MUTE, 1 * 1024);
	
	private float fSize = 22.0f;
	private final String fName = "RobotoMono-Regular";
	private ByteBuffer fRes = loadResource(DataUtils.FONT_ROBOTO_MONO_REGULAR, 150 * 1024);

	private NVGColor color01 = NVGColor.create();
	private NVGColor color02 = NVGColor.create();
	private NVGPaint paint01 = NVGPaint.create();
	private NVGPaint paint02 = NVGPaint.create();
	
	private int textAlpha01 = 0;
	private int textAlpha02 = 0;
	
	private boolean isMute = false;

	public Toolbar(Window window) {
		this.ctx = guiCreate();
		this.imgId01 = guiImageCreate(ctx, imgPloygon);
		this.imgId02 = guiImageCreate(ctx, imgMute);
		this.boundsMenu01 = new Rectangle(window.getWidth() - 54, 0,    48, 48);
		this.boundsMenu02 = new Rectangle(window.getWidth() - 54, 0+50, 48, 48);
		guiFontCreate(ctx, fName, fRes);
	}

	@Override
	protected void draw(Window window) {
		nvgImagePattern(ctx, window.getWidth() - 46, 6, 34, 34, 0, imgId01, 1, paint01);
		nvgImagePattern(ctx, window.getWidth() - 43, 6+54, 28, 28, 0, imgId02, 1, paint02);
		
		// Menu:显示模式
		nvgBeginPath(ctx);
		nvgRect(ctx, window.getWidth() - 6, 0, 6, 48);
		nvgFillColor(ctx, rgba(139, 229, 165, 255, color01));
		nvgFill(ctx);
		nvgBeginPath(ctx);
		nvgRect(ctx, (float) boundsMenu01.getX(), (float) boundsMenu01.getY(), (float) boundsMenu01.getWidth(), (float) boundsMenu01.getHeight());
		if (isMouseHover(boundsMenu01)) {
			nvgFillColor(ctx, rgba(139, 229, 165, 200, color01));
		} else {
			nvgFillColor(ctx, rgba(61, 61, 63, 190, color01));
		}
		nvgFill(ctx);
		
		nvgFillPaint(ctx, paint01);
		nvgFill(ctx);
		
		nvgFontSize(ctx, fSize);
		nvgFontFace(ctx, fName);
		nvgTextAlign(ctx, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
		nvgFillColor(ctx, rgba(61, 61, 63, textAlpha01, color01));
		nvgText(ctx, window.getWidth() - 189, 17, TEXT_MENU_01);
		nvgFillColor(ctx, rgba(255, 255, 255, textAlpha01, color01));
		nvgText(ctx, window.getWidth() - 190, 16, TEXT_MENU_01);
		
		// Menu:静音
		nvgBeginPath(ctx);
		nvgRect(ctx, window.getWidth() - 6, 0+50, 6, 48);
		nvgFillColor(ctx, rgba(139, 229, 165, 255, color02));
		nvgFill(ctx);
		nvgBeginPath(ctx);
		nvgRect(ctx, (float) boundsMenu02.getX(), (float) boundsMenu02.getY(), (float) boundsMenu02.getWidth(), (float) boundsMenu02.getHeight());
		if (isMouseHover(boundsMenu02)) {
			nvgFillColor(ctx, rgba(139, 229, 165, 200, color02));
		} else {
			nvgFillColor(ctx, rgba(61, 61, 63, 190, color02));
		}
		nvgFill(ctx);

		nvgFillPaint(ctx, paint02);
		nvgFill(ctx);
		
		nvgFontSize(ctx, fSize);
		nvgFontFace(ctx, fName);
		nvgTextAlign(ctx, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
		nvgFillColor(ctx, rgba(61, 61, 63, textAlpha02, color02));
		nvgText(ctx, window.getWidth() - 189, 16+51, TEXT_MENU_02);
		nvgFillColor(ctx, rgba(255, 255, 255, textAlpha02, color02));
		nvgText(ctx, window.getWidth() - 190, 16+50, TEXT_MENU_02);
		
	}

	@Override
	public void update(Window window) {
		if (isMouseHover(boundsMenu01)) {
			textAlpha01 = 255;
			if (MouseInput.getInstance().isMouseClicked(Mouse.MOUSE_BUTTON_LEFT)) {
				RendererManager.changePolygonMode();
			}
		} else {
			if (textAlpha01 > 0) textAlpha01 -= 25;
		}
		
		if (isMouseHover(boundsMenu02)) {
			textAlpha02 = 255;
			if (MouseInput.getInstance().isMouseClicked(Mouse.MOUSE_BUTTON_LEFT)) {
				if (isMute) AudioManager.GetInstance().playAll();
				else AudioManager.GetInstance().pauseAll();
				isMute = !isMute;
			}
		} else {
			if (textAlpha02 > 0) textAlpha02 -= 25;
		}
	}
	
	@Override
	public void destory() {
		guiImageDestory(ctx, imgId01);
		guiImageDestory(ctx, imgId02);
		guiDestory(ctx);
	}

}
