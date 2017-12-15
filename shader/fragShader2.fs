#version 330 core

in vec2 pass_tc;
in vec3 surface_normal;
in vec3 to_light_vector;

out vec4 out_color;

uniform sampler2D textureSampler;
uniform vec3 lightColor;

void main (void) {

	vec3 unitNormal = normalize(surface_normal);
	vec3 unitLightVector = normalize(to_light_vector);
	float nDotl = dot(unitNormal, unitLightVector);
	float brightness = max(nDotl, 0);
	vec3 diffuse = brightness * lightColor;

	out_color = vec4(diffuse, 1) * texture(textureSampler, pass_tc);
}