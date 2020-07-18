#include "ConsoleDefinition.h"
#ifdef DEBUG
    #include <iostream>
#endif
#include "../../Menus/Console.h"

ConsoleDefinition::ConsoleDefinition(ScriptManager* mgr, Console* con) {
    engine = mgr->getAngelScriptEngine();
    console = con;
    scriptContext = engine->CreateContext();

    engine->RegisterGlobalFunction("void registerCommand(const string&in name, const string&in helpText, function&in command)", asMETHOD(ConsoleDefinition, registerCommand), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void registerCommand(const string&in name, function&in command)", asMETHOD(ConsoleDefinition, registerCommandNoHelp), asCALL_THISCALL_ASGLOBAL, this);

    engine->SetDefaultNamespace("Debug");
    engine->RegisterGlobalFunction("void log(const string&in content)", asMETHOD(ConsoleDefinition, log), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void warning(const string&in content)", asMETHOD(ConsoleDefinition, warning), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void error(const string&in content)", asMETHOD(ConsoleDefinition, error), asCALL_THISCALL_ASGLOBAL, this);
}

void ConsoleDefinition::registerCommand(const PGE::String& name, const PGE::String& helpText, void* f, int typeId) {
    asIScriptFunction* func = (asIScriptFunction*)(((typeId & asTYPEID_OBJHANDLE) != 0) ? *((void**)f) : f);
    console->registerExternalCommand(name, helpText, func, scriptContext);
}

void ConsoleDefinition::registerCommandNoHelp(const PGE::String& name, void* f, int typeId) {
    registerCommand(name, "", f, typeId);
}

void ConsoleDefinition::log(const PGE::String& content) {
#ifdef DEBUG
    std::cout << "Debug::Log: " << content << std::endl;
#endif
    console->addConsoleMessage("Debug::Log: " + content);
}

void ConsoleDefinition::warning(const PGE::String& content) {
#ifdef DEBUG
    std::cout << "Debug::Warn: " << content << std::endl;
#endif
    console->logWarning("Debug::Log: " + content);
}

void ConsoleDefinition::error(const PGE::String& content) {
#ifdef DEBUG
    std::cerr << "Debug::Err: " << content << std::endl; //TODO: Error throwing.
#endif
    console->logError("Debug::Err: " + content);
}
