#include "Billboard.h"

Billboard::Billboard(PGE::Graphics* gfx, GraphicsResources* gr, const PGE::Vector3f& pos, const PGE::String& textureName) : gfxRes(gr), pos(pos) {
    mesh = PGE::Mesh::create(gfx, PGE::Primitive::TYPE::TRIANGLE);
    shader = gr->getShader(PGE::FilePath::fromStr("SCPCB/GFX/Shaders/Billboard/"), true);
    modelMatrix = shader->getVertexShaderConstant("modelMatrix");

    PGE::Texture* texture = gr->getTexture(PGE::FilePath::fromStr(textureName));
    material = new PGE::Material(shader, texture, false);
    mesh->setMaterial(material);

    shader->getFragmentShaderConstant("spriteColor")->setValue(PGE::Color::White);

    std::vector<PGE::Vertex> vertices;
    std::vector<PGE::Primitive> triangles;
    
    PGE::Vertex f;
    f.setVector4f("position", PGE::Vector4f(0.f, 0.f, 0.f, 1.f));
    f.setVector2f("texCoords", PGE::Vector2f(0.f, 0.f));
    vertices.push_back(f);
    f.setVector4f("position", PGE::Vector4f(1.f, 0.f, 0.f, 1.f));
    f.setVector2f("texCoords", PGE::Vector2f(1.f, 0.f));
    vertices.push_back(f);
    f.setVector4f("position", PGE::Vector4f(0.f, 1.f, 0.f, 1.f));
    f.setVector2f("texCoords", PGE::Vector2f(0.f, 1.f));
    vertices.push_back(f);
    f.setVector4f("position", PGE::Vector4f(1.f, 1.f, 0.f, 1.f));
    f.setVector2f("texCoords", PGE::Vector2f(1.f, 1.f));
    vertices.push_back(f);
    triangles.push_back(PGE::Primitive(0, 1, 2));
    triangles.push_back(PGE::Primitive(1, 2, 3));
    triangles.push_back(PGE::Primitive(0, 2, 1));
    triangles.push_back(PGE::Primitive(2, 1, 3));

    mesh->setGeometry(4, vertices, 4, triangles);
}

Billboard::~Billboard() {
    gfxRes->dropTexture(material->getTexture(0));
    gfxRes->dropShader(shader);
    delete mesh;
    delete material;
}

void Billboard::render(const PGE::Vector3f& look) const {
    modelMatrix->setValue(PGE::Matrix4x4f::constructWorldMat(pos, PGE::Vector3f::one, PGE::Vector3f::zero));
    mesh->render();
}