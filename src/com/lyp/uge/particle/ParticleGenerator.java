package com.lyp.uge.particle;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.game.GameApplication;
import com.lyp.uge.texture.ParticleTexture;

public class ParticleGenerator {

	private float pps; //particle per second
	private float speed;
	private float gravityFactor;
	private float life;
	private ParticleTexture mParticleTexture;
	
	private Random random = new Random();

	public ParticleGenerator(float pps, float speed, float gravityFactor, float life, ParticleTexture texture) {
		this.pps = pps;
		this.speed = speed;
		this.gravityFactor = gravityFactor;
		this.life = life;
		this.mParticleTexture = texture;
	}

	public void generateParticles(Vector3f generatePosition) {
		float fps = GameApplication.getFPS() == 0.0f ? 60.0f : GameApplication.getFPS();
		int count = (int) Math.floor(pps / fps); //即(个/秒) * (秒/帧) = (个/帧)
		float particlesToCreate = pps / fps;
		float partialParticle = particlesToCreate % 1;
		for (int i = 0; i < count; i++) {
			emitParticle(generatePosition);
		}
		if (Math.random() < partialParticle) {
			emitParticle(generatePosition);
		}
	}

	private void emitParticle(Vector3f center) {
		float dirX = (float) Math.random() * 2f - 1f;
		float dirZ = (float) Math.random() * 2f - 1f;
		Vector3f velocity = new Vector3f(dirX, 1.0f, dirZ);
		velocity.normalise();
		velocity.scale(speed);
		new Particle(mParticleTexture, new Vector3f(center), velocity, gravityFactor, life, random.nextInt(90), random.nextFloat() * 2.0f + 0.4f);
	}
}
