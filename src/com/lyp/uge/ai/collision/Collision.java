package com.lyp.uge.ai.collision;

import org.lwjgl.util.vector.Vector2f;

public class Collision {

	/**
	 * 判断两矩形是否相交
	 * 
	 * @param rect1Start
	 *            矩形1左上角坐标
	 * @param rect1End
	 *            矩形1右下角坐标
	 * @param rect2Start
	 *            矩形2左上角坐标
	 * @param rect2End
	 *            矩形2右下角坐标
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
	 * 判断点是否在矩形区域内
	 * 
	 * @param point
	 *            点的坐标
	 * @param rectStart
	 *            矩形区域左上角起始点的坐标
	 * @param rectWidth
	 *            矩形区域宽度
	 * @param rectHeight
	 *            矩形区域高度
	 * @return true在区域内，false不在区域内
	 */
	public static boolean isPointInRect(Vector2f point, Vector2f rectStart, float rectWidth, float rectHeight) {
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
		return ((w < x || w > point.x) && (h < y || h > point.y));
	}

	/**
	 * 判断两圆是否相交
	 * 
	 * @param cir1Center
	 *            圆形1的圆心
	 * @param radius1
	 *            圆形1的半径
	 * @param cir2Center
	 *            圆形2的圆心
	 * @param radius2
	 *            圆形2的半径
	 * @return true相交，false不相交
	 */
	public static boolean circleCollision(Vector2f cir1Center, float radius1, Vector2f cir2Center, float radius2) {
		if (Math.sqrt(Math.pow(cir1Center.x - cir2Center.x, 2) + Math.pow(cir1Center.y - cir2Center.y, 2)) <= (radius1 + radius2)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断点是否在圆内
	 * 
	 * @param point
	 *            点的坐标
	 * @param cirCenter
	 *            圆心坐标
	 * @param radius
	 *            圆的半径
	 * @return true在区域内，false不在区域内
	 */
	public static boolean isPointInCircle(Vector2f point, Vector2f cirCenter, float radius) {
		if (Math.sqrt(Math.pow(point.x - cirCenter.x, 2) + Math.pow(point.y - cirCenter.y, 2)) <= radius) {
			return true;
		} else {
			return false;
		}
	}
}
