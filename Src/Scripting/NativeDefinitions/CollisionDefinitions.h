#ifndef COLLISIONDEFINITIONS_H_INCLUDED
#define COLLISIONDEFINITIONS_H_INCLUDED

#include "../RefCounter.h"
#include <Misc/String.h>
#include <map>

class ScriptManager;
class CollisionMesh;
class CollisionMeshCollection;
class CScriptArray;

class CollisionDefinitions : public RefCounter {
    private:
        std::map<CollisionMesh*,int> meshRefCount;
        std::map<CollisionMeshCollection*,int> collectionRefCount;
        CollisionMesh* meshFactory(CScriptArray* verts, CScriptArray* inds);
        CollisionMeshCollection* collectionFactory();
        void addRef(void* mesh);
        void release(void* mesh);

    public:
        CollisionDefinitions(ScriptManager* scriptMgr, RefCounterManager* rcMgr);
};

#endif
