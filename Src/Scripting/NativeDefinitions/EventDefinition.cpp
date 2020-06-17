#include "EventDefinition.h"
#include "../Type.h"
#include "../ScriptManager.h"
#include <stdexcept>

EventDefinition::Argument::Argument(const PGE::String& nm, Type* t) {
    name = nm;
    type = t;
}

EventDefinition::Argument::Value::Value() {
    i32 = 0;
    u32 = 0;
    f = 0;
    d = 0;
}

EventDefinition::EventDefinition(ScriptManager* mgr, const PGE::String& nm, const ScriptFunction::Signature& sgntr) {
    name = nm;
    signature = sgntr;
    signature.returnType = Type::Void;
    signature.functionName = name + "Callback";

    for (int i = 0; i < sgntr.arguments.size(); i++) {
        Argument arg = Argument(sgntr.arguments[i].name, sgntr.arguments[i].type);
        arguments.push_back(arg);
    }

    engine = mgr->getAngelScriptEngine();

    engine->RegisterFuncdef(signature.toString().cstr());

    engine->SetDefaultNamespace(name.cstr());

    PGE::String declaration = "void register(" + signature.functionName + "@ callback)";
    engine->RegisterGlobalFunction(declaration.cstr(), asMETHOD(EventDefinition, registerCallback), asCALL_THISCALL_ASGLOBAL, this);

    declaration = "void unregister(" + signature.functionName + "@ callback)";
    engine->RegisterGlobalFunction(declaration.cstr(), asMETHOD(EventDefinition, unregisterCallback), asCALL_THISCALL_ASGLOBAL, this);

    engine->SetDefaultNamespace("");

    scriptContext = engine->CreateContext();
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
        if (scriptContext->Prepare(registeredCallbacks[i]) < 0) { throw std::runtime_error("ptooey!"); }

        for (int j = 0; j < arguments.size(); j++) {
            if (arguments[j].type == Type::Int32) {
                scriptContext->SetArgDWord(j, arguments[j].value.i32);
            } else if (arguments[j].type == Type::UInt32) {
                scriptContext->SetArgDWord(j, arguments[j].value.u32);
            } else if(arguments[j].type == Type::Float) {
                scriptContext->SetArgFloat(j, arguments[j].value.f);
            } else if(arguments[j].type == Type::Double) {
                scriptContext->SetArgDouble(j, arguments[j].value.d);
            }
        }

        scriptContext->Execute();
    }
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
