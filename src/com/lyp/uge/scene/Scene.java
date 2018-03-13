package com.lyp.uge.scene;

import java.util.ArrayList;
import java.util.List;

import com.lyp.uge.gameObject.GameObject;
import com.lyp.uge.gameObject.ObjectManager;
import com.lyp.uge.terrain.Terrain;
import com.lyp.uge.terrain.TerrainManager;
import com.lyp.uge.water.WaterTile;

public class Scene {

    private ObjectManager mObjectManager;
    private TerrainManager mTerrainManager;
    private List<WaterTile> mWaterTiles;

    public Scene() {
        mObjectManager = new ObjectManager();
        mTerrainManager = new TerrainManager();
        mWaterTiles = new ArrayList<WaterTile>();
    }

    public Scene(List<GameObject> gameObjects, List<Terrain> terrains) {
        mObjectManager = new ObjectManager(gameObjects);
        mTerrainManager = new TerrainManager(terrains);
        mWaterTiles = new ArrayList<WaterTile>();
    }
    
    public Scene(List<GameObject> gameObjects, List<Terrain> terrains, List<WaterTile> waterTiles) {
        mObjectManager = new ObjectManager(gameObjects);
        mTerrainManager = new TerrainManager(terrains);
        mWaterTiles = waterTiles;
    }

    public void addTerrain(Terrain terrain) {
        if (terrain == null) {
            return;
        }
		mTerrainManager.addTerrain(terrain);
	}

    public void addTerrains(List<Terrain> terrains) {
        if (terrains == null) {
            return;
        }
		mTerrainManager.addTerrains(terrains);
	}

    public void addObject(GameObject gameObject) {
		if (gameObject == null) {
			return;
		}
        mObjectManager.addObject(gameObject);
	}

    public void addObjects(List<GameObject> gameObjects) {
		if (gameObjects == null) {
			return;
		}
        for (int i = 0; i < gameObjects.size(); i++) {
            mObjectManager.addObject(gameObjects.get(i));
        }
    }
    
    public void addWaterTile(WaterTile waterTile) {
    	if (waterTile == null) {
			return;
		}
    	mWaterTiles.add(waterTile);
    }
    
    public void addWaterTiles(List<WaterTile> waterTiles) {
    	if (waterTiles == null) {
			return;
		}
    	mWaterTiles.addAll(waterTiles);
    }

    public void clearAll() {
        mTerrainManager.clearAll();
        mWaterTiles.clear();
        mObjectManager.clearAll();
    }

    public ObjectManager getObjectManager() {
        return mObjectManager;
    }

    public TerrainManager getTerrainManager() {
        return mTerrainManager;
    }
    
    public List<WaterTile> getWaterTiles() {
		return mWaterTiles;
	}
}