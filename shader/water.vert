#version 330 core

in vec3 position;

varying vec4 clipSpace;

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main (void) {

	vec4 worldPosition = modelMatrix * vec4(position.xyz, 1.0);
	
	clipSpace = projectionMatrix * viewMatrix * worldPosition;
	gl_Position = clipSpace;
}
