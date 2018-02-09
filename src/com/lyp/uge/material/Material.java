package com.lyp.uge.material;

/**
 * 贴图材质相关的属性
 * 
 * @author LYP
 */
public class Material {

	protected float shineDamper = 1.0f; // 光照亮度衰减率
	protected float reflectivity = 0.0f; // 光照反射率

	protected float ambientLightness = 0.25f; // 环境光强度，默认给予一定亮度

	protected boolean hasTransparency = false; // 是否有透明通道
	protected boolean useFakeLighting = false; // 是否使用假光替代原有法线（即让法线竖直向上(0,1,0)）

	protected boolean foggy = false; // 迷雾效果，默认关闭
	public static final float FOGGY_DENSITY_NULL = 0.0f; // 空值
	public static final float FOGGY_GRADIENT_NULL = 1.0f; // 空值
	protected float foggyDensity = FOGGY_DENSITY_NULL; // 雾气密度
	protected float foggyGradient = FOGGY_GRADIENT_NULL; // 雾气渐变强度

	public Material setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
		return this;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public Material setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
		return this;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public Material setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
		return this;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public Material setUseFakeLighting(boolean useFakeLighting) {
		this.useFakeLighting = useFakeLighting;
		return this;
	}

	public boolean isUseFakeLighting() {
		return useFakeLighting;
	}

	public Material setAmbientLightness(float ambientLightness) {
		this.ambientLightness = ambientLightness;
		return this;
	}

	public float getAmbientLightness() {
		return ambientLightness;
	}
	
	public Material setFoggy(boolean foggy) {
		this.foggy = foggy;
		return this;
	}

	public boolean isFoggy() {
		return foggy;
	}

	public Material setFoggyDensity(float foggyDensity) {
		this.foggyDensity = foggyDensity;
		return this;
	}
	
	public float getFoggyDensity() {
		return foggyDensity;
	}

	public Material setFoggyGradient(float foggyGradient) {
		this.foggyGradient = foggyGradient;
		return this;
	}
	
	public float getFoggyGradient() {
		return foggyGradient;
	}
}
