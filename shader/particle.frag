#version 330

in vec2 textureCoords_current;
in vec2 textureCoords_next;
in float blendFactor;

out vec4 out_color;

uniform sampler2D particleTexture;

void main (void) {

	vec4 colorCurrentSprite = texture(particleTexture, textureCoords_current);
	vec4 colorNextSprite = texture(particleTexture, textureCoords_next);
	
	out_color = mix(colorCurrentSprite, colorNextSprite, blendFactor);
}
