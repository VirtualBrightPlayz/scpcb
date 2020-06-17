#ifndef PLAYERCONTROLLERDEFINITIONS_H_INCLUDED
#define PLAYERCONTROLLERDEFINITIONS_H_INCLUDED

#include "../ScriptManager.h"
#include "../NativeDefinition.h"
#include <map>

class PlayerController;

class PlayerControllerDefinitions : public NativeDefinition {
    private:
        std::map<PlayerController*,int> refCount;
        PlayerController* playerControllerFactory(float radius, float height);
        void playerControllerAddRef(PlayerController* controller);
        void playerControllerRelease(PlayerController* controller);

        asIScriptEngine* engine;
    public:
        PlayerControllerDefinitions(ScriptManager* mgr);
};

#endif
