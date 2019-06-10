#include "Sprite.h"
#include "../Wrap/Material.h"

Sprite::Sprite(Graphics gfx, Shader shader, Texture tex) {
    if (sharedMesh.getInternal() == nullptr) { createSpriteMesh(gfx); }

    color = PGE::Color();
    this->shader = shader;
    texture = tex;
    this->gfx = gfx;
}

Sprite Sprite::create(Graphics gfx, Shader shader, Texture tex) {
    return Sprite(gfx, shader, tex);
}

Sprite Sprite::create(Graphics gfx, Shader shader, PGE::String texPath) {
    Texture tex = Texture::load(gfx, texPath);
    return Sprite(gfx, shader, tex);
}

void Sprite::setScale(float scale) {
    this->scale = PGE::Vector2f(scale, scale);
}

void Sprite::createSpriteMesh(Graphics gfx) {
    sharedMesh = Mesh::create(gfx, PGE::Primitive::TYPE::TRIANGLE);

    PGE::Vector2f verts[4];
    verts[0] = PGE::Vector2f(-0.5f, 0.5f);
    verts[1] = PGE::Vector2f(0.5f, -0.5f);
    verts[2] = PGE::Vector2f(-0.5f, -0.5f);
    verts[3] = PGE::Vector2f(0.5f, 0.5f);

    std::vector<PGE::Vertex> meshVerts;
    for (int i = 0;  i < 4; i++) {
        meshVerts.push_back(PGE::Vertex());
        meshVerts[i].setVector4f("position", PGE::Vector4f(verts[i].x, verts[i].y, 0.0f, 1.0f));
        meshVerts[i].setVector2f("texCoords", verts[i]);
    }

    std::vector<PGE::Primitive> prims;
    prims.push_back(PGE::Primitive(0, 1, 2));
    prims.push_back(PGE::Primitive(0, 3, 1));

    sharedMesh->setGeometry(meshVerts, prims);
}

void Sprite::render() {
    PGE::Matrix4x4f modelMat = PGE::Matrix4x4f::constructWorldMat(position, PGE::Vector3f(scale.x, scale.y, 0.0f), rotation);

    PGE::Shader::Constant* modelMatValue = shader->getVertexShaderConstant("worldMatrix");
    modelMatValue->setValue(modelMat);

    Material mat = Material::create(shader, texture);
    sharedMesh->setMaterial(mat.getInternal());

    sharedMesh->render();
}