#version 330 core

in vec3 position;
in vec2 tc;
in vec3 normal;

out vec2 pass_tc;
out vec3 surface_normal;
out vec3 to_light_vector;

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPos;

void main (void) {
	vec4 worldPosition = modelMatrix * vec4(position, 1.0);
	
	gl_Position = projectionMatrix * viewMatrix * worldPosition;
	pass_tc = tc;
	
	surface_normal = (modelMatrix * vec4(normal, 0)).xyz;
	to_light_vector = lightPos - worldPosition.xyz;
}