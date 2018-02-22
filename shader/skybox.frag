#version 330 core

in vec3 pass_tc;

out vec4 out_color;

uniform samplerCube textureCubeMap;
uniform vec3 fogColor;

const float lowerLimit = -30.0;
const float upperLimit = 50.0;

void main (void) {
	vec4 finalColor = texture(textureCubeMap, pass_tc);

	float mixFoctor = (pass_tc.y - lowerLimit) / (upperLimit - lowerLimit);
	mixFoctor = clamp(mixFoctor, 0.0, 1.0);
	out_color = mix(vec4(fogColor, 1.0), finalColor, mixFoctor);
}
