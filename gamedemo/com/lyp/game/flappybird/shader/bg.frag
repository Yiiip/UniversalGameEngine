#version 330 core

in vec2 pass_tc;
in vec3 pass_position;

out vec4 out_color;

uniform sampler2D textureSampler;
uniform vec3 birdPos;

void main (void) {
	out_color = texture(textureSampler, pass_tc);
	float radius = length(birdPos.xy - pass_position.xy);
	out_color *= 1.0 / (radius + 0.55) - 0.38;
//	out_color *= 0.8 / (radius + 0.5) + 0;
}
