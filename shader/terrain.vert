#version 330 core

in vec3 position;
in vec2 tc;
in vec3 normal;

out vec2 pass_tc;
out vec3 surface_normal;
out vec3 to_light_vectors[4]; //多光源
out vec3 to_camera_vector;
out float visibility;

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPositions[4]; //多光源
uniform float fogDensity; //0.0 remove fog
uniform float fogGradient; //1.0 remove fog

void main (void) {
	vec4 worldPosition = modelMatrix * vec4(position, 1.0);
	
	//the distance of the vertex from main camera
	vec4 positionRelativeToCam = viewMatrix * worldPosition;
	
	gl_Position = projectionMatrix * positionRelativeToCam;
	pass_tc = tc;
	
	surface_normal = (modelMatrix * vec4(normal, 0)).xyz;
	for (int i = 0; i < 4; i++) {
		to_light_vectors[i] = lightPositions[i] - worldPosition.xyz;
	}
	to_camera_vector = (inverse(viewMatrix) * vec4(0, 0, 0, 1)).xyz - worldPosition.xyz;

	//Foggy factors
	float distance = length(positionRelativeToCam.xyz);
	visibility = exp(-pow(fogDensity * distance, fogGradient));
	visibility = clamp(visibility, 0.0, 1.0);
}
