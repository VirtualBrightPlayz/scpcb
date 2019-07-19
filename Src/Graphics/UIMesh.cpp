#include "UIMesh.h"
#include "../Save/Config.h"

UIMesh::UIMesh(PGE::Graphics* gfx) {
    graphics = gfx;

    shaderTextured = PGE::Shader::load(graphics, PGE::FileName("GFX/Shaders/UI/").str());
    shaderTextureless = PGE::Shader::load(graphics, PGE::FileName("GFX/Shaders/UITextureless/").str());

    mesh = PGE::Mesh::create(gfx, PGE::Primitive::TYPE::TRIANGLE);
    material = nullptr;

    shaderTexturedColorConstant = shaderTextured->getFragmentShaderConstant("imageColor");
    shaderTexturelessColorConstant = shaderTextureless->getFragmentShaderConstant("imageColor");
    
    setColor(PGE::Color(1.f, 1.f, 1.f, 1.f));

    startedRender = false;

    /*TODO: reimplement
    
    // Define our screen space for UI elements.
    // Top Left     - [-50, -50]
    // Bottom Right - [50, 50]
    // Horizontal plane is scaled with the aspect ratio.
    PGE::Vector2f topLeft = PGE::Vector2f(-50.f * config.getAspectRatio(), -50.f);
    PGE::Vector2f bottomRight = PGE::Vector2f(50.f * config.getAspectRatio(), 50.f);
    PGE::Matrix4x4f orthoMat = PGE::Matrix4x4f::constructOrtho2DMat(topLeft.x, bottomRight.x, bottomRight.y, topLeft.y);
    
    */
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
    vertices.clear(); primitives.clear();
}

void UIMesh::setTextured(PGE::FileName textureName, bool tile) {
    endRender();

    tiled = tile;
    textureless = false;

    loadTexture(textureName);
    PGE::Texture* texture = nullptr;
    for (int i = 0; i < textures.size(); i++) {
        if (textures[i].name.equals(textureName)) {
            texture = textures[i].pgeTexture;
            break;
        }
    }

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
    PGE::Rectanglef uvRect = PGE::Rectanglef(rect.topLeftCorner().multiply(0.4f), rect.bottomRightCorner().multiply(0.4f));

    PGE::Vertex vertex;

    int index0 = vertices.size();
    vertex.setVector2f("position", rect.topLeftCorner());
    vertex.setVector2f("uv", uvRect.topLeftCorner());
    vertices.push_back(vertex);

    int index1 = vertices.size();
    vertex.setVector2f("position", rect.topRightCorner());
    vertex.setVector2f("uv", uvRect.topRightCorner());
    vertices.push_back(vertex);

    int index2 = vertices.size();
    vertex.setVector2f("position", rect.bottomLeftCorner());
    vertex.setVector2f("uv", uvRect.bottomLeftCorner());
    vertices.push_back(vertex);

    int index3 = vertices.size();
    vertex.setVector2f("position", rect.bottomRightCorner());
    vertex.setVector2f("uv", uvRect.bottomRightCorner());
    vertices.push_back(vertex);

    primitives.push_back(PGE::Primitive(index0, index1, index2));
    primitives.push_back(PGE::Primitive(index1, index2, index3));
}

void UIMesh::loadTexture(PGE::FileName textureName) {
    PGE::Texture* texture = nullptr;
    for (int i = 0; i < textures.size(); i++) {
        if (textures[i].name.equals(textureName)) {
            return;
        }
    }

    if (texture == nullptr) {
        texture = PGE::Texture::load(graphics, textureName.str());
        Texture cacheEntry;
        cacheEntry.name = textureName; cacheEntry.pgeTexture = texture;
        textures.push_back(cacheEntry);
    }
}

const Alignment operator&(const Alignment& a, const Alignment& b) {
    return (Alignment)((int)a & (int)b);
}

const Alignment operator|(const Alignment& a, const Alignment& b) {
    return (Alignment)((int)a | (int)b);
}
