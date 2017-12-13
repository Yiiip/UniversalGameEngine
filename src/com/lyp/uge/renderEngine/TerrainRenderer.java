package com.lyp.uge.renderEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.math.MathTools;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.shader.TerrainShader;
import com.lyp.uge.terrain.Terrain;
import com.lyp.uge.texture.Texture;

/**
 * 渲染地形对象
 * @author LYP
 *
 */
public class TerrainRenderer {

	private TerrainShader shaderProgram;

	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.shaderProgram = shader;
		this.shaderProgram.start();
		this.shaderProgram.loadProjectionMatrix(projectionMatrix);
		this.shaderProgram.stop();
	}
	
	public void render(List<Terrain> terrains) {
		for (Terrain terrain : terrains) {
			prepareTerrain(terrain);
			loadModelMatrix(terrain);
			glDrawElements(GL_TRIANGLES, terrain.getRawModel().getVertexCount(), GL_UNSIGNED_INT, 0);
			unbindTextureModel();
		}
	}
	
	private void prepareTerrain(Terrain terrain) {
		RawModel rawModel = terrain.getRawModel();
		glBindVertexArray(rawModel.getVaoID());
		glEnableVertexAttribArray(Loader.ATTR_POSITIONS);
		glEnableVertexAttribArray(Loader.ATTR_COORDINATES);
		glEnableVertexAttribArray(Loader.ATTR_NORMALS);
		
		if (shaderProgram instanceof TerrainShader) {
			Texture texture = terrain.getTexture();
			shaderProgram.loadSpecularLightingParms(texture.getShineDamper(), texture.getReflectivity());
			shaderProgram.loadAmbientLightness(texture.getAmbientLightness());
		}
		
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, terrain.getTexture().getTextureID());
	}
	
	private void loadModelMatrix(Terrain terrain) {
		Matrix4f transformationMatrix = MathTools.createTransformationMatrix(
				new Vector3f(terrain.getX(), 0.0f, terrain.getZ()), 
				0.0f, 0.0f, 0.0f, 1.0f);
		shaderProgram.loadTransformationMatrix(transformationMatrix);
	}

	private void unbindTextureModel() {
		glDisableVertexAttribArray(Loader.ATTR_POSITIONS);
		glDisableVertexAttribArray(Loader.ATTR_COORDINATES);
		glDisableVertexAttribArray(Loader.ATTR_NORMALS);
		glBindVertexArray(0);
	}
}
