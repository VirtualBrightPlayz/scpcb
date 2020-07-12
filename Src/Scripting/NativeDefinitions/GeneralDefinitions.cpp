#include "GeneralDefinitions.h"
#include <iostream>

GeneralDefinitions::GeneralDefinitions(ScriptManager* mgr) {
    engine = mgr->getAngelScriptEngine();

    engine->SetDefaultNamespace("Debug");
    engine->RegisterGlobalFunction("void Log(string content)", asMETHOD(GeneralDefinitions, Log), asCALL_THISCALL_ASGLOBAL, this);
    engine->RegisterGlobalFunction("void Err(string content)", asMETHOD(GeneralDefinitions, Err), asCALL_THISCALL_ASGLOBAL, this);
}

void GeneralDefinitions::Log(PGE::String content) {
    #ifdef DEBUG
        std::cout << "Debug::Log: " << content << std::endl;
    #else
        // TODO log file maybe
    #endif
}

void GeneralDefinitions::Err(PGE::String content) {
    #ifdef DEBUG
        std::cerr << "Debug::Err: " << content << std::endl; // TODO error throwing
    #else
        // TODO log file maybe
    #endif
}
