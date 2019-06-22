#include "UIMesh.h"

Shader UIMesh::shader;
PGE::Vector2f UIMesh::defaultTexCoords[4];

Image::Image(int x, int y, int width, int height, UIMesh* mesh) : x(x), y(y), width(width), height(height), alignment(Alignment::CenterXY), mesh(mesh) { }

Image::~Image() {
    mesh->removeSlice(this);
}

void Image::fillVertexPositions(PGE::Vector2f pos[]) const {
    // TODO:
}

void UIMesh::initialize(const Shader& shd) {
    shader = shd;

    defaultTexCoords[0] = PGE::Vector2f(0.0f, 1.0f);
    defaultTexCoords[1] = PGE::Vector2f(1.0f, 0.0f);
    defaultTexCoords[2] = PGE::Vector2f(0.0f, 0.0f);
    defaultTexCoords[3] = PGE::Vector2f(1.0f, 1.0f);
}

UIMesh::UIMesh(const Graphics& gfx, const Texture& tex, bool tiles) {
    mesh = Mesh::create(gfx, PGE::Primitive::TYPE::TRIANGLE);
    material = Material::create(shader, tex);
    mesh->setMaterial(material.getInternal());
    tiled = tiles;
}

UIMesh::UIMesh(const Graphics& gfx, const PGE::String& path, bool tiles) : UIMesh(gfx, Texture::load(gfx, path), tiles) { }

Image* UIMesh::createSlice(int x, int y, int width, int height) {
    slices.push_back(Image(x, y, width, height, this));
    return &slices.back();
}

void UIMesh::removeSlice(const Image* slice) {
    for (int i = 0; i < (int)slices.size(); i++) {
        if (&slices[i] == slice) {
            slices.erase(slices.begin() + i);
            return;
        }
    }
}

void UIMesh::bake() const {
    mesh->clearGeometry();

    std::vector<PGE::Vertex> verts;
    std::vector<PGE::Primitive> prims;

    for (int i = 0; i < (int)slices.size(); i++) {
        PGE::Vector2f position[4];
        slices[i].fillVertexPositions(position);

        PGE::Vector2f texCoords[4];
        if (tiled) {
            for (int i = 0; i < 4; i++) {
                texCoords[i] = position[i];
            }
        } else {
            for (int i = 0; i < 4; i++) {
                texCoords[i] = defaultTexCoords[i];
            }
        }

        PGE::Vertex vertices[4];
        int quadIndex = (int)verts.size();

        for (int i = 0; i < 4; i++) {
            vertices[i].setVector2f("position", position[i]);
            vertices[i].setVector2f("texCoords", texCoords[i]);
            verts.push_back(vertices[i]);
        }

        prims.push_back(PGE::Primitive(quadIndex + 0, quadIndex + 1, quadIndex + 2));
        prims.push_back(PGE::Primitive(quadIndex + 0, quadIndex + 3, quadIndex + 1));
//    prims.push_back(PGE::Primitive(quadIndex + 1, quadIndex + 0, quadIndex + 2));
//    prims.push_back(PGE::Primitive(quadIndex + 3, quadIndex + 0, quadIndex + 1));
    }

    mesh->setGeometry(verts, prims);
}
