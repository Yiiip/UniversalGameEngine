package com.lyp.uge.terrain;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.logger.Logger;
import com.lyp.uge.math.MathTools;
import com.lyp.uge.model.RawModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.texture.Texture;

public class Terrain {

	public static final float SIZE = 800.0f;
	private static int VERTEX_COUNT = 0;
	private static float MAX_ALTITUDE = 40.0f; //最大海拔高度，这里为默认值
	private static final int MAX_COLOR = 256 * 256 * 256;

	private float x; //世界坐标
	private float z; //世界坐标
	private RawModel rawModel;
	private TerrainTexturePack texturePack;
	private Texture blendMap;
	private BufferedImage mHeightMapImage;
	
	private float[][] mHeights; //存储每个Vertex顶点的高度值
	
	/**
	 * @param gridX 网格坐标，把1个Terrain看作一个格子
	 * @param gridZ
	 * @param loader
	 * @param texturePack 混合纹理对象集
	 * @param blendMapTexture 混合纹理映射图
	 * @param heightMapFile 高度图
	 */
	public Terrain(float gridX, float gridZ, Loader loader, 
			TerrainTexturePack texturePack, Texture blendMapTexture, 
			String heightMapFile) {
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.texturePack = texturePack;
		this.blendMap = blendMapTexture;
		this.rawModel = generateTerrain(loader, heightMapFile);
	}
	
	public Terrain(float gridX, float gridZ, Loader loader, 
			TerrainTexturePack texturePack, Texture blendMapTexture,
			String heightMapFile, float maxHeight) {
		MAX_ALTITUDE = maxHeight;
		this.x = gridX * SIZE;
		this.z = gridZ * SIZE;
		this.texturePack = texturePack;
		this.blendMap = blendMapTexture;
		this.rawModel = generateTerrain(loader, heightMapFile);
	}

	private RawModel generateTerrain(Loader loader, String heightMapFile) {
		try {
			this.mHeightMapImage = ImageIO.read(new File(heightMapFile));
		} catch (IOException e) {
			Logger.e("Error read file ! " + heightMapFile);
		}
		VERTEX_COUNT = mHeightMapImage.getHeight();
		
		mHeights = new float[VERTEX_COUNT][VERTEX_COUNT];
		int count = VERTEX_COUNT * VERTEX_COUNT;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[ 6 * (VERTEX_COUNT - 1) * (VERTEX_COUNT - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < VERTEX_COUNT; i++) {
			for (int j = 0; j < VERTEX_COUNT; j++) {
				float height = getAltitude(j, i, mHeightMapImage);
				mHeights[j][i] = height;
				vertices[vertexPointer * 3] = (float) j / ((float) VERTEX_COUNT - 1) * SIZE;
				vertices[vertexPointer * 3 + 1] = height;
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
		float height = image.getRGB(x, z); //range -256*256*256 ~ 0
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
	
	/**
	 * 
	 * @param worldX 物体在世界中的位置
	 * @param worldZ
	 * @return
	 */
	public float getHeightOfTerrain(float worldX, float worldZ) {
		float terrainX = worldX - this.x; //得到物体相对于所在Terrain上的坐标
		float terrainZ = worldZ - this.z;
		float gridSquareSize = SIZE / ((float)mHeights.length - 1); //该Terrain中每个正方格子的宽度
		int gridX = (int) Math.floor(terrainX / gridSquareSize);
		int gridZ = (int) Math.floor(terrainZ / gridSquareSize);
		if (gridX < 0 || gridZ < 0 || gridX >= mHeights.length-1 || gridZ >= mHeights.length-1) {
			return 0.0f;
		}
		float xCoord = (terrainX % gridSquareSize) / gridSquareSize; //得到相对于所在正方格子中的坐标
		float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;
		float targetHeight;
		if (xCoord <= (1 - zCoord)) { //对角线z=-x+1，注原点坐标在左上角
			targetHeight = MathTools.TriangleBarycentric (
					new Vector3f(0, mHeights[gridX][gridZ], 0),
					new Vector3f(1, mHeights[gridX + 1][gridZ], 0),
					new Vector3f(0, mHeights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		} else {
			targetHeight = MathTools.TriangleBarycentric (
					new Vector3f(1, mHeights[gridX + 1][gridZ], 0),
					new Vector3f(1, mHeights[gridX + 1][gridZ + 1], 1),
					new Vector3f(0, mHeights[gridX][gridZ + 1], 1),
					new Vector2f(xCoord, zCoord));
		}
		return targetHeight;
	}

	public RawModel getRawModel() {
		return rawModel;
	}

	public TerrainTexturePack getTexturePack() {
		return texturePack;
	}
	
	public Texture getBlendMap() {
		return blendMap;
	}
	
	public void addFoggy(float fogDensity, float fogGradient) {
		this.getTexturePack().getBgTexture().addFoggy(fogDensity, fogGradient);
	}
	
	public void removeFoggy() {
		this.getTexturePack().getBgTexture().removeFoggy();
	}
	
	public boolean isFoggy() {
		return this.getTexturePack().getBgTexture().isFoggy();
	}
	
	public float getFoggyDensity() {
		return this.getTexturePack().getBgTexture().getFoggyDensity();
	}
	
	public float getFoggyGradient() {
		return this.getTexturePack().getBgTexture().getFoggyGradient();
	}
}
