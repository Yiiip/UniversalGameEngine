package com.lyp.uge.shader;

import java.util.List;

import org.lwjgl.util.vector.Vector3f;

import com.lyp.uge.gameObject.light.Light;
import com.lyp.uge.gameObject.light.PointLight;

public class MultiLightsShader extends FoggyShader {
	
	public static final int MAX_LIGHTS = 4; //允许的最多光源个数（越多性能越低）

	private static String MULTI_LIGHTS_VERTEX_FILE = "shader/multi-lights.vert";
	private static String MULTI_LIGHTS_FRAGMENT_FILE = "shader/multi-lights.frag";
	
	protected int uniform_lightPositions[];
	protected int uniform_lightColors[];
	protected int uniform_lightAttenuation[];
	
	public MultiLightsShader() {
		super(MULTI_LIGHTS_VERTEX_FILE, MULTI_LIGHTS_FRAGMENT_FILE);
	}
	
	public MultiLightsShader(String vertexFile, String fragmentFile) {
		super(vertexFile, fragmentFile);
	}
	
	@Override
	protected void getAllUniformLocations() {
		super.getAllUniformLocations();
		
		uniform_lightPositions = new int[MAX_LIGHTS];
		uniform_lightColors = new int[MAX_LIGHTS];
		uniform_lightAttenuation = new int[MAX_LIGHTS];
		
		for (int i = 0; i < MAX_LIGHTS; i++) {
			uniform_lightPositions[i] = super.getUniformLocation("lightPositions[" + i + "]");
			uniform_lightColors[i] = super.getUniformLocation("lightColors[" + i + "]");
			uniform_lightAttenuation[i] = super.getUniformLocation("lightAttenuation[" + i + "]");
		}
	}
	
	public void loadMultiLights(List<Light> lights) {
		if (lights == null || lights.isEmpty()) {
			return;
		}
		for (int i = 0; i < MAX_LIGHTS; i++) {
			if (i < lights.size() && lights.get(i).isActive()) {
				super.loadVector(uniform_lightPositions[i], lights.get(i).getPosition());
				super.loadVector(uniform_lightColors[i], lights.get(i).getColor());
				if (lights.get(i) instanceof PointLight) {
					super.loadVector(uniform_lightAttenuation[i], ((PointLight) lights.get(i)).getLightAttenuation());
				} else {
					super.loadVector(uniform_lightAttenuation[i], new Vector3f(1.0f, 0.0f, 0.0f));
				}
			} else {
				super.loadVector(uniform_lightPositions[i], new Vector3f(0.0f, 0.0f, 0.0f));
				super.loadVector(uniform_lightColors[i], new Vector3f(0.0f, 0.0f, 0.0f));
				super.loadVector(uniform_lightAttenuation[i], new Vector3f(1.0f, 0.0f, 0.0f));
			}
		}
	}
}
