#version 330 core

varying vec2 textureCoords;

out vec4 out_color;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;

void main (void) {

	vec4 reflectColor = texture(reflectionTexture, textureCoords);
	vec4 refractColor = texture(refractionTexture, textureCoords);
	// vec4 waterColor = vec4(0.1, 0.4, 0.8, 1.0);

	out_color = mix(reflectColor, refractColor, 0.5);
}
