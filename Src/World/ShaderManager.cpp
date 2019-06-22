#include "ShaderManager.h"

ShaderManager::ShaderManager(const Graphics& gfx, const Camera* cam) {
    spriteShader = Shader::load(gfx, "GFX/Shaders/sprite/");
    PGE::Shader::Constant* projMatrix = spriteShader->getVertexShaderConstant("projectionMatrix");
    projMatrix->setValue(cam->getProjectionMatrix());
    spriteShaderView = spriteShader->getVertexShaderConstant("viewMatrix");

    uiShader = Shader::load(gfx, "GFX/Shaders/ui/");
    PGE::Shader::Constant* uiShaderProjection = uiShader->getVertexShaderConstant("projectionMatrix");
    uiShaderProjection->setValue(PGE::Matrix4x4f::constructOrtho2DMat(-50.f, 50.f, 50.f, -50.f));
}

void ShaderManager::update(const Camera* cam) {
    spriteShaderView->setValue(cam->getViewMatrix());
}

const Shader& ShaderManager::getSpriteShader() const {
    return spriteShader;
}

const Shader& ShaderManager::getUIShader() const {
    return uiShader;
}
