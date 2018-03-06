package com.lyp.uge.gui.widget;

import java.awt.Rectangle;

import org.lwjgl.util.vector.Vector2f;

import com.lyp.uge.ai.collision.Collision;
import com.lyp.uge.ai.fsm.StateMachine;
import com.lyp.uge.gui.Gui;
import com.lyp.uge.input.MouseInput;
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
	
	/**
	 * 
	 * @param bounds
	 * @return
	 */
	protected boolean isMouseHover(Rectangle bounds) {
		return Collision.isPointInRect(
				new Vector2f(MouseInput.getInstance().getPosX(), MouseInput.getInstance().getPosY()),
				new Vector2f(bounds.x, bounds.y), bounds.width, bounds.height);
	}
}
