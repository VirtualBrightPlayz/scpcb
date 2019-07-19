#ifndef SPRITE_H_INCLUDED
#define SPRITE_H_INCLUDED

#include <Mesh/Mesh.h>
#include <Math/Vector.h>
#include <Texture/Texture.h>
#include <Shader/Shader.h>
#include <Mesh/Mesh.h>
#include <Material/Material.h>

class Sprite {
    private:
        PGE::Shader::Constant* modelMatrixValue;
        PGE::Shader::Constant* spriteColorValue;
        PGE::Shader::Constant* scaleValue;
        PGE::Shader::Constant* rotationMatrixValue;

        PGE::Mesh* mesh;
        PGE::Texture* texture;
        PGE::Shader* shader;
        PGE::Material* material;
        PGE::Color color;

        PGE::Vector3f position;
        PGE::Vector2f scale;
        float rotation;
        PGE::Matrix4x4f modelMatrix;
        // Stores the roll rotations of the sprite.
        PGE::Matrix4x4f rotationMatrix;

    public:
        static PGE::Mesh* createSpriteMesh(PGE::Graphics* gfx);

        Sprite(PGE::Mesh* msh, PGE::Texture* tex, PGE::Shader* shdr);
        ~Sprite();

        void setScale(float scale);

        void setPosition(const PGE::Vector3f& pos);
        void setPosition(float x, float y, float z);

        void setRotation(float rad);
        void addRotation(float rad);

        void update();
        void render() const;
};

#endif // SPRITE_H_INCLUDED
