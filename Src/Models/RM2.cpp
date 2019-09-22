#include "RM2.h"
#include <fstream>

enum class FileSections {
    Textures = 1,
    VisibleGeometry = 2,
    InvisibleGeometry = 3,
    Waypoint = 4,
    PointLight = 5,
    Spotlight = 6,
    Prop = 7
};

enum class TextureLoadFlag {
    Opaque = 1,
    Alpha = 2
};

static PGE::String readByteString(std::ifstream& inFile) {
    unsigned char len = 0;
    inFile.read((char*)(void*)&len, 1);

    PGE::String retVal = "";
    for (int i = 0; i < (int)len; i++) {
        char chr = '\0';
        inFile.read(&chr, 1);
        retVal = PGE::String(retVal,PGE::String(chr));
    }

    return retVal;
}

RM2::RM2(GraphicsResources* gfxRes, PGE::FileName filename) {
    graphicsResources = gfxRes;

    std::ifstream inFile;
    inFile.open(filename.cstr(), std::ios_base::in | std::ios_base::binary);

    union IntBytes {
        int i;
        char c[4];
    };

    union UShortBytes {
        unsigned short i;
        char c[2];
    };

    union FloatBytes {
        float f;
        char c[4];
    };

    union UCharByte {
        unsigned char u;
        char c;
    };

    //header
    PGE::String header = ".RM2";
    char inHeader[4];
    
    inFile.read(inHeader, 4);

    if (memcmp(inHeader, header.cstr(), 4) != 0) {
        error = RM2Error::InvalidHeader;
    }

    //textures
    char chunkHeader = 0;
    inFile.read(&chunkHeader, 1);
    
    if ((FileSections)chunkHeader != FileSections::Textures) {
        error = RM2Error::UnexpectedChunk;
    }

    UCharByte textureCount;
    inFile.read(&textureCount.c, 1);

    const PGE::String extension = ".rm2";

    PGE::Texture* lightmapTextures[3];
    std::vector<PGE::Texture*> materialTextures;
    for (int i = 0; i < 3; i++) {
        const PGE::String lightmapSuffix = "_lm" + PGE::String(i) + ".png";
        PGE::String lightmapName = filename.str().substr(0, filename.size() - extension.size()) + lightmapSuffix;
        lightmapTextures[i] = gfxRes->getTexture(PGE::FileName::create(lightmapName));
        materialTextures.push_back(lightmapTextures[i]);
    }
    materialTextures.push_back(nullptr);


    for (int i = 0; i < (int)textureCount.u; i++) {
        PGE::String textureName = readByteString(inFile);

        PGE::Texture* texture = gfxRes->getTexture(PGE::FileName::create(textureName));
        
        char flag = 0;
        inFile.read(&flag, 1);

        materialTextures[3] = texture;

        PGE::Material* material = new PGE::Material(shader, materialTextures, (TextureLoadFlag)flag == TextureLoadFlag::Opaque);

        materials.push_back(material);
    }

    //everything else
    chunkHeader = 0;
    inFile.read(&chunkHeader, 1);

    bool reachedEndOfFile = inFile.eof();
    while (!reachedEndOfFile) {
        bool unrecognizedChunkHeader = false;
        switch ((FileSections)chunkHeader) {
            case FileSections::VisibleGeometry: {
                //TODO: put mesh together

                UCharByte textureIndex;
                inFile.read(&textureIndex.c, 1);

                UShortBytes vertexCount;
                inFile.read(vertexCount.c, 2);

                std::vector<PGE::Vector3f> vertexPositions;
                std::vector<int> indices;

                std::vector<PGE::Vertex> vertices;
                PGE::Vertex tempVertex;

                for (int i = 0; i < vertexCount.i; i++) {
                    FloatBytes inX;
                    FloatBytes inY;
                    FloatBytes inZ;

                    inFile.read(inX.c, 4);
                    inFile.read(inY.c, 4);
                    inFile.read(inZ.c, 4);

                    PGE::Vector3f position = PGE::Vector3f(inX.f, inY.f, inZ.f);

                    FloatBytes inDiffU;
                    FloatBytes inDiffV;
                    FloatBytes inLmU;
                    FloatBytes inLmV;

                    inFile.read(inDiffU.c, 4);
                    inFile.read(inDiffV.c, 4);
                    inFile.read(inLmU.c, 4);
                    inFile.read(inLmV.c, 4);

                    PGE::Vector2f diffUv = PGE::Vector2f(inDiffU.f, inDiffV.f);
                    PGE::Vector2f lmUv = PGE::Vector2f(inLmU.f, inLmV.f);

                    tempVertex.setVector3f("position", position);
                    tempVertex.setVector2f("diffUv", diffUv);
                    tempVertex.setVector2f("lmUv", lmUv);
                    vertices.push_back(tempVertex);

                    vertexPositions.push_back(position);
                }

                UShortBytes triangleCount;
                inFile.read(triangleCount.c, 2);

                std::vector<PGE::Primitive> primitives;
                for (int i = 0; i < triangleCount.i; i++) {
                    UShortBytes index0;
                    UShortBytes index1;
                    UShortBytes index2;

                    inFile.read(index0.c, 2);
                    inFile.read(index1.c, 2);
                    inFile.read(index2.c, 2);

                    primitives.push_back(PGE::Primitive(index0.i, index1.i, index2.i));

                    indices.push_back(index0.i);
                    indices.push_back(index1.i);
                    indices.push_back(index2.i);
                }

                PGE::Mesh* mesh = PGE::Mesh::create(gfxRes->getGraphics(), PGE::Primitive::TYPE::TRIANGLE);

                mesh->setMaterial(materials[textureIndex.u]);
                mesh->setGeometry(vertices.size(), vertices, primitives.size(), primitives);

                if (materials[textureIndex.u]->isOpaque())
                {
                    opaqueMeshes.push_back(mesh);
                }
                else
                {
                    alphaMeshes.push_back(mesh);
                }

                collisionMeshes.push_back(new CollisionMesh(vertexPositions, indices));
            } break;
            default: {
                unrecognizedChunkHeader = true;
            } break;
        }

        if (unrecognizedChunkHeader) {
            break; //TODO: throw error here once everything's implemented
        }

        inFile.read(&chunkHeader, 1);

        reachedEndOfFile = inFile.eof();
    }

    inFile.close();
}

void RM2::render(PGE::Matrix4x4f worldMatrix) {

}

