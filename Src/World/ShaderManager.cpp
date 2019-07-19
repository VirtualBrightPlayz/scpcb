#include "ShaderManager.h"
#include "../Save/Config.h"

ShaderManager::ShaderManager(PGE::Graphics* gfx, const Camera* cam) {
    spriteShader = PGE::Shader::load(gfx, PGE::FileName("GFX/Shaders/Sprite/").str());
    PGE::Shader::Constant* projMatrix = spriteShader->getVertexShaderConstant("projectionMatrix");
    projMatrix->setValue(cam->getProjectionMatrix());
    spriteShaderView = spriteShader->getVertexShaderConstant("viewMatrix");
}

void ShaderManager::update(const Camera* cam) {
    spriteShaderView->setValue(cam->getViewMatrix());
}

PGE::Shader* ShaderManager::getSpriteShader() const {
    return spriteShader;
}
