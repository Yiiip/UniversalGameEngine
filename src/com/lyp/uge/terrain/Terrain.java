package com.lyp.uge.terrain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.logger.Logger;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.texture.Texture;

public class Terrain {

	public static final float SIZE = 800.0f;
	private static int VERTEX_COUNT = 0;
	private static float MAX_ALTITUDE = 40.0f; //最大海拔高度
	private static final int MAX_COLOR = 256 * 256 * 256;

	private float x;
	private float z;
	private RawModel rawModel;
	private Texture texture;
	private BufferedImage mHeightMapImage;
	
	public Terrain(float gridX, float gridZ, Loader loader, Texture texture, String heightMapFile) {
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.texture = texture;
		this.rawModel = generateTerrain(loader, heightMapFile);
	}
	
	public Terrain(float gridX, float gridZ, Loader loader, Texture texture, String heightMapFile, float maxHeight) {
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.texture = texture;
		MAX_ALTITUDE = maxHeight;
		this.rawModel = generateTerrain(loader, heightMapFile);
	}

	private RawModel generateTerrain(Loader loader, String heightMapFile) {
		try {
			this.mHeightMapImage = ImageIO.read(new File(heightMapFile));
		} catch (IOException e) {
			Logger.e("Error read file ! " + heightMapFile);
		}
		VERTEX_COUNT = mHeightMapImage.getHeight();
		
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[ 6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = getAltitude(j, i, mHeightMapImage);
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
				Vector3f normal = calculateNormal(j, i, mHeightMapImage);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j / ((float) VERTEX_COUNT - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) VERTEX_COUNT - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < VERTEX_COUNT - 1; gz++) {
			for (int gx = 0; gx < VERTEX_COUNT - 1; gx++) {
				int topLeft = (gz * VERTEX_COUNT) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * VERTEX_COUNT) + gx;
				int bottomRight = bottomLeft + 1;
				indices[pointer++] = topLeft;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = topRight;
				indices[pointer++] = topRight;
				indices[pointer++] = bottomLeft;
				indices[pointer++] = bottomRight;
			}
		}
		return loader.loadToVAO(vertices, textureCoords, normals, indices);
	}

	/**
	 * Calculate each vertex altitude height according to heightMap.
	 * @param x
	 * @param z
	 * @param heightMap quadrate image
	 * @return
	 */
	private float getAltitude(int x, int z, BufferedImage image) {
		if (x <= 0 || x >= image.getHeight()
				|| z <= 0 || z >= image.getHeight()) {
			return 0.0f;
		}
		float height = image.getRGB(x, z);
		height += MAX_COLOR/2.f; //keep -MAX_COLOR/2 <= height <= MAX_COLOR/2
		height /= MAX_COLOR/2.f; //keep -1 <= height <= 1
		height *= MAX_ALTITUDE;  //keep -MAX_ALTITUDE <= height <= MAX_ALTITUDE
		return height;
	}
	
	private Vector3f calculateNormal(int x, int z, BufferedImage image) {
		float heightL = getAltitude(x-1, z, image);
		float heightR = getAltitude(x+1, z, image);
		float heightD = getAltitude(x, z-1, image);
		float heightU = getAltitude(x, z+1, image);
		Vector3f normal = new Vector3f(heightL-heightR, 2.f, heightD-heightU);
		normal.normalise();
		return normal;
	}
	
	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public Texture getTexture() {
		return texture;
	}
	
	public void addFoggy(float fogDensity, float fogGradient) {
		this.texture.addFoggy(fogDensity, fogGradient);
	}
	
	public void removeFoggy() {
		this.texture.removeFoggy();
	}
	
	public boolean isFoggy() {
		return this.texture.isFoggy();
	}
	
	public float getFoggyDensity() {
		return this.texture.getFoggyDensity();
	}
	
	public float getFoggyGradient() {
		return this.texture.getFoggyGradient();
	}
}
