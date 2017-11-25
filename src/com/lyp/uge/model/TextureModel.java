package com.lyp.uge.model;

import com.lyp.uge.texture.Texture;

public class TextureModel {

	private RawModel rawModel;
	private Texture texture;
	
	public TextureModel(RawModel rawModel, Texture texture) {
		this.rawModel = rawModel;
		this.texture = texture;
	}
	
	public RawModel getRawModel() {
		return rawModel;
	}
	
	public Texture getTexture() {
		return texture;
	}
}
