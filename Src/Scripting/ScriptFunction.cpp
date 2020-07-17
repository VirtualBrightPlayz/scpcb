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
asIScriptContext* ScriptFunction::getFunctionContext() const {
    return scriptContext;
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

        if (name == nullptr) { name = ""; }

        signature.arguments.push_back(Signature::Argument(type, name));
    }

    signature.returnType = scriptModule->typeFromTypeId(scriptFunction->GetReturnTypeId());

    scriptContext = asModule->GetEngine()->CreateContext();
    scriptContext->SetExceptionCallback(asMETHOD(ScriptFunction, exceptionCallback), this, asCALL_THISCALL);
}

ScriptFunction::~ScriptFunction() {
    scriptContext->Release();
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

void ScriptFunction::exceptionCallback(asIScriptContext* context) {
    asSMessageInfo info;
    info.message = context->GetExceptionString();
    int line = context->GetExceptionLineNumber(&info.row, &info.section);
    info.col = 0; //TODO: fix?
    info.type = asEMsgType::asMSGTYPE_ERROR;
    scriptModule->getScriptManager()->messageCallback(&info, nullptr);
}

void ScriptFunction::execute(ScriptObject* obj) {
    if (scriptContext->Prepare(scriptFunction) < 0) { throw std::runtime_error("ptooey!"); }
    if (obj != nullptr) { scriptContext->SetObject(obj->getAngelScriptObject()); }
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
        returnedObject = new ScriptObject(returnClass, asObj);
    } else if (signature.returnType == Type::String) {
        returnedString = *((PGE::String*)scriptContext->GetReturnObject());
    }

    scriptContext->Unprepare();
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
    return returnedString;
}

ScriptObject* ScriptFunction::getReturnObject() const {
    return returnedObject;
}
