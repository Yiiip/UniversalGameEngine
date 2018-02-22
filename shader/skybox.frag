#version 330 core

in vec3 pass_tc;

out vec4 out_color;

// Simulate day&night cycle using multiple textures.
uniform samplerCube textureCubeMap1;
uniform samplerCube textureCubeMap2;

uniform float blendFactor;
uniform vec3 fogColor;

const float lowerLimit = -30.0;
const float upperLimit = 50.0;

void main (void) {
	vec4 skyTexture1 = texture(textureCubeMap1, pass_tc);
	vec4 skyTexture2 = texture(textureCubeMap2, pass_tc);
	vec4 finalColor = mix(skyTexture1, skyTexture2, blendFactor);

	float mixFoctor = (pass_tc.y - lowerLimit) / (upperLimit - lowerLimit);
	mixFoctor = clamp(mixFoctor, 0.0, 1.0);
	out_color = mix(vec4(fogColor, 1.0), finalColor, mixFoctor);
}
