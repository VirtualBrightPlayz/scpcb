#include "GeneralDefinitions.h"
#ifdef DEBUG
    #include <iostream>
#endif
#include "../../Menus/Console.h"

GeneralDefinitions::GeneralDefinitions(ScriptManager* mgr) {
    engine = mgr->getAngelScriptEngine();

    engine->SetDefaultNamespace("Debug");
    engine->RegisterGlobalFunction("void Log(string content)", asMETHOD(GeneralDefinitions, Log), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void Err(string content)", asMETHOD(GeneralDefinitions, Err), asCALL_THISCALL_ASGLOBAL, this);
}

void GeneralDefinitions::Log(PGE::String content) {
    #ifdef DEBUG
        std::cout << "Debug::Log: " << content << std::endl;
    #endif
    Console::getConsole()->addConsoleMessage("Debug::Log: " + content, PGE::Color::Green);
}

void GeneralDefinitions::Err(PGE::String content) {
    #ifdef DEBUG
        std::cerr << "Debug::Err: " << content << std::endl; // TODO error throwing
    #endif
    Console::getConsole()->addConsoleMessage("Debug::Err: " + content, PGE::Color::Red);
}
