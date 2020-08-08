#ifndef BILLBOARD_H_INCLUDED
#define BILLBOARD_H_INCLUDED

#include <map>

#include <Mesh/Mesh.h>

#include "GraphicsResources.h"

class Billboard;
class BillboardManager {
    private:
        GraphicsResources* gfxRes;
        
        struct BillboardMesh {
            PGE::Mesh* mesh;
            PGE::Material* material;
            PGE::Texture* texture;

            std::vector<PGE::Vertex> vertices;
            std::vector<Billboard*> billboards;
        };

        std::vector<PGE::Primitive> primitives;

        std::map<long long, BillboardMesh> meshes;
        PGE::Shader* shader;

    public:
        BillboardManager(PGE::Graphics* gfx, GraphicsResources* gr);
        ~BillboardManager();

        void addBillboard(Billboard* billboard);
        void removeBillboard(Billboard* billboard);

        GraphicsResources* getGfxRes() const;

        void render();
};

class Billboard {
    protected:
        BillboardManager* bm;

        int vertexStartIndex;

        PGE::Vector3f position;
        PGE::Vector3f rotation; bool alwaysFacingCamera;
        PGE::Vector2f scale;
        PGE::Color color;

        PGE::String textureName;

    public:
        Billboard(BillboardManager* bm, const PGE::String& textureName, const PGE::Vector3f& pos, float rotation, const PGE::Vector2f& scale, const PGE::Color& color);
        Billboard(BillboardManager* bm, const PGE::String& textureName, const PGE::Vector3f& pos, const PGE::Vector3f& rotation, const PGE::Vector2f& scale, const PGE::Color& color);
        ~Billboard();

        void setTexture(const PGE::String& textureName);
        PGE::String getTexture() const;
        void setPosition(const PGE::Vector3f& position);
        void setRotation(const PGE::Vector3f& rotation);
        void setRotation(float rotation);
        void setScale(const PGE::Vector2f& scale);
        void setColor(const PGE::Color& color);

        bool updateVertices(std::vector<PGE::Vertex>& vertices, int startIndex);
};

#endif // BILLBOARD_H_INCLUDED
