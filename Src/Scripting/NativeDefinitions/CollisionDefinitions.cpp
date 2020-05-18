#include "CollisionDefinitions.h"
#include "../../Collision/CollisionMeshCollection.h"
#include <vector>
#include "../AngelScriptAddons/scriptarray/scriptarray.h"

CollisionMesh* CollisionDefinitions::collisionMeshFactory(CScriptArray* verts, CScriptArray* inds) {
    void* e = this;
    std::vector<PGE::Vector3f> vecVerts;
    std::vector<int> vecInds;
    for (int i=0;i<verts->GetSize();i++) {
        vecVerts.push_back(*((PGE::Vector3f*)verts->At(i)));
    }
    for (int i=0;i<inds->GetSize();i++) {
        vecInds.push_back(*((int*)inds->At(i)));
    }
    CollisionMesh* newMesh = new CollisionMesh(vecVerts, vecInds);
    refCount.emplace(newMesh, 1);
    return newMesh;
}

void CollisionDefinitions::collisionMeshAddRef(CollisionMesh* mesh) {
    refCount[mesh]++;
}

void CollisionDefinitions::collisionMeshRelease(CollisionMesh* mesh) {
    void* a = this;
    refCount[mesh]--;

    if (refCount[mesh] <= 0) { refCount.erase(mesh); delete mesh; }
}

void CollisionDefinitions::registerToEngine(ScriptManager* mgr) {
    asIScriptEngine* engine = mgr->getAngelScriptEngine();

    //Vector3f
    engine->RegisterObjectType("CollisionMesh", sizeof(PGE::Vector3f), asOBJ_REF);
    engine->RegisterObjectBehaviour("CollisionMesh", asBEHAVE_FACTORY, "CollisionMesh@ f(const array<Vector3f>&in verts, const array<int>&in inds)",
                                    asMETHOD(CollisionDefinitions, collisionMeshFactory), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterObjectBehaviour("CollisionMesh", asBEHAVE_ADDREF, "void f()", asMETHOD(CollisionDefinitions,collisionMeshAddRef), asCALL_THISCALL_OBJLAST, this);
    engine->RegisterObjectBehaviour("CollisionMesh", asBEHAVE_RELEASE, "void f()", asMETHOD(CollisionDefinitions,collisionMeshRelease), asCALL_THISCALL_OBJLAST, this);

}

void CollisionDefinitions::cleanup() {
    //TODO: implement
}
