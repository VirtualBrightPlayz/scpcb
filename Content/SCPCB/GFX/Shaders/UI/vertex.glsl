#version 330 core

uniform mat4 projectionMatrix;

in vec2 position;
in vec2 uv;

out vec2 fsUV;

void main() {
    gl_Position = projectionMatrix * vec4(position.xy, 0.5f, 1.0f);
    fsUV = uv;
}
