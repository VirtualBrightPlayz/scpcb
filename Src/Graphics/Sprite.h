#ifndef SPRITE_H_INCLUDED
#define SPRITE_H_INCLUDED

#include <Mesh/Mesh.h>
#include <Math/Vector.h>
#include "../Wrap/Texture.h"
#include "../Wrap/Shader.h"
#include "../Wrap/Mesh.h"

class Sprite {
private:
    static Mesh sharedMesh;
    static void createSpriteMesh(Graphics gfx);

    Texture texture;
    Shader shader;
    PGE::Color color;

    PGE::Vector3f position;
    PGE::Vector3f rotation;
    PGE::Vector2f scale;

    Graphics gfx;

    Sprite(Graphics gfx, Shader shader, Texture tex);

public:
    Sprite();

    static Sprite create(Graphics gfx, Shader shader, Texture tex);
    static Sprite create(Graphics gfx, Shader shader, PGE::String texPath);

    void setScale(float scale);
    void setPosition(const PGE::Vector3f& pos);
    void setPosition(float x, float y, float z);

    void render();
};

#endif // SPRITE_H_INCLUDED
