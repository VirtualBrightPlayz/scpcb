#include <math.h>

#include "Sprite.h"
#include "../Wrap/Material.h"

Mesh Sprite::sharedMesh;
Shader Sprite::shader;

void Sprite::initialize(const Graphics& gfx, const Shader& shd) {
    shader = shd;
    createSpriteMesh(gfx);
}

void Sprite::cleanup() {
    sharedMesh = Mesh();
    shader = Shader();
}

Sprite::Sprite() { }

Sprite::Sprite(const Graphics& gfx, const Texture& tex) {
    modelMatrixValue = shader->getVertexShaderConstant("modelMatrix");
    spriteColorValue = shader->getFragmentShaderConstant("spriteColor");
    scaleValue = shader->getVertexShaderConstant("scale");
    rotationMatrixValue = shader->getVertexShaderConstant("rotationMatrix");

    texture = tex;
    material = Material::create(shader, tex);
    this->gfx = gfx;

    position = PGE::Vector3f::zero;
    scale = PGE::Vector2f::one;
    rotation = 0.0f;
    color = PGE::Color();
}

Sprite Sprite::create(const Graphics& gfx, const Texture& tex) {
    return Sprite(gfx, tex);
}

Sprite Sprite::create(const Graphics& gfx, const PGE::String texPath) {
    Texture tex = Texture::load(gfx, texPath);
    return Sprite(gfx, tex);
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

void Sprite::setRotation(float rad) {
    rotation = rad;
}

void Sprite::addRotation(float rad) {
    rotation += rad;
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
//    prims.push_back(PGE::Primitive(1, 0, 2));
//    prims.push_back(PGE::Primitive(3, 0, 1));

    sharedMesh->setGeometry(meshVerts, prims);
}

void Sprite::update() {
    modelMatrix = PGE::Matrix4x4f::constructWorldMat(position, PGE::Vector3f::one, PGE::Vector3f::zero);
    float sinRoll = sin(rotation);
    float cosRoll = cos(rotation);
    rotationMatrix = PGE::Matrix4x4f(cosRoll,-sinRoll,0.f,0.f,
                                     sinRoll,cosRoll,0.f,0.f,
                                     0.f,0.f,1.f,0.f,
                                     0.f,0.f,0.f,1.f);
}

void Sprite::render() const {
    modelMatrixValue->setValue(modelMatrix);
    spriteColorValue->setValue(color);
    scaleValue->setValue(PGE::Vector3f(scale.x, scale.y, 1.f));
    rotationMatrixValue->setValue(rotationMatrix);

    sharedMesh->setMaterial(material.getInternal());

    sharedMesh->render();
}
