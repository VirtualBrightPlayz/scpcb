#include "ScriptManager.h"
#include "Script.h"

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

    errorCode = scriptModule->Build();
}

Script::~Script() {
    //do nothing
}

asIScriptModule* Script::getAngelScriptModule() const {
    return scriptModule;
}

PGE::String Script::typeToString(Script::Type type) {
    switch (type) {
        case Script::Type::Int32: {
            return "int32";
        } break;
        case Script::Type::UInt32: {
            return "uint32";
        } break;
        case Script::Type::Float: {
            return "float";
        } break;
        case Script::Type::Double: {
            return "double";
        } break;
        case Script::Type::String: {
            return "string";
        } break;
    }
}

Script::Function::Signature::Argument::Argument(Script::Type t, const PGE::String& n) {
    type = t; name = n;
}

Script::Function::Function(Script* script, Signature sgntr) {
    signature = sgntr;

    asIScriptModule* module = script->getAngelScriptModule();

    PGE::String decl = typeToString(signature.returnType) + " " + signature.functionName;
    decl = PGE::String(decl, "(");
    for (int i = 0; i < signature.arguments.size(); i++) {
        if (i>0) { decl = PGE::String(decl, ", "); }
        decl = PGE::String(decl, typeToString(signature.arguments[i].type) + " " + signature.arguments[i].name);
    }
    decl = PGE::String(decl, ")");

    scriptFunction = module->GetFunctionByDecl(decl.cstr());

    scriptContext = module->GetEngine()->CreateContext();
    scriptContext->Prepare(scriptFunction);
}

Script::Function::~Function() {
    scriptContext->Release();
}

const Script::Function::Signature& Script::Function::getSignature() const {
    return signature;
}

int Script::Function::getArgumentIndex(const PGE::String& argument) const {
    for (int i = 0; i < signature.arguments.size(); i++) {
        if (signature.arguments[i].name.equals(argument)) {
            return i;
        }
    }
    return -1;
}

void Script::Function::setArgument(const PGE::String& argument, int32_t i32) {
    int index = getArgumentIndex(argument);
    scriptContext->SetArgDWord(index, i32);
}

void Script::Function::setArgument(const PGE::String& argument, uint32_t u32) {
    int index = getArgumentIndex(argument);
    scriptContext->SetArgDWord(index, u32);
}

void Script::Function::setArgument(const PGE::String& argument, float f) {
    int index = getArgumentIndex(argument);
    scriptContext->SetArgFloat(index, f);
}

void Script::Function::setArgument(const PGE::String& argument, double d) {
    int index = getArgumentIndex(argument);
    scriptContext->SetArgDouble(index, d);
}

void Script::Function::setArgument(const PGE::String& argument, const PGE::String& s) {
    int index = getArgumentIndex(argument);
    
    StringPoolEntry obj = StringPoolEntry(s);
    scriptContext->SetArgObject(index, &obj);
}

void Script::Function::execute() {
    scriptContext->Execute();
}
