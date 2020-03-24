#version 330 core

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform vec2 scale;
uniform mat4 rotationMatrix;

in vec2 position;
in vec2 texCoords;

out vec2 fsTexCoords;

void main() {
    // Remove rotations (and scale).
    mat4 viewModel = viewMatrix * modelMatrix;
    viewModel[0][0] = 1.0f;
    viewModel[1][1] = 1.0f;
    viewModel[2][2] = 1.0f;
    viewModel[0][1] = 0.0f;
    viewModel[0][2] = 0.0f;
    viewModel[1][0] = 0.0f;
    viewModel[1][2] = 0.0f;
    viewModel[2][0] = 0.0f;
    viewModel[2][1] = 0.0f;

    // Add in sprite scaling and roll rotations.
    vec4 vertexScaleRotation = vec4(scale, 1.0f, 1.0f) * vec4(position.xy, 0.0f, 1.0f);
    vertexScaleRotation = rotationMatrix * vertexScaleRotation;

    gl_Position = projectionMatrix * viewModel * vertexScaleRotation;
    fsTexCoords = texCoords;
}
