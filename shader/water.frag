#version 330 core

in vec4 clipSpace;
in vec2 textureCoords;
in vec3 toCameraVector;
in vec3 fromLightVectors[4];

out vec4 out_color;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;
uniform sampler2D dudvMap;
uniform sampler2D normalMap;
uniform float moveFactor;
uniform vec3 lightColors[4];
uniform vec3 lightAttenuation[4];
uniform float shineDamper;
uniform float reflectivity;
uniform float ambientLightness;

const vec4 WATER_COLOR = vec4(150.0/255.0, 240.0/255.0, 255.0/255.0, 1.0);
const float WATER_COLOR_STRENGTH = 0.15;
const float WAVE_STRENGTH = 0.02;
const float REFLECT_STRENGTH = 0.5; // The more bigger, the more reflection. [0.0, 1.0] -> [1.0, +Infinity]

void main (void) {

	vec2 normalizedDeviceCoords = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
	vec2 reflectTexCoords = vec2(normalizedDeviceCoords.x, -normalizedDeviceCoords.y);
	vec2 refractTexCoords = vec2(normalizedDeviceCoords.x, normalizedDeviceCoords.y);

	// @Deprecated solution
	// vec2 dudvColorOffset1 = texture(dudvMap, vec2(textureCoords.x + moveFactor, textureCoords.y)).rg * 2.0 - 1.0; // Change dudv color range [0:1] -> [-1:1].
	// vec2 dudvColorOffset2 = texture(dudvMap, vec2(-textureCoords.x + moveFactor, textureCoords.y + moveFactor)).rg * 2.0 - 1.0;
	// vec2 distortion1 = dudvColorOffset1 * WAVE_STRENGTH;
	// vec2 distortion2 = dudvColorOffset2 * WAVE_STRENGTH;
	// vec2 totalDistortion = distortion1 + distortion2;
	
	// @Current solution
	vec2 dudvColorOffset = texture(dudvMap, vec2(textureCoords.x + moveFactor, textureCoords.y)).rg * 0.1;
	dudvColorOffset = textureCoords + vec2(dudvColorOffset.x, dudvColorOffset.y + moveFactor);
	vec2 dudvColor = (texture(dudvMap, dudvColorOffset).rg) * 2.0 - 1.0; // Change dudv color range [0:1] -> [-1:1].
	vec2 totalDistortion = dudvColor * WAVE_STRENGTH;

	refractTexCoords += totalDistortion;
	refractTexCoords = clamp(refractTexCoords, 0.001, 0.999); // Avoid glitch issues at the bottom of screen.
	
	reflectTexCoords += totalDistortion;
	reflectTexCoords.x = clamp(reflectTexCoords.x, 0.001, 0.999); // Avoid glitch issues.
	reflectTexCoords.y = clamp(reflectTexCoords.y, -0.999, -0.001);

	vec4 reflectColor = texture(reflectionTexture, reflectTexCoords);
	vec4 refractColor = texture(refractionTexture, refractTexCoords);
	
	vec4 normalMapColor = texture(normalMap, dudvColorOffset);
	vec3 normal = vec3(normalMapColor.r * 2.0 -1.0, normalMapColor.b * 100.0, normalMapColor.g * 2.0 - 1.0);
	vec3 unitNormal = normalize(normal);
	
	vec3 unitVectorToCamera = normalize(toCameraVector);
	float refractiveFactor = dot(unitVectorToCamera, unitNormal);
	refractiveFactor = pow(refractiveFactor, REFLECT_STRENGTH);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	for (int i = 0; i < 4; i++) {
		float distanceFromLight = length(fromLightVectors[i]);
		float attenuationFactor = lightAttenuation[i].x + lightAttenuation[i].y * distanceFromLight + lightAttenuation[i].z * distanceFromLight * distanceFromLight;
		vec3 unitFromLightVector = normalize(fromLightVectors[i]);
		float brightness = max(dot(unitNormal, unitFromLightVector), 0.0);
		
		vec3 reflectedLightDirection = reflect(unitFromLightVector, unitNormal);
		float specular = max(dot(reflectedLightDirection, unitVectorToCamera), 0.0);
		specular = pow(specular, shineDamper);
		vec3 specularHighlights = lightColors[i] * specular * reflectivity;
		totalSpecular += specularHighlights / attenuationFactor;
		
		vec3 diffuse = brightness * lightColors[i];
		totalDiffuse += diffuse / attenuationFactor;
	}
	totalDiffuse = max(totalDiffuse, ambientLightness);

	out_color = mix( mix(reflectColor, refractColor, refractiveFactor), WATER_COLOR, WATER_COLOR_STRENGTH);
	out_color = vec4(totalDiffuse, 1.0) * out_color + vec4(totalSpecular, 1.0);

	// For debug:
	// out_color = vec4(dudvColor.rg, 0.0, 1.0);	// debug dudvMap.
	// out_color = normalMapColor;					// debug normalMap.
}
