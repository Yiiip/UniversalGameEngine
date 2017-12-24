#version 330 core

in vec2 pass_tc;
in vec3 pass_position;

out vec4 out_color;

uniform sampler2D textureSampler;

void main (void) {
	out_color = vec4(1.0, 1.0, 1.0, 1.0) * texture(textureSampler, pass_tc);
}
