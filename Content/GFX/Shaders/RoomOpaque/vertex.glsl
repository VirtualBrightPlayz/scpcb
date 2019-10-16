#version 330 core

in vec4 position;
in vec3 normal;
in vec2 diffUV;
in vec2 lmUV;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;

out vec4 worldPos;
out vec3 fsNormal;
out vec2 fsDiffUV;
out vec2 fsLmUV;

void main() {
    worldPos = modelMatrix * position;
    gl_Position = projectionMatrix * viewModel * worldPos;

    fsNormal = (modelMatrix * vec4(normal, 0.0f)).xyz;
    fsDiffUV = diffUV;
    fsLmUV = lmUV;
}
