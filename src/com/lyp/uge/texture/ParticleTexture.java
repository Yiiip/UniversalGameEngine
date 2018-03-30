package com.lyp.uge.texture;

public class ParticleTexture extends Texture {
	
	protected int tileRows;
	
	public ParticleTexture(String textureFile, int tileRows) {
		super(textureFile);
		this.tileRows = tileRows;
	}
	
	public ParticleTexture(String textureFile) {
		this(textureFile, 1);
	}

	public int getTileRows() {
		return tileRows;
	}
}
