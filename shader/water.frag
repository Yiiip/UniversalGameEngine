#version 330 core

varying vec4 clipSpace;

out vec4 out_color;

uniform sampler2D reflectionTexture;
uniform sampler2D refractionTexture;

void main (void) {

	vec2 normalizedDeviceCoords = (clipSpace.xy / clipSpace.w) / 2.0 + 0.5;
	vec2 reflectTexCoords = vec2(normalizedDeviceCoords.x, -normalizedDeviceCoords.y);
	vec2 refractTexCoords = vec2(normalizedDeviceCoords.x, normalizedDeviceCoords.y);

	vec4 reflectColor = texture(reflectionTexture, reflectTexCoords);
	vec4 refractColor = texture(refractionTexture, refractTexCoords);
	
	vec4 waterColor = vec4(147.0/255.0, 235.0/255.0, 255.0/255.0, 1.0);

	out_color = mix( mix(reflectColor, refractColor, 0.5), waterColor, 0.2);
}
