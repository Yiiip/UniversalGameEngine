package com.lyp.uge.particle;

import java.util.Random;

import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.math.MathTools;

public class ParticleGenerator {

	private float pps; //particle per second
	private float speed;
	private float gravityFactor;
	private float life;
	
	private Random random = new Random();

	public ParticleGenerator(float pps, float speed, float gravityFactor, float life) {
		this.pps = pps;
		this.speed = speed;
		this.gravityFactor = gravityFactor;
		this.life = life;
	}

	public void generateParticles(Vector3f generatePosition) {
		float particlesToCreate = pps * (1.0f / 60.0f);
		int count = (int) Math.floor(pps * (1.0f / 60.0f));
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
		Vector3f velocity = new Vector3f(dirX, 1, dirZ);
		velocity.normalise();
		velocity.scale(speed);
		new Particle(new Vector3f(center), velocity, gravityFactor, life, random.nextInt(90), MathTools.clamp(random.nextFloat() + 0.3f, 0.3f, 1.0f));
	}
}