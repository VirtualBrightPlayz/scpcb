#include "CBR.h"

#include <set>
#include <fstream>

#include <Misc/BinaryReader.h>

#include "../Graphics/GraphicsResources.h"

enum Lightmapped {
    No = 0,
    Fully = 1,
    Outdated = 2,
};

CBR::CBR(GraphicsResources* gr, const PGE::String& filename) {
    shader = gr->getShader(PGE::FilePath::fromStr("SCPCB/GFX/Shaders/RoomOpaque/"), true);
    shaderModelMatrixConstant = shader->getVertexShaderConstant("modelMatrix");

    PGE::BinaryReader reader = PGE::BinaryReader(PGE::FilePath::fromStr(filename));

    if (reader.readFixedLengthString(3) != "CBR") {
        throw std::runtime_error("CBR file is corrupted/invalid!");
    }
    uint32_t revision = reader.readUInt();

    // Lightmaps
    bool lightmapped = reader.readByte() > No;
    PGE::Texture** lightmaps = new PGE::Texture*[4];
    if (lightmapped) {
        for (int i = 0; i < 4; i++) {
            int size = reader.readInt();
            uint8_t* bytes = reader.readBytes(size);
            lightmaps[i] = PGE::Texture::load(gr->getGraphics(), bytes, size);
            delete[] bytes;
        }
    } else {
        throw std::runtime_error("CBR file without lightmaps!");
    }

    // Texture dictionary
    int32_t texSize = reader.readInt();
    PGE::String* textureNames = new PGE::String[texSize];
    PGE::Material** materials = new PGE::Material*[texSize];
    std::set<int> toolTextures; // TODO: Better solution.
    for (int i = 0; i < texSize; i++) {
        textureNames[i] = reader.readNullTerminatedString();
        if (textureNames[i].findFirst("tooltextures") == -1) {
            std::vector<PGE::Texture*> textures;
            for (int j = 0; j < 3; j++) {
                textures.push_back(lightmaps[j]);
            }
            textures.push_back(gr->getTexture("SCPCB/GFX/Map/Textures/" + textureNames[i]));
            materials[i] = new PGE::Material(shader, textures);
        } else {
            toolTextures.insert(i);
        }
    }

    // Solids
    std::vector<PGE::Vertex>* vertices = new std::vector<PGE::Vertex>[texSize];
    std::vector<PGE::Primitive>* primitives = new std::vector<PGE::Primitive>[texSize];
    int solidCount = reader.readInt();
    for (int i = 0; i < solidCount; i++) {
        int faceCount = reader.readInt();
        for (int j = 0; j < faceCount; j++) {
            int textureID = reader.readInt();
            // 2 * Coordinate (= 3 * float) + 5 * decimal
            reader.skip(2 * 3 * 4 + 5 * 16);
            int vertexCount = reader.readInt();
            int vertexOffset = vertices[textureID].size();
            for (int k = 1; k < vertexCount - 1; k++) {
                primitives[textureID].push_back(PGE::Primitive(
                    vertexOffset,
                    vertexOffset + k,
                    vertexOffset + k + 1
                ));
            }
            for (int k = 0; k < vertexCount; k++) {
                PGE::Vertex tempVertex;
                tempVertex.setVector4f("position", PGE::Vector4f(reader.readVector3f(), 1.f));
                tempVertex.setVector3f("normal", PGE::Vector3f::one);
                tempVertex.setVector2f("lmUv", PGE::Vector2f(reader.readFloat(), reader.readFloat()));
                tempVertex.setVector2f("diffUv", PGE::Vector2f(reader.readFloat(), reader.readFloat()));
                tempVertex.setColor("color", PGE::Color::White);
                vertices[textureID].push_back(tempVertex);
            }
        }
    }
    meshes.reserve(texSize);
    for (int i = 0; i < texSize; i++) {
        if (toolTextures.find(i) == toolTextures.end()) {
            PGE::Mesh* newMesh = PGE::Mesh::create(gr->getGraphics(), PGE::Primitive::TYPE::TRIANGLE);
            newMesh->setMaterial(materials[i]);
            newMesh->setGeometry(vertices[i].size(), vertices[i], primitives[i].size(), primitives[i]);
            meshes.push_back(newMesh);
        }
    }

    delete[] vertices;
    delete[] primitives;
    delete[] textureNames;
    delete[] lightmaps;
}

CBR::~CBR() {

}

void CBR::render(const PGE::Matrix4x4f& modelMatrix) {
    shaderModelMatrixConstant->setValue(modelMatrix);
    for (PGE::Mesh* m : meshes) {
        m->render();
    }
}
