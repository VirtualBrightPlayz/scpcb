#include "ShaderManager.h"

ShaderManager::ShaderManager(Graphics& gfx, const Camera* cam) {
    spriteShader = Shader::load(gfx, "GFX/Shaders/sprite/");
    PGE::Shader::Constant* projMatrix = spriteShader->getVertexShaderConstant("projectionMatrix");
    projMatrix->setValue(cam->getProjectionMatrix());
}

void ShaderManager::update(const Camera* cam) {
    PGE::Shader::Constant* viewMatrix = spriteShader->getVertexShaderConstant("viewMatrix");
    viewMatrix->setValue(cam->getViewMatrix());
}

Shader& ShaderManager::getSpriteShader() {
    return spriteShader;
}
