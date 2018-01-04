#version 330 core

in vec2 pass_tc;
in vec3 surface_normal;
in vec3 to_light_vector;
in vec3 to_camera_vector;
in float visibility;

out vec4 out_color;

uniform sampler2D bgTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;

uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform float ambientLightness;
uniform vec3 skyColor;

void main (void) {

	vec4 blendMapColor = texture(blendMap, pass_tc);//blendMap not need to tiled

	float bgTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);
	vec2 tiledCoords = pass_tc * 45; //tiled and repeat texture
	vec4 bgTextureColor = texture(bgTexture, tiledCoords) * bgTextureAmount;
	vec4 rTextureColor = texture(rTexture, tiledCoords) * blendMapColor.r;
	vec4 gTextureColor = texture(gTexture, tiledCoords) * blendMapColor.g;
	vec4 bTextureColor = texture(bTexture, tiledCoords) * blendMapColor.b;

	vec4 totalColor = bgTextureColor + rTextureColor + gTextureColor + bTextureColor;


	vec3 unitNormal = normalize(surface_normal);
	vec3 unitLightVector = normalize(to_light_vector);

	float nDotl = dot(unitNormal, unitLightVector);
	float brightness = max(nDotl, ambientLightness);
	vec3 diffuse = brightness * lightColor;

	vec3 unitVectorToCamera = normalize(to_camera_vector);
	vec3 lightDirection = -unitLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
	float specularFactor = dot(reflectedLightDirection, unitVectorToCamera);
	specularFactor = max(specularFactor, 0.0);
	float dampedFactor = pow(specularFactor, shineDamper);
	vec3 finalSpecular = dampedFactor * reflectivity * lightColor;

	vec4 textureColor = totalColor;
	out_color = vec4(diffuse, 1.0) * textureColor + vec4(finalSpecular, 1.0);
	out_color = mix(vec4(skyColor, 1.0), out_color, visibility);
}
