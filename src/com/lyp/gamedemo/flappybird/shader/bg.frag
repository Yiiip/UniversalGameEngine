#version 330 core

in vec2 pass_tc;
in vec3 pass_position;

out vec4 out_color;

uniform sampler2D textureSampler;
uniform vec3 birdPos;

void main (void) {
	out_color = texture(textureSampler, pass_tc);
	out_color *= 3.0 / (length(birdPos.xy - pass_position.xy) + 3.0) + 0.25;
}
