#ifndef UIMESH_H_INCLUDED
#define UIMESH_H_INCLUDED

#include <vector>

#include <PGE/Graphics/Mesh.h>
#include <PGE/String/String.h>
#include <PGE/File/FilePath.h>
#include <PGE/Graphics/Shader.h>
#include <PGE/Math/Rectangle.h>
#include <PGE/Graphics/Material.h>
#include <PGE/Graphics/Texture.h>

class GraphicsResources;

class UIMesh {
    private:
        GraphicsResources* gfxRes;

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

        struct Vertex {
            PGE::Vector2f position;
            PGE::Vector2f uv;
        };

        PGE::StructuredData generateVertexData() const;

        std::vector<Vertex> vertices;
        std::vector<PGE::Mesh::Triangle> primitives;

        struct Texture
        {
            PGE::String name;
            PGE::Texture* pgeTexture;
        };
        std::vector<Texture> textures;

    public:
        float borderThickness;

        UIMesh(GraphicsResources* gr);
        ~UIMesh();

        void startRender();

        void setTextureless();
        void setTextured(PGE::Texture* texture, bool tile);
        void setTextured(const PGE::String& texture, bool tile);

        void setColor(PGE::Color col);

        void addRect(const PGE::Rectanglef& rect);

        void endRender();

        void loadTexture(const PGE::String& textureName);
};

#endif // UIMESH_H_INCLUDED
