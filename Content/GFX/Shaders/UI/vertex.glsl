#version 330 core

uniform mat4 projectionMatrix;

in vec2 position;
in vec2 texCoords;

out vec2 fsTexCoords;

void main() {
    gl_Position = projectionMatrix * vec4(position, 0.0f, 1.0f);
    fsTexCoords = texCoords;
}
