package com.lyp.uge.fontRendering;

import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import com.lyp.uge.fontMeshCreator.FontType;
import com.lyp.uge.fontMeshCreator.GUIText;
import com.lyp.uge.renderEngine.Loader;

public class FontRenderer {

	private FontShader shader;

	public FontRenderer() {
		shader = new FontShader();
	}
	
	public void render(Map<FontType, List<GUIText>> texts) {
		prepare();
		for (FontType font : texts.keySet()) {
			glActiveTexture(GL_TEXTURE0);
			glBindTexture(GL_TEXTURE_2D, font.getTextureAtlas());
			for (GUIText text : texts.get(font)) {
				renderText(text);
			}
		}
		endRendering();
	}

	public void cleanUp(){
		shader.cleanUp();
	}
	
	private void prepare() {
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_DEPTH_TEST);
		shader.start();
	}
	
	private void renderText(GUIText text) {
		glBindVertexArray(text.getMesh());
		glEnableVertexAttribArray(Loader.ATTR_POSITIONS);
		glEnableVertexAttribArray(Loader.ATTR_COORDINATES);
		shader.loadColor(text.getColor());
		shader.loadTranslation(text.getPosition());
		glDrawArrays(GL_TRIANGLES, 0, text.getVertexCount());
		glDisableVertexAttribArray(Loader.ATTR_POSITIONS);
		glDisableVertexAttribArray(Loader.ATTR_COORDINATES);
		glBindVertexArray(0);
	}
	
	private void endRendering() {
		shader.stop();
		glDisable(GL_BLEND);
		glEnable(GL_DEPTH_TEST);
	}

}
