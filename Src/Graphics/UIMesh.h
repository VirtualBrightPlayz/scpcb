#ifndef UIMESH_H_INCLUDED
#define UIMESH_H_INCLUDED

#include <vector>

#include "../Wrap/Mesh.h"
#include "../Wrap/Material.h"

class UIMesh;

// Creates and stores the information relating to a piece of a UIMesh.
class Image {
public:
    enum class Alignment {
        CenterXY = 0x0,
        Left = 0x1,
        Top = 0x2,
        Bottom = 0x4
    };

private:
    UIMesh* mesh;

    int x;
    int y;
    int width;
    int height;
    Alignment alignment;

    // Whether the X coordinate of the slice scales with the aspect ratio.
    bool aspectRatioScale;

public:
    Image(int x, int y, int width, int height, UIMesh* mesh);
    ~Image();

    // Fills the passed array of vectors with its quad positions.
    void fillVertexPositions(PGE::Vector2f pos[]) const;
};

class UIMesh {
private:
    static Shader shader;
    static PGE::Vector2f defaultTexCoords[4];

    Mesh mesh;
    Material material;

    std::vector<Image> slices;

    // Whether or not the texture applied to this mesh is meant to tile.
    bool tiled;

public:
    static void initialize(const Shader& shd);

    UIMesh(const Graphics& gfx, const Texture& tex, bool tiles);
    UIMesh(const Graphics& gfx, const PGE::String& path, bool tiles);

    // Generates a new quad for the mesh.
    Image* createSlice(int x, int y, int width, int height);
    void removeSlice(const Image* slice);

    // Uploads the vertex data.
    void bake() const;
    void render() const;
};

#endif // UIMESH_H_INCLUDED
