#include "GraphicsResources.h"

#include <assimp/mesh.h>

#include "Camera.h"
#include "../Save/Config.h"
#include "../Menus/GUI/GUIComponent.h"
#include "DebugGraphics.h"

GraphicsResources::GraphicsResources(PGE::Graphics* gfx, Config* con) {
    con->setGraphicsResources(this);
    updateOrthoMat(con->getAspectRatio());
    graphics = gfx;
    rpm = new ResourcePackManager(con->resourcePackLocations, con->enabledResourcePacks);
    debugGraphics = new DebugGraphics(gfx);

    modelImporter = new Assimp::Importer();
    modelImporter->SetPropertyInteger(AI_CONFIG_PP_RVC_FLAGS,
        aiComponent_CAMERAS |
        aiComponent_LIGHTS |
        aiComponent_COLORS |
        aiComponent_ANIMATIONS |
        aiComponent_BONEWEIGHTS
    );
    modelImporter->SetPropertyInteger(AI_CONFIG_PP_SLM_VERTEX_LIMIT, 65535);
    modelImporter->SetPropertyInteger(AI_CONFIG_PP_SLM_TRIANGLE_LIMIT, 65535);
    modelImporter->SetPropertyInteger(AI_CONFIG_PP_SBP_REMOVE, aiPrimitiveType_POINT | aiPrimitiveType_LINE);
}

GraphicsResources::~GraphicsResources() {
    delete rpm;
    delete debugGraphics;
    delete modelImporter;
}

PGE::Shader* GraphicsResources::getShader(const PGE::FilePath& filename, bool needsViewProjection) {
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
    newShader.needsViewProjection = needsViewProjection;
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
            }
            return;
        }
    }
}

PGE::Texture* GraphicsResources::getTexture(const PGE::String& filename) {
    for (int i = 0; i < (int)textures.size(); i++) {
        if (textures[i].name.equals(filename)) {
            textures[i].refCount++;
            return textures[i].texture;
        }
    }

    PGE::FilePath path = rpm->getHighestModPath(filename);
    if (!path.exists()) {
        return nullptr;
    }

    Texture newTexture;
    newTexture.refCount = 1;
    newTexture.texture = PGE::Texture::load(graphics, path);
    newTexture.name = filename;
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
            }
            return;
        }
    }
}

ModelInstance* GraphicsResources::getModelInstance(const PGE::String& filename) {
    for (int i = 0; i < (int)modelEntries.size(); i++) {
        if (modelEntries[i].filename.equals(filename)) {
            return new ModelInstance(modelEntries[i].model);
        }
    }

    Model* newModel = new Model(modelImporter, this, filename);
    modelEntries.push_back({ filename, newModel, 1 });

    return new ModelInstance(newModel);
}

void GraphicsResources::dropModelInstance(ModelInstance* mi) {
    for (int i = 0; i < (int)modelEntries.size(); i++) {
        if (modelEntries[i].model == mi->getModel()) {
            modelEntries[i].refCount--;
            delete mi;
            if (modelEntries[i].refCount <= 0) {
                delete modelEntries[i].model;
                modelEntries.erase(modelEntries.begin() + i);
            }
            return;
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

void GraphicsResources::setCameraUniforms(const Camera* cam) const {
    for (int i = 0; i < (int)shaders.size(); i++) {
        if (shaders[i].needsViewProjection) {
            shaders[i].shader->getVertexShaderConstant("viewMatrix")->setValue(cam->getViewMatrix());
            shaders[i].shader->getVertexShaderConstant("projectionMatrix")->setValue(cam->getProjectionMatrix());
        }
    }
    debugGraphics->setViewMatrix(cam->getViewMatrix());
    debugGraphics->setProjectionMatrix(cam->getProjectionMatrix());
}

PGE::Graphics* GraphicsResources::getGraphics() const {
    return graphics;
}

DebugGraphics* GraphicsResources::getDebugGraphics() const {
    return debugGraphics;
}

void GraphicsResources::updateShaderConstant(const PGE::FilePath& shd, const PGE::String& name, const PGE::Matrix4x4f& val) {
    for (int i = 0; i < (int)shaders.size(); i++) {
        if (shaders[i].filename.equals(shd)) {
            PGE::Shader::Constant* constant = shaders[i].shader->getVertexShaderConstant(name);
            constant->setValue(val);
            return;
        }
    }
}
