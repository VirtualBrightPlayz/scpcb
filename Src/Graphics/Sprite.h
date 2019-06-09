#ifndef Sprite_H_INCLUDED
#define Sprite_H_INCLUDED

#include <Mesh/Mesh.h>
#include "../Wrap/Texture.h"
#include "../Wrap/Shader.h"
#include "../Wrap/Mesh.h"

class Sprite {
private:
    static Mesh sharedMesh;

    Texture texture;
    PGE::Vector2f texCoords[4];
    Shader shader;
    PGE::Color color;

    float scale;

    Sprite(Graphics gfx, Shader shader, Texture tex);

public:
    static Sprite create(Graphics gfx, Shader shader, Texture tex);
    static Sprite create(Graphics gfx, Shader shader, PGE::String texPath);

    void setScale(float scale);
};

#endif // Sprite_H_INCLUDED
