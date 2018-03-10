#version 330 core

varying vec2 pass_tc;
varying vec3 pass_position;

uniform sampler2D textureSampler; //sampler2D is vec4(r, g, b, a)

void main (void) {

	vec3 color = texture2D(textureSampler, pass_tc).rgb;

	gl_FragColor = vec4(color.rgb, 1.0f);
}
