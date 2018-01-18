#version 330 core

in vec2 pass_tc;
in vec3 surface_normal;
in vec3 to_light_vectors[4];
in vec3 to_camera_vector;
in float visibility;

out vec4 out_color;

uniform sampler2D textureSampler;
uniform vec3 lightColors[4];
uniform float shineDamper;
uniform float reflectivity;
uniform float ambientLightness;
uniform vec3 skyColor;

void main (void) {

	vec3 unitNormal = normalize(surface_normal);
	vec3 unitVectorToCamera = normalize(to_camera_vector);

	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);

	for (int i = 0; i < 4; i++) {
		vec3 unitLightVector = normalize(to_light_vectors[i]);
		float nDotl = dot(unitNormal, unitLightVector);
		float brightness = max(nDotl, 0.0);

		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
		float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
		specularFactor = max(specularFactor, 0.0);
		float dampedFactor = pow(specularFactor, shineDamper);
		totalSpecular += dampedFactor * reflectivity * lightColors[i];

		totalDiffuse += brightness * lightColors[i];
	}
	totalDiffuse = max(totalDiffuse, ambientLightness);

	vec4 textureColor = texture(textureSampler, pass_tc);
	if (textureColor.a < 0.5) { discard; }

	out_color = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
	out_color = mix(vec4(skyColor, 1.0), out_color, visibility);
}
