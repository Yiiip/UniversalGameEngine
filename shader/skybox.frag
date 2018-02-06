#version 330 core

in vec3 pass_tc;

out vec4 out_color;

uniform samplerCube textureCubeMap;

void main (void) {
	out_color = texture(textureCubeMap, pass_tc);
}
