#version 330 core

in vec3 position;

out vec3 pass_tc;

uniform mat4 viewMatrix;
uniform mat4 projectionMatrix;

void main (void) {
	gl_Position = projectionMatrix * viewMatrix * vec4(position, 1.0);
	pass_tc = position; //每个像素的坐标就是表示采样textureCoordinate颜色对应的向量
}
