#version 330 core

in vec3 position;

out vec4 clipSpace;
out vec2 textureCoords;
out vec3 toCameraVector;
out vec3 fromLightVectors[4];
out float visibility;

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 cameraPosition;
uniform int tilingCount; // Total tiles: tilingCount * tilingCount.
uniform vec3 lightPositions[4];
uniform float fogDensity; //0.0 remove fog
uniform float fogGradient; //1.0 remove fog

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

	//Foggy factors
	vec4 positionRelativeToCam = viewMatrix * worldPosition; //the distance of the vertex from main camera
	float distance = length(positionRelativeToCam.xyz);
	visibility = exp(-pow(fogDensity * distance, fogGradient));
	visibility = clamp(visibility, 0.0, 1.0);
}
