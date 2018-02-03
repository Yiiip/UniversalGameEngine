package com.lyp.uge.gameObject.light;

import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.renderEngine.Loader;

public class PointLight extends Light {

	// 光的衰减因子，lightAttenuation=(attenu1)+(attenu2*d)+(attenu3*d*d)，d = distance from light.
	protected Vector3f lightAttenuation = new Vector3f(1.f, 0.f, 0.f); 

	public PointLight(Vector3f position, Vector3f color, Loader loader) {
		super(position, color, loader);
	}

	public PointLight(Vector3f position, Vector3f color, Loader loader, boolean renderSelf) {
		super(position, color, loader, renderSelf);
	}

	public PointLight(Vector3f position, Vector3f color, Loader loader, Vector3f attenuation) {
		super(position, color, loader);
		this.lightAttenuation = attenuation;
	}

	public PointLight(Vector3f position, Vector3f color, Loader loader, Vector3f attenuation, boolean renderSelf) {
		super(position, color, loader);
		this.lightAttenuation = attenuation;
		this.renderSelf = renderSelf;
	}

	public Vector3f getLightAttenuation() {
		return lightAttenuation;
	}

	public void setLightAttenuation(Vector3f lightAttenuation) {
		this.lightAttenuation = lightAttenuation;
	}
}
