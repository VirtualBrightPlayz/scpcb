#include "PickableDefinitions.h"

#include "../ScriptManager.h"
#include "../../World/Pickable.h"

Pickable* PickableDefinitions::pickableFactory() {
    Pickable* pickable = new Pickable();
    refCount.emplace(pickable, 1);
    refCounterManager->linkPtrToCounter(pickable, this);
    return pickable;
}

void PickableDefinitions::addRef(void* ptr) {
    Pickable* pickable = (Pickable*)ptr;
    if (refCount.find(pickable) == refCount.end()) { return; }
    refCount[pickable]++;
}

void PickableDefinitions::release(void* ptr) {
    Pickable* pickable = (Pickable*)ptr;
    if (refCount.find(pickable) == refCount.end()) { return; }
    refCount[pickable]--;

    if (refCount[pickable] <= 0) {
        refCount.erase(pickable);
        refCounterManager->unlinkPtr(pickable);
        delete pickable;
    }
}

PickableDefinitions::PickableDefinitions(ScriptManager* mgr, RefCounterManager* rcMgr) {
    engine = mgr->getAngelScriptEngine();

    refCounterManager = rcMgr;

    engine->RegisterObjectType("Pickable", sizeof(Pickable), asOBJ_REF);

    engine->RegisterObjectBehaviour("Pickable", asBEHAVE_FACTORY, "Pickable@ f()", asMETHOD(PickableDefinitions, pickableFactory), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterObjectBehaviour("Pickable", asBEHAVE_ADDREF, "void f()", asMETHOD(PickableDefinitions, addRef), asCALL_THISCALL_OBJLAST, this);
    engine->RegisterObjectBehaviour("Pickable", asBEHAVE_RELEASE, "void f()", asMETHOD(PickableDefinitions, release), asCALL_THISCALL_OBJLAST, this);

    engine->RegisterObjectProperty("Pickable", "Vector3f position", asOFFSET(Pickable, position));
    engine->RegisterObjectMethod("Pickable", "bool getPicked()", asMETHOD(Pickable, getPicked), asCALL_THISCALL);
}


