#version 330 core

in vec4 clipSpace;
in vec2 textureCoords;
in vec3 toCameraVector;
in vec3 waterSurfaceNormal;

out vec4 out_color;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;
uniform float moveFactor;

const vec4 WATER_COLOR = vec4(150.0/255.0, 240.0/255.0, 255.0/255.0, 1.0);
const float WATER_COLOR_STRENGTH = 0.15;
const float WAVE_STRENGTH = 0.02;
const float REFLECT_STRENGTH = 0.8; // The more bigger, the more reflection. [0.0, 1.0] -> [1.0, +Infinity]

void main (void) {

	vec2 normalizedDeviceCoords = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
	vec2 reflectTexCoords = vec2(normalizedDeviceCoords.x, -normalizedDeviceCoords.y);
	vec2 refractTexCoords = vec2(normalizedDeviceCoords.x, normalizedDeviceCoords.y);

	vec2 dudvColorOffset1 = texture(dudvMap, vec2(textureCoords.x + moveFactor, textureCoords.y)).rg * 2.0 - 1.0; // Change dudv color range [0:1] -> [-1:1].
	vec2 dudvColorOffset2 = texture(dudvMap, vec2(-textureCoords.x + moveFactor, textureCoords.y + moveFactor)).rg * 2.0 - 1.0;
	vec2 distortion1 = dudvColorOffset1 * WAVE_STRENGTH;
	vec2 distortion2 = dudvColorOffset2 * WAVE_STRENGTH;
	vec2 totalDistortion = distortion1 + distortion2;

	refractTexCoords += totalDistortion;
	refractTexCoords = clamp(refractTexCoords, 0.001, 0.999); // Avoid glitch issues at the bottom of screen.
	
	reflectTexCoords += distortion1;
	reflectTexCoords.x = clamp(reflectTexCoords.x, 0.001, 0.999); // Avoid glitch issues.
	reflectTexCoords.y = clamp(reflectTexCoords.y, -0.999, -0.001);

	vec4 reflectColor = texture(reflectionTexture, reflectTexCoords);
	vec4 refractColor = texture(refractionTexture, refractTexCoords);
	
	vec3 norToCameraVector = normalize(toCameraVector);
	float refractiveFactor = dot(norToCameraVector, waterSurfaceNormal);
	refractiveFactor = pow(refractiveFactor, REFLECT_STRENGTH);

	//out_color = vec4(dudvColorOffset1.rg, 0.0, 1.0); // for debug
	out_color = mix( mix(reflectColor, refractColor, refractiveFactor), WATER_COLOR, WATER_COLOR_STRENGTH);
}
