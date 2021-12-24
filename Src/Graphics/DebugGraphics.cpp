#include "DebugGraphics.h"

#include <math.h>

#include <PGE/Graphics/Material.h>

DebugGraphics::DebugGraphics(PGE::Graphics* gfx) {
    mesh = PGE::Mesh::create(*gfx);
    shader = PGE::Shader::load(*gfx, PGE::FilePath::fromStr("SCPCB/GFX/Shaders/Debug/"));
    material = PGE::Material::create(*gfx, *shader, PGE::Opaque::YES);
    mesh->setMaterial(material);

    colorConstant = shader->getFragmentShaderConstant("inColor");
    viewMatrixConstant = shader->getVertexShaderConstant("viewMatrix");
    projectionMatrixConstant = shader->getVertexShaderConstant("projectionMatrix");
}

DebugGraphics::~DebugGraphics() {
    delete mesh;
    delete shader;
}

void DebugGraphics::setViewMatrix(const PGE::Matrix4x4f& viewMatrix) {
    viewMatrixConstant->setValue(viewMatrix);
}

void DebugGraphics::setProjectionMatrix(const PGE::Matrix4x4f& projectionMatrix) {
    projectionMatrixConstant->setValue(projectionMatrix);
}

void DebugGraphics::draw3DLine(const PGE::Line3f& line, const PGE::Color& color, float thickness) {
    colorConstant->setValue(color);

    PGE::StructuredData vertices = PGE::StructuredData(shader->getVertexLayout(), 8);
    std::vector<PGE::Mesh::Triangle> triangles;

    PGE::Vector3f v1 = (line.pointB - line.pointA).normalize();
    PGE::Vector3f v2 = fabs(v1.dotProduct(PGE::Vector3f(0, 1, 0))) < 0.8f ? PGE::Vector3f(0, 1, 0) : PGE::Vector3f(0, 0, 1);
    PGE::Vector3f v3 = v1.crossProduct(v2).normalize();
    v2 = v1.crossProduct(v3).normalize();

    PGE::Vector3f tempPos;

    tempPos = line.pointA + v2 * thickness;
    vertices.setValue(0, "normal", v3);
    vertices.setValue(0, "position", PGE::Vector4f(tempPos, 1.f));
    tempPos = line.pointA + v2 * -thickness;
    vertices.setValue(1, "normal", v3);
    vertices.setValue(1, "position", PGE::Vector4f(tempPos, 1.f));
    tempPos = line.pointB + v2 * thickness;
    vertices.setValue(2, "normal", v3);
    vertices.setValue(2, "position", PGE::Vector4f(tempPos, 1.f));
    tempPos = line.pointB + v2 * -thickness;
    vertices.setValue(3, "normal", v3);
    vertices.setValue(3, "position", PGE::Vector4f(tempPos, 1.f));
    triangles.push_back(PGE::Mesh::Triangle(0,1,2));
    triangles.push_back(PGE::Mesh::Triangle(1,2,3));
    triangles.push_back(PGE::Mesh::Triangle(0,2,1));
    triangles.push_back(PGE::Mesh::Triangle(2,1,3));

    tempPos = line.pointA + v3 * thickness;
    vertices.setValue(4, "normal", v2);
    vertices.setValue(4, "position", PGE::Vector4f(tempPos, 1.f));
    tempPos = line.pointA + v3 * -thickness;
    vertices.setValue(5, "normal", v2);
    vertices.setValue(5, "position", PGE::Vector4f(tempPos, 1.f));
    tempPos = line.pointB + v3 * thickness;
    vertices.setValue(6, "normal", v2);
    vertices.setValue(6, "position", PGE::Vector4f(tempPos, 1.f));
    tempPos = line.pointB + v3 * -thickness;
    vertices.setValue(7, "normal", v2);
    vertices.setValue(7, "position", PGE::Vector4f(tempPos, 1.f));
    triangles.push_back(PGE::Mesh::Triangle(4,5,6));
    triangles.push_back(PGE::Mesh::Triangle(5,6,7));
    triangles.push_back(PGE::Mesh::Triangle(4,6,5));
    triangles.push_back(PGE::Mesh::Triangle(6,5,7));

    mesh->setGeometry(std::move(vertices), triangles);
    mesh->render();
}
