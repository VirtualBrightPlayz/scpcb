#version 330 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 worldMatrix;

in vec4 position;
in vec2 texCoords;

out vec2 fsTexCoords;

void main() {
    gl_Position = projectionMatrix * viewMatrix * worldMatrix * position;
    fsTexCoords = texCoords;
}
