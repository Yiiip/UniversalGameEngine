package com.lyp.uge.prefab;

import com.lyp.uge.model.RawModel;
import com.lyp.uge.texture.Texture;

/**
 * This class represent a prefab.
 */
public class TextureModel {

	private RawModel rawModel = null;
	private Texture texture = null;
	
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
