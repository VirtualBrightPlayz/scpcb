#ifndef PLAYERCONTROLLERDEFINITIONS_H_INCLUDED
#define PLAYERCONTROLLERDEFINITIONS_H_INCLUDED

#include "../ScriptManager.h"
#include "../NativeDefinition.h"
#include <map>

class PlayerController;
class Camera;

class PlayerControllerDefinitions : public NativeDefinition {
    private:
        std::map<PlayerController*,int> refCount;
        PlayerController* playerControllerFactory(float radius, float height);
        void playerControllerAddRef(PlayerController* controller);
        void playerControllerRelease(PlayerController* controller);

        Camera* tempCamera;
        void __UPDATE_PLAYERCONTROLLER_TEST_TODO_REMOVE(PlayerController* controller);

        asIScriptEngine* engine;
    public:
        PlayerControllerDefinitions(ScriptManager* mgr, Camera* cam);
};

#endif
