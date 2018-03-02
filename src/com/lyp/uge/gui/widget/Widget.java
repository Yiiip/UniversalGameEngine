package com.lyp.uge.gui.widget;

import com.lyp.uge.ai.fsm.StateMachine;
import com.lyp.uge.gui.Gui;
import com.lyp.uge.renderEngine.RendererManager;
import com.lyp.uge.window.Window;

public abstract class Widget extends Gui {
	
	protected long ctx;

	protected StateMachine mStateMachine;
	
	/**
	 * onDraw is public visible for outside.
	 * @param window
	 */
	public void onDraw(Window window) {
		Gui.guiBegin(ctx, window);
		draw(window);
		Gui.guiEnd(ctx);
		RendererManager.restoreRenderState();
	}
}
