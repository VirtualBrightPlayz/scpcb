#include <assimp/Importer.hpp>
#include <assimp/scene.h>
#include <assimp/postprocess.h>

#include "Model.h"
#include "GraphicsResources.h"

Model::Model(GraphicsResources* gfx, const PGE::String& filename) {
    gfxRes = gfx;
    shader = gfx->getShader(PGE::FileName::create("GFX/Shaders/Model/"));
    
    Assimp::Importer importer;

    const aiScene* scene = importer.ReadFile(PGE::FileName::create(filename).cstr(),
        aiProcess_Triangulate |
        aiProcess_ConvertToLeftHanded);

    struct NodeIndPair {
        const aiNode* node;
        int ind;
        NodeIndPair(const aiNode* n,int i) {
            node = n; ind = i;
        }
    };

    int meshCount = scene->mRootNode->mNumMeshes;
    std::vector<NodeIndPair> nodeIndPairs;
    nodeIndPairs.push_back(NodeIndPair(scene->mRootNode,0));
    while (nodeIndPairs.size()>0) {
        NodeIndPair& pair = nodeIndPairs[nodeIndPairs.size()-1];
        if (pair.ind < pair.node->mNumChildren) {
            meshCount += pair.node->mChildren[pair.ind]->mNumMeshes;
            NodeIndPair newPair(pair.node->mChildren[pair.ind],0);
            pair.ind++;
            nodeIndPairs.push_back(newPair);
        } else {
            nodeIndPairs.pop_back();
        }
    }

    int slashPos0 = filename.findLast('/')+1;
    int slashPos1 = filename.findLast('\\')+1;
    PGE::String path = filename.substr(0, slashPos0 > slashPos1 ? slashPos0 : slashPos1);
    
    for (int i=0;i<scene->mNumMaterials;i++) {
        const aiMaterial* material = scene->mMaterials[i];
        //for (int j=0;j<material->GetTextureCount(aiTextureType_DIFFUSE);j++) {
        const int j=0;
        aiString textureName;
        aiReturn ret = material->GetTexture(aiTextureType_DIFFUSE,j,&textureName);
        textures.push_back(PGE::Texture::load(gfx->getGraphics(), PGE::FileName::create(path+textureName.C_Str())));
        materials.push_back(new PGE::Material(shader, textures[textures.size()-1]));
        //}
    }

    for (int i=0;i<meshCount;i++) {
        const aiMesh* mesh = scene->mMeshes[i];
        std::vector<PGE::Vertex> pgeVerts;
        for (int v=0;v<mesh->mNumVertices;v++) {
            PGE::Vertex newVert;
            newVert.setVector4f("position", PGE::Vector4f(-mesh->mVertices[v].x ,mesh->mVertices[v].y, mesh->mVertices[v].z, 1.f));
            newVert.setVector3f("normal", PGE::Vector3f(-mesh->mNormals[v].x, mesh->mNormals[v].y, mesh->mNormals[v].z).normalize());
            newVert.setVector2f("uv", PGE::Vector2f(-mesh->mTextureCoords[0][v].x, mesh->mTextureCoords[0][v].y));
            pgeVerts.push_back(newVert);
        }
        std::vector<PGE::Primitive> pgePrims;
        for (int p=0;p<mesh->mNumFaces;p++) {
            PGE::Primitive prim(mesh->mFaces[p].mIndices[0],mesh->mFaces[p].mIndices[1],mesh->mFaces[p].mIndices[2]);
            pgePrims.push_back(prim);
        }
        PGE::Mesh* pgeMesh = PGE::Mesh::create(gfx->getGraphics(), PGE::Primitive::TYPE::TRIANGLE);
        pgeMesh->setGeometry((int)pgeVerts.size(), pgeVerts, (int)pgePrims.size(), pgePrims);
        pgeMesh->setMaterial(materials[mesh->mMaterialIndex]);
        meshes.push_back(pgeMesh);
    }
}

Model::~Model() {
    gfxRes->dropShader(shader);
}

std::vector<PGE::Mesh*> Model::getMeshes() const {
    return meshes;
}
