package com.lyp.uge.terrain;

import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.model.RawModel;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.texture.Texture;

/**
 * 基于噪声的地形
 * 
 * @author LYP
 *
 */
public class NoiseTerrain extends Terrain {

	private int mMeshCount = 512;

	public NoiseTerrain(float size, float gridX, float gridZ, Loader loader, TerrainTexturePack texturePack, Texture blendMapTexture) {
		super(size, gridX, gridZ, loader, texturePack, blendMapTexture);
		super.rawModel = generateTerrain(loader);
	}

	/**
	 * 
	 * @param worldX
	 *            物体在世界中的x坐标
	 * @param worldZ
	 *            物体在世界中的z坐标
	 * @return
	 */
	@Override
	public float getHeightOfTerrain(float worldX, float worldZ) {
		return super.getHeightOfTerrain(worldX, worldZ);
	}

	private RawModel generateTerrain(Loader loader) {

		HeightGenerator heightGenerator = new HeightGenerator();

		mHeights = new float[mMeshCount][mMeshCount];
		int count = mMeshCount * mMeshCount;
		float[] vertices = new float[count * 3];
		float[] normals = new float[count * 3];
		float[] textureCoords = new float[count * 2];
		int[] indices = new int[6 * (mMeshCount - 1) * (mMeshCount - 1)];
		int vertexPointer = 0;
		for (int i = 0; i < mMeshCount; i++) {
			for (int j = 0; j < mMeshCount; j++) {
				float height = getHeight(j, i, heightGenerator);
				mHeights[j][i] = height;
				vertices[vertexPointer * 3] = (float) j / ((float) mMeshCount - 1) * size;
				vertices[vertexPointer * 3 + 1] = height;
				vertices[vertexPointer * 3 + 2] = (float) i / ((float) mMeshCount - 1) * size;
				Vector3f normal = calculateNormal(j, i, heightGenerator);
				normals[vertexPointer * 3] = normal.x;
				normals[vertexPointer * 3 + 1] = normal.y;
				normals[vertexPointer * 3 + 2] = normal.z;
				textureCoords[vertexPointer * 2] = (float) j / ((float) mMeshCount - 1);
				textureCoords[vertexPointer * 2 + 1] = (float) i / ((float) mMeshCount - 1);
				vertexPointer++;
			}
		}
		int pointer = 0;
		for (int gz = 0; gz < mMeshCount - 1; gz++) {
			for (int gx = 0; gx < mMeshCount - 1; gx++) {
				int topLeft = (gz * mMeshCount) + gx;
				int topRight = topLeft + 1;
				int bottomLeft = ((gz + 1) * mMeshCount) + gx;
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

	private float getHeight(int x, int z, HeightGenerator heightGenerator) {
		return heightGenerator.generateHeight(x, z);
	}

	private Vector3f calculateNormal(int x, int z, HeightGenerator heightGenerator) {
		float heightL = getHeight(x - 1, z, heightGenerator);
		float heightR = getHeight(x + 1, z, heightGenerator);
		float heightD = getHeight(x, z - 1, heightGenerator);
		float heightU = getHeight(x, z + 1, heightGenerator);
		Vector3f normal = new Vector3f(heightL - heightR, 2.f, heightD - heightU);
		normal.normalise();
		return normal;
	}

	public void setMeshCount(int meshCount) {
		this.mMeshCount = meshCount;
	}

	public int getMeshCount() {
		return mMeshCount;
	}
}
