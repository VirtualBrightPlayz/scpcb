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
    engine->RegisterGlobalFunction("void log(const string&in content)", asMETHOD(ConsoleDefinitions, log), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void warning(const string&in content)", asMETHOD(ConsoleDefinitions, warning), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void error(const string&in content)", asMETHOD(ConsoleDefinitions, error), asCALL_THISCALL_ASGLOBAL, this);
}

void ConsoleDefinitions::registerCommand(const PGE::String& name, const PGE::String& helpText, void* f, int typeId) {
    asIScriptFunction* func = (asIScriptFunction*)(((typeId & asTYPEID_OBJHANDLE) != 0) ? *((void**)f) : f);
    console->registerExternalCommand(name, helpText, func, scriptContext);
}

void ConsoleDefinitions::registerCommandNoHelp(const PGE::String& name, void* f, int typeId) {
    registerCommand(name, "", f, typeId);
}

void ConsoleDefinitions::log(const PGE::String& content) {
#ifdef DEBUG
    std::cout << "Debug::Log: " << content << std::endl;
#endif
    console->addConsoleMessage("Debug::Log: " + content);
}

void ConsoleDefinitions::warning(const PGE::String& content) {
#ifdef DEBUG
    std::cout << "Debug::Warn: " << content << std::endl;
#endif
    console->logWarning("Debug::Log: " + content);
}

void ConsoleDefinitions::error(const PGE::String& content) {
#ifdef DEBUG
    std::cerr << "Debug::Err: " << content << std::endl; //TODO: Error throwing.
#endif
    console->logError("Debug::Err: " + content);
}
