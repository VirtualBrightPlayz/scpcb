#include "UIMesh.h"
#include "../Save/Config.h"

Shader UIMesh::shader;
PGE::Vector2f UIMesh::defaultTexCoords[4];

Image::Image(float x, float y, float width, float height, UIMesh* mesh) : x(x), y(y), width(width), height(height), alignment(Alignment::CenterXY), mesh(mesh) { }

Image::~Image() {
    // Make its reference to parent null to avoid cyclic destruction.
    if (mesh != nullptr) {
        UIMesh* parent = mesh;
        mesh = nullptr;
        parent->removeSlice(this);
    }
}

void Image::setAlignment(Alignment align) {
    alignment = align;
}

void Image::fillVertexPositions(PGE::Vector2f pos[]) const {
    float trueX = x;
    float trueY = y;

    if ((alignment & Alignment::Left) != Alignment::CenterXY) {
        trueX += -50.f * config.getAspectRatio();
    }

    if ((alignment & Alignment::Top) != Alignment::CenterXY) {
        trueY += -50.f;
    }

    if ((alignment & Alignment::Bottom) != Alignment::CenterXY){
        trueY += 50.f;
    }

    pos[0] = PGE::Vector2f(trueX, trueY);
    pos[1] = PGE::Vector2f(trueX + width, trueY + height);
    pos[2] = PGE::Vector2f(trueX, trueY + height);
    pos[3] = PGE::Vector2f(trueX + width, trueY);
}

void UIMesh::initialize(const Shader& shd) {
    shader = shd;

    defaultTexCoords[0] = PGE::Vector2f(0.0f, 1.0f);
    defaultTexCoords[1] = PGE::Vector2f(1.0f, 0.0f);
    defaultTexCoords[2] = PGE::Vector2f(0.0f, 0.0f);
    defaultTexCoords[3] = PGE::Vector2f(1.0f, 1.0f);
}

void UIMesh::cleanup() {
    shader = Shader();
}

UIMesh::UIMesh() { }

UIMesh::UIMesh(const Graphics& gfx, const Texture& tex, bool tiles) {
    mesh = Mesh::create(gfx, PGE::Primitive::TYPE::TRIANGLE);
    material = Material::create(shader, tex);
    mesh->setMaterial(material.getInternal());
    tiled = tiles;
}

UIMesh::UIMesh(const Graphics& gfx, const PGE::String& path, bool tiles) : UIMesh(gfx, Texture::load(gfx, path), tiles) { }

Image* UIMesh::createSlice(float x, float y, float width, float height) {
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
            // Texture coordinates are relative to the bottom left while our positioning is top left.
            // So we need to flip it vertically.

            // Also lower the scale from 50 to 2 so there's less frequent tiling.
            float screenToCoordsScale = 2.f / 50.f;

            texCoords[0] = position[0].multiply(screenToCoordsScale);
            texCoords[1] = position[1].multiply(screenToCoordsScale);
            texCoords[2] = position[2].multiply(screenToCoordsScale);
            texCoords[3] = position[3].multiply(screenToCoordsScale);
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

//        prims.push_back(PGE::Primitive(quadIndex + 0, quadIndex + 1, quadIndex + 2));
//        prims.push_back(PGE::Primitive(quadIndex + 0, quadIndex + 3, quadIndex + 1));
        prims.push_back(PGE::Primitive(quadIndex + 1, quadIndex + 0, quadIndex + 2));
        prims.push_back(PGE::Primitive(quadIndex + 3, quadIndex + 0, quadIndex + 1));
    }

    mesh->setGeometry(verts, prims);
}

void UIMesh::render() const {
    mesh->render();
}

const Alignment operator&(const Alignment& a, const Alignment& b) {
    return (Alignment)((int)a & (int)b);
}

const Alignment operator|(const Alignment& a, const Alignment& b) {
    return (Alignment)((int)a | (int)b);
}
