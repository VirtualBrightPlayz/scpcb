#include "GraphicsResources.h"
#include "../Save/Config.h"
#include "../Menus/GUI/GUIComponent.h"

GraphicsResources::GraphicsResources(PGE::Graphics* gfx, Config* con) {
    con->setGraphicsResources(this);
    updateOrthoMat(con->getAspectRatio());
    graphics = gfx;
}

PGE::Shader* GraphicsResources::getShader(PGE::FileName filename) {
    for (int i = 0; i < (int)shaders.size(); i++) {
        if (shaders[i].filename.equals(filename)) {
            shaders[i].refCount++;
            return shaders[i].shader;
        }
    }

    Shader newShader;
    newShader.refCount = 1;
    newShader.shader = PGE::Shader::load(graphics, filename);
    newShader.filename = filename;
    shaders.push_back(newShader);
    return newShader.shader;
}

void GraphicsResources::dropShader(PGE::Shader* shader) {
    for (int i = 0; i < (int)shaders.size(); i++) {
        if (shaders[i].shader == shader) {
            shaders[i].refCount--;
            if (shaders[i].refCount <= 0) {
                delete shaders[i].shader;
                shaders.erase(shaders.begin() + i);
                return;
            }
        }
    }
}

PGE::Texture* GraphicsResources::getTexture(PGE::FileName filename) {
    for (int i = 0; i < (int)textures.size(); i++) {
        if (textures[i].filename.equals(filename)) {
            textures[i].refCount++;
            return textures[i].texture;
        }
    }

    Texture newTexture;
    newTexture.refCount = 1;
    newTexture.texture = PGE::Texture::load(graphics, filename);
    newTexture.filename = filename;
    textures.push_back(newTexture);
    return newTexture.texture;
}

void GraphicsResources::dropTexture(PGE::Texture* texture) {
    for (int i = 0; i < (int)textures.size(); i++) {
        if (textures[i].texture == texture) {
            textures[i].refCount--;
            if (textures[i].refCount <= 0) {
                delete textures[i].texture;
                textures.erase(textures.begin() + i);
                return;
            }
        }
    }
}

void GraphicsResources::updateOrthoMat(float aspectRatio) {
    // Define our screen space for UI elements.
    // Top Left     - [-50, -50]
    // Bottom Right - [50, 50]
    // Horizontal plane is scaled with the aspect ratio.
    float w = GUIComponent::SCALE_MAGNITUDE * aspectRatio * 2.f;
    float h = GUIComponent::SCALE_MAGNITUDE * 2.f;
    float nearZ = 0.01f;
    float farZ = 1.f;
    orthoMat = PGE::Matrix4x4f::constructOrthographicMat(w, h, nearZ, farZ);
    
    // Update existing shaders.
    updateShaderConstant(uiShaderPath, "projectionMatrix", orthoMat);
    updateShaderConstant(uiTexturelessShaderPath, "projectionMatrix", orthoMat);
    updateShaderConstant(fontShaderPath, "projectionMatrix", orthoMat);
}

PGE::Matrix4x4f GraphicsResources::getOrthoMat() const {
    return orthoMat;
}

PGE::Graphics* GraphicsResources::getGraphics() const {
    return graphics;
}

void GraphicsResources::updateShaderConstant(PGE::FileName shd, PGE::String name, PGE::Matrix4x4f val) {
    for (int i = 0; i < (int)shaders.size(); i++) {
        if (shaders[i].filename.equals(shd)) {
            PGE::Shader::Constant* constant = shaders[i].shader->getVertexShaderConstant(name);
            constant->setValue(val);
            return;
        }
    }
}
