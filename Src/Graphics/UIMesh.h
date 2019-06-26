#ifndef UIMESH_H_INCLUDED
#define UIMESH_H_INCLUDED

#include <vector>

#include "../Wrap/Mesh.h"
#include "../Wrap/Material.h"

class UIMesh;

enum class Alignment {
    CenterXY = 0x0,
    Left = 0x1,
    Right = 0x2,
    Top = 0x4,
    Bottom = 0x8
};

// Creates and stores the information relating to a piece of a UIMesh.
class Image {
private:
    UIMesh* mesh;

    // Total number of slices created.
    static int totalSliceCount;

    // The slice's depth relative to the total number of slices created.
    int depthOrder;

    float x;
    float y;
    float width;
    float height;

    Alignment alignment;

public:
    // Whether this slice needs to be drawn.
    bool visible;

    Image()=default;
    Image(float x, float y, float width, float height, UIMesh* mesh);
    ~Image();

    void setAlignment(Alignment align);

    // Fills the passed array of vectors with its quad positions.
    void fillVertexPositions(PGE::Vector3f pos[]) const;
};

class UIMesh {
private:
    static Shader shader;
    static Shader shaderTextureless;
    static PGE::Vector2f defaultTexCoords[4];

    Mesh mesh;
    Material material;
    PGE::Color color;
    PGE::Shader::Constant* imageColorValue;

    std::vector<Image> slices;

    // Whether or not the texture applied to this mesh is meant to tile.
    bool tiled;
    // Whether the mesh has a texture or just a color fill.
    bool textureless;

public:
    static void initialize(const Shader& shd, const Shader& shdNoTex);
    static void cleanup();

    UIMesh();
    UIMesh(const Graphics& gfx, const Texture& tex, bool tiles);
    UIMesh(const Graphics& gfx, const PGE::String& path, bool tiles);
    UIMesh(const Graphics& gfx, const PGE::Color& color);

    // Generates a new quad for the mesh.
    Image* createSlice(float x, float y, float width, float height);
    void removeSlice(const Image* slice);

    // Uploads the vertex data.
    void bake() const;
    void render() const;
};

const Alignment operator&(const Alignment& a, const Alignment& b);
const Alignment operator|(const Alignment& a, const Alignment& b);

#endif // UIMESH_H_INCLUDED
