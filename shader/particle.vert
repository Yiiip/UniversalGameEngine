#version 330

in vec2 position;

out vec2 textureCoords_current;
out vec2 textureCoords_next;
out float blendFactor;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;
uniform vec2 spriteOffset_current;
uniform vec2 spriteOffset_next;
uniform int rows;
uniform float blend;

void main (void) {

	vec2 textureCoords = position + vec2(0.5, 0.5);
	textureCoords.y = 1.0 - textureCoords.y;
	textureCoords /= rows;
	textureCoords_current = textureCoords + spriteOffset_current;
	textureCoords_next = textureCoords + spriteOffset_next;
	blendFactor = blend;

	gl_Position = projectionMatrix * modelViewMatrix * vec4(position.x, position.y, 0.0, 1.0);
}
