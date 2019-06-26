#version 330 core

uniform sampler2D tex0;
uniform vec4 imageColor;

in vec2 fsTexCoords;

out vec4 outColor;

void main() {
    outColor = imageColor * texture(tex0, fsTexCoords);
}
