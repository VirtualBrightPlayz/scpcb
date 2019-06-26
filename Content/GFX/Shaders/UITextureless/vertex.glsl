#version 330 core

uniform mat4 projectionMatrix;

in vec3 position;

void main() {
    gl_Position = projectionMatrix * vec4(position, 1.0f);
}
