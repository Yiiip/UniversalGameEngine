package com.lyp.uge.gameObject.tool;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import com.lyp.uge.game.Global;
import com.lyp.uge.gameObject.camera.Camera;
import com.lyp.uge.input.MouseInput;
import com.lyp.uge.logger.Logger;
import com.lyp.uge.math.MathTools;
import com.lyp.uge.window.WindowManager;
import com.sun.istack.internal.NotNull;

public class MousePicker {

	private Vector3f mRay;
	
	private Camera mCamera;
	private Matrix4f mProjectionMatrix;
	private Matrix4f mViewMatrix;
	
	public MousePicker(@NotNull Camera camera, @NotNull Matrix4f projectionMatrix) {
		this.mCamera = camera;
		this.mProjectionMatrix = projectionMatrix;
	}
	
	public void update() {
		mViewMatrix = MathTools.createViewMatrix(mCamera);
		mRay = castMouseRay(MouseInput.getInstance().getPosX(), MouseInput.getInstance().getPosY());
		
		if (Global.debug_mouse_picker) { Logger.d("MouseRay", mRay.toString()); }
	}
	
	private Vector3f castMouseRay(float mouseX, float mouseY) {
		Vector2f normalizedDeviceCoords = toNormalizedDeviceCoords(mouseX, mouseY);
		Vector4f homogeneousClipCoords = new Vector4f(normalizedDeviceCoords.x, normalizedDeviceCoords.y, -1f, 1f);
		Vector4f eyeCoords = toEyeCoords(homogeneousClipCoords);
		Vector3f worldCoordsRay = toWorldCoords(eyeCoords);
		return worldCoordsRay;
	}
	
	private Vector2f toNormalizedDeviceCoords(float mouseX, float mouseY) {
		// lwjgl3 mouse coordinates is starts from top left.
		float x = mouseX / WindowManager.getWindowWidth() * 2 - 1; // Scale range to [-1:1]
		float y = mouseY / WindowManager.getWindowHeight() * 2 - 1; // Scale range to [-1:1]
		return new Vector2f(x, y);
	}
	
	private Vector4f toEyeCoords(Vector4f homogeneousClipCoords) {
		Matrix4f invertedProjectionMatrix = Matrix4f.invert(mProjectionMatrix, null);
		Vector4f eyeCoords = Matrix4f.transform(invertedProjectionMatrix, homogeneousClipCoords, null);
		return new Vector4f(eyeCoords.x, eyeCoords.y, -1f, 0f); // (z = -1, w = 0) means "forwards, and not a point".
	}
	
	private Vector3f toWorldCoords(Vector4f eyeCoords) {
		Matrix4f invertedViewMatrix = Matrix4f.invert(mViewMatrix, null);
		Vector4f rayWorld = Matrix4f.transform(invertedViewMatrix, eyeCoords, null);
		Vector3f mouseRay = new Vector3f(rayWorld.x, rayWorld.y, rayWorld.z);
		mouseRay.normalise(); // Just a ray direction.
		return mouseRay;
	}

	public Vector3f getCurrentRay() {
		return mRay;
	}
}
