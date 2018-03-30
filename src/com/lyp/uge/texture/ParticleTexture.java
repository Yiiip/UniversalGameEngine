package com.lyp.uge.texture;

public class ParticleTexture extends Texture {

	protected int tileRows;
	protected boolean subtransparentMode = false; // Used for rendering mode.

	public ParticleTexture(String textureFile) {
		this(textureFile, 1);
	}

	public ParticleTexture(String textureFile, int tileRows) {
		super(textureFile);
		this.tileRows = tileRows;
	}

	public ParticleTexture(String textureFile, boolean subtransparentMode) {
		this(textureFile, 1);
		this.subtransparentMode = subtransparentMode;
	}

	public ParticleTexture(String textureFile, int tileRows, boolean subtransparentMode) {
		this(textureFile, tileRows);
		this.subtransparentMode = subtransparentMode;
	}

	public int getTileRows() {
		return tileRows;
	}

	public boolean useSubtransparentMode() {
		return subtransparentMode;
	}
}
