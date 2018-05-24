package com.lyp.uge.water;

import com.lyp.uge.material.Material;

public class WaterTile {

	public static final float WATER_TILE_SIZE = 60.0f; // for scale
	public static final int WATER_TILING_COUNT = 3; // for repeat dudvMap

	private float x;
	private float z;
	private float height;
	private float size = WATER_TILE_SIZE;
	private int tiling = WATER_TILING_COUNT;

	private Material mMaterial;

	public WaterTile(float centerX, float centerZ, float height) {
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
		this.mMaterial = new Material();
	}
	
	public WaterTile(float centerX, float centerZ, float height, float size) {
		this(centerX, centerZ, height);
		this.size = size;
	}
	
	public WaterTile(float centerX, float centerZ, float height, float size, int tilingCount) {
		this(centerX, centerZ, height, size);
		this.tiling = tilingCount;
	}

	public float getHeight() {
		return height;
	}

	public float getX() {
		return x;
	}

	public float getZ() {
		return z;
	}
	
	public float getSize() {
		return size;
	}
	
	public int getTilingCount() {
		return tiling;
	}

	public WaterTile addFoggy(float fogDensity, float fogGradient) {
		if (!mMaterial.isFoggy()) {
			mMaterial.setFoggy(true);
		}
		mMaterial
			.setFoggyDensity(fogDensity)
			.setFoggyGradient(fogGradient);
		return this;
	}

	public void removeFoggy() {
		mMaterial.setFoggy(false)
			.setFoggyDensity(Material.FOGGY_DENSITY_NULL)
			.setFoggyGradient(Material.FOGGY_GRADIENT_NULL);
	}

	public boolean isFoggy() {
		return mMaterial.isFoggy();
	}

	public Material getMaterial()
	{
		return this.mMaterial;
	}
}
