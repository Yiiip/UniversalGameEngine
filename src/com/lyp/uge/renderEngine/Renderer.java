package com.lyp.uge.renderEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import com.lwjgl.util.vector.Matrix4f;
import com.lyp.uge.gameObject.Entity;
import com.lyp.uge.math.MathTools;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.model.TextureModel;
import com.lyp.uge.shader.ShaderProgram;
import com.lyp.uge.shader.StaticShader;
import com.lyp.uge.window.WindowManager;

public class Renderer {
	
	private static float FIELD_OF_VIEW_ANGLE = 70;
	private static float NEAR_PLANE = 0.1f; //最近平面处
	private static float FAR_PLANE = 1000.0f; //最远平面处
	
	private Matrix4f projectionMatrix;
	
	public Renderer() {
	}
	
	public Renderer(StaticShader shaderProgram) {
		createProjectionMatrix();
		shaderProgram.start();
		shaderProgram.loadProjectionMatrix(projectionMatrix);
		shaderProgram.stop();
	}

	public void prepare() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(.14f, .14f, .14f, 1.0f);
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
		glBindTexture(GL_TEXTURE_2D, textureModel.getTexture().getTextureID());
		glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(Loader.ATTR_POSITIONS);
		glDisableVertexAttribArray(Loader.ATTR_COORDINATES);
		glBindVertexArray(0);
	}
	
	public void render(Entity entity, StaticShader shaderProgram) {
		TextureModel textureModel = entity.getModel();
		RawModel rawModel = textureModel.getRawModel();
		glBindVertexArray(rawModel.getVaoID());
		glEnableVertexAttribArray(Loader.ATTR_POSITIONS);
		glEnableVertexAttribArray(Loader.ATTR_COORDINATES);
		
		Matrix4f transformationMatrix = MathTools.createTransformationMatrix(
				entity.getPosition(), 
				entity.getRotateX(), 
				entity.getRotateY(), 
				entity.getRotateZ(), 
				entity.getScale());
		shaderProgram.loadTransformationMatrix(transformationMatrix);
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureModel.getTexture().getTextureID());
		glDrawElements(GL_TRIANGLES, rawModel.getVertexCount(), GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(Loader.ATTR_POSITIONS);
		glDisableVertexAttribArray(Loader.ATTR_COORDINATES);
		glBindVertexArray(0);
	}
	
	private void createProjectionMatrix() {
		float aspectRatio = (float) WindowManager.DEFAULT_WIDTH / (float) WindowManager.DEFAULT_HEIGHT;
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
