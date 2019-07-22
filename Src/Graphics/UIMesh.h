#ifndef UIMESH_H_INCLUDED
#define UIMESH_H_INCLUDED

#include <Misc/String.h>
#include <Misc/FileName.h>

#include <vector>
#include <Math/Rectangle.h>

#include <Mesh/Mesh.h>
#include <Material/Material.h>

class UIMesh;
class ShaderManager;

class UIMesh {
    private:
        PGE::Graphics* graphics;

        //TODO: store transformation matrix constants
        PGE::Shader* shaderTextured;
        PGE::Shader::Constant* shaderTexturedColorConstant;

        PGE::Shader* shaderTextureless;
        PGE::Shader::Constant* shaderTexturelessColorConstant;

        PGE::Mesh* mesh;
        PGE::Material* material;
        PGE::Color color;

        // Whether or not the texture applied to this mesh is meant to tile.
        bool tiled;
        // Whether the mesh has a texture or just a color fill.
        bool textureless;

        bool startedRender;

        std::vector<PGE::Vertex> vertices;
        std::vector<PGE::Primitive> primitives;

        struct Texture
        {
            PGE::FileName name;
            PGE::Texture* pgeTexture;
        };
        std::vector<Texture> textures;
    public:
        UIMesh(PGE::Graphics* gfx, const ShaderManager* sm);

        void startRender();

        void setTextureless();
        void setTextured(PGE::FileName texture, bool tile);

        void setColor(PGE::Color col);

        void addRect(const PGE::Rectanglef& rect);

        void endRender();

        void loadTexture(PGE::FileName textureName);
};

#endif // UIMESH_H_INCLUDED
