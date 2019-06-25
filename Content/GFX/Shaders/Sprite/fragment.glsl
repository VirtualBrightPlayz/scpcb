#version 330 core

uniform sampler2D tex0;
uniform vec4 spriteColor;

in vec2 fsTexCoords;

out vec4 outColor;

void main() {
    outColor = spriteColor * texture(tex0, fsTexCoords);
}
