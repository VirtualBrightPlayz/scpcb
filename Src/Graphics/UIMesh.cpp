#include "UIMesh.h"
#include "../Save/Config.h"

int Image::totalSliceCount = 0;
Shader UIMesh::shader;
Shader UIMesh::shaderTextureless;
PGE::Vector2f UIMesh::defaultTexCoords[4];

Image::Image(float x, float y, float width, float height, UIMesh* mesh)
: x(x), y(y), width(width), height(height), alignment(Alignment::CenterXY), mesh(mesh) {
    depthOrder = totalSliceCount;
    totalSliceCount++;
    visible = true;
}

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

void Image::fillVertexPositions(PGE::Vector3f pos[]) const {
    float trueX = x;
    float trueY = y;

    if ((alignment & Alignment::Left) != Alignment::CenterXY) {
        trueX += -50.f * config.getAspectRatio();
    }

    if ((alignment & Alignment::Right) != Alignment::CenterXY) {
        trueX += 50.f * config.getAspectRatio();
    }

    if ((alignment & Alignment::Top) != Alignment::CenterXY) {
        trueY += -50.f;
    }

    if ((alignment & Alignment::Bottom) != Alignment::CenterXY){
        trueY += 50.f;
    }

    // Get the image's depth.
    float depth = (1.f + depthOrder) / totalSliceCount;

    pos[0] = PGE::Vector3f(trueX, trueY, depth);
    pos[1] = PGE::Vector3f(trueX + width, trueY + height, depth);
    pos[2] = PGE::Vector3f(trueX, trueY + height, depth);
    pos[3] = PGE::Vector3f(trueX + width, trueY, depth);
}

void UIMesh::initialize(const Shader& shd, const Shader& shdNoTex) {
    shader = shd;
    shaderTextureless = shdNoTex;

    defaultTexCoords[0] = PGE::Vector2f(0.0f, 1.0f);
    defaultTexCoords[1] = PGE::Vector2f(1.0f, 0.0f);
    defaultTexCoords[2] = PGE::Vector2f(0.0f, 0.0f);
    defaultTexCoords[3] = PGE::Vector2f(1.0f, 1.0f);
}

void UIMesh::cleanup() {
    shader = Shader();
    shaderTextureless = Shader();
}

UIMesh::UIMesh() { }

UIMesh::UIMesh(const Graphics& gfx, const Texture& tex, bool tiles) {
    mesh = Mesh::create(gfx, PGE::Primitive::TYPE::TRIANGLE);
    material = Material::create(shader, tex);
    mesh->setMaterial(material.getInternal());
    tiled = tiles;
    textureless = false;
    color = PGE::Color(1.f, 1.f, 1.f, 1.f);
    imageColorValue = shader->getFragmentShaderConstant("imageColor");
}

UIMesh::UIMesh(const Graphics& gfx, const PGE::String& path, bool tiles) : UIMesh(gfx, Texture::load(gfx, path), tiles) { }

UIMesh::UIMesh(const Graphics& gfx, const PGE::Color& color) {
    mesh = Mesh::create(gfx, PGE::Primitive::TYPE::TRIANGLE);
    material = Material::create(shaderTextureless);
    mesh->setMaterial(material.getInternal());
    tiled = false;
    textureless = true;
    this->color = color;
    imageColorValue = shaderTextureless->getFragmentShaderConstant("imageColor");
}

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
        if (!slices[i].visible) { continue; }

        PGE::Vector3f position[4];
        slices[i].fillVertexPositions(position);

        PGE::Vector2f texCoords[4];
        if (!textureless) {
            if (tiled) {
                // Lower the scale from [-50, 50] to [-2, 2] so there's less frequent tiling.
                float screenToCoordsScale = 2.f / 50.f;

                texCoords[0] = PGE::Vector2f(position[0].x * screenToCoordsScale, position[0].y * screenToCoordsScale);
                texCoords[1] = PGE::Vector2f(position[1].x * screenToCoordsScale, position[1].y * screenToCoordsScale);
                texCoords[2] = PGE::Vector2f(position[2].x * screenToCoordsScale, position[2].y * screenToCoordsScale);
                texCoords[3] = PGE::Vector2f(position[3].x * screenToCoordsScale, position[3].y * screenToCoordsScale);
            } else {
                for (int i = 0; i < 4; i++) {
                    texCoords[i] = defaultTexCoords[i];
                }
            }
        }

        PGE::Vertex vertices[4];
        int quadIndex = (int)verts.size();

        for (int i = 0; i < 4; i++) {
            vertices[i].setVector3f("position", position[i]);
            if (!textureless) { vertices[i].setVector2f("texCoords", texCoords[i]); }
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
    imageColorValue->setValue(color);

    mesh->render();
}

const Alignment operator&(const Alignment& a, const Alignment& b) {
    return (Alignment)((int)a & (int)b);
}

const Alignment operator|(const Alignment& a, const Alignment& b) {
    return (Alignment)((int)a | (int)b);
}
