package com.lyp.uge.texture;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.*;

import javax.imageio.ImageIO;

import com.lyp.uge.utils.BufferUtils;

public class Texture {

	private int textureID;
	private int width;
	private int height;
	
	private float shineDamper = 1.0f;	//光照亮度衰减率
	private float reflectivity = 0.0f;	//光照反射率
	private boolean hasTransparency = false; //是否有透明通道
	private boolean useFakeLighting = false; //是否使用假光替代原有法线（即让法线竖直向上(0,1,0)）
	private float ambientLightness = 0.25f;	//环境光强度，默认给予一定亮度
	
	public Texture(String path) {
		this.textureID = loadTexture(path);
	}

	private int loadTexture(String path) {
		int[] pixels = null;
		try {
			BufferedImage bufferedImage = ImageIO.read(new FileInputStream(path));
			this.width = bufferedImage.getWidth();
			this.height = bufferedImage.getHeight();
			pixels = new int[width * height];
			bufferedImage.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int[] data = new int[width * height];
		for (int i = 0; i < width * height; i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			
			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		int genTextureID = glGenTextures();
		glBindTexture(GL_TEXTURE_2D, genTextureID);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));
		glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_LOD_BIAS, 0f); //TODO <0 in font texture or 0 in texture
		unbindTexture();
		return genTextureID;
	}
	
	public void bindTexture() {
		glBindTexture(GL_TEXTURE_2D, textureID);
	}
	
	public void unbindTexture() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getID() {
		return textureID;
	}
	
	public Texture setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
		return this;
	}
	
	public float getReflectivity() {
		return reflectivity;
	}
	
	public Texture setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
		return this;
	}
	
	public float getShineDamper() {
		return shineDamper;
	}
	
	public Texture setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
		return this;
	}
	
	public boolean isHasTransparency() {
		return hasTransparency;
	}
	
	public Texture setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
		return this;
	}
	
	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}
	
	public Texture setAmbientLightness(float ambientLightness) {
		this.ambientLightness = ambientLightness;
		return this;
	}
	
	public float getAmbientLightness() {
		return ambientLightness;
	}
}
