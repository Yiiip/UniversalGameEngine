package com.lyp.uge.renderEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.List;
import java.util.Map;

import org.lwjgl.util.vector.Matrix4f;

import com.lyp.uge.game.Global;
import com.lyp.uge.gameObject.GameObject;
import com.lyp.uge.math.MathTools;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.prefab.TextureModel;
import com.lyp.uge.shader.FoggyShader;
import com.lyp.uge.shader.Shader;
import com.lyp.uge.shader.SpecularLightShader;
import com.lyp.uge.texture.Texture;
import com.lyp.uge.window.WindowManager;

/**
 * 渲染GameObject对象(3D)
 * @author LYP
 *
 */
public class Renderer {
	
	private static float FIELD_OF_VIEW_ANGLE = 70;
	private static float NEAR_PLANE = 0.1f; //最近平面处
	private static float FAR_PLANE = 1000.0f; //最远平面处
	
	private Matrix4f projectionMatrix;
	private Shader mShader;
	
	public Renderer() {
	}
	
	//旧版
	public Renderer(Shader shaderProgram) {
		this.mShader = shaderProgram;
		if (Global.mode_culling_back) { RendererManager.enableCulling();}
		createProjectionMatrix();
		shaderProgram.start();
		shaderProgram.loadProjectionMatrix(projectionMatrix);
		shaderProgram.stop();
	}
	
	//新版
	public Renderer(Shader shaderProgram, Matrix4f projectionMatrix) {
		this.mShader = shaderProgram;
		if (Global.mode_culling_back) { RendererManager.enableCulling();}
		shaderProgram.start();
		shaderProgram.loadProjectionMatrix(projectionMatrix);
		shaderProgram.stop();
	}

	//TODO 这里是旧版prepare，新版已移入RendererManager
	public void prepare() {
		this.prepare(.12f, .12f, .12f, 1.0f);
	}
	public void prepare(float r, float g, float b, float a) {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(r, g, b, a);
	}
	
	public void renderArrays(RawModel model) { //without indices buffer
		glBindVertexArray(model.getVaoID());
		glEnableVertexAttribArray(Loader.ATTR_POSITIONS);
		glDrawArrays(GL_TRIANGLES, 0, model.getVertexCount());
		glDisableVertexAttribArray(Loader.ATTR_POSITIONS);
		glBindVertexArray(0);
	}
	
	public void render(RawModel model) {
		glBindVertexArray(model.getVaoID());
		glEnableVertexAttribArray(Loader.ATTR_POSITIONS);
		glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(Loader.ATTR_POSITIONS);
		glBindVertexArray(0);
	}
	
	public void render(TextureModel textureModel) {
		RawModel rawModel = textureModel.getRawModel();
		glBindVertexArray(rawModel.getVaoID());
		glEnableVertexAttribArray(Loader.ATTR_POSITIONS);
		glEnableVertexAttribArray(Loader.ATTR_COORDINATES);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureModel.getTexture().getID());
		glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(Loader.ATTR_POSITIONS);
		glDisableVertexAttribArray(Loader.ATTR_COORDINATES);
		glBindVertexArray(0);
	}
	
	public void render(GameObject object, Shader shaderProgram) {
		TextureModel textureModel = object.getModel();
		RawModel rawModel = textureModel.getRawModel();
		glBindVertexArray(rawModel.getVaoID());
		glEnableVertexAttribArray(Loader.ATTR_POSITIONS);
		glEnableVertexAttribArray(Loader.ATTR_COORDINATES);
		glEnableVertexAttribArray(Loader.ATTR_NORMALS);
		
		Matrix4f transformationMatrix = MathTools.createTransformationMatrix(
				object.getPosition(), 
				object.getRotateX(), 
				object.getRotateY(), 
				object.getRotateZ(), 
				object.getScale());
		shaderProgram.loadModelMatrix(transformationMatrix);
		
		if (shaderProgram instanceof SpecularLightShader) { //高光反射光Shader
			Texture texture = textureModel.getTexture();
			((SpecularLightShader) shaderProgram).loadSpecularLightingParms(texture.getShineDamper(), texture.getReflectivity());
		}
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureModel.getTexture().getID());
		glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(Loader.ATTR_POSITIONS);
		glDisableVertexAttribArray(Loader.ATTR_COORDINATES);
		glDisableVertexAttribArray(Loader.ATTR_NORMALS);
		glBindVertexArray(0);
	}
	
	/**
	 * 优化后的渲染方法
	 * @param objects
	 */
	public void render(Map<TextureModel, List<GameObject>> objects) {
		for (TextureModel textureModel : objects.keySet()) {
			this.prepareTextureModel(textureModel);
			List<GameObject> tempObjs = objects.get(textureModel);
			for (GameObject tempObj : tempObjs) {
				this.prepareInstance(tempObj);
				glDrawElements(GL_TRIANGLES, textureModel.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
			}
			this.unbindTextureModel();
		}
	}
	
	private void prepareTextureModel(TextureModel textureModel) {
		RawModel rawModel = textureModel.getRawModel();
		glBindVertexArray(rawModel.getVaoID());
		glEnableVertexAttribArray(Loader.ATTR_POSITIONS);
		glEnableVertexAttribArray(Loader.ATTR_COORDINATES);
		glEnableVertexAttribArray(Loader.ATTR_NORMALS);
		
		Texture texture = textureModel.getTexture();
		if (mShader instanceof SpecularLightShader) { //高光反射光Shader
			if (texture.isHasTransparency()) {
				RendererManager.disableCulling();
			}
			((SpecularLightShader) mShader).loadSpecularLightingParms(texture.getShineDamper(), texture.getReflectivity());
			((SpecularLightShader) mShader).loadAmbientLightness(texture.getAmbientLightness());
			((SpecularLightShader) mShader).loadFakeLightingParms(texture.isUseFakeLighting());
		}
		if (mShader instanceof FoggyShader) { //迷雾效果Shader
			if (texture.isFoggy()) {
				((FoggyShader) mShader).setupFogDensity(texture.getFoggyDensity());
				((FoggyShader) mShader).setupFogGradient(texture.getFoggyGradient());
			} else {
				((FoggyShader) mShader).setupFogDensity(Texture.FOGGY_DENSITY_NULL);
				((FoggyShader) mShader).setupFogGradient(Texture.FOGGY_GRADIENT_NULL);
			}
		}
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureModel.getTexture().getID());
	}
	
	private void prepareInstance(GameObject object) {
		Matrix4f transformationMatrix = MathTools.createTransformationMatrix(
				object.getPosition(), 
				object.getRotateX(), 
				object.getRotateY(), 
				object.getRotateZ(), 
				object.getScale());
		mShader.loadModelMatrix(transformationMatrix);
	}

	private void unbindTextureModel() {
		if (Global.mode_culling_back) { RendererManager.enableCulling();}
		glDisableVertexAttribArray(Loader.ATTR_POSITIONS);
		glDisableVertexAttribArray(Loader.ATTR_COORDINATES);
		glDisableVertexAttribArray(Loader.ATTR_NORMALS);
		glBindVertexArray(0);
	}
	
	//TODO 这里是旧版，新版已移入RendererManager
	private void createProjectionMatrix() {
		float aspectRatio = (float) WindowManager.getWindowWidth() / (float) WindowManager.getWindowHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FIELD_OF_VIEW_ANGLE/2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = FAR_PLANE - NEAR_PLANE;
        
        projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((FAR_PLANE + NEAR_PLANE) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * NEAR_PLANE * FAR_PLANE) / frustum_length);
        projectionMatrix.m33 = 0;
	}
}
