#version 330 core

uniform mat4 projectionMatrix;

in vec3 position;
in vec2 texCoords;

out vec2 fsTexCoords;

void main() {
    gl_Position = projectionMatrix * vec4(position.xyz, 1.0f);
    fsTexCoords = texCoords;
}
