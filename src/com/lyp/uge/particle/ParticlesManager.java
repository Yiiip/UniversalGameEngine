package com.lyp.uge.particle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.lwjgl.util.vector.Matrix4f;

import com.lyp.uge.gameObject.camera.Camera;
import com.lyp.uge.renderEngine.Loader;
import com.lyp.uge.renderEngine.ParticleRenderer;

public class ParticlesManager {

	private static List<Particle> mParticles = new ArrayList<Particle>();
	private static ParticleRenderer mRenderer;
	
	public static void init(Loader loader, Matrix4f projectionMatrix) {
		if (mRenderer == null) {
			mRenderer = new ParticleRenderer(loader, projectionMatrix);
		}
		mParticles.clear();
	}
	
	public static void addParticle(Particle particle) {
		mParticles.add(particle);
	}
	
	public static void update() {
		Iterator<Particle> iterator = mParticles.iterator();
		while (iterator.hasNext()) {
			Particle particle = (Particle) iterator.next();
			if (!particle.updateParticle()) {
				iterator.remove();
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
