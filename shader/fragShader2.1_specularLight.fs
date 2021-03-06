#version 330 core

in vec2 pass_tc;
in vec3 surface_normal;
in vec3 to_light_vector;
in vec3 to_camera_vector;

out vec4 out_color;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform float ambientLightness;

void main (void) {

	vec3 unitNormal = normalize(surface_normal);
	vec3 unitLightVector = normalize(to_light_vector);

	float nDotl = dot(unitNormal, unitLightVector);
	float brightness = max(nDotl, ambientLightness); //if second parameter >0 means ambient light(环境光)
	vec3 diffuse = brightness * lightColor;

	vec3 unitVectorToCamera = normalize(to_camera_vector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
	float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
	specularFactor = max(specularFactor, 0);
	float dampedFactor = pow(specularFactor, shineDamper);
	vec3 finalSpecular = dampedFactor * reflectivity * lightColor;

	vec4 textureColor = texture(textureSampler, pass_tc);
	if (textureColor.a < 0.5) { discard; } //discard alpha channel

	out_color = vec4(diffuse, 1) * textureColor
		+ vec4(finalSpecular, 1);
}