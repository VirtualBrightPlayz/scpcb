#ifndef SPRITE_H_INCLUDED
#define SPRITE_H_INCLUDED

#include <Mesh/Mesh.h>
#include <Math/Vector.h>
#include "../Wrap/Texture.h"
#include "../Wrap/Shader.h"
#include "../Wrap/Mesh.h"
#include "../Wrap/Material.h"

class Sprite {
private:
    static Mesh sharedMesh;
    static Shader shader;
    static void createSpriteMesh(const Graphics& gfx);

    PGE::Shader::Constant* modelMatrixValue;
    PGE::Shader::Constant* spriteColorValue;
    PGE::Shader::Constant* scaleValue;
    PGE::Shader::Constant* rotationMatrixValue;

    Texture texture;
    Material material;
    PGE::Color color;

    PGE::Vector3f position;
    PGE::Vector2f scale;
    float rotation;
    PGE::Matrix4x4f modelMatrix;
    // Stores the roll rotations of the sprite.
    PGE::Matrix4x4f rotationMatrix;

    Graphics gfx;

    Sprite(const Graphics& gfx, const Texture& tex);

public:
    static void initialize(const Graphics& gfx, const Shader& shd);

    Sprite();

    static Sprite create(const Graphics& gfx, const Texture& tex);
    static Sprite create(const Graphics& gfx, const PGE::String texPath);

    void setScale(float scale);

    void setPosition(const PGE::Vector3f& pos);
    void setPosition(float x, float y, float z);

    void setRotation(float rad);
    void addRotation(float rad);

    // No more bets.
    void update();
    void render();
};

#endif // SPRITE_H_INCLUDED
