#version 330 core

in vec2 pass_tc;
in vec3 pass_position;

out vec4 out_color;

uniform sampler2D textureSampler;
uniform int pipeDirection;

void main (void) {
	vec2 newTextureCoord = vec2(pass_tc.x, pass_tc.y);
	if (pipeDirection == 1) {
		newTextureCoord.y = 1 - newTextureCoord.y;
	}

	out_color = texture(textureSampler, newTextureCoord);
	if (out_color.a < 1.0) { discard; }
}
