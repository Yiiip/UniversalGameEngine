#version 330 core

in vec2 pass_tc;

out vec4 out_color;

uniform sampler2D textureSampler;

void main (void) {
	out_color = texture(textureSampler, pass_tc);
	if (out_color.a < 0.5) { discard; }
}
