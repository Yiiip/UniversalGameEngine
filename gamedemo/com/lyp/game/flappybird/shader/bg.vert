#version 330 core

in vec3 position;
in vec2 tc;

out vec2 pass_tc;
out vec3 pass_position;

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main (void) {
	vec4 worldPosition = modelMatrix * vec4(position, 1.0);
	
	gl_Position = projectionMatrix * viewMatrix * worldPosition;
	pass_tc = tc;
	pass_position = vec3(worldPosition);
}
