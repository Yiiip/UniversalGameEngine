#version 330 core

in vec3 position;
in vec2 tc;

out vec2 pass_tc;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;

void main (void) {
	vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
	
	gl_Position = projectionMatrix * viewMatrix * worldPosition;
	pass_tc = tc;
}