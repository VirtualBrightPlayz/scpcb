#include "ShaderManager.h"

ShaderManager::ShaderManager(Graphics gfx) {
    spriteShader = Shader::load(gfx, "GFX/Shaders/sprite/");
}

Shader ShaderManager::getSpriteShader() {
    return spriteShader;
}
