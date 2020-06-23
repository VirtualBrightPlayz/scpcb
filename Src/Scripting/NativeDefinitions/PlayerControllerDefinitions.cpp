#include "PlayerControllerDefinitions.h"
#include "../../PlayerController/PlayerController.h"

#include "../../Graphics/Camera.h"

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

void PlayerControllerDefinitions::__UPDATE_PLAYERCONTROLLER_TEST_TODO_REMOVE(PlayerController* controller) {
    controller->update(tempCamera->getYawAngle(), tempCamera->getPitchAngle(), Input::Forward);
    tempCamera->setPosition(controller->getPosition().add(PGE::Vector3f(0.f, 1.5f, 0.f)));
}

PlayerControllerDefinitions::PlayerControllerDefinitions(ScriptManager* mgr, Camera* cam) {
    tempCamera = cam;

    engine = mgr->getAngelScriptEngine();

    engine->RegisterObjectType("PlayerController", sizeof(PlayerController), asOBJ_REF);
    engine->RegisterObjectBehaviour("PlayerController", asBEHAVE_FACTORY, "PlayerController@ f(float radius, float height)",
        asMETHOD(PlayerControllerDefinitions, playerControllerFactory), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterObjectBehaviour("PlayerController", asBEHAVE_ADDREF, "void f()", asMETHOD(PlayerControllerDefinitions,playerControllerAddRef), asCALL_THISCALL_OBJLAST, this);
    engine->RegisterObjectBehaviour("PlayerController", asBEHAVE_RELEASE, "void f()", asMETHOD(PlayerControllerDefinitions,playerControllerRelease), asCALL_THISCALL_OBJLAST, this);

    engine->RegisterObjectMethod("PlayerController", "void setCollisionCollection(Collision::Collection@ coll)", asMETHOD(PlayerController,setCollisionMeshCollection), asCALL_THISCALL);
    engine->RegisterObjectMethod("PlayerController", "void update(float yaw, float pitch, int inputs)", asMETHOD(PlayerController,update), asCALL_THISCALL);

    engine->RegisterGlobalFunction("void __UPDATE_PLAYERCONTROLLER_TEST_TODO_REMOVE(PlayerController@ controller)", asMETHOD(PlayerControllerDefinitions, __UPDATE_PLAYERCONTROLLER_TEST_TODO_REMOVE), asCALL_THISCALL_ASGLOBAL, this);

    engine->RegisterObjectMethod("PlayerController", "void setPosition(const Vector3f&in pos)", asMETHOD(PlayerController, setPosition), asCALL_THISCALL);
}
