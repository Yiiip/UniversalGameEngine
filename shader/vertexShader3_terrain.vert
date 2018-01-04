#version 330 core

in vec3 position;
in vec2 tc;
in vec3 normal;

out vec2 pass_tc;
out vec3 surface_normal;
out vec3 to_light_vector;
out vec3 to_camera_vector;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPos;
uniform float fogDensity; //0.0 remove fog
uniform float fogGradient; //1.0 remove fog

void main (void) {
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	
	//the distance of the vertex from main camera
	vec4 positionRelaticeToCam = viewMatrix * worldPosition;
	
	gl_Position = projectionMatrix * positionRelaticeToCam;
	pass_tc = tc;
	
	surface_normal = (transformationMatrix * vec4(normal, 0)).xyz;
	to_light_vector = lightPos - worldPosition.xyz;
	to_camera_vector = (inverse(viewMatrix) * vec4(0, 0, 0, 1)).xyz - worldPosition.xyz;

	//Foggy factors
	float distance = length(positionRelaticeToCam.xyz);
	visibility = exp(-pow(fogDensity * distance, fogGradient));
	visibility = clamp(visibility, 0.0, 1.0);
}
