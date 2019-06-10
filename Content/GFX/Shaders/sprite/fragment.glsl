#version 330 core

uniform sampler2D tex0;
uniform vec4 spriteColor;

in vec4 fsTransformedPos;
in vec2 fsTexCoords;

out vec4 outColor;
out vec4 outDepth;

void main() {
    outColor = spriteColor * texture(tex0, fsTexCoords);
    outDepth = vec4(fsTransformedPos.w, 0.0, 0.0, 1.0);
}
