#include "Sprite.h"
#include "../Wrap/Material.h"

Mesh Sprite::sharedMesh;

Sprite::Sprite() { }

Sprite::Sprite(Graphics& gfx, Shader& shader, Texture& tex) {
    if (sharedMesh.getInternal() == nullptr) { createSpriteMesh(gfx); }

    color = PGE::Color();
    this->shader = shader;
    texture = tex;
    this->gfx = gfx;

    resetScaleToTextureSize();
}

Sprite Sprite::create(Graphics& gfx, Shader& shader, Texture& tex) {
    return Sprite(gfx, shader, tex);
}

Sprite Sprite::create(Graphics& gfx, Shader& shader, const PGE::String texPath) {
    Texture tex = Texture::load(gfx, texPath);
    return Sprite(gfx, shader, tex);
}

void Sprite::setScale(float scale) {
    width *= scale;
    height *= scale;
}

void Sprite::resetScaleToTextureSize() {
    width = texture->getWidth();
    height = texture->getHeight();
}

void Sprite::setPosition(const PGE::Vector3f& pos) {
    position = pos;
}

void Sprite::setPosition(float x, float y, float z) {
    position = PGE::Vector3f(x, y, z);
}

void Sprite::createSpriteMesh(Graphics& gfx) {
    sharedMesh = Mesh::create(gfx, PGE::Primitive::TYPE::TRIANGLE);

    PGE::Vector4f verts[4];
    verts[0] = PGE::Vector4f(-0.5f, 0.5f, 0.0f, 1.0f);
    verts[1] = PGE::Vector4f(0.5f, -0.5f, 0.0f, 1.0f);
    verts[2] = PGE::Vector4f(-0.5f, -0.5f, 0.0f, 1.0f);
    verts[3] = PGE::Vector4f(0.5f, 0.5f, 0.0f, 1.0f);

    PGE::Vector2f texCoords[4];
    texCoords[0] = PGE::Vector2f(0.0f, 1.0f);
    texCoords[1] = PGE::Vector2f(1.0f, 0.0f);
    texCoords[2] = PGE::Vector2f(0.0f, 0.0f);
    texCoords[3] = PGE::Vector2f(1.0f, 1.0f);

    std::vector<PGE::Vertex> meshVerts;
    for (int i = 0;  i < 4; i++) {
        meshVerts.push_back(PGE::Vertex());
        meshVerts[i].setVector4f("position", verts[i]);
        meshVerts[i].setVector2f("texCoords", texCoords[i]);
    }

    std::vector<PGE::Primitive> prims;
    prims.push_back(PGE::Primitive(0, 1, 2));
    prims.push_back(PGE::Primitive(0, 3, 1));

    sharedMesh->setGeometry(meshVerts, prims);
}

void Sprite::render() {
    PGE::Matrix4x4f modelMat = PGE::Matrix4x4f::constructWorldMat(position, PGE::Vector3f(width, height, 1.0f), rotation);

    PGE::Shader::Constant* modelMatValue = shader->getVertexShaderConstant("worldMatrix");
    modelMatValue->setValue(modelMat);

    PGE::Shader::Constant* colorValue = shader->getFragmentShaderConstant("spriteColor");
    colorValue->setValue(color);

    Material mat = Material::create(shader, texture);
    sharedMesh->setMaterial(mat.getInternal());

    sharedMesh->render();
}
