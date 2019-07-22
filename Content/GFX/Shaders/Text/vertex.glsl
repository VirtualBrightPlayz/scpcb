#version 330 core

uniform mat4 modelMatrix;
uniform mat4 projectionMatrix;

in vec3 position;
in vec2 uv;

out vec2 fsUV;

void main() {
    gl_Position = projectionMatrix * modelMatrix * vec4(position.xyz, 1.0f);
    fsUV = uv;
}
