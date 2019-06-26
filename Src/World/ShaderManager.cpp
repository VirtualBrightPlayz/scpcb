#include "ShaderManager.h"
#include "../Save/Config.h"

ShaderManager::ShaderManager(const Graphics& gfx, const Camera* cam) {
    spriteShader = Shader::load(gfx, "GFX/Shaders/Sprite/");
    PGE::Shader::Constant* projMatrix = spriteShader->getVertexShaderConstant("projectionMatrix");
    projMatrix->setValue(cam->getProjectionMatrix());
    spriteShaderView = spriteShader->getVertexShaderConstant("viewMatrix");

    uiShader = Shader::load(gfx, "GFX/Shaders/UI/");
    PGE::Shader::Constant* uiShaderProjection = uiShader->getVertexShaderConstant("projectionMatrix");
    uiShaderNoTex = Shader::load(gfx, "GFX/Shaders/UITextureless/");
    PGE::Shader::Constant* uiShaderNoTexProjection = uiShaderNoTex->getVertexShaderConstant("projectionMatrix");

    // Define our screen space for UI elements.
    // Top Left     - [-50, -50]
    // Bottom Right - [50, 50]
    // Horizontal plane is scaled with the aspect ratio.
    PGE::Vector2f topLeft = PGE::Vector2f(-50.f * config.getAspectRatio(), -50.f);
    PGE::Vector2f bottomRight = PGE::Vector2f(50.f * config.getAspectRatio(), 50.f);
    PGE::Matrix4x4f orthoMat = PGE::Matrix4x4f::constructOrtho2DMat(topLeft.x, bottomRight.x, bottomRight.y, topLeft.y);

    uiShaderProjection->setValue(orthoMat);
    uiShaderNoTexProjection->setValue(orthoMat);
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

const Shader& ShaderManager::getUIShaderNoTex() const {
    return uiShaderNoTex;
}
