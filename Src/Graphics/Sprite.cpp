#include "Sprite.h"

Sprite::Sprite(Graphics gfx, Shader shader, Texture tex) {
    color = PGE::Color();
    this->shader = shader;
    texture = tex;
}

Sprite Sprite::create(Graphics gfx, Shader shader, Texture tex) {
    return Sprite(gfx, shader, tex);
}

Sprite Sprite::create(Graphics gfx, Shader shader, PGE::String texPath) {
    Texture tex = Texture::load(gfx, texPath);
    return Sprite(gfx, shader, tex);
}

void Sprite::setScale(float scale) {
    this->scale = scale;
}