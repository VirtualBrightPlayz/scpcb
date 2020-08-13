#include "ConsoleDefinitions.h"
#ifdef DEBUG
    #include <iostream>
#endif
#include "../../Menus/Console.h"

ConsoleDefinitions::ConsoleDefinitions(ScriptManager* mgr, Console* con) {
    engine = mgr->getAngelScriptEngine();
    console = con;
    scriptContext = engine->CreateContext();

    engine->RegisterGlobalFunction("void registerCommand(const string&in name, const string&in helpText, function&in command)", asMETHOD(ConsoleDefinitions, registerCommand), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void registerCommand(const string&in name, function&in command)", asMETHOD(ConsoleDefinitions, registerCommandNoHelp), asCALL_THISCALL_ASGLOBAL, this);

    engine->SetDefaultNamespace("Debug");
    engine->RegisterGlobalFunction("void log(?&in content)", asMETHOD(ConsoleDefinitions, log), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void warning(?&in content)", asMETHOD(ConsoleDefinitions, warning), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void error(?&in content)", asMETHOD(ConsoleDefinitions, error), asCALL_THISCALL_ASGLOBAL, this);
}

ConsoleDefinitions::~ConsoleDefinitions() {
    scriptContext->Release();
}

void ConsoleDefinitions::registerCommand(const PGE::String& name, const PGE::String& helpText, void* f, int typeId) const {
    asIScriptFunction* func = (asIScriptFunction*)(((typeId & asTYPEID_OBJHANDLE) != 0) ? *((void**)f) : f);
    console->registerExternalCommand(name, helpText, func, scriptContext);
}

void ConsoleDefinitions::registerCommandNoHelp(const PGE::String& name, void* f, int typeId) const {
    registerCommand(name, "", f, typeId);
}

void ConsoleDefinitions::internalLog(void* ref, int typeId, LogType type) const {
    PGE::String typeString;
    switch (type) {
        case LogType::Log: {
            typeString = "Log";
        } break;
        case LogType::Warning: {
            typeString = "Warn";
        } break;
        case LogType::Error: {
            typeString = "Err";
        } break;
    }
    PGE::String content;
    if (typeId == engine->GetStringFactoryReturnTypeId()) {
        content = *(PGE::String*)ref;
    } else {
        switch (typeId) {
            case asTYPEID_VOID: { // This should never happen.
                throw new std::runtime_error(("VOID PARAMETER! " + PGE::String((uint64_t)ref, true)).cstr());
            } break;
            case asTYPEID_BOOL: {
                content = (*(bool*)ref) ? "True" : "False";
            } break;
            case asTYPEID_INT8: {
                content = PGE::String((int32_t) * (int8_t*)ref);
            } break;
            case asTYPEID_INT16: {
                content = PGE::String((int32_t) * (int16_t*)ref);
            } break;
            case asTYPEID_INT32: {
                content = PGE::String(*(int32_t*)ref);
            } break;
            case asTYPEID_INT64: {
                content = PGE::String(*(int64_t*)ref);
            } break;
            case asTYPEID_UINT8: {
                content = PGE::String((uint32_t) * (uint8_t*)ref);
            } break;
            case asTYPEID_UINT16: {
                content = PGE::String((uint32_t) * (uint16_t*)ref);
            } break;
            case asTYPEID_UINT32: {
                content = PGE::String(*(uint32_t*)ref);
            } break;
            case asTYPEID_UINT64: {
                content = PGE::String(*(uint64_t*)ref);
            } break;
            case asTYPEID_FLOAT: {
                content = PGE::String(*(float*)ref);
            } break;
            case asTYPEID_DOUBLE: {
                content = PGE::String(*(double*)ref);
            } break;
            default: { // Object.
                asITypeInfo* typeInfo = engine->GetTypeInfoById(typeId);
                asIScriptFunction* toString = typeInfo->GetMethodByName("toString");
                if (toString != nullptr) {
                    asIScriptContext* context = engine->RequestContext();
                    context->Prepare(toString);
                    context->SetObject(ref);
                    context->Execute();
                    void* ret = context->GetAddressOfReturnValue();
                    if (ret != nullptr) {
                        content = *(PGE::String*)ret;
                        engine->ReturnContext(context);
                        break;
                    }
                }
                content = PGE::String(typeInfo->GetName()) + "@" + PGE::String((uint64_t)ref, true);
            } break;
        }
    }
#ifdef DEBUG
    std::ostream& out = (type == LogType::Error ? std::cerr : std::cout);
    out << "Debug::" << typeString << ": " << content << std::endl;
#endif
    console->addConsoleMessage("Debug::" + typeString + ": " + content);
    if (type == LogType::Error) {
        throw new std::runtime_error(("ERROR! " + content).cstr());
    }
}

void ConsoleDefinitions::log(void* ref, int typeId) const {
    internalLog(ref, typeId, LogType::Log);
}

void ConsoleDefinitions::warning(void* ref, int typeId) const {
    internalLog(ref, typeId, LogType::Warning);
}

void ConsoleDefinitions::error(void* ref, int typeId) const {
    internalLog(ref, typeId, LogType::Error);
}
