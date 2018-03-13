package com.lyp.uge.gameObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.lyp.uge.prefab.TextureModel;

public class ObjectManager {

	private Map<TextureModel, List<GameObject>> mObjects;
	
	public ObjectManager() {
		this.mObjects = new HashMap<TextureModel, List<GameObject>>();
	}
	
	public ObjectManager(List<GameObject> gameObjects) {
		this();
		for (GameObject object : gameObjects) {
			addObject(object);
		}
	}

	public void update() {
		if (mObjects == null || mObjects.size() == 0) {
			return;
		}
		Iterator<List<GameObject>> iterator = mObjects.values().iterator();
		while (iterator.hasNext()) {
			List<GameObject> list = (List<GameObject>) iterator.next();
			for (int i = 0; i < list.size(); i++) {
				list.get(i).update();
			}
		}
	}

	public void addObject(GameObject object) {
		TextureModel textureModel = object.getModel();
		List<GameObject> objs = mObjects.get(textureModel);
		if (objs != null) {
			objs.add(object);
		} else {
			List<GameObject> newObjs = new ArrayList<>();
			newObjs.add(object);
			mObjects.put(textureModel, newObjs);
		}
	}

	public void removeObject(GameObject object) {
		if (mObjects == null || mObjects.size() == 0) {
			return;
		}
		TextureModel textureModel = object.getModel();
		List<GameObject> objs = mObjects.get(textureModel);
		if (objs != null) {
			objs.remove(object);
		}
	}

	public void removeObjectsById(int id) {
		if (mObjects == null || mObjects.size() == 0) {
			return;
		}
		Iterator<List<GameObject>> iterator = mObjects.values().iterator();
		while (iterator.hasNext()) {
			List<GameObject> list = (List<GameObject>) iterator.next();
			for (int i = 0; i < list.size(); i++) {
				GameObject temp = list.get(i);
				if (temp.id == id) {
					list.remove(temp);
				}
			}
		}
	}
	
	public void clearAll() {
		this.mObjects.clear();
	}
	
	public Map<TextureModel, List<GameObject>> getObjects() {
		return mObjects;
	}
}
