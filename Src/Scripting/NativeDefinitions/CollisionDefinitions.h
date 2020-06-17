#ifndef COLLISIONDEFINITIONS_H_INCLUDED
#define COLLISIONDEFINITIONS_H_INCLUDED

#include "../ScriptManager.h"
#include "../NativeDefinition.h"
#include <Misc/String.h>
#include <map>

class CollisionMesh;
class CScriptArray;

class CollisionDefinitions : public NativeDefinition {
    private:
        std::map<CollisionMesh*,int> refCount;
        CollisionMesh* collisionMeshFactory(CScriptArray* verts, CScriptArray* inds);
        void collisionMeshAddRef(CollisionMesh* mesh);
        void collisionMeshRelease(CollisionMesh* mesh);

        asIScriptEngine* engine;
    public:
        CollisionDefinitions(ScriptManager* mgr);
};

#endif
