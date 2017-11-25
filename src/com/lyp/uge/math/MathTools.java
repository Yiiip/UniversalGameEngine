package com.lyp.uge.math;

import com.lwjgl.util.vector.Matrix4f;
import com.lwjgl.util.vector.Vector3f;

public class MathTools {

	public static Matrix4f createTransformationMatrix(
			Vector3f translation, float rotateX, float rotateY, float rotateZ, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.setIdentity();
		Matrix4f.translate(translation, matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotateX), new Vector3f(1, 0, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotateY), new Vector3f(0, 1, 0), matrix, matrix);
		Matrix4f.rotate((float) Math.toRadians(rotateZ), new Vector3f(0, 0, 1), matrix, matrix);
		Matrix4f.scale(new Vector3f(scale, scale, scale), matrix, matrix);
		return matrix;
	}
}
