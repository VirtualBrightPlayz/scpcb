#include "EventDefinition.h"
#include "../Type.h"
#include "../ScriptManager.h"

EventDefinition::EventDefinition(const PGE::String& nm, const ScriptFunction::Signature& sgntr) {
    name = nm;
    signature = sgntr;
    signature.returnType = Type::Void;
    signature.functionName = name + "Callback";
}

void EventDefinition::registerToEngine(ScriptManager* mgr) {
    asIScriptEngine* engine = mgr->getAngelScriptEngine();

    engine->RegisterFuncdef(signature.toString().cstr());

    engine->SetDefaultNamespace(name.cstr());

    PGE::String declaration = "void register(" + signature.functionName + "@ callback)";
    engine->RegisterGlobalFunction(declaration.cstr(), asMETHOD(EventDefinition, registerCallback), asCALL_THISCALL_ASGLOBAL, this);

    declaration = "void unregister(" + signature.functionName + "@ callback)";
    engine->RegisterGlobalFunction(declaration.cstr(), asMETHOD(EventDefinition, unregisterCallback), asCALL_THISCALL_ASGLOBAL, this);

    engine->SetDefaultNamespace("");

    scriptContext = engine->CreateContext();
}

void EventDefinition::cleanup() {
    //TODO: implement
}

void EventDefinition::registerCallback(asIScriptFunction* f) {
    registeredCallbacks.push_back(f);
}

void EventDefinition::unregisterCallback(asIScriptFunction* f) {
    for (int i=registeredCallbacks.size()-1;i>=0;i--) {
        if (registeredCallbacks[i] == f) {
            registeredCallbacks.erase(registeredCallbacks.begin() + i);
        }
    }
}

void EventDefinition::execute() {

}
