#include "Function.h"

PGE::String Function::Signature::toString() const {
    PGE::String retVal = typeToString(returnType, false) + " " + functionName;
    retVal = PGE::String(retVal, "(");
    for (int i = 0; i < arguments.size(); i++) {
        if (i>0) { retVal = PGE::String(retVal, ", "); }
        retVal = PGE::String(retVal, typeToString(arguments[i].type, true) + " " + arguments[i].name);
    }
    retVal = PGE::String(retVal, ")");
    return retVal;
}

Function::Signature::Argument::Argument(Type t, const PGE::String& n) {
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
ScriptFunction::ScriptFunction(Script* script, Function::Signature sgntr) {
    signature = sgntr;

    asIScriptModule* module = script->getAngelScriptModule();

    PGE::String decl = signature.toString();

    scriptFunction = module->GetFunctionByDecl(decl.cstr());

    scriptContext = module->GetEngine()->CreateContext();
    if (scriptContext->Prepare(scriptFunction)<0) { throw std::exception("ptooey!"); }
}

ScriptFunction::~ScriptFunction() {
    scriptContext->Release();
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
