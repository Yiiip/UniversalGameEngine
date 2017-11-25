package com.lyp.uge.renderEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import com.lyp.uge.model.RawModel;
import com.lyp.uge.model.TextureModel;

public class Renderer {
	
	public Renderer() {
	}

	public void prepare() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		glClearColor(0, 0, 0, 1);
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
		RawModel model = textureModel.getRawModel();
		glBindVertexArray(model.getVaoID());
		glEnableVertexAttribArray(Loader.ATTR_POSITIONS);
		glEnableVertexAttribArray(Loader.ATTR_COORDINATES);
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, textureModel.getTexture().getTextureID());
		glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
		glDisableVertexAttribArray(Loader.ATTR_POSITIONS);
		glDisableVertexAttribArray(Loader.ATTR_COORDINATES);
		glBindVertexArray(0);
	}
}
