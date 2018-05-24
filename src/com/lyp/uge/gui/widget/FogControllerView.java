package com.lyp.uge.gui.widget;

import static org.lwjgl.nanovg.NanoVG.*;

import java.nio.ByteBuffer;

import org.lwjgl.nanovg.NVGColor;

import javafx.scene.shape.Rectangle;

import com.lyp.uge.input.Mouse;
import com.lyp.uge.input.MouseInput;
import com.lyp.uge.utils.DataUtils;
import com.lyp.uge.window.Window;

public class FogControllerView extends Widget {

	private static final String TEXT_FOG_DENSITY	= "fogDensity: ";
	private static final String TEXT_FOG_GRADIENT	= "fogGradient: ";
	private static final float MIN_FOG_DENSITY 		= 0.00f;
	private static final float MAX_FOG_DENSITY 		= 0.01f;
	private static final float STEP_FOG_DENSITY		= 0.0002f;
	private static final float MIN_FOG_GRADIENT		= 1.0f;
	private static final float MAX_FOG_GRADIENT		= 5.0f;
	private static final float STEP_FOG_GRADIENT	= 0.1f;
	
	private float x = 0.0f;
	private float y = 100.0f;
	private float w = 220.0f;
	private float h = 110.0f;
	private float padding = 12.0f;
	private float slideBarHeight = 8.0f;
	private float slideBarWidth = 180.0f;
	private float sliderSize = 10.0f;

	private Rectangle slideBarBounds01;
	private Rectangle slideBarBounds02;
	private Rectangle sliderBounds01;
	private Rectangle sliderBounds02;

	private float fSize = 19.0f;
	private final String fName = "RobotoMono-Regular";
	private ByteBuffer fRes = loadResource(DataUtils.FONT_ROBOTO_MONO_REGULAR, 150 * 1024);
	private NVGColor colorBg = rgba(255, 0, 0, 220);
	private NVGColor colorSlider = rgba(139, 229, 165, 220);

	private Float mFogDensity, mFogGradient;
	private float mMouseLastX = -1.0f;
	
	public FogControllerView(Float fogDensity, Float fogGradient) {
		this.mFogDensity = fogDensity;
		this.mFogGradient = fogGradient;
		this.ctx = guiCreate();
		guiFontCreate(ctx, fName, fRes);

		this.slideBarBounds01 = new Rectangle(x + padding, y + padding + fSize * 1.2f, slideBarWidth, slideBarHeight);
		this.slideBarBounds02 = new Rectangle(x + padding, y + padding + fSize * 3.2f, slideBarWidth, slideBarHeight);

		float fogDensityXOffset = slideBarWidth * (mFogDensity - MIN_FOG_DENSITY) / (MAX_FOG_DENSITY - MIN_FOG_DENSITY);
		this.sliderBounds01 = new Rectangle(x + padding - sliderSize/2 + fogDensityXOffset, y + padding + fSize * 1.4f - sliderSize/2, sliderSize, sliderSize);
		float fogGradientXOffset = slideBarWidth * (mFogGradient - MIN_FOG_GRADIENT) / (MAX_FOG_GRADIENT - MIN_FOG_GRADIENT);
		this.sliderBounds02 = new Rectangle(x + padding - sliderSize/2 + fogGradientXOffset, y + padding + fSize * 3.4f - sliderSize/2, sliderSize, sliderSize);
	}

