package com.lyp.uge.particle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.util.vector.Matrix4f;

import com.lyp.uge.gameObject.camera.Camera;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.ParticleRenderer;
import com.lyp.uge.texture.ParticleTexture;

public class ParticlesManager {

	private static Map<ParticleTexture, List<Particle>> mParticles = new HashMap<>();
	private static ParticleRenderer mRenderer;
	
	public static void init(Loader loader, Matrix4f projectionMatrix) {
		if (mRenderer == null) {
			mRenderer = new ParticleRenderer(loader, projectionMatrix);
		}
		mParticles.clear();
	}
	
	public static void addParticle(Particle particle) {
		List<Particle> pList = mParticles.get(particle.getTexture());
		if (pList == null) {
			pList = new ArrayList<>();
			mParticles.put(particle.getTexture(), pList);
		}
		pList.add(particle);
	}
	
	public static void update() {
		Iterator<Entry<ParticleTexture, List<Particle>>> mapIterator = mParticles.entrySet().iterator();
		while (mapIterator.hasNext()) {
			List<Particle> pList = mapIterator.next().getValue();
			
			Iterator<Particle> iterator = pList.iterator();
			while (iterator.hasNext()) {
				Particle particle = (Particle) iterator.next();
				if (!particle.updateParticle()) {
					iterator.remove();
					
					if (pList.isEmpty()) {
						mapIterator.remove();
					}
				}
			}
		}
	}
	
	public static void render(Camera camera) {
		mRenderer.render(mParticles, camera);
	}
	
	public static void cleanUp() {
		mRenderer.cleanUp();
	}
}
