package com.lyp.uge.gui;


import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.lwjgl.nanovg.NVGColor;

import com.lyp.uge.logger.Logger;
import com.lyp.uge.utils.IOUtils;
import com.lyp.uge.window.Window;

public abstract class Gui {

	protected abstract void draw(Window window);
	public abstract void update();
	public abstract void destory();
	
	public static long guiCreate() {
		long vgContext = nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES);
        if (vgContext == NULL) {
            Logger.e("Could not init nanovg.");
            return -1;
        } else {
			Logger.i("GUI", "Nvg created!");
			return vgContext;
		}
	}
	
	public static int guiFontCreate(long vgContext, String fontName, ByteBuffer resource) {
		return nvgCreateFontMem(vgContext, fontName, resource, 0);
	}
	
	public static void guiBegin(long vgContext, Window window) {
		nvgBeginFrame(vgContext, window.getWidth(), window.getHeight(), window.getAspectRatio());
	}
	
	public static void guiEnd(long vgContext) {
		nvgEndFrame(vgContext);
	}
	
	public static void guiDestory(long vgContext) {
		nvgDelete(vgContext);
	}
	
	public static NVGColor rgba(int r, int g, int b, int a) {
		NVGColor color = NVGColor.create();
        color.r(r / 255.0f);
        color.g(g / 255.0f);
        color.b(b / 255.0f);
        color.a(a / 255.0f);
        return color;
    }
	
	public static NVGColor rgba(int r, int g, int b, int a, NVGColor color) {
		if (color == null) {
			color = NVGColor.create();
		}
        color.r(r / 255.0f);
        color.g(g / 255.0f);
        color.b(b / 255.0f);
        color.a(a / 255.0f);
        return color;
    }
	
	public static ByteBuffer loadResource(String resource, int bufferSize) {
        try {
            return IOUtils.ioResourceToByteBuffer(resource, bufferSize);
        } catch (IOException e) {
        	Logger.e("Failed to load resource: " + resource);
            throw new RuntimeException("Failed to load resource: " + resource, e);
        }
    }
}
