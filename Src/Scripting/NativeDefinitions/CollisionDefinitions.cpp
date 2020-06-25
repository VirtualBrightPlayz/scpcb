#include "CollisionDefinitions.h"
#include "../../Collision/CollisionMeshCollection.h"
#include <vector>
#include "../AngelScriptAddons/scriptarray/scriptarray.h"

CollisionMesh* CollisionDefinitions::meshFactory(CScriptArray* verts, CScriptArray* inds) {
    std::vector<PGE::Vector3f> vecVerts;
    std::vector<int> vecInds;
    for (int i=0;i<verts->GetSize();i++) {
        vecVerts.push_back(*((PGE::Vector3f*)verts->At(i)));
    }
    for (int i=0;i<inds->GetSize();i++) {
        vecInds.push_back(*((int*)inds->At(i)));
    }
    CollisionMesh* newMesh = new CollisionMesh(vecVerts, vecInds);
    meshRefCount.emplace(newMesh, 1);
    refCounterManager->linkPtrToCounter(newMesh, this);
    return newMesh;
}

CollisionMeshCollection* CollisionDefinitions::collectionFactory() {
    CollisionMeshCollection* newMeshCollection = new CollisionMeshCollection();
    collectionRefCount.emplace(newMeshCollection, 1);
    refCounterManager->linkPtrToCounter(newMeshCollection, this);
    return newMeshCollection;
}

void CollisionDefinitions::addRef(void* ptr) {
    if (meshRefCount.find((CollisionMesh*)ptr) != meshRefCount.end()) {
        meshRefCount[(CollisionMesh*)ptr]++;
    }
    if (collectionRefCount.find((CollisionMeshCollection*)ptr) != collectionRefCount.end()) {
        collectionRefCount[(CollisionMeshCollection*)ptr]++;
    }
}

void CollisionDefinitions::release(void* ptr) {
    if (meshRefCount.find((CollisionMesh*)ptr) != meshRefCount.end()) {
        CollisionMesh* castPtr = (CollisionMesh*)ptr;
        meshRefCount[castPtr]--;

        if (meshRefCount[castPtr] <= 0) {
            meshRefCount.erase(castPtr);
            refCounterManager->unlinkPtr(castPtr);
            delete castPtr;
        }
    }
    if (collectionRefCount.find((CollisionMeshCollection*)ptr) != collectionRefCount.end()) {
        CollisionMeshCollection* castPtr = (CollisionMeshCollection*)ptr;
        collectionRefCount[castPtr]--;

        if (collectionRefCount[castPtr] <= 0) {
            collectionRefCount.erase(castPtr);
            refCounterManager->unlinkPtr(castPtr);
            delete castPtr;
        }
    }
}

CollisionDefinitions::CollisionDefinitions(ScriptManager* mgr, RefCounterManager* rcMgr) {
    engine = mgr->getAngelScriptEngine();

    refCounterManager = rcMgr;

    engine->SetDefaultNamespace("Collision");

    engine->RegisterObjectType("Mesh", sizeof(CollisionMesh), asOBJ_REF);
    engine->RegisterObjectBehaviour("Mesh", asBEHAVE_FACTORY, "Mesh@ f(const array<Vector3f>&in verts, const array<int>&in inds)",
        asMETHOD(CollisionDefinitions, meshFactory), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterObjectBehaviour("Mesh", asBEHAVE_ADDREF, "void f()", asMETHOD(CollisionDefinitions,addRef), asCALL_THISCALL_OBJLAST, this);
    engine->RegisterObjectBehaviour("Mesh", asBEHAVE_RELEASE, "void f()", asMETHOD(CollisionDefinitions,release), asCALL_THISCALL_OBJLAST, this);

    engine->RegisterTypedef("Instance", "int");

    engine->RegisterObjectType("Collection", sizeof(CollisionMeshCollection), asOBJ_REF);
    engine->RegisterObjectBehaviour("Collection", asBEHAVE_FACTORY, "Collection@ f()",
        asMETHOD(CollisionDefinitions, collectionFactory), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterObjectBehaviour("Collection", asBEHAVE_ADDREF, "void f()", asMETHOD(CollisionDefinitions,addRef), asCALL_THISCALL_OBJLAST, this);
    engine->RegisterObjectBehaviour("Collection", asBEHAVE_RELEASE, "void f()", asMETHOD(CollisionDefinitions,release), asCALL_THISCALL_OBJLAST, this);
    engine->RegisterObjectMethod("Collection", "Instance addInstance(Mesh@ mesh, Matrix4x4f matrix)", asMETHOD(CollisionMeshCollection, addInstance), asCALL_THISCALL);
    engine->RegisterObjectMethod("Collection", "void removeInstance(Instance instance)", asMETHOD(CollisionMeshCollection, removeInstance), asCALL_THISCALL);

    engine->SetDefaultNamespace("");
}
