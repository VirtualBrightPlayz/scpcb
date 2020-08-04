#include "Billboard.h"

BillboardManager::BillboardManager(PGE::Graphics* gfx, GraphicsResources* gr) {
    gfxRes = gr;
    mesh = PGE::Mesh::create(gfx, PGE::Primitive::TYPE::TRIANGLE);
    shader = gr->getShader(PGE::FilePath::fromStr("SCPCB/GFX/Shaders/Billboard/"), true);
    modelMatrix = shader->getVertexShaderConstant("modelMatrix");
    spriteColor = shader->getFragmentShaderConstant("spriteColor");

    std::vector<PGE::Vertex> vertices;
    std::vector<PGE::Primitive> triangles;

    PGE::Vertex v;
    v.setVector4f("position", PGE::Vector4f(-1.f, -1.f, 0.f, 1.f));
    v.setVector2f("texCoords", PGE::Vector2f(0.f, 0.f));
    vertices.push_back(v);
    v.setVector4f("position", PGE::Vector4f(1.f, -1.f, 0.f, 1.f));
    v.setVector2f("texCoords", PGE::Vector2f(1.f, 0.f));
    vertices.push_back(v);
    v.setVector4f("position", PGE::Vector4f(-1.f, 1.f, 0.f, 1.f));
    v.setVector2f("texCoords", PGE::Vector2f(0.f, 1.f));
    vertices.push_back(v);
    v.setVector4f("position", PGE::Vector4f(1.f, 1.f, 0.f, 1.f));
    v.setVector2f("texCoords", PGE::Vector2f(1.f, 1.f));
    vertices.push_back(v);
    triangles.push_back(PGE::Primitive(0, 1, 2));
    triangles.push_back(PGE::Primitive(1, 2, 3));
    triangles.push_back(PGE::Primitive(0, 2, 1));
    triangles.push_back(PGE::Primitive(2, 1, 3));

    mesh->setGeometry(4, vertices, 4, triangles);
}

BillboardManager::~BillboardManager() {
    gfxRes->dropShader(shader);
    delete mesh;
}

GraphicsResources* BillboardManager::getGfxRes() const {
    return gfxRes;
}

PGE::Mesh* BillboardManager::getMesh() const {
    return mesh;
}

PGE::Shader* BillboardManager::getShader() const {
    return shader;
}

PGE::Shader::Constant* BillboardManager::getModelMatrix() const {
    return modelMatrix;
}

PGE::Shader::Constant* BillboardManager::getSpriteColor() const {
    return spriteColor;
}


Billboard::Billboard(BillboardManager* bm, const PGE::Vector3f& pos, const PGE::String& textureName, const PGE::Vector2f& scale, const PGE::Color& color) : bm(bm) {
    this->pos = pos;
    this->scale = scale;
    this->color = color;
    
    PGE::Texture* texture = bm->getGfxRes()->getTexture(PGE::FilePath::fromStr(textureName));
    material = new PGE::Material(bm->getShader(), texture, false);
}

Billboard::~Billboard() {
    bm->getGfxRes()->dropTexture(material->getTexture(0));
    delete material;
}

void Billboard::render(const PGE::Matrix4x4f& camRotationMatrix) const {
    bm->getMesh()->setMaterial(material);
    bm->getSpriteColor()->setValue(color);
    bm->getModelMatrix()->setValue(PGE::Matrix4x4f::scale(PGE::Vector3f(scale.x, scale.y, 0.f)).product(camRotationMatrix.product(PGE::Matrix4x4f::translate(pos))));
    bm->getMesh()->render();
}


RotatedBillboard::RotatedBillboard(BillboardManager* bm, const PGE::Vector3f& pos, const PGE::String& textureName, const PGE::Vector3f& rotation, const PGE::Vector2f& scale, const PGE::Color& color)
    : Billboard(bm, pos, textureName, scale, color) {
    this->rotation = rotation;
}

void RotatedBillboard::render(const PGE::Matrix4x4f& camRotationMatrix) const {
    bm->getMesh()->setMaterial(material);
    bm->getSpriteColor()->setValue(color);
    bm->getModelMatrix()->setValue(PGE::Matrix4x4f::constructWorldMat(pos, PGE::Vector3f(scale.x, scale.y, 0.f), rotation));
    bm->getMesh()->render();
}
