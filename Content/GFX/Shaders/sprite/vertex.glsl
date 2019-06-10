#version 330 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 worldMatrix;

in vec4 position;
in vec2 texCoords;

out vec4 fsTransformedPos;
out vec2 fsTexCoords;

void main() {
    fsTransformedPos = projectionMatrix * viewMatrix * worldMatrix * position;
    gl_Position = fsTransformedPos;
    fsTexCoords = texCoords;
}
