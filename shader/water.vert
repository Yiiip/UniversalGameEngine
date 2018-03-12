#version 330 core

in vec3 position;

varying vec2 textureCoords;

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main (void) {

	vec4 worldPosition = modelMatrix * vec4(position.xyz, 1.0);
	
	gl_Position = projectionMatrix * viewMatrix * worldPosition;
	
	textureCoords = vec2(position.x / 2.0 + 0.5, position.z / 2.0 + 0.5); // normalized position range is [-1:1]
}
