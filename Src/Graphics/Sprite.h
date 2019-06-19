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
    static void createSpriteMesh(const Graphics& gfx);

    Texture texture;
    Material material;
    Shader shader;
    PGE::Color color;

    PGE::Vector3f position;
    PGE::Vector2f scale;
    float rotation;

    Graphics gfx;

    Sprite(const Graphics& gfx, const Shader& shader, const Texture& tex);

public:
    Sprite();

    static Sprite create(const Graphics& gfx, const Shader& shader, const Texture& tex);
    static Sprite create(const Graphics& gfx, const Shader& shader, const PGE::String texPath);

    void setScale(float scale);
    void setPosition(const PGE::Vector3f& pos);
    void setPosition(float x, float y, float z);
    void setRotation(float rad);

    void render();
};

#endif // SPRITE_H_INCLUDED
