package com.lyp.test;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.system.MemoryUtil.*;

import org.lwjgl.nanovg.NVGColor;

import com.lyp.uge.game.GameApplication;
import com.lyp.uge.logger.Logger;

public class TestGUI extends GameApplication {
	
	private long vg;
	
	static final NVGColor colorA = NVGColor.create();
	
	int blue = 0, flag = 1;
	float x = 10;
	float y = 10;
	float w = 200;
	float h = 35;
	
	@Override
	protected void onInitWindow(int winWidth, int winHeight, String winTitle, boolean winResizeable) {
		super.onInitWindow(1600, 900, "testGUI", false);
	}

	@Override
	protected void onCreate() {
		vg = nvgCreate(NVG_ANTIALIAS);
        if (vg == NULL) {
            Logger.e("Could not init nanovg.");
        } else {
			Logger.i("GUI", "Nvg created!");
		}
	}

	@Override
	protected void onUpdate() {
		if (blue >= 255) {
			flag = -1;
		} else if (blue <= 0) {
			flag = 1;
		}
		blue += flag;
		w += (float) flag * 4;
		h += (float) flag / 2;
		x += (float) flag;
		y += (float) flag * 2;
	}

	@Override
	protected void onRender() {
		nvgBeginFrame(vg, getMainWindow().getWidth(), getMainWindow().getHeight(), getMainWindow().getAspectRatio());
		
        nvgBeginPath(vg);
        nvgRect(vg, x, y, w, h);
        nvgFillColor(vg, rgba(90, 90, blue, 255, colorA));
        nvgFill(vg);
        
        nvgEndFrame(vg);
	}

	@Override
	protected void onDestory() {

	}

	public static void main(String[] args) {
		new TestGUI().start();
	}

	static NVGColor rgba(int r, int g, int b, int a, NVGColor color) {
        color.r(r / 255.0f);
        color.g(g / 255.0f);
        color.b(b / 255.0f);
        color.a(a / 255.0f);
        return color;
    }
}
