package com.lyp.uge.renderEngine;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.util.List;

import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.material.Material;
import com.lyp.uge.math.MathTools;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.shader.TerrainShader;
import com.lyp.uge.terrain.Terrain;
import com.lyp.uge.terrain.TerrainTexturePack;
import com.lyp.uge.texture.Texture;

/**
 * 渲染地形对象
 * @author LYP
 *
 */
public class TerrainRenderer {

	private TerrainShader mTerrainShader;

	public TerrainRenderer(TerrainShader shader, Matrix4f projectionMatrix) {
		this.mTerrainShader = shader;
		this.mTerrainShader.start();
		this.mTerrainShader.loadProjectionMatrix(projectionMatrix);
		this.mTerrainShader.setupTextureUnits();
		this.mTerrainShader.stop();
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
		
		bindTextures(terrain);
		
		TerrainTexturePack texturePack = terrain.getTexturePack();
		Texture bgTexture = texturePack.getBgTexture(); //TODO
		if (mTerrainShader instanceof TerrainShader) {
			mTerrainShader.loadSpecularLightingParms(bgTexture.getShineDamper(), bgTexture.getReflectivity());
			mTerrainShader.loadAmbientLightness(bgTexture.getAmbientLightness());
			if (bgTexture.isFoggy()) {
				mTerrainShader.setupFogDensity(terrain.getFoggyDensity());
				mTerrainShader.setupFogGradient(terrain.getFoggyGradient());
			} else {
				mTerrainShader.setupFogDensity(Material.FOGGY_DENSITY_NULL);
				mTerrainShader.setupFogGradient(Material.FOGGY_GRADIENT_NULL);
			}
		}
		
	}
	
	private void loadModelMatrix(Terrain terrain) {
		Matrix4f modelMatrix = MathTools.createModelMatrix(
				new Vector3f(terrain.getX(), 0.0f, terrain.getZ()), 
				0.0f, 0.0f, 0.0f, 1.0f);
		mTerrainShader.loadModelMatrix(modelMatrix);
	}

	private void bindTextures(Terrain terrain) {
		TerrainTexturePack texturePack = terrain.getTexturePack();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_2D, texturePack.getBgTexture().getID());
		glActiveTexture(GL_TEXTURE1);
		glBindTexture(GL_TEXTURE_2D, texturePack.getRTexture().getID());
		glActiveTexture(GL_TEXTURE2);
		glBindTexture(GL_TEXTURE_2D, texturePack.getGTexture().getID());
		glActiveTexture(GL_TEXTURE3);
		glBindTexture(GL_TEXTURE_2D, texturePack.getBTexture().getID());
		glActiveTexture(GL_TEXTURE4);
		glBindTexture(GL_TEXTURE_2D, terrain.getBlendMap().getID());
	}
	
	private void unbindTextureModel() {
		glDisableVertexAttribArray(Loader.ATTR_POSITIONS);
		glDisableVertexAttribArray(Loader.ATTR_COORDINATES);
		glDisableVertexAttribArray(Loader.ATTR_NORMALS);
		glBindVertexArray(0);
	}
}
