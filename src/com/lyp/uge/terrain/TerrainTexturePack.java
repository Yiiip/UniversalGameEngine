package com.lyp.uge.terrain;

import com.lyp.uge.texture.Texture;

public class TerrainTexturePack {

	private Texture bgTexture;
	private Texture rTexture;
	private Texture gTexture;
	private Texture bTexture;
	
	public TerrainTexturePack(Texture bgTexture, Texture rTexture, Texture gTexture, Texture bTexture) {
		this.bgTexture = bgTexture;
		this.rTexture = rTexture;
		this.gTexture = gTexture;
		this.bTexture = bTexture;
	}
	public Texture getBgTexture() {
		return bgTexture;
	}
	public Texture getRTexture() {
		return rTexture;
	}
	public Texture getGTexture() {
		return gTexture;
	}
	public Texture getBTexture() {
		return bTexture;
	}
}
