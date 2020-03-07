#include "ScriptModule.h"
#include "Script.h"
#include "ScriptClass.h"
#include "ScriptFunction.h"
#include "ScriptManager.h"

#include <stdexcept>

ScriptModule::ScriptModule(ScriptManager* mgr, const PGE::String& nm) {
    scriptManager = mgr;
    asIScriptEngine* engine = scriptManager->getAngelScriptEngine();
    name = nm;
    scriptModule = engine->GetModule(name.cstr(), asGM_ALWAYS_CREATE);
    built = false;
}

void ScriptModule::addScript(const PGE::String& sectionName, Script* script) {
    if (built) { throw std::runtime_error("Module already built!"); }

    int errorCode = scriptModule->AddScriptSection(sectionName.cstr(), script->getScriptContents().cstr(), script->getScriptContents().size());
    if (errorCode < 0) { throw std::runtime_error("kablooey!"); }

    scripts.push_back(script);
}

void ScriptModule::build() {


    int errorCode = scriptModule->Build();
    if (errorCode < 0) { throw std::runtime_error("whabammy!"); }

    int typeCount = scriptModule->GetObjectTypeCount();
    for (int i = 0; i < typeCount; i++) {
        asITypeInfo* typeInfo = scriptModule->GetObjectTypeByIndex(i);

        ScriptClass* newClass = new ScriptClass(this, typeInfo);
        classes.push_back(newClass);
        if ((typeInfo->GetFlags() & asOBJ_SHARED) != 0) {
            scriptManager->registerSharedClass(newClass);
        }
    }

    for (int i = 0; i < classes.size(); i++) {
        classes[i]->finalizeInitialization();
    }

    int functionCount = scriptModule->GetFunctionCount();
    for (int i = 0; i < functionCount; i++) {
        asIScriptFunction* asFunction = scriptModule->GetFunctionByIndex(i);
        ScriptFunction* newFunction = new ScriptFunction(this, asFunction);

        functions.push_back(newFunction);
    }
}

asIScriptModule* ScriptModule::getAngelScriptModule() const {
    return scriptModule;
}

ScriptManager* ScriptModule::getScriptManager() const {
    return scriptManager;
}

ScriptClass* ScriptModule::getClassByName(const PGE::String& name) const {
    for (int i = 0; i < classes.size(); i++) {
        if (classes[i]->getName().equals(name)) { return classes[i]; }
    }
    return nullptr;
}

ScriptClass* ScriptModule::getClassByTypeId(int typeId) const {
    for (int i = 0; i < classes.size(); i++) {
        if (classes[i]->getTypeId() == typeId) { return classes[i]; }
    }
    return nullptr;
}

ScriptFunction* ScriptModule::getFunctionByName(const PGE::String& name) const {
    for (int i = 0; i < functions.size(); i++) {
        if (functions[i]->getSignature().functionName.equals(name)) { return functions[i]; }
    }
    return nullptr;
}

Type* ScriptModule::typeFromTypeId(int typeId) const {
    bool discard;
    return typeFromTypeId(typeId, discard);
}

Type* ScriptModule::typeFromTypeId(int typeId, bool& isClssType) const {
    asIScriptModule* module = scriptModule;
    asIScriptEngine* engine = module->GetEngine();

    int stringFactoryTypeId = engine->GetStringFactoryReturnTypeId();
    int arrayTypeId = engine->GetDefaultArrayTypeId();

    int originalTypeId = typeId;
    bool isRef = (typeId & asTYPEID_OBJHANDLE) != 0;
    bool isTemplate = (typeId & asTYPEID_TEMPLATE) != 0;
    typeId = typeId & (~asTYPEID_OBJHANDLE) & (~asTYPEID_TEMPLATE);

    isClssType = false;
    Type* type = nullptr;
    switch (typeId) {
    case asTYPEID_INT32: {
        type = Type::Int32;
    } break;
    case asTYPEID_UINT32: {
        type = Type::UInt32;
    } break;
    case asTYPEID_FLOAT: {
        type = Type::Float;
    } break;
    case asTYPEID_DOUBLE: {
        type = Type::Double;
    } break;
    case asTYPEID_VOID: {
        type = Type::Void;
    } break;
    default: {
        if (typeId == stringFactoryTypeId) {
            type = Type::String;
        }
        else if (isTemplate) {
            if (scriptManager->isArrayTypeId(originalTypeId)) {
                asITypeInfo* typeInfo = engine->GetTypeInfoById(originalTypeId);
                Type* baseType = typeFromTypeId(typeInfo->GetSubTypeId());
                type = baseType->getArrayType();
            }
            else {
                throw std::runtime_error("Templates are currently not supported for types other than arrays");
            }
        }
        else {
            ScriptClass* clss = getClassByTypeId(typeId);
            if (clss == nullptr) { clss = scriptManager->getSharedClassByTypeId(typeId); }
            type = clss;
            isClssType = true;
        }
    } break;
    }

    if (type == nullptr) {
        return Type::Unsupported;
    }

    if (isRef) {
        type = type->asRef();
    }

    return type;
}
