#include "CBR.h"

#include <set>
#include <fstream>

#include <PGE/File/BinaryReader.h>

#include "../Graphics/GraphicsResources.h"
#include "../Utils/TextureUtil.h"

const PGE::String TEXTURE_PATH = "SCPCB/GFX/Map/Textures/";

PGE::FilePath CBR::shaderPath;
PGE::FilePath CBR::shaderNormalPath;

enum Lightmapped {
    No = 0,
    Fully = 1,
    Outdated = 2,
};

CBR::CBR(GraphicsResources* gr, const PGE::String& filename) {
    if (!shaderPath.isValid()) {
        shaderPath = PGE::FilePath::fromStr("SCPCB/GFX/Shaders/RoomOpaque/");
        shaderNormalPath = PGE::FilePath::fromStr("SCPCB/GFX/Shaders/RoomOpaqueNormalMap/");
    }

    this->gr = gr;

    shader = gr->getShader(PGE::FilePath::fromStr("SCPCB/GFX/Shaders/RoomOpaque/"), true);
    shaderModelMatrixConstant = shader->getVertexShaderConstant("modelMatrix");

    shaderNormal = gr->getShader(PGE::FilePath::fromStr("SCPCB/GFX/Shaders/RoomOpaqueNormalMap/"), true);
    shaderNormalModelMatrixConstant = shaderNormal->getVertexShaderConstant("modelMatrix");

    PGE::BinaryReader reader = PGE::BinaryReader(PGE::FilePath::fromStr(filename));

    PGE_ASSERT(reader.readFixedLengthString(3) == "CBR", "CBR file is corrupted/invalid!");
    uint32_t revision = reader.readUInt32();

    // Lightmaps
    PGE_ASSERT(reader.readByte() > No, "CBN file without lightmaps");
    lightmaps = new PGE::Texture*[4];
    for (int i = 0; i < 4; i++) {
        int size = reader.readInt32();
        std::vector<PGE::byte> bytes = reader.readBytes(size);
        lightmaps[i] = TextureHelper::load(gr->getGraphics(), bytes);
    }

    // Texture dictionary
    int32_t texSize = reader.readInt32();
    std::vector<PGE::String> textureNames(texSize);
    allTextures = std::vector<PGE::Texture*>();
    materials = std::vector<PGE::Material*>(texSize);
    std::set<int> toolTextures;
    // TODO: only skip tooltextures that are not recognized for an ingame purpose
    // i.e. tooltextures/invisible_collision should be handled as a special case
    for (int i = 0; i < texSize; i++) {
        textureNames[i] = reader.readNullTerminatedString();
        if (textureNames[i].findFirst("tooltextures") == textureNames[i].end()) {
            std::vector<PGE::Texture*> textures;
            for (int j = 0; j < 3; j++) {
                textures.push_back(lightmaps[j]);
            }
            PGE::Texture* tex = gr->getTexture(TEXTURE_PATH + textureNames[i]);
            textures.push_back(tex);
            allTextures.push_back(tex);
            bool normalMapped = false;
            PGE::Texture* texNormal = gr->getTexture(TEXTURE_PATH + textureNames[i] + "_n");
            if (texNormal != nullptr) {
                normalMapped = true;
                textures.push_back(texNormal);
                allTextures.push_back(texNormal);
            }
            materials[i] = new PGE::Material(normalMapped ? shaderNormal : shader, textures);
        } else {
            toolTextures.insert(i);
        }
    }

    // Solids
    // 2D arrays
    std::vector<std::vector<PGE::Vertex>> vertices = std::vector<std::vector<PGE::Vertex>>(texSize);
    std::vector<std::vector<PGE::Primitive>> primitives = std::vector<std::vector<PGE::Primitive>>(texSize);
    int solidCount = reader.readInt32();
    for (int i = 0; i < solidCount; i++) {
        int faceCount = reader.readInt32();
        for (int j = 0; j < faceCount; j++) {
            int textureID = reader.readInt32();
            // 2 * Coordinate (= 3 * float) + 5 * decimal
            reader.skip(2 * 3 * 4 + 5 * 16);
            int vertexCount = reader.readInt32();
            int vertexOffset = (int)vertices[textureID].size();
            for (int k = 1; k < vertexCount - 1; k++) {
                primitives[textureID].push_back(PGE::Primitive(
                    vertexOffset,
                    vertexOffset + k + 1,
                    vertexOffset + k
                ));
            }
            for (int k = 0; k < vertexCount; k++) {
                PGE::Vertex tempVertex;
                tempVertex.setVector4f("position", PGE::Vector4f(reader.readVector3f(), 1.f));
                tempVertex.setVector3f("normal", PGE::Vectors::ONE3F);
                tempVertex.setVector2f("lmUv", reader.readVector2f());
                tempVertex.setVector2f("diffUv", reader.readVector2f());
                tempVertex.setColor("color", PGE::Colors::WHITE);
                vertices[textureID].push_back(tempVertex);
            }
        }
    }
    meshes.reserve(texSize);
    for (int i = 0; i < texSize; i++) {
        if (toolTextures.find(i) == toolTextures.end()) {
            PGE::Mesh* newMesh = PGE::Mesh::create(gr->getGraphics(), PGE::Primitive::Type::TRIANGLE);
            newMesh->setMaterial(materials[i]);
            newMesh->setGeometry(vertices[i], primitives[i]);
            meshes.push_back(newMesh);
        }
    }
}

CBR::~CBR() {
    gr->dropShader(shader);
    gr->dropShader(shaderNormal);
    for (int i = 0; i < 4; i++) {
        delete lightmaps[i];
    }
    delete[] lightmaps;
    for (PGE::Texture* texture : allTextures) {
        gr->dropTexture(texture);
    }
    for (PGE::Material* material : materials) {
        delete material;
    }
}

void CBR::render(const PGE::Matrix4x4f& modelMatrix) {
    shaderModelMatrixConstant->setValue(modelMatrix);
    shaderNormalModelMatrixConstant->setValue(modelMatrix);
    for (PGE::Mesh* m : meshes) {
        m->render();
    }
}
