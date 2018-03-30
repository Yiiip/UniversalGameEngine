package com.lyp.uge.particle;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.gameObject.GameObject;
import com.lyp.uge.gameObject.camera.Camera;
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
	
	protected Vector2f currentSpriteOffset = new Vector2f();
	protected Vector2f nextSpriteOffset = new Vector2f();
	protected float blend;
	
	protected float distanceFromCamera;
	
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
		
		updateSpritesTextureCoord();
		
		elapsedTime += (1.0f / 60.0f);
	}
	
	/**
	 * Call this method to update rather than update().
	 * @return Whether the particle is still alive.
	 */
	public boolean updateParticle(Camera camera) {
		update();
		distanceFromCamera = Vector3f.sub(camera.getPosition(), position, null).lengthSquared(); // Just using lengthSquared() to estimate whether the value is big or small.
		return (elapsedTime < life);
	}
	
	protected void updateSpritesTextureCoord() {
		float lifeFactor =  elapsedTime / life;
		int spritesCount = mTexture.getTileRows() * mTexture.getTileRows();
		float progress = lifeFactor * spritesCount;
		int currentIndex = (int) Math.floor(progress);
		int nextIndex = (currentIndex < spritesCount-1) ? (currentIndex+1) : currentIndex;
		this.blend = progress % 1;
		setSpriteOffset(mTexture, currentSpriteOffset, currentIndex);
		setSpriteOffset(mTexture, nextSpriteOffset, nextIndex);
	}
	
	protected void setSpriteOffset(ParticleTexture spritesTexture, Vector2f offset, int index) {
		int column = index % spritesTexture.getTileRows();
		int row = index / spritesTexture.getTileRows();
		offset.x = (float) column / spritesTexture.getTileRows();
		offset.y = (float) row / spritesTexture.getTileRows();
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

	public Vector2f getCurrentSpriteOffset() {
		return currentSpriteOffset;
	}

	public Vector2f getNextSpriteOffset() {
		return nextSpriteOffset;
	}

	public float getBlend() {
		return blend;
	}
	
	public float getDistanceFromCamera() {
		return distanceFromCamera;
	}
}
