#include "EventDefinition.h"

#include "../Type.h"
#include "../ScriptManager.h"
#include "../ScriptModule.h"
#include "../ScriptFunction.h"

EventDefinition::EventDefinition(ScriptManager* mgr, const PGE::String& nm, std::vector<ScriptFunction::Signature::Argument> argList) {
    scriptManager = mgr;

    name = nm;
    signature = { Type::Void,  name + "Callback", argList };

    for (const auto& arg : signature.arguments) {
        arguments.push_back(CachedArgument(arg.name, arg.type));
    }

    engine = mgr->getAngelScriptEngine();

    engine->RegisterFuncdef(signature.toString().cstr());

    engine->SetDefaultNamespace(name.cstr());

    PGE::String declaration = "void register(" + signature.functionName + "@ callback)";
    engine->RegisterGlobalFunction(declaration.cstr(), asMETHOD(EventDefinition, registerCallback), asCALL_THISCALL_ASGLOBAL, this);

    declaration = "void unregister(" + signature.functionName + "@ callback)";
    engine->RegisterGlobalFunction(declaration.cstr(), asMETHOD(EventDefinition, unregisterCallback), asCALL_THISCALL_ASGLOBAL, this);
}

void EventDefinition::setArgument(const PGE::String& argument, int32_t i32) {
    for (int i = 0; i < arguments.size(); i++) {
        if (arguments[i].name.equals(argument)) {
            arguments[i].type = Type::Int32;
            arguments[i].value.i32 = i32;
        }
    }
}

void EventDefinition::setArgument(const PGE::String& argument, uint32_t u32) {
    for (int i = 0; i < arguments.size(); i++) {
        if (arguments[i].name.equals(argument)) {
            arguments[i].type = Type::UInt32;
            arguments[i].value.i32 = u32;
        }
    }
}

void EventDefinition::setArgument(const PGE::String& argument, float f) {
    for (int i = 0; i < arguments.size(); i++) {
        if (arguments[i].name.equals(argument)) {
            arguments[i].type = Type::Float;
            arguments[i].value.f = f;
        }
    }
}

void EventDefinition::setArgument(const PGE::String& argument, double d) {
    for (int i = 0; i < arguments.size(); i++) {
        if (arguments[i].name.equals(argument)) {
            arguments[i].type = Type::Double;
            arguments[i].value.d = d;
        }
    }
}

void EventDefinition::execute() {
    for (int i = 0; i < registeredCallbacks.size(); i++) {
        registeredCallbacks[i]->prepare();

        const ScriptFunction::Signature& signature = registeredCallbacks[i]->getSignature();

        for (int j = 0; j < arguments.size(); j++) {
            if (arguments[j].type == Type::Int32) {
                registeredCallbacks[i]->setArgument(signature.arguments[j].name, arguments[j].value.i32);
            } else if (arguments[j].type == Type::UInt32) {
                registeredCallbacks[i]->setArgument(signature.arguments[j].name, arguments[j].value.u32);
            } else if(arguments[j].type == Type::Float) {
                registeredCallbacks[i]->setArgument(signature.arguments[j].name, arguments[j].value.f);
            } else if(arguments[j].type == Type::Double) {
                registeredCallbacks[i]->setArgument(signature.arguments[j].name, arguments[j].value.d);
            }
        }

        registeredCallbacks[i]->execute();
    }
}

void EventDefinition::registerCallback(asIScriptFunction* f) {
    ScriptFunction* scriptFunction = nullptr;
    const std::vector<ScriptModule*>& modules = scriptManager->getScriptModules();
    for (int i=0;i<modules.size();i++) {
        scriptFunction = modules[i]->getFunctionByAngelScriptPtr(f);
        if (scriptFunction != nullptr) { break; }
    }
    registeredCallbacks.push_back(scriptFunction);
}

void EventDefinition::unregisterCallback(asIScriptFunction* f) {
    ScriptFunction* scriptFunction = nullptr;
    const std::vector<ScriptModule*>& modules = scriptManager->getScriptModules();
    for (int i=0;i<modules.size();i++) {
        scriptFunction = modules[i]->getFunctionByAngelScriptPtr(f);
        if (scriptFunction != nullptr) { break; }
    }
    for (int i = (int)registeredCallbacks.size()-1; i >= 0; i--) {
        if (registeredCallbacks[i] == scriptFunction) {
            registeredCallbacks.erase(registeredCallbacks.begin() + i);
        }
    }
}
