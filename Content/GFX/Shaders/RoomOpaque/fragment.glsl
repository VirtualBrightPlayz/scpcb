#version 330 core

in vec4 worldPos;
in vec3 fsNormal;
in vec2 fsDiffUV;
in vec2 fsLmUV;

uniform sampler2D lm0;
uniform sampler2D lm1;
uniform sampler2D lm2;
uniform sampler2D diff;

out vec4 outColor;

void main() {
    texture(tex0, fsTexCoords)
    vec4 diffColor = texture(diff, fsDiffUV) * 2;
    vec4 lmColor0 = texture(lm0, fsLmUV);
    vec4 lmColor1 = texture(lm1, fsLmUV);
    vec4 lmColor2 = texture(lm2, fsLmUV);
    vec3 bumpColor = vec3(0.0f, 0.0f, 1.0f);

    vec3 lmBasis0 = vec3(-1.0 / sqrt(6.0), -1.0 / sqrt(2.0), 1 / sqrt(3.0));
    vec3 lmBasis1 = vec3(-1.0 / sqrt(6.0), 1.0 / sqrt(2.0), 1 / sqrt(3.0));
    vec3 lmBasis2 = vec3(sqrt(2.0 / 3.0), 0.0, 1 / sqrt(3.0));

    vec4 lmColor = lmColor0 * dot(lmBasis0, bumpColor) + lmColor1 * dot(lmBasis1, bumpColor) + lmColor2 * dot(lmBasis2, bumpColor);
    lmColor = vec4(lmColor.x, lmColor.y, lmColor.z, 1.0f);

    outColor = diffColor * lmColor;
}
