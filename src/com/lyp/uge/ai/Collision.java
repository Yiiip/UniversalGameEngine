package com.lyp.uge.ai;

import org.lwjgl.util.vector.Vector2f;

public class Collision {

	/**
	 * 判断两矩形是否相交
	 * @param rect1Start 第一个矩形左上角坐标
	 * @param rect1End 第一个矩形右下角坐标
	 * @param rect2Start 第二个矩形左上角坐标
	 * @param rect2End 第二个矩形右下角坐标
	 * @return true相交，false不相交
	 */
	public static boolean rectangleCollision(Vector2f r1Start, Vector2f r1End, Vector2f r2Start, Vector2f r2End) {
		float aRectStartX = Math.max(r1Start.x, r2Start.x);
		float aRectStartY = Math.max(r1Start.y, r2Start.y);
		float iRectEndX = Math.min(r1End.x, r2End.x);
		float iRectEndY = Math.min(r1End.y, r2End.y);
		if (aRectStartX <= iRectEndX && aRectStartY <= iRectEndY) {
			return true;
		} else {
			return false;
		}
	}
}
