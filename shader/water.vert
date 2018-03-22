#version 330 core

in vec3 position;

out vec4 clipSpace;
out vec2 textureCoords;
out vec3 toCameraVector;
out vec3 fromLightVectors[4];

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 cameraPosition;
uniform int tilingCount; // Total tiles: tilingCount * tilingCount.
uniform vec3 lightPositions[4];

void main (void) {

	vec4 worldPosition = modelMatrix * vec4(position.xyz, 1.0);
	
	clipSpace = projectionMatrix * viewMatrix * worldPosition;
	gl_Position = clipSpace;
	
	textureCoords = vec2(position.x / 2.0 + 0.5, position.z / 2.0 + 0.5) * tilingCount;
	// The texture coordinate range is [0:1],
	// Here we need a normalized position range, that is [-1:1].
	
	toCameraVector = cameraPosition - worldPosition.xyz;
	
	for (int i = 0; i < 4; i++) {
		fromLightVectors[i] = worldPosition.xyz - lightPositions[i];
	}
}
