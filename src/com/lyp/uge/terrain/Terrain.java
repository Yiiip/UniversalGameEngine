package com.lyp.uge.terrain;

import com.lyp.uge.model.RawModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.texture.Texture;

public class Terrain {

	public static final float SIZE = 800;
	private static final int VERTEX_COUNT = 128;

	private float x;
	private float z;
	private RawModel rawModel;
	private Texture texture;
	
	public static final float FOGGY_DENSITY_NULL = 0.0f;
	public static final float FOGGY_GRADIENT_NULL = 1.0f;
	private boolean foggy = false; //non fog
	private float foggyDensity = FOGGY_DENSITY_NULL; //non fog
	private float foggyGradient = FOGGY_GRADIENT_NULL; //non fog
	
	public Terrain(float gridX, float gridZ, Loader loader, Texture texture) {
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.texture = texture;
		this.rawModel = generateTerrain(loader);
	}

	private RawModel generateTerrain(Loader loader) {
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[ 6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = 0;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) VERTEX_COUNT - 1) * SIZE;
				normals[vertexPointer * 3] = 0;
				normals[vertexPointer * 3 + 1] = 1;
				normals[vertexPointer * 3 + 2] = 0;
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
		this.foggy = true;
		this.foggyDensity = fogDensity;
		this.foggyGradient = fogGradient;
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
