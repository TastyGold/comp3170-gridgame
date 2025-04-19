#version 410

in vec4 a_position; // The vertex position from our java class

in vec3 a_worldPos; // The instanced world pos
in float a_tilePresence;

uniform mat4 u_mvpMatrix;

void main() {
	// Passing the data into the fragment shader
	mat4 trs = mat4(a_tilePresence,0,0,0, 0,a_tilePresence,0,0, 0,0,a_tilePresence,0, a_worldPos.xyz,a_tilePresence);
	gl_Position = u_mvpMatrix * trs * a_position;
}
