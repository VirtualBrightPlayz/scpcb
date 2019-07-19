#include <math.h>

#include "Sprite.h"

Sprite::Sprite(PGE::Mesh* msh, PGE::Texture* tex, PGE::Shader* shdr) {
    mesh = msh; texture = tex; shader = shdr;

    modelMatrixValue = shader->getVertexShaderConstant("modelMatrix");
    spriteColorValue = shader->getFragmentShaderConstant("spriteColor");
    scaleValue = shader->getVertexShaderConstant("scale");
    rotationMatrixValue = shader->getVertexShaderConstant("rotationMatrix");

    texture = tex;
    material = new PGE::Material(shader, tex);

    position = PGE::Vector3f::zero;
    scale = PGE::Vector2f::one;
    rotation = 0.0f;
    color = PGE::Color();
}

Sprite::~Sprite() {
    delete material;
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

PGE::Mesh* Sprite::createSpriteMesh(PGE::Graphics* gfx) {
    PGE::Mesh* mesh = PGE::Mesh::create(gfx, PGE::Primitive::TYPE::TRIANGLE);

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
    //prims.push_back(PGE::Primitive(1, 0, 2));
    //prims.push_back(PGE::Primitive(3, 0, 1));

    mesh->setGeometry(meshVerts, prims);

    return mesh;
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
    scaleValue->setValue(scale);
    rotationMatrixValue->setValue(rotationMatrix);

    mesh->setMaterial(material);

    mesh->render();
}
