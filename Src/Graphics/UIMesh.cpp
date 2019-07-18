#include "UIMesh.h"
#include "../Save/Config.h"

UIMesh::UIMesh(PGE::Graphics* gfx, PGE::Shader* shdTextured, PGE::Shader* shdTextureless) {
    mesh = PGE::Mesh::create(gfx, PGE::Primitive::TYPE::TRIANGLE);
    
    color = PGE::Color(1.f, 1.f, 1.f, 1.f);

    shaderTexturedColorConstant = shaderTextured->getFragmentShaderConstant("imageColor");
    shaderTexturelessColorConstant = shaderTextureless->getFragmentShaderConstant("imageColor");

    startedRender = false;
}

void UIMesh::startRender() {
    startedRender = true;

    mesh->clearGeometry();
    vertices.clear(); primitives.clear();
}

void UIMesh::endRender() {
    if (vertices.size() > 0 && primitives.size() > 0) {
        mesh->setGeometry(vertices, primitives);
        mesh->render();
    }
    startedRender = false;

    mesh->clearGeometry();
    mesh->clearGeometry();
    vertices.clear(); primitives.clear();
}

void UIMesh::setTextured(PGE::Texture* texture, bool tile) {
    endRender();

    tiled = tile;
    textureless = false;

    PGE::Material* prevMaterial = material;
    material = new PGE::Material(shaderTextured, texture);
    mesh->setMaterial(material);
    delete prevMaterial;

    startRender();
}

void UIMesh::setTextureless() {
    endRender();

    textureless = false;

    PGE::Material* prevMaterial = material;
    material = new PGE::Material(shaderTextureless);
    mesh->setMaterial(material);
    delete prevMaterial;

    startRender();
}

void UIMesh::setColor(PGE::Color col) {
    color = col;
    shaderTexturedColorConstant->setValue(color);
    shaderTexturelessColorConstant->setValue(color);
}

void UIMesh::addRect(const PGE::Rectanglef& rect) {
    PGE::Rectanglef uvRect = PGE::Rectanglef(rect.topLeftCorner().multiply(2.4f), rect.bottomRightCorner().multiply(2.4f));

    PGE::Vertex vertex;

    vertex.setVector2f("position", rect.topLeftCorner());
    vertex.setVector2f("uv", uvRect.topLeftCorner());
    vertices.push_back(vertex);

    vertex.setVector2f("position", rect.topRightCorner());
    vertex.setVector2f("uv", uvRect.topRightCorner());
    vertices.push_back(vertex);

    vertex.setVector2f("position", rect.bottomLeftCorner());
    vertex.setVector2f("uv", uvRect.bottomLeftCorner());
    vertices.push_back(vertex);

    vertex.setVector2f("position", rect.bottomRightCorner());
    vertex.setVector2f("uv", uvRect.bottomRightCorner());
    vertices.push_back(vertex);
}

const Alignment operator&(const Alignment& a, const Alignment& b) {
    return (Alignment)((int)a & (int)b);
}

const Alignment operator|(const Alignment& a, const Alignment& b) {
    return (Alignment)((int)a | (int)b);
}
