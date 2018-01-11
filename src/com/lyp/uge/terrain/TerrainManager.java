package com.lyp.uge.terrain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.util.vector.Vector2f;

import com.lyp.uge.ai.Collision;
import com.lyp.uge.logger.Logger;

public class TerrainManager {

	private List<Terrain> mTerrains;
	
	public TerrainManager(List<Terrain> terrains) {
		this.mTerrains = terrains = new ArrayList<>();
	}
	
	public TerrainManager(Terrain[] terrains) {
		this.mTerrains = Arrays.asList(terrains);
	}
	
	public int getCapacity() {
		if (mTerrains == null) Logger.e("TerrainManager::getCapacity() inner NULL.");
		return mTerrains == null ? 0 : mTerrains.size();
	}
	
	public Terrain inside(float x, float z) {
		Terrain targetTerrain = null;
		for (Terrain terrain : mTerrains) {
			if (Collision.isPointInside(new Vector2f(x, z), new Vector2f(terrain.getX(), terrain.getZ()), Terrain.SIZE, Terrain.SIZE)) {
				targetTerrain = terrain;
				break;
			} else {
				continue;
			}
		}
		return targetTerrain;
	}
}
