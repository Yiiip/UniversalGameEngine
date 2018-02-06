package com.lyp.uge.texture;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import static org.lwjgl.opengl.GL11.*;

import com.lyp.uge.logger.Logger;
import com.lyp.uge.utils.BufferUtils;

public class Texture {

	protected int mTextureID;
	protected TextureData mTextureData;
	
	protected float shineDamper = 1.0f;	//光照亮度衰减率
	protected float reflectivity = 0.0f;	//光照反射率
	protected boolean hasTransparency = false; //是否有透明通道
	protected boolean useFakeLighting = false; //是否使用假光替代原有法线（即让法线竖直向上(0,1,0)）
	protected float ambientLightness = 0.25f;	//环境光强度，默认给予一定亮度
	
	public static final float FOGGY_DENSITY_NULL = 0.0f;
	public static final float FOGGY_GRADIENT_NULL = 1.0f;
	protected boolean foggy = false; //non fog
	protected float foggyDensity = FOGGY_DENSITY_NULL; //non fog
	protected float foggyGradient = FOGGY_GRADIENT_NULL; //non fog

	protected Texture() {
	}
	
	public Texture(String path) {
		this.mTextureID = loadTexture(path);
	}
	
	private int loadTexture(String path) {
		int[] pixels = null;
		int w = 0;
		int h = 0;
		try {
			BufferedImage bufferedImage = ImageIO.read(new FileInputStream(path));
			w = bufferedImage.getWidth();
			h = bufferedImage.getHeight();
			pixels = new int[w * h];
			bufferedImage.getRGB(0, 0, w, h, pixels, 0, w);
		} catch (IOException e) {
			Logger.e(e);
		}

		int[] data = new int[w * h];
		for (int i = 0; i < w * h; i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);
			
			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		int genTextureID = glGenTextures();
		bindTexture(genTextureID);
		GL30.glGenerateMipmap(GL_TEXTURE_2D);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, w, h, 0, GL_RGBA, GL_UNSIGNED_BYTE, BufferUtils.createIntBuffer(data));
		glTexParameterf(GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, -0.4f);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR); //GL_NEAREST
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST); //GL_NEAREST
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
		unbindTexture();
		
		Logger.d("Loading", path);
		mTextureData = new TextureData(w, h, null);
		
		return genTextureID;
	}
	
	public void bindTexture(int texID) {
		glBindTexture(GL_TEXTURE_2D, texID);
	}
	
	public void unbindTexture() {
		glBindTexture(GL_TEXTURE_2D, 0);
	}
	
	protected void setID(int id) {
		this.mTextureID = id;
	}
	
	protected void setWidth(int width) {
		mTextureData.setWidth(width);
	}
	
	protected void setHeight(int height) {
		mTextureData.setHeight(height);
	}

	public int getID() {
		return this.mTextureID;
	}
	
	public int getHeight() {
		return mTextureData.getHeight();
	}
	
	public int getWidth() {
		return mTextureData.getWidth();
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
	
	public Texture addFoggy(float fogDensity, float fogGradient) {
		this.foggy = true;
		this.foggyDensity = fogDensity;
		this.foggyGradient = fogGradient;
		return this;
	}
	
	public void removeFoggy() {
		this.foggy = false;
		this.foggyDensity = FOGGY_DENSITY_NULL;
		this.foggyGradient = FOGGY_GRADIENT_NULL;
	}
	
	public boolean isFoggy() {
		return foggy;
	}
	
	public float getFoggyDensity() {
		return foggyDensity;
	}
	
	public float getFoggyGradient() {
		return foggyGradient;
	}
}
