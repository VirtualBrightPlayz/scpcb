#include "Model.h"
#include <SDL.h>
#include <assimp/Importer.hpp>
#include <assimp\scene.h>
#include <assimp\postprocess.h>

using namespace CBN;
using namespace PGE;

Model::Model(Graphics* graphics,String filename,Shader* shader) {
    Assimp::Importer importer;

    const aiScene* scene = importer.ReadFile(filename.cstr(),
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
    String path = filename.substr(0,slashPos0>slashPos1?slashPos0:slashPos1);
    
    for (int i=0;i<scene->mNumMaterials;i++) {
        const aiMaterial* material = scene->mMaterials[i];
        //for (int j=0;j<material->GetTextureCount(aiTextureType_DIFFUSE);j++) {
        const int j=0;
        aiString textureName;
        aiReturn ret = material->GetTexture(aiTextureType_DIFFUSE,j,&textureName);
        textures.push_back(Texture::load(graphics,path+textureName.C_Str()));
        materials.push_back(new Material(shader,textures[textures.size()-1]));
        //}
    }

    for (int i=0;i<meshCount;i++) {
        const aiMesh* mesh = scene->mMeshes[i];
        std::vector<Vertex> pgeVerts;
        for (int v=0;v<mesh->mNumVertices;v++) {
            Vertex newVert;
            newVert.setVector4f("position",Vector4f(-mesh->mVertices[v].x,mesh->mVertices[v].y,mesh->mVertices[v].z,1.f));
            newVert.setVector3f("normal",Vector3f(mesh->mNormals[v].x,mesh->mNormals[v].y,mesh->mNormals[v].z));
            newVert.setVector2f("uv",Vector2f(mesh->mTextureCoords[0][v].x,mesh->mTextureCoords[0][v].y));
            pgeVerts.push_back(newVert);
        }
        std::vector<Primitive> pgePrims;
        for (int p=0;p<mesh->mNumFaces;p++) {
            Primitive prim(mesh->mFaces[p].mIndices[0],mesh->mFaces[p].mIndices[1],mesh->mFaces[p].mIndices[2]);
            pgePrims.push_back(prim);
        }
        Mesh* pgeMesh = Mesh::create(graphics,Primitive::TYPE::TRIANGLE);
        pgeMesh->setGeometry(pgeVerts,pgePrims);
        pgeMesh->setMaterial(materials[mesh->mMaterialIndex]);
        meshes.push_back(pgeMesh);
    }
}

std::vector<Mesh*> Model::getMeshes() const {
    return meshes;
}
