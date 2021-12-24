#ifndef BILLBOARD_H_INCLUDED
#define BILLBOARD_H_INCLUDED

#include <unordered_map>

#include <PGE/String/String.h>

#include <PGE/Graphics/Mesh.h>
#include <PGE/Graphics/Texture.h>
#include <PGE/Graphics/Shader.h>
#include <PGE/StructuredData/StructuredData.h>

class GraphicsResources;
class Camera;

class Billboard;
class BillboardManager {
    private:
        GraphicsResources* gfxRes;
        Camera* camera;
        
        struct BillboardMesh {
            BillboardMesh() = default;
            BillboardMesh(const BillboardMesh& other);
            void operator=(const BillboardMesh& other);

            PGE::Mesh* mesh;
            PGE::Material* material;
            PGE::Texture* texture;

            PGE::StructuredData vertices;
            std::vector<Billboard*> billboards;

            bool geomChanged;
        };

        std::vector<PGE::Mesh::Triangle> primitives;

        std::unordered_map<PGE::String::Key, BillboardMesh> meshes;
        PGE::Shader* shader;

    public:
        BillboardManager(PGE::Graphics* gfx, GraphicsResources* gr, Camera* cam);
        ~BillboardManager();

        void addBillboard(Billboard* billboard);
        void removeBillboard(Billboard* billboard);

        void render();
};

class Billboard {
    private:
        BillboardManager* bm;

        bool visible;

        int vertexStartIndex;

        PGE::Vector3f position;
        PGE::Vector3f rotation; bool alwaysFacingCamera;
        PGE::Vector2f scale;
        PGE::Color color;

        PGE::String textureName;

    public:
        float camDistance;

        Billboard(BillboardManager* bm, const PGE::String& textureName, const PGE::Vector3f& pos, float rotation, const PGE::Vector2f& scale, const PGE::Color& color);
        Billboard(BillboardManager* bm, const PGE::String& textureName, const PGE::Vector3f& pos, const PGE::Vector3f& rotation, const PGE::Vector2f& scale, const PGE::Color& color);
        ~Billboard();

        void setTexture(const PGE::String& textureName);
        PGE::String getTexture() const;
        void setPosition(const PGE::Vector3f& position);
        const PGE::Vector3f& getPosition() const;
        void setRotation(const PGE::Vector3f& rotation);
        void setRotation(float rotation);
        void setScale(const PGE::Vector2f& scale);
        void setColor(const PGE::Color& color);
        void setVisible(bool vis);
        bool getVisible();

        void markAsDirty();

        bool updateVertices(PGE::StructuredData& vertices, int startIndex);
};

#endif // BILLBOARD_H_INCLUDED
