package com.lyp.uge.particle;

import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.gameObject.GameObject;
import com.lyp.uge.prefab.TextureModel;
import com.lyp.uge.renderEngine.ParticleRenderer;
import com.lyp.uge.texture.ParticleTexture;

public class Particle extends GameObject {
	
	public static float PARTICLE_GRAVITY = -50.0f;
	
	protected Vector3f velocity;
	protected float gravityFactor;
	protected float life;
	protected float elapsedTime = 0.0f;
	
	protected ParticleTexture mTexture;
	
	public Particle(ParticleTexture texture, Vector3f position, Vector3f velocity, float gravityFactor, float life, float rotation, float scale) {
		this.position = position;
		this.velocity = velocity;
		this.gravityFactor = gravityFactor;
		this.life = life;
		this.mTexture = texture;
		super.rotateZ = rotation;
		super.scale = scale;
		super.model = new TextureModel(ParticleRenderer.getQuadModel(), mTexture);
		addToManager();
	}
	
	private void addToManager() {
		ParticlesManager.addParticle(this);
	}
	
	@Override
	public void update() {
		// Gravity
		velocity.y += PARTICLE_GRAVITY * gravityFactor * (1.0f / 60.0f); //TODO current frame
		
		// Moving
		Vector3f change = new Vector3f(velocity);
		change.scale(1.0f / 60.0f);
		Vector3f.add(change, position, position);
		
		elapsedTime += (1.0f / 60.0f);
	}
	
	/**
	 * Call this method to update rather than update().
	 * @return Whether the particle is still alive.
	 */
	public boolean updateParticle() {
		update();
		return (elapsedTime < life);
	}

	@Override
	public void destory() {
	}
	
	public float getRotation() {
		return super.getRotateZ();
	}
	
	public ParticleTexture getTexture() {
		return mTexture;
	}
}
