#version 330 core

varying vec2 textureCoords;

out vec4 out_color;

uniform sampler2D reflectionTexture;

void main (void) {

	vec4 reflectColor = texture(reflectionTexture, textureCoords);
	vec4 waterColor = vec4(0.1, 0.4, 0.8, 1.0);

	out_color = mix(reflectColor, waterColor, 0.1);
}
