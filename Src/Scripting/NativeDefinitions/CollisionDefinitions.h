#ifndef COLLISIONDEFINITIONS_H_INCLUDED
#define COLLISIONDEFINITIONS_H_INCLUDED

#include "../ScriptManager.h"
#include "../NativeDefinition.h"
#include <Misc/String.h>
#include <map>

class CollisionMesh;
class CollisionMeshCollection;
class CScriptArray;

class CollisionDefinitions : public NativeDefinition {
    private:
        std::map<CollisionMesh*,int> meshRefCount;
        std::map<CollisionMeshCollection*,int> collectionRefCount;
        CollisionMesh* meshFactory(CScriptArray* verts, CScriptArray* inds);
        CollisionMeshCollection* collectionFactory();
        void addRef(void* mesh);
        void release(void* mesh);

        asIScriptEngine* engine;
    public:
        CollisionDefinitions(ScriptManager* mgr);
};

#endif
