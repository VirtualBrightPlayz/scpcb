#include "ScriptManager.h"
#include "Script.h"
#include "Function.h"
#include "Type.h"
#include "Class.h"

PGE::String Function::Signature::toString() const {
    PGE::String retVal = returnType->getName() + " " + functionName;
    retVal = PGE::String(retVal, "(");
    for (int i = 0; i < arguments.size(); i++) {
        if (i>0) { retVal = PGE::String(retVal, ", "); }
        retVal = PGE::String(retVal, arguments[i].type->getName() + " " + arguments[i].name);
    }
    retVal = PGE::String(retVal, ")");
    return retVal;
}

Function::Signature::Argument::Argument(const Type* t, const PGE::String& n) {
    type = t; name = n;
}

const Function::Signature& Function::getSignature() const {
    return signature;
}

int Function::getArgumentIndex(const PGE::String& argument) const {
    for (int i = 0; i < signature.arguments.size(); i++) {
        if (signature.arguments[i].name.equals(argument)) {
            return i;
        }
    }
    return -1;
}

//ScriptFunction
ScriptFunction::ScriptFunction(Script* scrpt, asIScriptFunction* asScriptFunction) {
    script = scrpt;
    asIScriptModule* module = script->getAngelScriptModule();
    
    scriptFunction = asScriptFunction;

    signature.functionName = scriptFunction->GetName();

    int argCount = scriptFunction->GetParamCount();
    for (int i = 0; i < argCount; i++) {
        int typeId;
        asDWORD flags;
        const char* name;
        const char* defaultArg;

        scriptFunction->GetParam(i, &typeId, &flags, &name, &defaultArg);

        const Type* type = typeFromTypeId(typeId);

        signature.arguments.push_back(Signature::Argument(type, name));
    }

    signature.returnType = typeFromTypeId(scriptFunction->GetReturnTypeId());

    scriptContext = module->GetEngine()->CreateContext();
    if (scriptContext->Prepare(scriptFunction)<0) { throw std::exception("ptooey!"); }
}

ScriptFunction::~ScriptFunction() {
    scriptContext->Release();
}

const Type* ScriptFunction::typeFromTypeId(int typeId) const {
    asIScriptModule* module = script->getAngelScriptModule();
    asIScriptEngine* engine = module->GetEngine();

    int stringFactoryTypeId = engine->GetStringFactoryReturnTypeId();

    const Type* type = nullptr;
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
        default: {
            if (typeId == stringFactoryTypeId) {
                type = Type::String;
            } else {
                ScriptClass* clss = script->getClassByTypeId(typeId);
                if (clss == nullptr) { clss = script->getScriptManager()->getSharedClassByTypeId(typeId); }
                type = clss;
            }
        } break;
    }
    return type;
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

void ScriptFunction::execute() {
    scriptContext->Execute();
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

//NativeFunction
NativeFunction::NativeFunction(ScriptManager* scriptMgr,asSFuncPtr fnPtr,Function::Signature sgntr) {
    scriptManager = scriptMgr;
    functionPointer = fnPtr;
    signature = sgntr;

    asIScriptEngine* engine = scriptManager->getAngelScriptEngine();
    engine->RegisterGlobalFunction(signature.toString().cstr(),functionPointer,asCALL_CDECL);
}

NativeFunction::~NativeFunction() {
    asIScriptEngine* engine = scriptManager->getAngelScriptEngine();
    engine->GetGlobalFunctionByDecl(signature.toString().cstr())->Release();
}
