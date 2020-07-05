#include "PlayerControllerDefinitions.h"
#include "../../PlayerController/PlayerController.h"

#include "../../Graphics/Camera.h"

PlayerController* PlayerControllerDefinitions::playerControllerFactory(float radius, float height) {
    PlayerController* newController = new PlayerController(radius, height);
    refCount.emplace(newController, 1);
    refCounterManager->linkPtrToCounter(newController, this);
    return newController;
}

void PlayerControllerDefinitions::addRef(void* ptr) {
    PlayerController* controller = (PlayerController*)ptr;
    if (refCount.find(controller) == refCount.end()) { return; }
    refCount[controller]++;
}

void PlayerControllerDefinitions::release(void* ptr) {
    PlayerController* controller = (PlayerController*)ptr;
    if (refCount.find(controller) == refCount.end()) { return; }
    refCount[controller]--;

    if (refCount[controller] <= 0) {
        refCount.erase(controller);
        refCounterManager->unlinkPtr(controller);
        if (controller->getCollisionMeshCollection() != nullptr) {
            refCounterManager->release(controller->getCollisionMeshCollection());
        }
        delete controller;
    }
}


void PlayerControllerDefinitions::setCollisionCollection(PlayerController* controller, CollisionMeshCollection* collection) {
    PlayerControllerDefinitions* e = this;
    if (controller->getCollisionMeshCollection() != nullptr) {
        refCounterManager->release(controller->getCollisionMeshCollection());
    }
    controller->setCollisionMeshCollection(collection);
}

void PlayerControllerDefinitions::__UPDATE_PLAYERCONTROLLER_TEST_TODO_REMOVE(PlayerController* controller, Input input) {
    controller->update(tempCamera->getYawAngle(), tempCamera->getPitchAngle(), input);
    tempCamera->setPosition(controller->getPosition().add(PGE::Vector3f(0.f, 15.f, 0.f)));

    release(controller);
}

PlayerControllerDefinitions::PlayerControllerDefinitions(ScriptManager* mgr, RefCounterManager* rcMgr, Camera* cam) {
    tempCamera = cam;

    engine = mgr->getAngelScriptEngine();

    refCounterManager = rcMgr;

    engine->RegisterObjectType("PlayerController", sizeof(PlayerController), asOBJ_REF);
    engine->RegisterObjectBehaviour("PlayerController", asBEHAVE_FACTORY, "PlayerController@ f(float radius, float height)",
        asMETHOD(PlayerControllerDefinitions, playerControllerFactory), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterObjectBehaviour("PlayerController", asBEHAVE_ADDREF, "void f()", asMETHOD(PlayerControllerDefinitions,addRef), asCALL_THISCALL_OBJLAST, this);
    engine->RegisterObjectBehaviour("PlayerController", asBEHAVE_RELEASE, "void f()", asMETHOD(PlayerControllerDefinitions,release), asCALL_THISCALL_OBJLAST, this);

    engine->RegisterObjectMethod("PlayerController", "void setCollisionCollection(Collision::Collection@ coll)", asMETHOD(PlayerControllerDefinitions,setCollisionCollection), asCALL_THISCALL_OBJFIRST, this);
    engine->RegisterObjectMethod("PlayerController", "void update(float yaw, float pitch, Input inputs)", asMETHOD(PlayerController,update), asCALL_THISCALL);

    engine->RegisterGlobalFunction("void __UPDATE_PLAYERCONTROLLER_TEST_TODO_REMOVE(PlayerController@ controller, int input)", asMETHOD(PlayerControllerDefinitions, __UPDATE_PLAYERCONTROLLER_TEST_TODO_REMOVE), asCALL_THISCALL_ASGLOBAL, this);

    engine->RegisterObjectMethod("PlayerController", "void setPosition(const Vector3f&in pos)", asMETHOD(PlayerController, setPosition), asCALL_THISCALL);
}
