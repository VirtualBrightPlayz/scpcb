#ifndef GRAPHICSRESOURCES_H_INCLUDED
#define GRAPHICSRESOURCES_H_INCLUDED

#include <unordered_map>

#include <assimp/Importer.hpp>

#include <String/Key.h>

#include <Graphics/Graphics.h>
#include <Shader/Shader.h>
#include <Texture/Texture.h>

class Model;
class ModelInstance;
class ResourcePackManager;
class Config;
class Camera;
class DebugGraphics;

class GraphicsResources {
    private:
        struct ShaderEntry {
            PGE::FilePath filename;
            PGE::Shader* shader;
            int refCount;
            bool needsViewProjection;
        };
        std::unordered_map<PGE::Shader*, ShaderEntry*> shaderToShaders;
        std::unordered_map<PGE::String::Key, ShaderEntry*> pathToShaders;

        struct TextureEntry {
            // This needs to stay a string for the Resource Packs to work.
            PGE::String name;
            PGE::Texture* texture;
            int refCount;
        };
        std::unordered_map<PGE::Texture*, TextureEntry*> textureToTextures;
        std::unordered_map<PGE::String::Key, TextureEntry*> pathToTextures;

        struct ModelEntry {
            // Having this as a string makes the loading of textures easier.
            PGE::String filename;
            Model* model;
            int refCount;
        };
        std::unordered_map<Model*, ModelEntry*> modelToModels;
        std::unordered_map<PGE::String::Key, ModelEntry*> pathToModels;

        PGE::Matrix4x4f orthoMat;

        PGE::Graphics* graphics;

        DebugGraphics* debugGraphics;

        Assimp::Importer* modelImporter;

    public:
        ResourcePackManager* rpm;
        
        GraphicsResources(PGE::Graphics* gfx, Config* con);
        ~GraphicsResources();

        PGE::Shader* getShader(const PGE::FilePath& filename, bool needsViewProjection);
        void dropShader(PGE::Shader* shader);

        PGE::Texture* getTexture(const PGE::String& filename);
        void dropTexture(PGE::Texture* texture);

        ModelInstance* getModelInstance(const PGE::String& filename);
        void dropModelInstance(ModelInstance* mi);
    
        void updateOrthoMat(float aspectRatio);
        PGE::Matrix4x4f getOrthoMat() const;
        void setCameraUniforms(const Camera* cam) const;

        PGE::Graphics* getGraphics() const;
        DebugGraphics* getDebugGraphics() const;
};

#endif // GRAPHICSRESOURCES_H_INCLUDED
