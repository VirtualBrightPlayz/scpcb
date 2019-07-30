#ifndef GFXRESMANAGER_H_INCLUDED
#define GFXRESMANAGER_H_INCLUDED

#include <Graphics/Graphics.h>
#include <Shader/Shader.h>
#include <Texture/Texture.h>

class Config;

class GfxResManager {
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

        PGE::Graphics* graphics;
    public:
        GfxResManager(PGE::Graphics* gfx, Config* con);

        PGE::Shader* getShader(PGE::FileName filename);
        void dropShader(PGE::Shader* shader);

        PGE::Texture* getTexture(PGE::FileName filename);
        void dropTexture(PGE::Texture* texture);
    
        void updateOrthoMat(float aspectRatio);
        PGE::Matrix4x4f getOrthoMat() const;

        PGE::Graphics* getGraphics() const;
};

#endif // ShaderManager_H_INCLUDED
