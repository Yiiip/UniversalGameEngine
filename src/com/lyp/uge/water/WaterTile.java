package com.lyp.uge.water;

public class WaterTile {

	public static final float WATER_TILE_SIZE = 60.0f; // for scale
	public static final int WATER_TILING_COUNT = 3; // for repeat dudvMap

	private float x;
	private float z;
	private float height;
	private float size = WATER_TILE_SIZE;
	private int tiling = WATER_TILING_COUNT;

	public WaterTile(float centerX, float centerZ, float height) {
		this.x = centerX;
		this.z = centerZ;
		this.height = height;
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
}
