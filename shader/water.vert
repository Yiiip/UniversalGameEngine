#version 330 core

in vec3 position;

out vec4 clipSpace;
out vec2 textureCoords;

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform int tilingCount; // Total tiles: tilingCount * tilingCount.

void main (void) {

	vec4 worldPosition = modelMatrix * vec4(position.xyz, 1.0);
	
	clipSpace = projectionMatrix * viewMatrix * worldPosition;
	gl_Position = clipSpace;
	
	textureCoords = vec2(position.x / 2.0 + 0.5, position.z / 2.0 + 0.5) * tilingCount;
	// The texture coordinate range is [0:1],
	// Here we need a normalized position range, that is [-1:1].
}
