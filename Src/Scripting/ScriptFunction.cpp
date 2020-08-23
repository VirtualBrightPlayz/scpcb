#include "ScriptManager.h"
#include "ScriptModule.h"
#include "ScriptFunction.h"
#include "Type.h"
#include "ScriptClass.h"
#include "ScriptObject.h"
#include <stdexcept>

PGE::String ScriptFunction::Signature::toString() const {
    PGE::String retVal = returnType->getName() + " " + functionName;
    retVal = PGE::String(retVal, "(");
    for (int i = 0; i < arguments.size(); i++) {
        if (i>0) { retVal = PGE::String(retVal, ", "); }
        retVal = PGE::String(retVal, arguments[i].type->getName() + " " + arguments[i].name);
    }
    retVal = PGE::String(retVal, ")");
    return retVal;
}

ScriptFunction::Signature::Argument::Argument(Type* t, const PGE::String& n) {
    type = t; name = n;
}

const ScriptFunction::Signature& ScriptFunction::getSignature() const {
    return signature;
}

asIScriptFunction* ScriptFunction::getAngelScriptFunction() const {
    return scriptFunction;
}

int ScriptFunction::getArgumentIndex(const PGE::String& argument) const {
    for (int i = 0; i < signature.arguments.size(); i++) {
        if (signature.arguments[i].name.equals(argument)) {
            return i;
        }
    }
    return -1;
}

//ScriptFunction
ScriptFunction::ScriptFunction(ScriptModule* module, asIScriptFunction* asScriptFunction, asIScriptFunction* asFuncWithSignature) {
    scriptModule = module;
    asIScriptModule* asModule = scriptModule->getAngelScriptModule();

    returnedObject = nullptr;
    
    scriptFunction = asScriptFunction;

    if (asFuncWithSignature == nullptr) { asFuncWithSignature = scriptFunction; }

    signature.functionName = scriptFunction->GetName();

    int argCount = asFuncWithSignature->GetParamCount();
    for (int i = 0; i < argCount; i++) {
        int typeId;
        asDWORD flags;
        const char* name;
        const char* defaultArg;

        asFuncWithSignature->GetParam(i, &typeId, &flags, &name, &defaultArg);

        Type* type = scriptModule->typeFromTypeId(typeId);
        if (type == nullptr) {
            type = scriptModule->getScriptManager()->getClassByTypeId(typeId);
        }

        if (name == nullptr) { name = ""; }

        signature.arguments.push_back(Signature::Argument(type, name));
    }

    int retTypeId = scriptFunction->GetReturnTypeId();
    signature.returnType = scriptModule->typeFromTypeId(retTypeId);
    if (signature.returnType == nullptr) {
        signature.returnType = scriptModule->getScriptManager()->getClassByTypeId(retTypeId);
    }
}

ScriptFunction::~ScriptFunction() {
    unprepare();
    if (returnedObject != nullptr) { delete returnedObject; }
}

void ScriptFunction::prepare() {
    asIScriptContext* activeContext = asGetActiveContext();
    if (activeContext != nullptr) {
        pushedState = true;
        scriptContext = activeContext;
        scriptContext->PushState();
    } else {
        pushedState = false;
        scriptContext = scriptModule->getAngelScriptModule()->GetEngine()->RequestContext();
    }
    scriptContext->Prepare(scriptFunction);
}

void ScriptFunction::unprepare() {
    if (scriptContext == nullptr) { return; }

    if (pushedState) {
        scriptContext->PopState();
    } else {
        scriptModule->getAngelScriptModule()->GetEngine()->ReturnContext(scriptContext);
    }
    scriptContext = nullptr;
}

void ScriptFunction::setObject(ScriptObject* obj) {
    scriptContext->SetObject(obj->getAngelScriptObject());
}

void ScriptFunction::setObjectNative(void* obj) {
    scriptContext->SetObject(obj);
}

void ScriptFunction::setArgument(const PGE::String& argument, int32_t i32) {
    int index = getArgumentIndex(argument);
    scriptContext->SetArgDWord(index, i32);
}

void ScriptFunction::setArgument(const PGE::String& argument, uint32_t u32) {
    int index = getArgumentIndex(argument);
    scriptContext->SetArgDWord(index, u32);
}

void ScriptFunction::setArgument(const PGE::String& argument, float f) {
    int index = getArgumentIndex(argument);
    scriptContext->SetArgFloat(index, f);
}

void ScriptFunction::setArgument(const PGE::String& argument, double d) {
    int index = getArgumentIndex(argument);
    scriptContext->SetArgDouble(index, d);
}

void ScriptFunction::setArgument(const PGE::String& argument, const PGE::String& s) {
    int index = getArgumentIndex(argument);

    if (stringArgs.find(index) == stringArgs.end()) {
        stringArgs.emplace(index, s);
    } else {
        stringArgs[index] = s;
    }
    scriptContext->SetArgObject(index, &(stringArgs[index]));
}

void ScriptFunction::setArgument(const PGE::String& argument, ScriptObject* obj) {
    int index = getArgumentIndex(argument);

    scriptContext->SetArgObject(index, obj->getAngelScriptObject());
}

void ScriptFunction::setArgumentNative(const PGE::String& argument, void* obj) {
    int index = getArgumentIndex(argument);

    scriptContext->SetArgObject(index, obj);
}

void ScriptFunction::execute() {
    scriptContext->Execute();

    if (signature.returnType->isClassType()) {
        asIScriptObject* asObj = nullptr;
        ScriptClass* returnClass = nullptr;
        if (signature.returnType->isRefType()) {
            returnClass = (ScriptClass*)(((RefType*)signature.returnType)->getBaseType());
            asObj = *(asIScriptObject**)scriptContext->GetAddressOfReturnValue();
        } else {
            returnClass = (ScriptClass*)signature.returnType;
            asObj = (asIScriptObject*)scriptContext->GetAddressOfReturnValue();
        }
        if (returnedObject != nullptr) { delete returnedObject; }
        returnedObject = new ScriptObject(returnClass, asObj);
    }

    unprepare();
}

int32_t ScriptFunction::getReturnInt32() const {
    return scriptContext->GetReturnDWord();
}

uint32_t ScriptFunction::getReturnUInt32() const {
    return scriptContext->GetReturnDWord();
}

float ScriptFunction::getReturnFloat() const {
    return scriptContext->GetReturnFloat();
}

double ScriptFunction::getReturnDouble() const {
    return scriptContext->GetReturnDouble();
}

PGE::String ScriptFunction::getReturnString() const {
    return *((PGE::String*)scriptContext->GetReturnObject());
}

ScriptObject* ScriptFunction::getReturnObject() const {
    return returnedObject;
}
