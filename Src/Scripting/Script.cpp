#include "ScriptManager.h"
#include "Script.h"
#include "Class.h"
#include "Function.h"

#include <fstream>

Script::Script(ScriptManager* scriptMgr, const PGE::FileName& filename, const PGE::String& moduleName) {
    scriptManager = scriptMgr;

    PGE::String scriptContents = "";

    char* readBuf = new char[1024];

    std::ifstream inFile;
    inFile.open(filename.cstr(), std::ios_base::in);

    bool reachedEndOfFile = inFile.eof();
    while (!reachedEndOfFile) {
        inFile.read(readBuf,1023);
        readBuf[inFile.gcount()]='\0';
        scriptContents = PGE::String(scriptContents,readBuf);

        reachedEndOfFile = inFile.eof();
    }

    inFile.close();

    delete[] readBuf;

    PGE::String sectionName;
    sectionName = filename.str().substr(filename.str().findLast("/") + 1);
    sectionName = sectionName.substr(0, sectionName.findFirst("."));

    asIScriptEngine* engine = scriptManager->getAngelScriptEngine();

    scriptModule = engine->GetModule(moduleName.size()>0 ? moduleName.cstr() : nullptr, asGM_CREATE_IF_NOT_EXISTS);

    int errorCode = scriptModule->AddScriptSection(sectionName.cstr(), scriptContents.cstr(), scriptContents.size());
    if (errorCode<0) { throw std::exception("kablooey!"); }
    
    errorCode = scriptModule->Build();
    if (errorCode<0) { throw std::exception("whabammy!"); }

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

Script::~Script() {
    //do nothing
}

asIScriptModule* Script::getAngelScriptModule() const {
    return scriptModule;
}

ScriptManager* Script::getScriptManager() const {
    return scriptManager;
}

ScriptClass* Script::getClassByName(const PGE::String& name) const {
    for (int i = 0; i < classes.size(); i++) {
        if (classes[i]->getName().equals(name)) { return classes[i]; }
    }
    return nullptr;
}

ScriptClass* Script::getClassByTypeId(int typeId) const {
    for (int i = 0; i < classes.size(); i++) {
        if (classes[i]->getTypeId() == typeId) { return classes[i]; }
    }
    return nullptr;
}

ScriptFunction* Script::getFunctionByName(const PGE::String& name) const {
    for (int i = 0; i < functions.size(); i++) {
        if (functions[i]->getSignature().functionName.equals(name)) { return functions[i]; }
    }
    return nullptr;
}

Type* Script::typeFromTypeId(int typeId) const {
    bool discard;
    return typeFromTypeId(typeId, discard);
}

Type* Script::typeFromTypeId(int typeId, bool& isClssType) const {
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
            } else if (isTemplate) {
                if (scriptManager->isArrayTypeId(originalTypeId)) {
                    asITypeInfo* typeInfo = engine->GetTypeInfoById(originalTypeId);
                    Type* baseType = typeFromTypeId(typeInfo->GetSubTypeId());
                    type = baseType->getArrayType();
                } else {
                    throw std::exception("Templates are currently not supported for types other than arrays");
                }
            } else {
                ScriptClass* clss = getClassByTypeId(typeId);
                if (clss == nullptr) { clss = scriptManager->getSharedClassByTypeId(typeId); }
                type = clss;
                isClssType = true;
            }
        } break;
    }

    if (isRef) {
        type = type->asRef();
    }

    return type;
}
