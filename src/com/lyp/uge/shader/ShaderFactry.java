package com.lyp.uge.shader;

import java.util.HashMap;
import java.util.Map;

public class ShaderFactry {

	//Type of shader (material maybe)
	public static final int WITH_DEFAULT		= 0x000;
	public static final int WITH_SPECULAR_LIGHT	= 0x001;
	public static final int WITH_FOG			= 0x002;
	public static final int WITH_MULTI_LIGHTS	= 0x003;
	
	private static ShaderFactry mInstance;
	private Map<Integer, Shader> mCache;
	
	private ShaderFactry() {
		mCache = new HashMap<>();
	}

	public final static ShaderFactry instance() {
		if (mInstance == null) {
			mInstance = new ShaderFactry();
		}
		return mInstance;
	}
	
	public Shader make(int shaderType) {
		Shader shader = mCache.get(shaderType);
		if (shader != null) {
			return shader;
		}
		
		switch (shaderType) {
			case WITH_SPECULAR_LIGHT:
				shader = new SpecularLightShader();
				break;
			case WITH_FOG:
				shader = new FoggyShader();
				break;
			case WITH_MULTI_LIGHTS:
				shader = new MultiLightsShader();
				break;
			default:
				break;
		}
		mCache.put(shaderType, shader);
		return shader;
	}
}
