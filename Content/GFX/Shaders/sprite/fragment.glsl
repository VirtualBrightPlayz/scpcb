#version 330 core

uniform sampler2D tex0;
uniform vec4 spriteColor;

in vec2 fsTexCoords;

out vec4 outColor;

void main() {
    outColor = vec4(1.0f, 0.0f, 0.0f, 1.0f);
//    outColor = spriteColor * texture(tex0, fsTexCoords);
}
