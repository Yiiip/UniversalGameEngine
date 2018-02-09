package com.lyp.uge.texture;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

public class CubeMapTexture extends Texture {

	public CubeMapTexture(String[] textureFiles) {
		super.mTextureID = loadCubeMap(textureFiles);
	}

	private int loadCubeMap(String[] textureFiles) {
		int genTextureID = glGenTextures();
		glActiveTexture(GL_TEXTURE0);
		glBindTexture(GL_TEXTURE_CUBE_MAP, genTextureID);
		
		for (int i = 0; i < textureFiles.length; i++) {
			TextureData tempTexData = TextureLoader.decodeFromPNG(textureFiles[i]);
			//利用索引设置到盒纹理的六个面上
			glTexImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL_RGBA, tempTexData.getWidth(), tempTexData.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, tempTexData.getByteBuffer());
		}
		
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		
		glBindTexture(GL_TEXTURE_CUBE_MAP, 0);
		
		return genTextureID;
	}
}