	@Override
	protected void draw(Window window) {
		// Draw panel
		nvgBeginPath(ctx);
		nvgRect(ctx, x, y, w, h);
		nvgFillColor(ctx, rgba(61, 61, 63, 190, colorBg));
		nvgFill(ctx);

		nvgBeginPath(ctx);
		nvgRect(ctx, x, y, w, padding / 4);
		nvgFillColor(ctx, rgba(139, 229, 165, 255, colorBg));
		nvgFill(ctx);

		//Draw slide bar
		nvgBeginPath(ctx);
		nvgRoundedRect(ctx, (float) slideBarBounds01.getX(), (float) slideBarBounds01.getY(), (float) slideBarBounds01.getWidth(), (float) slideBarBounds01.getHeight(), slideBarHeight / 2.0f);
		nvgFillColor(ctx, rgba(0, 165, 240, 210, colorBg));
		nvgFill(ctx);

		nvgBeginPath(ctx);
		nvgRoundedRect(ctx, (float) slideBarBounds02.getX(), (float) slideBarBounds02.getY(), (float) slideBarBounds02.getWidth(), (float) slideBarBounds02.getHeight(), slideBarHeight / 2.0f);
		nvgFillColor(ctx, rgba(0, 165, 240, 210, colorBg));
		nvgFill(ctx);

		nvgBeginPath(ctx);
		nvgCircle(ctx, (float) sliderBounds01.getX() + (float) sliderBounds01.getHeight() / 2.0f, (float) sliderBounds01.getY() + (float) sliderBounds01.getHeight() / 2.0f, (float) sliderBounds01.getHeight() - 2.0f);
		nvgFillColor(ctx, rgba(139, 229, 255, 255, colorSlider));
		nvgFill(ctx);
		
		nvgBeginPath(ctx);
		nvgCircle(ctx, (float) sliderBounds02.getX() + (float) sliderBounds02.getHeight() / 2.0f, (float) sliderBounds02.getY() + (float) sliderBounds02.getHeight() / 2.0f, (float) sliderBounds02.getHeight() - 2.0f);
		nvgFillColor(ctx, rgba(139, 229, 255, 255, colorSlider));
		nvgFill(ctx);
		
		//Draw text
		nvgFontSize(ctx, fSize);
		nvgFontFace(ctx, fName);
		nvgTextAlign(ctx, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
		nvgFillColor(ctx, rgba(139, 229, 255, 255, colorBg));
		nvgText(ctx, x + padding, y + padding, TEXT_FOG_DENSITY + mFogDensity);
		nvgText(ctx, x + padding, y + padding + fSize * 2f, TEXT_FOG_GRADIENT + mFogGradient);
	}

	@Override
	public void update(Window window) {
		if (mMouseLastX == -1.0f) {
			mMouseLastX = MouseInput.getInstance().getPosX();
		}
		if (isMouseHover(slideBarBounds01) || isMouseHover(sliderBounds01)) {
			if (MouseInput.getInstance().isMousePressed(Mouse.MOUSE_BUTTON_LEFT) && MouseInput.getInstance().getPosX() > mMouseLastX) {
				if (mFogDensity < MAX_FOG_DENSITY) {
					mFogDensity += STEP_FOG_DENSITY;
				}
				float fogDensityXOffset = slideBarWidth * (mFogDensity - MIN_FOG_DENSITY) / (MAX_FOG_DENSITY - MIN_FOG_DENSITY);
				sliderBounds01.setX(x + padding - sliderSize/2 + fogDensityXOffset);
			} else if (MouseInput.getInstance().isMousePressed(Mouse.MOUSE_BUTTON_LEFT) && MouseInput.getInstance().getPosX() < mMouseLastX) {
				if (mFogDensity > MIN_FOG_DENSITY) {
					mFogDensity -= STEP_FOG_DENSITY;
				}
				float fogDensityXOffset = slideBarWidth * (mFogDensity - MIN_FOG_DENSITY) / (MAX_FOG_DENSITY - MIN_FOG_DENSITY);
				sliderBounds01.setX(x + padding - sliderSize/2 + fogDensityXOffset);
			}
		}
		if (isMouseHover(slideBarBounds02) || isMouseHover(sliderBounds02)) {
			if (MouseInput.getInstance().isMousePressed(Mouse.MOUSE_BUTTON_LEFT) && MouseInput.getInstance().getPosX() > mMouseLastX) {
				if (mFogGradient < MAX_FOG_GRADIENT) {
					mFogGradient += STEP_FOG_GRADIENT;
				}
				float fogGradientXOffset = slideBarWidth * (mFogGradient - MIN_FOG_GRADIENT) / (MAX_FOG_GRADIENT - MIN_FOG_GRADIENT);
				sliderBounds02.setX(x + padding - sliderSize/2 + fogGradientXOffset);
			} else if (MouseInput.getInstance().isMousePressed(Mouse.MOUSE_BUTTON_LEFT) && MouseInput.getInstance().getPosX() < mMouseLastX) {
				if (mFogGradient > MIN_FOG_GRADIENT) {
					mFogGradient -= STEP_FOG_GRADIENT;
				}
				float fogGradientXOffset = slideBarWidth * (mFogGradient - MIN_FOG_GRADIENT) / (MAX_FOG_GRADIENT - MIN_FOG_GRADIENT);
				sliderBounds02.setX(x + padding - sliderSize/2 + fogGradientXOffset);
			}
		}
		mMouseLastX = MouseInput.getInstance().getPosX();
	}

	@Override
	public void destory() {
		guiDestory(ctx);
	}

	public Float getFogDensity()
	{
		return this.mFogDensity;
	}

	public Float getFogGradient()
	{
		return this.mFogGradient;
	}
}
