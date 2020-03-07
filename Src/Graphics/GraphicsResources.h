#ifndef GRAPHICSRESOURCES_H_INCLUDED
#define GRAPHICSRESOURCES_H_INCLUDED

#include <Graphics/Graphics.h>
#include <Shader/Shader.h>
#include <Texture/Texture.h>

class Config;
class Camera;

class GraphicsResources {
    private:
        struct Shader {
            PGE::FileName filename;
            PGE::Shader* shader;
            int refCount;
            bool needsViewProjection;
        };
        std::vector<Shader> shaders;

        struct Texture {
            PGE::FileName filename;
            PGE::Texture* texture;
            int refCount;
        };
        std::vector<Texture> textures;

        PGE::Matrix4x4f orthoMat;
        PGE::FileName uiShaderPath = PGE::FileName::fromStr("GFX/Shaders/UI/");
        PGE::FileName uiTexturelessShaderPath = PGE::FileName::fromStr("GFX/Shaders/UITextureless/");
        PGE::FileName fontShaderPath = PGE::FileName::fromStr("GFX/Shaders/Text/");

        PGE::Graphics* graphics;

    public:
        GraphicsResources(PGE::Graphics* gfx, Config* con);

        PGE::Shader* getShader(const PGE::FileName& filename, bool needsViewProjection);
        void dropShader(PGE::Shader* shader);
        void updateShaderConstant(const PGE::FileName& shd, const PGE::String& constant, const PGE::Matrix4x4f& val);

        PGE::Texture* getTexture(const PGE::FileName& filename);
        void dropTexture(PGE::Texture* texture);
    
        void updateOrthoMat(float aspectRatio);
        PGE::Matrix4x4f getOrthoMat() const;
        void setCameraUniforms(const Camera* cam) const;

        PGE::Graphics* getGraphics() const;
};

#endif // GRAPHICSRESOURCES_H_INCLUDED
