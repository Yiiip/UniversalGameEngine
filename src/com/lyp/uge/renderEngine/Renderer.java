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

public class Renderer {
	
	public Renderer() {
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
}
