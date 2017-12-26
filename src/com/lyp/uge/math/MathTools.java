package com.lyp.uge.math;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import com.lyp.uge.gameObject.Camera;

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
	
	public static Matrix4f createViewMatrix(Camera camera) {
		Matrix4f viewMatrix = new Matrix4f();
		viewMatrix.setIdentity();
		Matrix4f.rotate((float) Math.toRadians(camera.getPitch()), new Vector3f(1, 0, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getYaw()), new Vector3f(0, 1, 0), viewMatrix, viewMatrix);
		Matrix4f.rotate((float) Math.toRadians(camera.getRoll()), new Vector3f(0, 0, 1), viewMatrix, viewMatrix);
		Vector3f cameraPos = camera.getPosition();
		Vector3f negativeCameraPos = new Vector3f(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		Matrix4f.translate(negativeCameraPos, viewMatrix, viewMatrix);
		return viewMatrix;
	}
	
	public static Matrix4f createProjectionMatrix(
			float viewAngle, float nearPlane, float farPlane, float winWidth, float winHeight) {
		float aspectRatio = winWidth / winHeight;
        float y_scale = (float) ((1.0f / Math.tan(Math.toRadians(viewAngle / 2.0f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = farPlane - nearPlane; //视锥体长度
        
        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((farPlane + nearPlane) / frustum_length);
        projectionMatrix.m23 = -1.0f;
        projectionMatrix.m32 = -((2 * nearPlane * farPlane) / frustum_length);
        projectionMatrix.m33 = 0.0f;
        return projectionMatrix;
	}
	
	public static Matrix4f createOrthographicMatrix(
			float left, float right, float top, float bottom, float near, float far) {
        Matrix4f orthographicMatrix = new Matrix4f();
        orthographicMatrix.m00 = 2.0f / (left - right);
        orthographicMatrix.m11 = 2.0f / (top - bottom);
        orthographicMatrix.m22 = 2.0f / (near - far);
        orthographicMatrix.m03 = (left + right) / (left - right);
        orthographicMatrix.m13 = (bottom + top) / (bottom - top);
        orthographicMatrix.m23 = (near + far) / (near - far);
        orthographicMatrix.m33 = 1.0f;
        return orthographicMatrix;
	}
	
	public static float clamp(float value, float min, float max) {
		if (value > max) return max;
		if (value < min) return min;
		return value;
	}
}
