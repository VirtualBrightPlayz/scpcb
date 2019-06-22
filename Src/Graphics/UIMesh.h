#ifndef UIMESH_H_INCLUDED
#define UIMESH_H_INCLUDED

#include <vector>

#include "../Wrap/Mesh.h"
#include "../Wrap/Material.h"

class UIMesh;

// Creates and stores the information relating to a piece of a UIMesh.
class UISlice {
private:
    UIMesh* mesh;
    PGE::Vertex quad[4];
    
    // Whether the X coordinate of the slice scales with the aspect ratio.
    bool aspectRatioScale;

public:
    UISlice(int x, int y, int width, int height, const UIMesh* mesh);
};

class UIMesh {
private:
    Mesh mesh;
    Material material;
    
    std::vector<UISlice> slices;
    
    // Whether or not the texture applied to this mesh is meant to tile.
    bool tiled;
    
public:
    UISlice* createSlice(int x, int y, int width, int height);
    
    // Uploads the vertex data.
    void bake() const;
    void render() const;
};

#endif // UIMESH_H_INCLUDED
