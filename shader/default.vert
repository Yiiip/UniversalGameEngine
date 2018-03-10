#version 330 core

attribute vec3 position;
attribute vec2 tc;

varying vec2 pass_tc;
varying vec3 pass_position;

uniform mat4 modelMatrix;
uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main (void) {

	vec4 worldPosition = modelMatrix * vec4(position, 1.0);
	gl_Position = projectionMatrix * viewMatrix * worldPosition;

	pass_tc = tc;
	pass_position = position;
}
