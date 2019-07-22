#include "ShaderManager.h"
#include "../Save/Config.h"

ShaderManager::ShaderManager(PGE::Graphics* gfx, Config* con, const Camera* cam) {
    spriteShader = PGE::Shader::load(gfx, PGE::FileName::create("GFX/Shaders/Sprite/"));
    PGE::Shader::Constant* projMatrix = spriteShader->getVertexShaderConstant("projectionMatrix");
    projMatrix->setValue(cam->getProjectionMatrix());
    spriteShaderView = spriteShader->getVertexShaderConstant("viewMatrix");
    
    fontShader = PGE::Shader::load(gfx, PGE::FileName::create("GFX/Shaders/Text/"));
    
    con->setShaderManager(this);
    updateOrthoMat(con->getAspectRatio());
}

void ShaderManager::update(const Camera* cam) {
    spriteShaderView->setValue(cam->getViewMatrix());
}

PGE::Shader* ShaderManager::getSpriteShader() const {
    return spriteShader;
}

PGE::Shader* ShaderManager::getFontShader() const {
    return fontShader;
}

void ShaderManager::updateOrthoMat(float aspectRatio) {
    // Define our screen space for UI elements.
    // Top Left     - [-50, -50]
    // Bottom Right - [50, 50]
    // Horizontal plane is scaled with the aspect ratio.
    float SCALE_MAGNITUDE = 50.f;
    float w = SCALE_MAGNITUDE * aspectRatio * 2.f;
    float h = SCALE_MAGNITUDE * 2.f;
    float nearZ = 0.01f;
    float farZ = 1.f;
    orthoMat = PGE::Matrix4x4f::constructOrthographicMat(w, h, nearZ, farZ);
}

PGE::Matrix4x4f ShaderManager::getOrthoMat() const {
    return orthoMat;
}
