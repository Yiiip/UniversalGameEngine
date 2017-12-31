package com.lyp.uge.gameObject;

import java.util.Iterator;
import java.util.LinkedList;

public class ObjectManager {

	LinkedList<GameObject> mObjects = null;
	
	public ObjectManager() {
		this.mObjects = new LinkedList<GameObject>();
	}

	public void update() {
		if (mObjects == null || mObjects.size() == 0) {
			return;
		}
		for (int i = 0; i < mObjects.size(); i++) {
			GameObject tempObject = mObjects.get(i);
			tempObject.update();
		}
	}

	public boolean addObject(GameObject object) {
		return this.mObjects.add(object);
	}

	public boolean removeObject(GameObject object) {
		return this.mObjects.remove(object);
	}

	public void removeObjectsById(int id) {
		if (mObjects == null || mObjects.size() == 0) {
			return;
		}
		Iterator<GameObject> it = this.mObjects.iterator();
		while (it.hasNext()) {
			GameObject tempObject = it.next();
			if (tempObject.id == id) {
				it.remove();
			}
		}
	}
	
	public void cleanAll() {
		this.mObjects.clear();
	}
}
