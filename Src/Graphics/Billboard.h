#ifndef Billboard_H_INCLUDED
#define Billboard_H_INCLUDED

#include <Mesh/Mesh.h>

#include "GraphicsResources.h"

class BillboardManager {
    private:
        GraphicsResources* gfxRes;
        
        PGE::Mesh* mesh;
        PGE::Shader* shader;
        PGE::Shader::Constant* modelMatrix;
        PGE::Shader::Constant* spriteColor;

    public:
        BillboardManager(PGE::Graphics* gfx, GraphicsResources* gr);
        ~BillboardManager();

        GraphicsResources* getGfxRes() const;

        PGE::Mesh* getMesh() const;
        PGE::Shader* getShader() const;
        PGE::Shader::Constant* getModelMatrix() const;
        PGE::Shader::Constant* getSpriteColor() const;
};

class Billboard {
    private:
        void dropMaterial();
        void loadMaterial(const PGE::String& textureName);

    protected:
        const BillboardManager* bm;
        PGE::Material* material;

    public:
        PGE::Vector3f pos;
        PGE::Vector2f scale;
        PGE::Color color;
        
        Billboard(BillboardManager* bm, const PGE::Vector3f& pos, const PGE::String& textureName, const PGE::Vector2f& scale, const PGE::Color& color);
        ~Billboard();

        void setTexture(const PGE::String& textureName);

        virtual void render(const PGE::Matrix4x4f& camRotationMatrix) const;
};

class RotatedBillboard : public Billboard {
    public:
        PGE::Vector3f rotation;
        
        RotatedBillboard(BillboardManager* bm, const PGE::Vector3f& pos, const PGE::String& textureName, const PGE::Vector3f& rotation, const PGE::Vector2f& scale, const PGE::Color& color);

        virtual void render(const PGE::Matrix4x4f& camRotationMatrix) const override;
};

#endif // Billboard_H_INCLUDED
