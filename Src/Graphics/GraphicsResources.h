#ifndef GRAPHICSRESOURCES_H_INCLUDED
#define GRAPHICSRESOURCES_H_INCLUDED

#include <Graphics/Graphics.h>
#include <Shader/Shader.h>
#include <Texture/Texture.h>

class Config;

class GraphicsResources {
    private:
        struct Shader {
            PGE::FileName filename;
            PGE::Shader* shader;
            int refCount;
        };
        std::vector<Shader> shaders;

        struct Texture {
            PGE::FileName filename;
            PGE::Texture* texture;
            int refCount;
        };
        std::vector<Texture> textures;

        PGE::Matrix4x4f orthoMat;
        PGE::FileName uiShaderPath = PGE::FileName::create("GFX/Shaders/UI/");
        PGE::FileName uiTexturelessShaderPath = PGE::FileName::create("GFX/Shaders/UITextureless/");
        PGE::FileName fontShaderPath = PGE::FileName::create("GFX/Shaders/Text/");

        PGE::Graphics* graphics;

    public:
        GraphicsResources(PGE::Graphics* gfx, Config* con);

        PGE::Shader* getShader(PGE::FileName filename);
        void dropShader(PGE::Shader* shader);
        void updateShaderConstant(PGE::FileName shd, PGE::String constant, PGE::Matrix4x4f val);

        PGE::Texture* getTexture(PGE::FileName filename);
        void dropTexture(PGE::Texture* texture);
    
        void updateOrthoMat(float aspectRatio);
        PGE::Matrix4x4f getOrthoMat() const;

        PGE::Graphics* getGraphics() const;
};

#endif // GRAPHICSRESOURCES_H_INCLUDED
