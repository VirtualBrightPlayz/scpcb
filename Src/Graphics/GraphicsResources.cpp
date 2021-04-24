#include "GraphicsResources.h"

#include <assimp/mesh.h>

#include "../Models/Model.h"
#include "../Utils/ResourcePackManager.h"
#include "../Utils/TextureUtil.h"
#include "../Save/Config.h"
#include "Camera.h"
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
    std::map<long long, ShaderEntry*>::iterator find = pathToShaders.find(filename.getHashCode());
    if (find != pathToShaders.end()) {
        find->second->refCount++;
        return find->second->shader;
    }

    ShaderEntry* newShader = new ShaderEntry();
    newShader->refCount = 1;
    newShader->shader = PGE::Shader::load(graphics, filename);
    newShader->filename = filename;
    newShader->needsViewProjection = needsViewProjection;
    pathToShaders.emplace(filename.getHashCode(), newShader);
    shaderToShaders.emplace(newShader->shader, newShader);
    return newShader->shader;
}

void GraphicsResources::dropShader(PGE::Shader* shader) {
    std::map<PGE::Shader*, ShaderEntry*>::iterator find = shaderToShaders.find(shader);
    if (find != shaderToShaders.end()) {
        ShaderEntry* shaderEntry = find->second;
        shaderEntry->refCount--;
        if (shaderEntry->refCount <= 0) {
            delete shaderEntry->shader;
            pathToShaders.erase(shaderEntry->filename.getHashCode());
            shaderToShaders.erase(find);
            delete shaderEntry;
        }
    }
}

PGE::Texture* GraphicsResources::getTexture(const PGE::String& filename) {
    std::map<long long, TextureEntry*>::iterator find = pathToTextures.find(filename.getHashCode());
    if (find != pathToTextures.end()) {
        find->second->refCount++;
        return find->second->texture;
    }

    PGE::FilePath path = rpm->getHighestModPath(filename);
    if (!path.exists()) {
        return nullptr;
    }

    TextureEntry* newTexture = new TextureEntry();
    newTexture->refCount = 1;
    newTexture->texture = TextureHelper::load(graphics, path);
    newTexture->name = filename;
    pathToTextures.emplace(filename.getHashCode(), newTexture);
    textureToTextures.emplace(newTexture->texture, newTexture);
    return newTexture->texture;
}

void GraphicsResources::dropTexture(PGE::Texture* texture) {
    std::map<PGE::Texture*, TextureEntry*>::iterator find = textureToTextures.find(texture);
    if (find != textureToTextures.end()) {
        TextureEntry* textureEntry = find->second;
        textureEntry->refCount--;
        if (textureEntry->refCount <= 0) {
            delete texture;
            pathToTextures.erase(textureEntry->name.getHashCode());
            textureToTextures.erase(find);
            delete textureEntry;
        }
    } else {
        delete texture;
    }
}

ModelInstance* GraphicsResources::getModelInstance(const PGE::String& filename) {
    std::map<long long, ModelEntry*>::iterator find = pathToModels.find(filename.getHashCode());
    if (find != pathToModels.end()) {
        find->second->refCount++;
        return new ModelInstance(find->second->model);
    }

    ModelEntry* newModel = new ModelEntry();
    newModel->refCount = 1;
    newModel->model = new Model(modelImporter, this, filename);
    newModel->filename = filename;
    pathToModels.emplace(filename.getHashCode(), newModel);
    modelToModels.emplace(newModel->model, newModel);
    return new ModelInstance(newModel->model);
}

void GraphicsResources::dropModelInstance(ModelInstance* mi) {
    std::map<Model*, ModelEntry*>::iterator find = modelToModels.find(mi->getModel());
    if (find != modelToModels.end()) {
        ModelEntry* model = find->second;
        model->refCount--;
        if (model->refCount <= 0) {
            delete model->model;
            pathToModels.erase(model->filename.getHashCode());
            modelToModels.erase(find);
            delete model;
        }
    }
}

void GraphicsResources::updateOrthoMat(float aspectRatio) {
    // Define our screen space for UI elements.
    // Top Left     - [-50, -50]
    // Bottom Right - [50, 50]
    // Horizontal plane is scaled with the aspect ratio.
    float w = 100.f * aspectRatio;
    float h = 100.f;
    float nearZ = 0.01f;
    float farZ = 1.f;
    orthoMat = PGE::Matrix4x4f::constructOrthographicMat(w, h, nearZ, farZ);
}

PGE::Matrix4x4f GraphicsResources::getOrthoMat() const {
    return orthoMat;
}

void GraphicsResources::setCameraUniforms(const Camera* cam) const {
    for (const auto& entry : shaderToShaders) {
        if (entry.second->needsViewProjection) {
            entry.first->getVertexShaderConstant("viewMatrix")->setValue(cam->getViewMatrix());
            entry.first->getVertexShaderConstant("projectionMatrix")->setValue(cam->getProjectionMatrix());
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
