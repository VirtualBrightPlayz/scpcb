#ifndef PLAYERCONTROLLERDEFINITIONS_H_INCLUDED
#define PLAYERCONTROLLERDEFINITIONS_H_INCLUDED

#include "../ScriptManager.h"
#include "RefCounter.h"
#include <map>

class PlayerController;
class Camera;
class CollisionMeshCollection;

class PlayerControllerDefinitions : public RefCounter {
    private:
        std::map<PlayerController*,int> refCount;
        PlayerController* playerControllerFactory(float radius, float height);
        void addRef(void* ptr) override;
        void release(void* ptr) override;

        void setCollisionCollection(PlayerController* controller, CollisionMeshCollection* collection);

        Camera* tempCamera;
        void __UPDATE_PLAYERCONTROLLER_TEST_TODO_REMOVE(PlayerController* controller);

        asIScriptEngine* engine;
        RefCounterManager* refCounterManager;
    public:
        PlayerControllerDefinitions(ScriptManager* mgr, RefCounterManager* rcMgr, Camera* cam);
};

#endif
