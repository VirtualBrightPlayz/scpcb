#include "PlayerControllerDefinitions.h"
#include "../../PlayerController/PlayerController.h"

PlayerController* PlayerControllerDefinitions::playerControllerFactory(float radius, float height) {
    PlayerController* newController = new PlayerController(radius, height);
    refCount.emplace(newController, 1);
    return newController;
}

void PlayerControllerDefinitions::playerControllerAddRef(PlayerController* controller) {
    refCount[controller]++;
}

void PlayerControllerDefinitions::playerControllerRelease(PlayerController* controller) {
    refCount[controller]--;

    if (refCount[controller] <= 0) { refCount.erase(controller); delete controller; }
}

PlayerControllerDefinitions::PlayerControllerDefinitions(ScriptManager* mgr) {
    engine = mgr->getAngelScriptEngine();

    engine->RegisterObjectType("PlayerController", sizeof(PlayerController), asOBJ_REF);
    engine->RegisterObjectBehaviour("PlayerController", asBEHAVE_FACTORY, "PlayerController@ f(float radius, float height)",
        asMETHOD(PlayerControllerDefinitions, playerControllerFactory), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterObjectBehaviour("PlayerController", asBEHAVE_ADDREF, "void f()", asMETHOD(PlayerControllerDefinitions,playerControllerAddRef), asCALL_THISCALL_OBJLAST, this);
    engine->RegisterObjectBehaviour("PlayerController", asBEHAVE_RELEASE, "void f()", asMETHOD(PlayerControllerDefinitions,playerControllerRelease), asCALL_THISCALL_OBJLAST, this);

    engine->RegisterObjectMethod("PlayerController", "void setPosition(const Vector3f&in pos)", asMETHOD(PlayerController, setPosition), asCALL_THISCALL);
}
