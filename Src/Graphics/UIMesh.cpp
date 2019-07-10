#include "UIMesh.h"
#include "../Save/Config.h"

UIMesh::UIMesh(const Graphics& gfx, const Shader& shdTextured, const Shader& shdTextureless) {
    mesh = Mesh::create(gfx, PGE::Primitive::TYPE::TRIANGLE);
    
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
        mesh->render();
    }
    startedRender = false;

    mesh->clearGeometry();
    mesh->clearGeometry();
    vertices.clear(); primitives.clear();
}

void UIMesh::setTextured(const Texture& texture, bool tile) {
    endRender();

    tiled = tile;
    textureless = false;

    material = Material::create(shaderTextured, texture);
    mesh->setMaterial(material.getInternal());

    startRender();
}

void UIMesh::setTextureless() {
    endRender();

    textureless = false;

    material = Material::create(shaderTextureless);
    mesh->setMaterial(material.getInternal());

    startRender();
}

void UIMesh::setColor(PGE::Color col) {
    color = col;
    shaderTexturedColorConstant->setValue(color);
    shaderTexturelessColorConstant->setValue(color);
}

void UIMesh::addRect(const PGE::Rectanglef& rect) {
    //TODO: implement
}

const Alignment operator&(const Alignment& a, const Alignment& b) {
    return (Alignment)((int)a & (int)b);
}

const Alignment operator|(const Alignment& a, const Alignment& b) {
    return (Alignment)((int)a | (int)b);
}
