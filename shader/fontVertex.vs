#version 330

in vec2 position;
in vec2 tc;

out vec2 pass_tc;

uniform vec2 translation;

void main(void){
	gl_Position = vec4(position + translation * vec2(2.0, -2.0), 0.0, 1.0);
	pass_tc = tc;
}