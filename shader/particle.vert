#version 330

in vec2 position;
in mat4 modelViewMatrix;
in vec4 spriteOffsets; // spriteOffset_current->xy, spriteOffset_next->zw
in float blend;

out vec2 textureCoords_current;
out vec2 textureCoords_next;
out float blendFactor;

uniform mat4 projectionMatrix;
uniform int rows;

void main (void) {

	vec2 textureCoords = position + vec2(0.5, 0.5);
	textureCoords.y = 1.0 - textureCoords.y;
	textureCoords /= rows;
	textureCoords_current = textureCoords + spriteOffsets.xy;
	textureCoords_next = textureCoords + spriteOffsets.zw;
	blendFactor = blend;

	gl_Position = projectionMatrix * modelViewMatrix * vec4(position.x, position.y, 0.0, 1.0);
}
