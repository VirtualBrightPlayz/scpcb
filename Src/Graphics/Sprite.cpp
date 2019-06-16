#include "Sprite.h"
#include "../Wrap/Material.h"

Mesh Sprite::sharedMesh;

Sprite::Sprite() { }

Sprite::Sprite(const Graphics& gfx, const Shader& shader, const Texture& tex) {
    if (sharedMesh.getInternal() == nullptr) { createSpriteMesh(gfx); }

    color = PGE::Color();
    this->shader = shader;
    texture = tex;
    material = Material::create(shader, tex);
    this->gfx = gfx;

    position = PGE::Vector3f::zero;
    scale = PGE::Vector2f::one;
    rotation = 0.0f;
}

Sprite Sprite::create(const Graphics& gfx, const Shader& shader, const Texture& tex) {
    return Sprite(gfx, shader, tex);
}

Sprite Sprite::create(const Graphics& gfx, const Shader& shader, const PGE::String texPath) {
    Texture tex = Texture::load(gfx, texPath);
    return Sprite(gfx, shader, tex);
}

void Sprite::setScale(float scale) {
    this->scale.x = scale;
    this->scale.y = scale;
}

void Sprite::setPosition(const PGE::Vector3f& pos) {
    position = pos;
}

void Sprite::setPosition(float x, float y, float z) {
    position = PGE::Vector3f(x, y, z);
}

void Sprite::createSpriteMesh(const Graphics& gfx) {
    sharedMesh = Mesh::create(gfx, PGE::Primitive::TYPE::TRIANGLE);

    PGE::Vector2f verts[4];
    verts[0] = PGE::Vector2f(-0.5f, 0.5f);
    verts[1] = PGE::Vector2f(0.5f, -0.5f);
    verts[2] = PGE::Vector2f(-0.5f, -0.5f);
    verts[3] = PGE::Vector2f(0.5f, 0.5f);

    PGE::Vector2f texCoords[4];
    texCoords[0] = PGE::Vector2f(0.0f, 1.0f);
    texCoords[1] = PGE::Vector2f(1.0f, 0.0f);
    texCoords[2] = PGE::Vector2f(0.0f, 0.0f);
    texCoords[3] = PGE::Vector2f(1.0f, 1.0f);

    std::vector<PGE::Vertex> meshVerts;
    for (int i = 0;  i < 4; i++) {
        meshVerts.push_back(PGE::Vertex());
        meshVerts[i].setVector2f("position", verts[i]);
        meshVerts[i].setVector2f("texCoords", texCoords[i]);
    }

    std::vector<PGE::Primitive> prims;
    prims.push_back(PGE::Primitive(0, 1, 2));
    prims.push_back(PGE::Primitive(0, 3, 1));

    sharedMesh->setGeometry(meshVerts, prims);
}

void Sprite::render() {
    PGE::Matrix4x4f modelMat = PGE::Matrix4x4f::constructWorldMat(position, PGE::Vector3f(scale.x, scale.y, 1.0f), PGE::Vector3f(0.0f, 0.0f, rotation));

    PGE::Shader::Constant* modelMatValue = shader->getVertexShaderConstant("worldMatrix");
    modelMatValue->setValue(modelMat);

    PGE::Shader::Constant* colorValue = shader->getFragmentShaderConstant("spriteColor");
    colorValue->setValue(color);

    sharedMesh->setMaterial(this->material.getInternal());

    sharedMesh->render();
}
