package com.lyp.uge.ai.collision;

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
	
	/**
	 * 判断一点是否在矩形区域内
	 * @param point 点的坐标
	 * @param rectStart 矩形区域左上角起始点的坐标
	 * @param rectWidth 矩形区域宽度
	 * @param rectHeight 矩形区域高度
	 * @return true在区域内，false不在区域内
	 */
	public static boolean isPointInside(Vector2f point, Vector2f rectStart, float rectWidth, float rectHeight) {
        float w = rectWidth;
        float h = rectHeight;
        if (w < 0 || h < 0) {
            return false;
        }
        float x = rectStart.x;
        float y = rectStart.y;
        if (point.x < x || point.y < y) {
            return false;
        }
        w += x;
        h += y;
        return ((w < x || w > point.x) &&
                (h < y || h > point.y));
    }
}
